--
-- Eigene Geometrie einer Verortung (Umgriff und Flurstücke) als PostGIS-Geometrie in eigenen Tabellen.
-- Die Geometrie wird nur geladen, wenn das Formular sie braucht. Gebietspolygone werden nicht übernommen (siehe V51).
--
-- Voraussetzung: PostGIS ist installiert oder das Recht zum Installieren ist vorhanden. Besteht die Extension schon,
-- sind für IF NOT EXISTS keine besonderen Rechte nötig.
--
SET LOCAL search_path = isidbuser, public;

CREATE EXTENSION IF NOT EXISTS postgis SCHEMA public;

-- id ist die ID des Eigentümers (Bauvorhaben oder Abfrage). Die IDs sind tabellenübergreifend eindeutig.
CREATE TABLE verortung_geometrie
(
    id             varchar(36) PRIMARY KEY,
    umgriff        geometry(MultiPolygon, 4326) NOT NULL,
    geometrie_hash varchar(64)
);

CREATE INDEX verortung_geometrie_umgriff_index ON verortung_geometrie USING gist (umgriff);

-- nummer identifiziert ein Flurstück wie im Frontend. Der Nenner kann leer sein und taugt deshalb nicht als Schlüssel.
CREATE TABLE verortung_flurstueck
(
    verortung_geometrie_id varchar(36)                  NOT NULL REFERENCES verortung_geometrie (id) ON DELETE CASCADE,
    gemarkung_nummer       numeric,
    nummer                 varchar(255),
    geometrie              geometry(MultiPolygon, 4326) NOT NULL,
    CONSTRAINT verortung_flurstueck_key UNIQUE NULLS NOT DISTINCT (verortung_geometrie_id, gemarkung_nummer, nummer)
);

-- Ein Fremdschlüssel ist nicht möglich, solange die Eigentümer in vier Tabellen liegen. Der Trigger räumt stattdessen auf.
CREATE FUNCTION verortung_geometrie_mit_eigentuemer_loeschen() RETURNS trigger AS
$$
BEGIN
    DELETE FROM isidbuser.verortung_geometrie WHERE id = OLD.id;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER verortung_geometrie_loeschen AFTER DELETE ON bauvorhaben
    FOR EACH ROW EXECUTE FUNCTION verortung_geometrie_mit_eigentuemer_loeschen();
CREATE TRIGGER verortung_geometrie_loeschen AFTER DELETE ON bauleitplanverfahren
    FOR EACH ROW EXECUTE FUNCTION verortung_geometrie_mit_eigentuemer_loeschen();
CREATE TRIGGER verortung_geometrie_loeschen AFTER DELETE ON baugenehmigungsverfahren
    FOR EACH ROW EXECUTE FUNCTION verortung_geometrie_mit_eigentuemer_loeschen();
CREATE TRIGGER verortung_geometrie_loeschen AFTER DELETE ON weiteres_verfahren
    FOR EACH ROW EXECUTE FUNCTION verortung_geometrie_mit_eigentuemer_loeschen();

-- Nur für diese Migration: alle Verortungen mit eigenem Umgriff.
CREATE TEMPORARY TABLE verortung_alt ON COMMIT DROP AS
SELECT id, verortung
FROM (SELECT id, verortung FROM bauvorhaben
      UNION ALL SELECT id, verortung FROM bauleitplanverfahren
      UNION ALL SELECT id, verortung FROM baugenehmigungsverfahren
      UNION ALL SELECT id, verortung FROM weiteres_verfahren) alle
WHERE jsonb_typeof(verortung -> 'multiPolygon' -> 'coordinates') = 'array'
  AND jsonb_array_length(verortung -> 'multiPolygon' -> 'coordinates') > 0;

-- Alle Flurstücke mit Polygon aus den Gemarkungen dieser Verortungen.
CREATE TEMPORARY VIEW flurstueck_alt AS
SELECT v.id,
       COALESCE((f ->> 'gemarkungNummer')::numeric, (g ->> 'nummer')::numeric) AS gemarkung_nummer,
       f ->> 'nummer'                                                          AS nummer,
       f -> 'multiPolygon'                                                     AS multi_polygon
FROM verortung_alt v
         CROSS JOIN LATERAL jsonb_array_elements(CASE jsonb_typeof(v.verortung -> 'gemarkungen') WHEN 'array' THEN v.verortung -> 'gemarkungen' ELSE '[]' END) g
         CROSS JOIN LATERAL jsonb_array_elements(CASE jsonb_typeof(g -> 'flurstuecke') WHEN 'array' THEN g -> 'flurstuecke' ELSE '[]' END) f
WHERE jsonb_typeof(f -> 'multiPolygon' -> 'coordinates') = 'array';

INSERT INTO verortung_geometrie (id, umgriff)
SELECT id, ST_Multi(ST_Force2D(ST_GeomFromGeoJSON(verortung -> 'multiPolygon')))
FROM verortung_alt;

-- Der Hash bleibt leer: Beim ersten Speichern mit Polygonen wird die Geometrie einmal neu geschrieben.
INSERT INTO verortung_flurstueck (verortung_geometrie_id, gemarkung_nummer, nummer, geometrie)
SELECT DISTINCT ON (id, gemarkung_nummer, nummer)
       id, gemarkung_nummer, nummer, ST_Multi(ST_Force2D(ST_GeomFromGeoJSON(multi_polygon)))
FROM flurstueck_alt;

-- Die Polygone werden in V51 aus dem jsonb entfernt. Deshalb hier prüfen, dass alles angekommen ist.
DO
$$
DECLARE
    umgriffe_alt    bigint;
    umgriffe_neu    bigint;
    flurstuecke_alt bigint;
    flurstuecke_neu bigint;
BEGIN
    SELECT count(*) INTO umgriffe_alt FROM verortung_alt;
    SELECT count(*) INTO umgriffe_neu FROM verortung_geometrie;
    SELECT count(*) INTO flurstuecke_alt FROM (SELECT DISTINCT id, gemarkung_nummer, nummer FROM flurstueck_alt) x;
    SELECT count(*) INTO flurstuecke_neu FROM verortung_flurstueck;
    IF umgriffe_alt <> umgriffe_neu OR flurstuecke_alt <> flurstuecke_neu THEN
        RAISE EXCEPTION 'Geometrie unvollständig übernommen: Umgriffe %/%, Flurstücke %/%',
            umgriffe_neu, umgriffe_alt, flurstuecke_neu, flurstuecke_alt;
    END IF;
END;
$$;

DROP VIEW flurstueck_alt;

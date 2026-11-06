--
-- Entfernt alle Polygone aus den jsonb-Spalten verortung. Umgriff und Flurstücke liegen seit V50 in eigenen Tabellen,
-- Gebietspolygone liefert der Geodaten-EAI. Die JSON-Struktur bleibt sonst gleich, Pentaho und Reports lesen sie unverändert.
--
SET LOCAL search_path = isidbuser, public;

-- Entfernt multiPolygon aus den Elementen eines Arrays. Andere Werte (z. B. null) bleiben unverändert.
CREATE FUNCTION pg_temp.ohne_polygon(gebiete jsonb) RETURNS jsonb AS
$$
SELECT CASE
           WHEN jsonb_typeof(gebiete) = 'array' THEN
               COALESCE((SELECT jsonb_agg(e - 'multiPolygon') FROM jsonb_array_elements(gebiete) e), '[]'::jsonb)
           ELSE gebiete
           END
$$ LANGUAGE sql IMMUTABLE;

CREATE FUNCTION pg_temp.gemarkungen_ohne_polygon(gemarkungen jsonb) RETURNS jsonb AS
$$
SELECT CASE
           WHEN jsonb_typeof(gemarkungen) = 'array' THEN
               COALESCE((SELECT jsonb_agg(
                                    CASE
                                        WHEN g ? 'flurstuecke' THEN
                                            jsonb_set(g - 'multiPolygon', '{flurstuecke}', pg_temp.ohne_polygon(g -> 'flurstuecke'))
                                        ELSE g - 'multiPolygon'
                                        END)
                         FROM jsonb_array_elements(gemarkungen) g), '[]'::jsonb)
           ELSE gemarkungen
           END
$$ LANGUAGE sql IMMUTABLE;

-- Der Report zu Kita-Plätzen liest den ersten Eintrag (-> 0). Die feste Reihenfolge macht das Ergebnis eindeutig.
CREATE FUNCTION pg_temp.kitaplanungsbereiche_ohne_polygon(bereiche jsonb) RETURNS jsonb AS
$$
SELECT CASE
           WHEN jsonb_typeof(bereiche) = 'array' THEN
               COALESCE((SELECT jsonb_agg(e - 'multiPolygon' ORDER BY (e ->> 'kitaPlb')::bigint NULLS LAST)
                         FROM jsonb_array_elements(bereiche) e), '[]'::jsonb)
           ELSE bereiche
           END
$$ LANGUAGE sql IMMUTABLE;

-- Fügt keine Schlüssel hinzu, die es in der Verortung nicht gibt.
CREATE FUNCTION pg_temp.schlank(v jsonb) RETURNS jsonb AS
$$
SELECT (v - 'multiPolygon')
           || (SELECT COALESCE(jsonb_object_agg(a.schluessel, pg_temp.ohne_polygon(v -> a.schluessel)), '{}'::jsonb)
               FROM unnest(ARRAY ['stadtbezirke', 'bezirksteile', 'viertel', 'grundschulsprengel', 'mittelschulsprengel']) a(schluessel)
               WHERE v ? a.schluessel)
           || CASE WHEN v ? 'gemarkungen' THEN jsonb_build_object('gemarkungen', pg_temp.gemarkungen_ohne_polygon(v -> 'gemarkungen')) ELSE '{}'::jsonb END
           || CASE WHEN v ? 'kitaplanungsbereiche' THEN jsonb_build_object('kitaplanungsbereiche', pg_temp.kitaplanungsbereiche_ohne_polygon(v -> 'kitaplanungsbereiche')) ELSE '{}'::jsonb END
$$ LANGUAGE sql IMMUTABLE;

-- Schwerpunkt: Punkt, der garantiert in der Fläche liegt, und seine Lage in EPSG:25832.
-- ST_MakeValid fängt ungültige Bestandsgeometrien ab.
CREATE FUNCTION pg_temp.mit_schwerpunkt(v jsonb, umgriff geometry) RETURNS jsonb AS
$$
WITH p AS (SELECT ST_PointOnSurface(ST_MakeValid(umgriff)) AS punkt)
SELECT v || jsonb_build_object(
        'centroid', jsonb_build_object('type', 'Point', 'coordinates', jsonb_build_array(ST_X(punkt), ST_Y(punkt))),
        'centroidUtm', jsonb_build_object('zone', '32U', 'east', ST_X(ST_Transform(punkt, 25832)), 'north', ST_Y(ST_Transform(punkt, 25832))))
FROM p
$$ LANGUAGE sql STABLE;

UPDATE bauvorhaben o
SET verortung = CASE WHEN g.id IS NULL THEN pg_temp.schlank(o.verortung) ELSE pg_temp.mit_schwerpunkt(pg_temp.schlank(o.verortung), g.umgriff) END
FROM bauvorhaben b
         LEFT JOIN verortung_geometrie g ON g.id = b.id
WHERE b.id = o.id AND o.verortung IS NOT NULL;

UPDATE bauleitplanverfahren o
SET verortung = CASE WHEN g.id IS NULL THEN pg_temp.schlank(o.verortung) ELSE pg_temp.mit_schwerpunkt(pg_temp.schlank(o.verortung), g.umgriff) END
FROM bauleitplanverfahren b
         LEFT JOIN verortung_geometrie g ON g.id = b.id
WHERE b.id = o.id AND o.verortung IS NOT NULL;

UPDATE baugenehmigungsverfahren o
SET verortung = CASE WHEN g.id IS NULL THEN pg_temp.schlank(o.verortung) ELSE pg_temp.mit_schwerpunkt(pg_temp.schlank(o.verortung), g.umgriff) END
FROM baugenehmigungsverfahren b
         LEFT JOIN verortung_geometrie g ON g.id = b.id
WHERE b.id = o.id AND o.verortung IS NOT NULL;

UPDATE weiteres_verfahren o
SET verortung = CASE WHEN g.id IS NULL THEN pg_temp.schlank(o.verortung) ELSE pg_temp.mit_schwerpunkt(pg_temp.schlank(o.verortung), g.umgriff) END
FROM weiteres_verfahren b
         LEFT JOIN verortung_geometrie g ON g.id = b.id
WHERE b.id = o.id AND o.verortung IS NOT NULL;

-- Einrichtungen haben nur einen Punkt, er bleibt unverändert im jsonb.
UPDATE infrastruktureinrichtung
SET verortung = pg_temp.schlank(verortung)
WHERE verortung IS NOT NULL;

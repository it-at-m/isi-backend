BEGIN;

--
-- Enums, die bisher als Ordinalwert (Position im Java-Enum) gespeichert wurden, werden als Text (Name) gespeichert.
-- Damit hängen die gespeicherten Daten nicht mehr von der Reihenfolge der Enum-Werte ab.
--
-- Unbekannte Ordinalwerte werden zu NULL umgewandelt und lassen die Migration an der NOT-NULL-Bedingung scheitern,
-- statt stillschweigend falsche Werte zu speichern.
--

-- bedarfsmeldung.infrastruktureinrichtung_typ (Enum InfrastruktureinrichtungTyp, Reihenfolge seit 2023 unverändert)
ALTER TABLE isidbuser.bedarfsmeldung
    DROP CONSTRAINT bedarfsmeldung_infrastruktureinrichtung_typ_check;

ALTER TABLE isidbuser.bedarfsmeldung
    ALTER COLUMN infrastruktureinrichtung_typ TYPE character varying(255)
    USING CASE infrastruktureinrichtung_typ
        WHEN 0 THEN 'UNSPECIFIED'
        WHEN 1 THEN 'KINDERKRIPPE'
        WHEN 2 THEN 'KINDERGARTEN'
        WHEN 3 THEN 'GS_NACHMITTAG_BETREUUNG'
        WHEN 4 THEN 'HAUS_FUER_KINDER'
        WHEN 5 THEN 'GRUNDSCHULE'
        WHEN 6 THEN 'MITTELSCHULE'
        WHEN 7 THEN 'NACHBARSCHAFTSTREFF'
    END;

ALTER TABLE isidbuser.bedarfsmeldung
    ALTER COLUMN infrastruktureinrichtung_typ SET NOT NULL;

ALTER TABLE isidbuser.bedarfsmeldung
    ADD CONSTRAINT bedarfsmeldung_infrastruktureinrichtung_typ_check CHECK (infrastruktureinrichtung_typ IN (
        'UNSPECIFIED',
        'KINDERKRIPPE',
        'KINDERGARTEN',
        'GS_NACHMITTAG_BETREUUNG',
        'HAUS_FUER_KINDER',
        'GRUNDSCHULE',
        'MITTELSCHULE',
        'NACHBARSCHAFTSTREFF'
    ));

-- baugebiet.art_bauliche_nutzung (Enum ArtBaulicheNutzung, Reihenfolge seit V27 unverändert)
ALTER TABLE isidbuser.baugebiet
    DROP CONSTRAINT baugebiet_art_bauliche_nutzung_check;

ALTER TABLE isidbuser.baugebiet
    ALTER COLUMN art_bauliche_nutzung TYPE character varying(255)
    USING CASE art_bauliche_nutzung
        WHEN 0 THEN 'UNSPECIFIED'
        WHEN 1 THEN 'WR'
        WHEN 2 THEN 'WA'
        WHEN 3 THEN 'MU'
        WHEN 4 THEN 'MK'
        WHEN 5 THEN 'MI'
        WHEN 6 THEN 'GE'
        WHEN 7 THEN 'FREIE_EINGABE'
    END;

ALTER TABLE isidbuser.baugebiet
    ALTER COLUMN art_bauliche_nutzung SET NOT NULL;

ALTER TABLE isidbuser.baugebiet
    ADD CONSTRAINT baugebiet_art_bauliche_nutzung_check CHECK (art_bauliche_nutzung IN (
        'UNSPECIFIED',
        'WR',
        'WA',
        'MU',
        'MK',
        'MI',
        'GE',
        'FREIE_EINGABE'
    ));

COMMIT;

BEGIN;

--
-- Bereinigung der CHECK-Constraints für Enum-Spalten, sodass sie den Java-Enums entsprechen.
-- Abgesichert durch den Test EnumCheckConstraintTest.
--

-- Verfahrensstand im Baugenehmigungsverfahren: nur die für diese Abfrageart erlaubten Werte
-- (Verfahrensstand.getVerfahrensstandForBaugenehmigungsverfahren). Die bisherige Liste enthielt veraltete Werte.
ALTER TABLE isidbuser.baugenehmigungsverfahren
    DROP CONSTRAINT baugenehmigungsverfahren_verfahrensstand_check;
ALTER TABLE isidbuser.baugenehmigungsverfahren
    ADD CONSTRAINT baugenehmigungsverfahren_verfahrensstand_check CHECK (verfahrensstand IN (
        'UNSPECIFIED',
        'VORBEREITUNG_BAUGENEHMIGUNG',
        'VORBEREITUNG_VORBESCHEID',
        'FREIE_EINGABE'
    ));

-- Verfahrensstand im Weiteren Verfahren: nur die für diese Abfrageart erlaubten Werte
-- (Verfahrensstand.getVerfahrensstandForWeiteresVerfahren). Die bisherige Liste enthielt veraltete Werte.
ALTER TABLE isidbuser.weiteres_verfahren
    DROP CONSTRAINT weiteres_verfahren_verfahrensstand_check;
ALTER TABLE isidbuser.weiteres_verfahren
    ADD CONSTRAINT weiteres_verfahren_verfahrensstand_check CHECK (verfahrensstand IN (
        'UNSPECIFIED',
        'VORABFRAGE_OHNE_KONKRETEN_STAND',
        'STRUKTURKONZEPT',
        'RAHMENPLANUNG',
        'POTENTIALUNTERSUCHUNG',
        'STAEDTEBAULICHE_SANIERUNGSMASSNAHME',
        'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME',
        'FREIE_EINGABE',
        'STANDORTABFRAGE'
    ));

-- Der mit V44 eingeführte Infrastruktureinrichtungstyp NACHBARSCHAFTSTREFF fehlte in den Stammdaten-Constraints.
ALTER TABLE isidbuser.versorgungsquote_gruppenstaerke
    DROP CONSTRAINT versorgungsquote_gruppenstae_infrastruktureinrichtung_typ_check;
ALTER TABLE isidbuser.versorgungsquote_gruppenstaerke
    ADD CONSTRAINT versorgungsquote_gruppenstae_infrastruktureinrichtung_typ_check CHECK (infrastruktureinrichtung_typ IN (
        'UNSPECIFIED',
        'KINDERKRIPPE',
        'KINDERGARTEN',
        'GS_NACHMITTAG_BETREUUNG',
        'HAUS_FUER_KINDER',
        'GRUNDSCHULE',
        'MITTELSCHULE',
        'NACHBARSCHAFTSTREFF'
    ));

ALTER TABLE isidbuser.sobon_orientierungswert_soziale_infrastruktur
    DROP CONSTRAINT sobon_orientierungswert_soziale_infrastru_einrichtungstyp_check;
ALTER TABLE isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ADD CONSTRAINT sobon_orientierungswert_soziale_infrastru_einrichtungstyp_check CHECK (einrichtungstyp IN (
        'UNSPECIFIED',
        'KINDERKRIPPE',
        'KINDERGARTEN',
        'GS_NACHMITTAG_BETREUUNG',
        'HAUS_FUER_KINDER',
        'GRUNDSCHULE',
        'MITTELSCHULE',
        'NACHBARSCHAFTSTREFF'
    ));

-- Doppelte Constraints entfernen: *_sobon_relevant_check1 ist identisch zu *_sobon_relevant_check.
ALTER TABLE isidbuser.bauleitplanverfahren
    DROP CONSTRAINT IF EXISTS bauleitplanverfahren_sobon_relevant_check1;
ALTER TABLE isidbuser.weiteres_verfahren
    DROP CONSTRAINT IF EXISTS weiteres_verfahren_sobon_relevant_check1;

-- Constraint-Namen an die mit V22 umbenannte Spalte mitzeichnung_beschlussentwurf angleichen.
ALTER TABLE isidbuser.bauleitplanverfahren
    RENAME CONSTRAINT bauleitplanverfahren_offizielle_mitzeichnung_check TO bauleitplanverfahren_mitzeichnung_beschlussentwurf_check;
ALTER TABLE isidbuser.weiteres_verfahren
    RENAME CONSTRAINT weiteres_verfahren_offizielle_mitzeichnung_check TO weiteres_verfahren_mitzeichnung_beschlussentwurf_check;

COMMIT;

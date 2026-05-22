BEGIN;

--
-- Verfahrensstand
--

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
DROP CONSTRAINT bauvorhaben_verfahrensstand_check;

UPDATE isidbuser.bauvorhaben
SET verfahrensstand = CASE
    WHEN verfahrensstand = 'VORBEREITUNG_ECKDATENBESCHLUSS' THEN 'FREIE_EINGABE'
    WHEN verfahrensstand = 'VORBEREITUNG_FRUEHZEITIGE_BETEILIGUNG' THEN 'VORBEREITUNG_FRUEHZEITIGE_BETEILIGUNG'
    WHEN verfahrensstand = 'VORBEREITUNG_WETTBEWERBAUSLOBUNG' THEN 'SIMULIERT_VORBEREITUNG_WETTBEWERBAUSLOBUNG'
    WHEN verfahrensstand = 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS' THEN 'SIMULIERT_VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'
    WHEN verfahrensstand = 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG' THEN 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'
    WHEN verfahrensstand = 'VORBEREITUNG_SATZUNGSBESCHLUSS' THEN 'VORBEREITUNG_SATZUNGSBESCHLUSS'
    WHEN verfahrensstand = 'VORLIEGENDER_SATZUNGSBESCHLUSS' THEN 'FREIE_EINGABE'
    WHEN verfahrensstand = 'RECHTSVERBINDLICHKEIT_AMTSBLATT' THEN 'INKRAFTGETRETEN_VEROEFFENTLICHUNG_AMTSBLATT'
    WHEN verfahrensstand = 'AUFTEILUNGSPLAN' THEN 'INKRAFTGETRETEN_FOERDERMIXPLAN'
    WHEN verfahrensstand = 'VORBEREITUNG_VORBESCHEID' THEN 'VORBEREITUNG_VORBESCHEID'
    WHEN verfahrensstand = 'VORBEREITUNG_BAUGENEHMIGUNG' THEN 'VORBEREITUNG_BAUGENEHMIGUNG'
    WHEN verfahrensstand = 'VORABFRAGE_OHNE_KONKRETEN_STAND' THEN 'VORABFRAGE_OHNE_KONKRETEN_STAND'
    WHEN verfahrensstand = 'STRUKTURKONZEPT' THEN 'STRUKTURKONZEPT'
    WHEN verfahrensstand = 'RAHMENPLANUNG' THEN 'RAHMENPLANUNG'
    WHEN verfahrensstand = 'POTENTIALUNTERSUCHUNG' THEN 'POTENTIALUNTERSUCHUNG'
    WHEN verfahrensstand = 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME' THEN 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'
    WHEN verfahrensstand = 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME' THEN 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'
    WHEN verfahrensstand = 'FREIE_EINGABE' THEN 'FREIE_EINGABE'
    ELSE verfahrensstand
END;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD CONSTRAINT bauvorhaben_verfahrensstand_check CHECK (verfahrensstand::text = ANY (ARRAY['UNSPECIFIED'::character varying::text, 'SIMULIERT_VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying::text, 'SIMULIERT_VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying::text, 'VORBEREITUNG_FRUEHZEITIGE_BETEILIGUNG'::character varying::text, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying::text, 'VORBEREITUNG_SATZUNGSBESCHLUSS'::character varying::text, 'INKRAFTGETRETEN_VEROEFFENTLICHUNG_AMTSBLATT'::character varying::text, 'INKRAFTGETRETEN_FOERDERMIXPLAN'::character varying::text, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying::text, 'VORBEREITUNG_VORBESCHEID'::character varying::text, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying::text, 'STRUKTURKONZEPT'::character varying::text, 'RAHMENPLANUNG'::character varying::text, 'POTENTIALUNTERSUCHUNG'::character varying::text, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying::text, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying::text, 'STANDORTABFRAGE'::character varying::text, 'FREIE_EINGABE'::character varying::text]));

--
-- Wesentliche Rechtsgrundlage
--

ALTER TABLE IF EXISTS isidbuser.bauvorhaben_wesentliche_rechtsgrundlage
DROP CONSTRAINT bauvorhaben_wesentliche_recht_wesentliche_rechtsgrundlage_check;

UPDATE isidbuser.bauvorhaben_wesentliche_rechtsgrundlage
SET wesentliche_rechtsgrundlage =
    CASE
        WHEN wesentliche_rechtsgrundlage = 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30' THEN 'EINFACHER_BEBAUUNGSPLAN'
        WHEN wesentliche_rechtsgrundlage = 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35' THEN 'EINFACHER_BEBAUUNGSPLAN'
        WHEN wesentliche_rechtsgrundlage = 'QUALIFIZIERTER_BEBAUUNGSPLAN' THEN 'QUALIFIZIERTER_BEBAUUNGSPLAN'
        WHEN wesentliche_rechtsgrundlage = 'VORHABENSBEZOGENER_BEBAUUNGSPLAN' THEN 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'
        WHEN wesentliche_rechtsgrundlage = 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9' THEN 'BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG'
        WHEN wesentliche_rechtsgrundlage = 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35' THEN 'BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG'
        WHEN wesentliche_rechtsgrundlage = 'INNENBEREICH' THEN 'INNENBEREICH'
        WHEN wesentliche_rechtsgrundlage = 'AUSSENBEREICH' THEN 'AUSSENBEREICH'
        WHEN wesentliche_rechtsgrundlage = 'BEFREIUNG' THEN 'BEFREIUNG'
        WHEN wesentliche_rechtsgrundlage = 'FREIE_EINGABE' THEN 'FREIE_EINGABE'
        ELSE wesentliche_rechtsgrundlage
    END;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben_wesentliche_rechtsgrundlage
    ADD CONSTRAINT bauvorhaben_wesentliche_recht_wesentliche_rechtsgrundlage_check CHECK (wesentliche_rechtsgrundlage::text = ANY (ARRAY['EINFACHER_BEBAUUNGSPLAN'::character varying::text, 'QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying::text, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying::text, 'BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG'::character varying::text, 'INNENBEREICH'::character varying::text, 'AUSSENBEREICH'::character varying::text, 'BEFREIUNG'::character varying::text, 'FREIE_EINGABE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD COLUMN wesentliche_rechtsgrundlage_angaben_zur_befreiung character varying(1000);

END;

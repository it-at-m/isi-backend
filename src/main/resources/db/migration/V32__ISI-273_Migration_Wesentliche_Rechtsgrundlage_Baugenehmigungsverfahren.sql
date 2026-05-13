BEGIN;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn_wesentliche_rechtsgrundlage
DROP CONSTRAINT abfragevariante_baugenehmigun_wesentliche_rechtsgrundlage_check;

UPDATE isidbuser.abfrgvar_baugnhmgsverfhrn_wesentliche_rechtsgrundlage
SET wesentliche_rechtsgrundlage =
    CASE
        WHEN wesentliche_rechtsgrundlage = 'QUALIFIZIERTER_BEBAUUNGSPLAN' THEN 'BEPLANTER_BEREICH_PARAGRAPH_30'
        WHEN wesentliche_rechtsgrundlage = 'VORHABENSBEZOGENER_BEBAUUNGSPLAN' THEN 'BEPLANTER_BEREICH_PARAGRAPH_30'
        WHEN wesentliche_rechtsgrundlage = 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35' THEN 'BEPLANTER_BEREICH_PARAGRAPH_30'
        WHEN wesentliche_rechtsgrundlage = 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35' THEN 'BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG_PARAGRAPH_9_IVM_34'
        WHEN wesentliche_rechtsgrundlage = 'INNENBEREICH' THEN 'INNENBEREICH'
        WHEN wesentliche_rechtsgrundlage = 'AUSSENBEREICH' THEN 'AUSSENBEREICH'
        WHEN wesentliche_rechtsgrundlage = 'BEFREIUNG' THEN 'BEPLANTER_BEREICH_PARAGRAPH_30_MIT_BEFREIUNG_PARAGRAPH_31'
        WHEN wesentliche_rechtsgrundlage = 'FREIE_EINGABE' THEN 'FREIE_EINGABE'
        ELSE wesentliche_rechtsgrundlage
    END;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn_wesentliche_rechtsgrundlage
    ADD CONSTRAINT abfragevariante_baugenehmigun_wesentliche_rechtsgrundlage_check CHECK (wesentliche_rechtsgrundlage::text = ANY (ARRAY['BEPLANTER_BEREICH_PARAGRAPH_30'::character varying::text, 'INNENBEREICH'::character varying::text, 'AUSSENBEREICH'::character varying::text, 'BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG_PARAGRAPH_9_IVM_34'::character varying::text, 'BEPLANTER_BEREICH_PARAGRAPH_30_MIT_BEFREIUNG_PARAGRAPH_31'::character varying::text, 'FREIE_EINGABE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn_wesentliche_rechtsgrundlage
    ADD COLUMN wesentliche_rechtsgrundlage_angaben_zur_befreiung character varying(1000);

END;

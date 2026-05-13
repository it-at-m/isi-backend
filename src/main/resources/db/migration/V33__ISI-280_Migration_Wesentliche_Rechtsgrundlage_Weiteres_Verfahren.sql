BEGIN;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn_wesentliche_rechtsgrundlage
DROP CONSTRAINT abfragevariante_weiteres_verf_wesentliche_rechtsgrundlage_check;

UPDATE isidbuser.abfrgvar_weitrs_vrfhrn_wesentliche_rechtsgrundlage
SET wesentliche_rechtsgrundlage =
    CASE
        WHEN wesentliche_rechtsgrundlage = 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35' THEN 'EINFACHER_BEBAUUNGSPLAN'
        WHEN wesentliche_rechtsgrundlage = 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30' THEN 'EINFACHER_BEBAUUNGSPLAN'
        WHEN wesentliche_rechtsgrundlage = 'QUALIFIZIERTER_BEBAUUNGSPLAN' THEN 'QUALIFIZIERTER_BEBAUUNGSPLAN'
        WHEN wesentliche_rechtsgrundlage = 'VORHABENSBEZOGENER_BEBAUUNGSPLAN' THEN 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'
        WHEN wesentliche_rechtsgrundlage = 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35' THEN 'BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG'
        WHEN wesentliche_rechtsgrundlage = 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9' THEN 'BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG'
        WHEN wesentliche_rechtsgrundlage = 'INNENBEREICH' THEN 'INNENBEREICH'
        WHEN wesentliche_rechtsgrundlage = 'AUSSENBEREICH' THEN 'AUSSENBEREICH'
        WHEN wesentliche_rechtsgrundlage = 'BEFREIUNG' THEN 'BEFREIUNG'
        WHEN wesentliche_rechtsgrundlage = 'FREIE_EINGABE' THEN 'FREIE_EINGABE'
        ELSE wesentliche_rechtsgrundlage
    END;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn_wesentliche_rechtsgrundlage
    ADD CONSTRAINT abfragevariante_weiteres_verf_wesentliche_rechtsgrundlage_check CHECK (wesentliche_rechtsgrundlage::text = ANY (ARRAY['EINFACHER_BEBAUUNGSPLAN'::character varying::text, 'QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying::text, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying::text, 'BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG'::character varying::text, 'INNENBEREICH'::character varying::text, 'AUSSENBEREICH'::character varying::text, 'BEFREIUNG'::character varying::text, 'FREIE_EINGABE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn
    ADD COLUMN wesentliche_rechtsgrundlage_angaben_zur_befreiung character varying(1000);

END;

BEGIN;

---
-- Ändern Wesentliche Rechtsgrundlage -> Planart
---
ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn_wesentliche_rechtsgrundlage
DROP CONSTRAINT abfragevariante_bauleitplanve_wesentliche_rechtsgrundlage_check;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn_wesentliche_rechtsgrundlage RENAME TO abfrgvar_bauleitplnvrfhrn_planart;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn_planart
    RENAME COLUMN wesentliche_rechtsgrundlage TO planart;

CREATE VIEW isidbuser.abfrgvar_bauleitplnvrfhrn_wesentliche_rechtsgrundlage AS
SELECT
    abfrgvar_bauleitplnvrfhrn_id,
    planart AS wesentliche_rechtsgrundlage
from isidbuser.abfrgvar_bauleitplnvrfhrn_planart;

DROP VIEW isidbuser.abfrgvar_bauleitplnvrfhrn_wesentliche_rechtsgrundlage;

UPDATE isidbuser.abfrgvar_bauleitplnvrfhrn_planart
SET planart = CASE
                  WHEN planart = 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30' THEN 'EINFACHER_BEBAUUNGSPLAN'
                  WHEN planart = 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9' THEN 'BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG'
                  ELSE planart
    END;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn_planart
    ADD CONSTRAINT abfragevariante_bauleitplanve_planart_check CHECK (planart::text = ANY (ARRAY['EINFACHER_BEBAUUNGSPLAN'::character varying::text, 'QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying::text, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying::text, 'BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG'::character varying::text, 'FREIE_EINGABE'::character varying::text]));

END;
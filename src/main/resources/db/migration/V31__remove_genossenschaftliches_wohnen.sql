ALTER TABLE isidbuser.abfragevariante_baugenehmigungsverfahren
    DROP COLUMN IF EXISTS gf_wohnen_genossenschaftliches_wohnen,
    DROP COLUMN IF EXISTS we_genossenschaftliches_wohnen;

ALTER TABLE isidbuser.abfragevariante_bauleitplanverfahren
    DROP COLUMN IF EXISTS gf_wohnen_genossenschaftliches_wohnen,
    DROP COLUMN IF EXISTS we_genossenschaftliches_wohnen;

ALTER TABLE isidbuser.abfragevariante_weiteres_verfahren
    DROP COLUMN IF EXISTS gf_wohnen_genossenschaftliches_wohnen,
    DROP COLUMN IF EXISTS we_genossenschaftliches_wohnen;

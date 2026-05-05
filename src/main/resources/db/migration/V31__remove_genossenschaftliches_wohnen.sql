ALTER TABLE isidbuser.abfrgvar_baugnhmgsverfhrn
    DROP COLUMN IF EXISTS gf_wohnen_genossenschaftliches_wohnen,
    DROP COLUMN IF EXISTS we_genossenschaftliches_wohnen;

ALTER TABLE isidbuser.abfrgvar_bauleitplnvrfhrn
    DROP COLUMN IF EXISTS gf_wohnen_genossenschaftliches_wohnen,
    DROP COLUMN IF EXISTS we_genossenschaftliches_wohnen;

ALTER TABLE isidbuser.abfrgvar_weitrs_vrfhrn
    DROP COLUMN IF EXISTS gf_wohnen_genossenschaftliches_wohnen,
    DROP COLUMN IF EXISTS we_genossenschaftliches_wohnen;
 
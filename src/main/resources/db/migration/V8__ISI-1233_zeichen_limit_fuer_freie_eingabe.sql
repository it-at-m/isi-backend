ALTER TABLE IF EXISTS isidbuser.abfragevariante_baugenehmigungsverfahren
    ALTER COLUMN wesentliche_rechtsgrundlage_freie_eingabe TYPE character varying(1000);

ALTER TABLE IF EXISTS isidbuser.abfragevariante_baugenehmigungsverfahren
    ALTER COLUMN anmerkung TYPE character varying(1000);

ALTER TABLE IF EXISTS isidbuser.abfragevariante_baugenehmigungsverfahren
    ALTER COLUMN hinweis_versorgung TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.abfragevariante_bauleitplanverfahren 
    ALTER COLUMN wesentliche_rechtsgrundlage_freie_eingabe TYPE character varying(1000);

ALTER TABLE IF EXISTS isidbuser.abfragevariante_bauleitplanverfahren
    ALTER COLUMN anmerkung TYPE character varying(1000);

ALTER TABLE IF EXISTS isidbuser.abfragevariante_bauleitplanverfahren
    ALTER COLUMN hinweis_versorgung TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.abfragevariante_weiteres_verfahren 
    ALTER COLUMN wesentliche_rechtsgrundlage_freie_eingabe TYPE character varying(1000);

ALTER TABLE IF EXISTS isidbuser.abfragevariante_weiteres_verfahren
    ALTER COLUMN anmerkung TYPE character varying(1000);

ALTER TABLE IF EXISTS isidbuser.abfragevariante_weiteres_verfahren
    ALTER COLUMN hinweis_versorgung TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    ALTER COLUMN stand_verfahren_freie_eingabe TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ALTER COLUMN stand_verfahren_freie_eingabe TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ALTER COLUMN stand_verfahren_freie_eingabe TYPE character varying(1000);

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ALTER COLUMN anmerkung TYPE character varying(1000);

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ALTER COLUMN wesentliche_rechtsgrundlage_freie_eingabe TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ALTER COLUMN stand_verfahren_freie_eingabe TYPE character varying(1000);
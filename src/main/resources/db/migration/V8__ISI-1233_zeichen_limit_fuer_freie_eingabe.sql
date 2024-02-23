ALTER TABLE IF EXISTS isidbuser.abfragevariante_bauleitplanverfahren_angelegt_dto
    ALTER COLUMN wesentliche_rechtsgrundlage_freie_eingabe TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.bauvorhaben_dto
    ALTER COLUMN stand_verfahren_freie_eingabe TYPE character varying(1000);

ALTER TABLE IF EXISTS isidbuser.bauvorhaben_dto
    ALTER COLUMN anmerkung TYPE character varying(1000);

ALTER TABLE IF EXISTS isidbuser.bauvorhaben_dto
    ALTER COLUMN wesentliche_rechtsgrundlage_freie_eingabe TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.abfrage_angelegt_dto
    ALTER COLUMN anmerkung TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.abfragevariante_baugenehmigungsverfahren_angelegt_dto
    ALTER COLUMN wesentliche_rechtsgrundlage_freie_eingabe TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.abfragevariante_weiteres_verfahren_angelegt
    ALTER COLUMN wesentliche_rechtsgrundlage_freie_eingabe TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren_angelegt_dto
    ALTER COLUMN stand_verfahren_freie_eingabe TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren_angelegt_dto
    ALTER COLUMN stand_verfahren_freie_eingabe TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren_angelegt_dto
    ALTER COLUMN stand_verfahren_freie_eingabe TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.abfragevariante_baugenehmigungsverfahren_in_bearbeitung_sachbearbeitung_dto
    ALTER COLUMN anmerkung TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.abfragevariante_baugenehmigungsverfahren_sachbearbeitung_in_bearbeitung_sachbearbeitung_dto
    ALTER COLUMN anmerkung TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.abfragevariante_bauleitplanverfahren_in_bearbeitung_sachbearbeitung_dto
    ALTER COLUMN anmerkung TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.abfragevariante_bauleitplanverfahren_sachbearbeitung_in_bearbeitung_sachbearbeitung_dto
    Alter COLUMN anmerkung TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.abfragevariante_weiteres_verfahren_in_bearbeitung_sachbearbeitung_dto
    ALTER COLUMN anmerkung TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.abfragevariante_weiteres_verfahren_sachbearbeitung_in_bearbeitung_sachbearbeitung_dto
    ALTER COLUMN anmerkung TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

ALTER TABLE IF EXISTS isidbuser.abfrage
    ALTER COLUMN anmerkung TYPE character varying(1000);

------------------------------------------------------------------------------------------------------

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
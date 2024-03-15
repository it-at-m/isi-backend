--
-- Hinzufügen der Bearbeitungsinformationen zu Bauvorhaben und Infrastruktureinrichtungen
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD COLUMN bearbeitende_person_email character varying(255),
    ADD COLUMN bearbeitende_person_name character varying(255),
    ADD COLUMN bearbeitende_person_organisationseinheit character varying(255);

ALTER TABLE IF EXISTS isidbuser.infrastruktureinrichtung
    ADD COLUMN bearbeitende_person_email character varying(255),
    ADD COLUMN bearbeitende_person_name character varying(255),
    ADD COLUMN bearbeitende_person_organisationseinheit character varying(255);

END;

--
-- Tabelle für Bearbeitungshistorie bei Abfragen
--

CREATE TABLE isidbuser.abfrage_bearbeitungshistorie (
    abfrage_id character varying(36) COLLATE pg_catalog."default" NOT NULL,
    bearbeitende_person_email character varying(255) COLLATE pg_catalog."default",
    bearbeitende_person_name character varying(255) COLLATE pg_catalog."default",
    bearbeitende_person_organisationseinheit character varying(255) COLLATE pg_catalog."default",
    status_abfrage character varying(255) COLLATE pg_catalog."default",
    zeitpunkt timestamp without time zone
);

CREATE INDEX abfrage_bearbeitungshistorie_abfrage_id_index ON isidbuser.abfrage_bearbeitungshistorie USING btree (abfrage_id);
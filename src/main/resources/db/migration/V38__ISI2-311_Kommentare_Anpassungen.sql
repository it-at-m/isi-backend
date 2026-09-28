--
--
-- Neue Felder Im Kommentar im Bauvorhaben
--      Letzte Bearbeitung
--      Letzter Bearbeiter
--      Datum -> Erstellungsdatum
--
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.kommentar
    ADD COLUMN erstellungsdatum timestamp without time zone,
    ADD COLUMN bearbeitende_person_email character varying(255),
    ADD COLUMN bearbeitende_person_name character varying(255),
    ADD COLUMN bearbeitende_person_organisationseinheit character varying(255);

update isidbuser.kommentar
set erstellungsdatum = CURRENT_DATE,
    text = CONCAT_WS(chr(10), NULLIF(datum, ''), text),
    bearbeitende_person_name = 'ITM-ISI';

ALTER TABLE IF EXISTS isidbuser.kommentar
    DROP COLUMN IF EXISTS datum;

END;

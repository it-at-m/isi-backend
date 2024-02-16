--
-- Neue Option "Vorbereitung Satzungsbeschluss" für Stand des Verfahrens entsprechend ISI-1254.
-- Betrifft Bauleitplanverfahren und Bauvorhaben.
--

BEGIN;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    DROP CONSTRAINT IF EXISTS bauvorhaben_stand_verfahren_check;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD CONSTRAINT bauvorhaben_stand_verfahren_check CHECK (stand_verfahren::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying, 'VORBEREITUNG_SATZUNGSBESCHLUSS'::character varying, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying, 'AUFTEILUNGSPLAN'::character varying, 'VORBEREITUNG_VORBESCHEID'::character varying, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying, 'STRUKTURKONZEPT'::character varying, 'RAHMENPLANUNG'::character varying, 'POTENTIALUNTERSUCHUNG'::character varying, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying, 'STANDORTABFRAGE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    DROP CONSTRAINT IF EXISTS bauleitplanverfahren_stand_verfahren_check;

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD CONSTRAINT bauleitplanverfahren_stand_verfahren_check CHECK (stand_verfahren::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying, 'VORBEREITUNG_SATZUNGSBESCHLUSS'::character varying, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying, 'AUFTEILUNGSPLAN'::character varying, 'VORBEREITUNG_VORBESCHEID'::character varying, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying, 'STRUKTURKONZEPT'::character varying, 'RAHMENPLANUNG'::character varying, 'POTENTIALUNTERSUCHUNG'::character varying, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying, 'STANDORTABFRAGE'::character varying]::text[]));

END;

--
-- Hinzufügen der freien Eingabe samt Anmerkungsfeld bei Art der
-- baulichen Nutzung in Baugebiet bei Abfrage und im Bauvorhaben bei artFnp.
--
BEGIN;

--
-- Anpassung Baugebiet
--
ALTER TABLE IF EXISTS isidbuser.baugebiet
    DROP CONSTRAINT baugebiet_art_bauliche_nutzung_check;

-- Hinzufügen der Freien Eingabe am Ende der Enums sowie des Eingabefeldes
ALTER TABLE IF EXISTS isidbuser.baugebiet
    ADD COLUMN art_bauliche_nutzung_freie_eingabe character varying(1000),
    ADD CONSTRAINT baugebiet_art_bauliche_nutzung_check CHECK (art_bauliche_nutzung >= 0 AND art_bauliche_nutzung <= 8);


--
-- Anpassung Bauvorhaben
--
ALTER TABLE IF EXISTS isidbuser.bauvorhaben_art_fnp
    DROP CONSTRAINT bauvorhaben_art_fnp_art_fnp_check;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben_art_fnp
    ADD CONSTRAINT bauvorhaben_art_fnp_art_fnp_check CHECK (art_fnp::text = ANY (ARRAY['WR'::character varying, 'WA'::character varying, 'MU'::character varying, 'MK'::character varying, 'MI'::character varying, 'GE'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD COLUMN art_fnp_freie_eingabe character varying(1000);

END;
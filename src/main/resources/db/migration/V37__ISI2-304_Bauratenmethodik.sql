--
-- Hinzufügen Start 4.2-Verfahren / Bauratenmethodik-Vorbelegung (Bauleitplanverfahren)
-- und Bauratenmethodik (SobonBerechnung, eingebettet in Bauleitplanverfahren und Weiteres Verfahren)
--
BEGIN;

-- Bestehende Bauleitplanverfahren haben kein erfasstes Start-4.2-Verfahren-Datum, daher werden
-- sie als "Datum unbekannt" markiert, damit die Pflichtfeld-Kombination weiterhin erfüllt ist.
ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD COLUMN start_42_verfahren date,
    ADD COLUMN start_42_verfahren_datum_unbekannt boolean NOT NULL DEFAULT true,
    ADD COLUMN bauratenmethodik_vorbelegung varchar(255);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn
    ADD COLUMN bauratenmethodik varchar(255);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn
    ADD COLUMN bauratenmethodik varchar(255);

END;

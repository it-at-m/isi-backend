--
-- Neue Felder "isASobonBerechnung" und "sobonFoerdermix" für die abfragevariante entsprechend ISI-837.
-- Betrifft Bauleitplanverfahren und Weiteres Verfahren.
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.abfragevariante_bauleitplanverfahren
    ADD COLUMN is_a_sobon_berechnung             BOOLEAN,
    ADD COLUMN sobon_foerdermix_bezeichnung_jahr VARCHAR(255),
    ADD COLUMN sobon_foerdermix_bezeichnung      VARCHAR(80);

ALTER TABLE IF EXISTS isidbuser.abfragevariante_weiteres_verfahren
    ADD COLUMN is_a_sobon_berechnung             BOOLEAN,
    ADD COLUMN sobon_foerdermix_bezeichnung_jahr VARCHAR(255),
    ADD COLUMN sobon_foerdermix_bezeichnung      VARCHAR(80)
;

CREATE TABLE IF NOT EXISTS isidbuser.abfragevariante_bauleitplanverfahren_foerderarten
(
    abfragevariante_bauleitplanverfahren_id character varying(36) NOT NULL,
    anteil_prozent                          numeric(19, 2),
    bezeichnung                             character varying(255),
    CONSTRAINT fk_abfragevariante_bauleitplanverfahren_foerderarten FOREIGN KEY (abfragevariante_bauleitplanverfahren_id)
        REFERENCES isidbuser.abfragevariante_bauleitplanverfahren(id)
);

ALTER TABLE isidbuser.abfragevariante_bauleitplanverfahren_foerderarten
    OWNER TO isidbuser;

CREATE TABLE IF NOT EXISTS isidbuser.abfragevariante_weiteres_verfahren_foerderarten
(
    abfragevariante_weiteres_verfahren_id character varying(36) NOT NULL,
    anteil_prozent                        numeric(19, 2),
    bezeichnung                           character varying(255),
    CONSTRAINT fk_abfragevariante_weiteresverfahren_foerderarten FOREIGN KEY (abfragevariante_weiteres_verfahren_id)
        REFERENCES isidbuser.abfragevariante_weiteres_verfahren(id)
);

ALTER TABLE isidbuser.abfragevariante_weiteres_verfahren_foerderarten
    OWNER TO isidbuser;
END;
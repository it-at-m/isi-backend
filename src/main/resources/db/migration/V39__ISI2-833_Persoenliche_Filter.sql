BEGIN;

--
-- Persönliche Filter
--

CREATE TABLE isidbuser.personal_filter
(
    id                              character varying(36) PRIMARY KEY,
    version                         BIGINT,
    created_date_time               TIMESTAMP              NOT NULL,
    last_modified_date_time         TIMESTAMP              NOT NULL,

    personalid                      character varying(255) NOT NULL,
    filter_name                     character varying(255) NOT NULL,

    -- Embedded FilterSettings
    sort_by                         character varying(255) NOT NULL,
    sort_order                      character varying(255) NOT NULL,
    select_bauleitplanverfahren     BOOLEAN                NOT NULL,
    select_baugenehmigungsverfahren BOOLEAN                NOT NULL,
    select_weiteres_verfahren       BOOLEAN                NOT NULL,
    select_bauvorhaben              BOOLEAN                NOT NULL,
    select_grundschule              BOOLEAN                NOT NULL,
    select_gs_nachmittag_betreuung  BOOLEAN                NOT NULL,
    select_haus_fuer_kinder         BOOLEAN                NOT NULL,
    select_kindergarten             BOOLEAN                NOT NULL,
    select_kinderkrippe             BOOLEAN                NOT NULL,
    select_mittelschule             BOOLEAN                NOT NULL,
    realisierungsbeginn_von         INTEGER,
    realisierungsbeginn_bis         INTEGER,
    nur_eigene_abfragen             BOOLEAN,
    sobon_relevant                  character varying(255) NOT NULL,
    we_gesamt_von                   INTEGER,
    we_gesamt_bis                   INTEGER,
    gf_wohnen_geplant_von           DECIMAL(19, 2),
    gf_wohnen_geplant_bis           DECIMAL(19, 2),
    CONSTRAINT personal_filter_sobon_relevant_check CHECK (((sobon_relevant)::text <> 'UNSPECIFIED'::text))
);

CREATE TABLE isidbuser.personal_filter_stadtbezirk_nummer
(
    personal_filter_id character varying(36)  NOT NULL,
    stadtbezirk_nummer character varying(255) NOT NULL,
    FOREIGN KEY (personal_filter_id) REFERENCES personal_filter (id)
);

CREATE TABLE isidbuser.personal_filter_kitaplanungsbereich_kita_plbt
(
    personal_filter_id            character varying(36)  NOT NULL,
    kitaplanungsbereich_kita_plbt character varying(255) NOT NULL,
    FOREIGN KEY (personal_filter_id) REFERENCES personal_filter (id)
);

CREATE TABLE isidbuser.personal_filter_grundschulsprengel_nummer
(
    personal_filter_id        character varying(36) NOT NULL,
    grundschulsprengel_nummer BIGINT                NOT NULL,
    FOREIGN KEY (personal_filter_id) REFERENCES personal_filter (id)
);

CREATE TABLE isidbuser.personal_filter_mittelschulsprengel_nummer
(
    personal_filter_id         character varying(36) NOT NULL,
    mittelschulsprengel_nummer BIGINT                NOT NULL,
    FOREIGN KEY (personal_filter_id) REFERENCES personal_filter (id)
);

CREATE TABLE isidbuser.personal_filter_status_abfrage
(
    personal_filter_id character varying(36)  NOT NULL,
    status_abfrage     character varying(255) NOT NULL,
    FOREIGN KEY (personal_filter_id) REFERENCES personal_filter (id)
);

CREATE TABLE isidbuser.personal_filter_verfahrensstand
(
    personal_filter_id character varying(36)  NOT NULL,
    verfahrensstand    character varying(255) NOT NULL,
    FOREIGN KEY (personal_filter_id) REFERENCES personal_filter (id)
);

CREATE TABLE isidbuser.personal_filter_infrastruktureinrichtung_status
(
    personal_filter_id              character varying(36)  NOT NULL,
    infrastruktureinrichtung_status character varying(255) NOT NULL,
    FOREIGN KEY (personal_filter_id) REFERENCES personal_filter (id)
);

CREATE INDEX personal_filter_stadtbezirk_nummer_id_idx ON isidbuser.personal_filter_stadtbezirk_nummer (personal_filter_id);
CREATE INDEX personal_filter_kitaplanungsbereich_kita_plbt_id_idx ON isidbuser.personal_filter_kitaplanungsbereich_kita_plbt (personal_filter_id);
CREATE INDEX personal_filter_grundschulsprengel_nummer_id_idx ON isidbuser.personal_filter_grundschulsprengel_nummer (personal_filter_id);
CREATE INDEX personal_filter_mittelschulsprengel_nummer_id_idx ON isidbuser.personal_filter_mittelschulsprengel_nummer (personal_filter_id);
CREATE INDEX personal_filter_status_abfrage_id_idx ON isidbuser.personal_filter_status_abfrage (personal_filter_id);
CREATE INDEX personal_filter_verfahrensstand_id_idx ON isidbuser.personal_filter_verfahrensstand (personal_filter_id);
CREATE INDEX personal_filter_infrastruktureinrichtung_status_id_idx ON isidbuser.personal_filter_infrastruktureinrichtung_status (personal_filter_id);
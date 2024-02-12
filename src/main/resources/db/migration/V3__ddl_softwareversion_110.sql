--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 15.5

-- Started on 2024-02-06 12:39:09 CET

--
-- TOC entry 5 (class 2615 OID 16425)
-- Name: isidbuser; Type: SCHEMA; Schema: -; Owner: isidbuser
--

--
-- TOC entry 219 (class 1259 OID 19378)
-- Name: abfragevariante; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

SET default_tablespace = '';

SET default_table_access_method = heap;

CREATE TABLE isidbuser.abfragevariante (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    anmerkung character varying(255),
    geschossflaeche_wohnen_planungsursaechlich numeric(10,2),
    so_bonorientierungswert_jahr character varying(255),
    abfragevarianten_name character varying(30) NOT NULL,
    abfragevarianten_nr integer NOT NULL,
    anzahl_we_baurechtlich_festgesetzt integer,
    anzahl_we_baurechtlich_genehmigt integer,
    gesamtanzahl_we integer,
    geschossflaeche_genossenschaftliche_wohnungen numeric(10,2),
    geschossflaeche_seniorenwohnungen numeric(10,2),
    geschossflaeche_sonstiges numeric(10,2),
    geschossflaeche_studentenwohnungen numeric(10,2),
    geschossflaeche_wohnen numeric(10,2),
    geschossflaeche_wohnen_bestandswohnbaurecht numeric(10,2),
    geschossflaeche_wohnen_festgesetzt numeric(10,2),
    geschossflaeche_wohnen_genehmigt numeric(10,2),
    geschossflaeche_wohnen_so_bo_nursaechlich numeric(10,2),
    is_relevant boolean NOT NULL,
    planungsrecht character varying(255) NOT NULL,
    realisierung_von integer NOT NULL,
    satzungsbeschluss timestamp without time zone,
    sonderwohnformen boolean NOT NULL,
    abfrage_abfragevarianten_sachbearbeitung_id character varying(36),
    abfrage_abfragevarianten_id character varying(36)
);


ALTER TABLE isidbuser.abfragevariante OWNER TO isidbuser;

--
-- TOC entry 221 (class 1259 OID 19634)
-- Name: abfragevariante_baugenehmigungsverfahren; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.abfragevariante_baugenehmigungsverfahren (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    abfragevarianten_nr integer NOT NULL,
    name character varying(30) NOT NULL,
    anmerkung character varying(255),
    gf_wohnen_baurechtlich_festgesetzt numeric(10,2),
    gf_wohnen_baurechtlich_genehmigt numeric(10,2),
    gf_wohnen_bestandswohnbaurecht numeric(10,2),
    gf_wohnen_genossenschaftliches_wohnen numeric(10,2),
    gf_wohnen_gesamt numeric(10,2),
    gf_wohnen_planungsursaechlich numeric(10,2),
    gf_wohnen_seniorinnen_wohnen numeric(10,2),
    gf_wohnen_sonderwohnformen boolean NOT NULL,
    gf_wohnen_studentisches_wohnen numeric(10,2),
    gf_wohnen_weiteres_nicht_infrastrukturrelevantes_wohnen numeric(10,2),
    realisierung_von integer NOT NULL,
    sobon_orientierungswert_jahr character varying(255),
    we_baurechtlich_festgesetzt integer,
    we_baurechtlich_genehmigt integer,
    we_genossenschaftliches_wohnen integer,
    we_gesamt integer,
    we_seniorinnen_wohnen integer,
    we_sonderwohnformen boolean NOT NULL,
    we_studentisches_wohnen integer,
    we_weiteres_nicht_infrastrukturrelevantes_wohnen integer,
    wesentliche_rechtsgrundlage_freie_eingabe character varying(255),
    abfragevarianten_sachbearbeitung_baugenehmigungsverfahren_id character varying(36),
    abfragevarianten_baugenehmigungsverfahren_id character varying(36)
);


ALTER TABLE isidbuser.abfragevariante_baugenehmigungsverfahren OWNER TO isidbuser;

--
-- TOC entry 222 (class 1259 OID 19641)
-- Name: abfragevariante_baugenehmigungsverfahren_wesentliche_rechtsgrun; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.abfragevariante_baugenehmigungsverfahren_wesentliche_rechtsgrun (
    abfragevariante_baugenehmigungsverfahren_id character varying(36) NOT NULL,
    wesentliche_rechtsgrundlage character varying(255)
);


ALTER TABLE isidbuser.abfragevariante_baugenehmigungsverfahren_wesentliche_rechtsgrun OWNER TO isidbuser;

--
-- TOC entry 223 (class 1259 OID 19644)
-- Name: abfragevariante_bauleitplanverfahren; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.abfragevariante_bauleitplanverfahren (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    abfragevarianten_nr integer NOT NULL,
    name character varying(30) NOT NULL,
    anmerkung character varying(255),
    gf_wohnen_bestandswohnbaurecht numeric(10,2),
    gf_wohnen_genossenschaftliches_wohnen numeric(10,2),
    gf_wohnen_gesamt numeric(10,2),
    gf_wohnen_planungsursaechlich numeric(10,2),
    gf_wohnen_seniorinnen_wohnen numeric(10,2),
    gf_wohnen_sobon_ursaechlich numeric(10,2),
    gf_wohnen_sonderwohnformen boolean NOT NULL,
    gf_wohnen_studentisches_wohnen numeric(10,2),
    gf_wohnen_weiteres_nicht_infrastrukturrelevantes_wohnen numeric(10,2),
    realisierung_von integer NOT NULL,
    satzungsbeschluss timestamp without time zone,
    sobon_orientierungswert_jahr character varying(255),
    we_genossenschaftliches_wohnen integer,
    we_gesamt integer,
    we_seniorinnen_wohnen integer,
    we_sonderwohnformen boolean NOT NULL,
    we_studentisches_wohnen integer,
    we_weiteres_nicht_infrastrukturrelevantes_wohnen integer,
    wesentliche_rechtsgrundlage_freie_eingabe character varying(255),
    abfragevarianten_sachbearbeitung_bauleitplanverfahren_id character varying(36),
    abfragevarianten_bauleitplanverfahren_id character varying(36)
);


ALTER TABLE isidbuser.abfragevariante_bauleitplanverfahren OWNER TO isidbuser;

--
-- TOC entry 224 (class 1259 OID 19651)
-- Name: abfragevariante_bauleitplanverfahren_wesentliche_rechtsgrundlag; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.abfragevariante_bauleitplanverfahren_wesentliche_rechtsgrundlag (
    abfragevariante_bauleitplanverfahren_id character varying(36) NOT NULL,
    wesentliche_rechtsgrundlage character varying(255)
);


ALTER TABLE isidbuser.abfragevariante_bauleitplanverfahren_wesentliche_rechtsgrundlag OWNER TO isidbuser;

--
-- TOC entry 225 (class 1259 OID 19654)
-- Name: abfragevariante_weiteres_verfahren; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.abfragevariante_weiteres_verfahren (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    abfragevarianten_nr integer NOT NULL,
    name character varying(30) NOT NULL,
    anmerkung character varying(255),
    gf_wohnen_baurechtlich_festgesetzt numeric(10,2),
    gf_wohnen_baurechtlich_genehmigt numeric(10,2),
    gf_wohnen_bestandswohnbaurecht numeric(10,2),
    gf_wohnen_genossenschaftliches_wohnen numeric(10,2),
    gf_wohnen_gesamt numeric(10,2),
    gf_wohnen_planungsursaechlich numeric(10,2),
    gf_wohnen_seniorinnen_wohnen numeric(10,2),
    gf_wohnen_sobon_ursaechlich numeric(10,2),
    gf_wohnen_sonderwohnformen boolean NOT NULL,
    gf_wohnen_studentisches_wohnen numeric(10,2),
    gf_wohnen_weiteres_nicht_infrastrukturrelevantes_wohnen numeric(10,2),
    realisierung_von integer NOT NULL,
    satzungsbeschluss timestamp without time zone,
    sobon_orientierungswert_jahr character varying(255),
    we_baurechtlich_festgesetzt integer,
    we_baurechtlich_genehmigt integer,
    we_genossenschaftliches_wohnen integer,
    we_gesamt integer,
    we_seniorinnen_wohnen integer,
    we_sonderwohnformen boolean NOT NULL,
    we_studentisches_wohnen integer,
    we_weiteres_nicht_infrastrukturrelevantes_wohnen integer,
    wesentliche_rechtsgrundlage_freie_eingabe character varying(255),
    abfragevarianten_weiteres_verfahren_id character varying(36),
    abfragevarianten_sachbearbeitung_weiteres_verfahren_id character varying(36)
);


ALTER TABLE isidbuser.abfragevariante_weiteres_verfahren OWNER TO isidbuser;

--
-- TOC entry 226 (class 1259 OID 19661)
-- Name: abfragevariante_weiteres_verfahren_wesentliche_rechtsgrundlage; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.abfragevariante_weiteres_verfahren_wesentliche_rechtsgrundlage (
    abfragevariante_weiteres_verfahren_id character varying(36) NOT NULL,
    wesentliche_rechtsgrundlage character varying(255)
);


ALTER TABLE isidbuser.abfragevariante_weiteres_verfahren_wesentliche_rechtsgrundlage OWNER TO isidbuser;

--
-- TOC entry 227 (class 1259 OID 19664)
-- Name: bauabschnitt; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.bauabschnitt (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    bezeichnung character varying(255) NOT NULL,
    technical boolean NOT NULL,
    abfragevariante_weiteres_verfahren_id character varying(36),
    abfragevariante_bauleitplanverfahren_id character varying(36),
    abfragevariante_baugenehmigungsverfahren_id character varying(36)
);


ALTER TABLE isidbuser.bauabschnitt OWNER TO isidbuser;

--
-- TOC entry 228 (class 1259 OID 19669)
-- Name: baugebiet; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.baugebiet (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    art_bauliche_nutzung integer NOT NULL,
    bezeichnung character varying(255) NOT NULL,
    gf_wohnen_baurechtlich_festgesetzt numeric(10,2),
    gf_wohnen_baurechtlich_genehmigt numeric(10,2),
    gf_wohnen_geplant numeric(10,2),
    realisierung_von integer NOT NULL,
    technical boolean NOT NULL,
    we_baurechtlich_festgesetzt integer,
    we_baurechtlich_genehmigt integer,
    we_geplant integer,
    bauabschnitt_id character varying(36)
);


ALTER TABLE isidbuser.baugebiet OWNER TO isidbuser;

--
-- TOC entry 229 (class 1259 OID 19674)
-- Name: baugenehmigungsverfahren; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.baugenehmigungsverfahren (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    anmerkung character varying(255),
    name character varying(70) NOT NULL,
    status_abfrage character varying(255) NOT NULL,
    sub character varying(255) NOT NULL,
    bauvorhaben_id character varying(36),
    angabe_lage_ergaenzende_adressinformation character varying(255),
    latitude double precision,
    longitude double precision,
    hausnummer character varying(255),
    ort character varying(255),
    plz character varying(255),
    strasse character varying(255),
    aktenzeichen_pro_lbk character varying(255),
    bebauungsplannummer character varying(255),
    frist_bearbeitung timestamp without time zone NOT NULL,
    stand_verfahren character varying(255) NOT NULL,
    stand_verfahren_freie_eingabe character varying(255),
    verortung jsonb
);


ALTER TABLE isidbuser.baugenehmigungsverfahren OWNER TO isidbuser;

--
-- TOC entry 230 (class 1259 OID 19681)
-- Name: bauleitplanverfahren; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.bauleitplanverfahren (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    anmerkung character varying(255),
    name character varying(70) NOT NULL,
    status_abfrage character varying(255) NOT NULL,
    sub character varying(255) NOT NULL,
    bauvorhaben_id character varying(36),
    angabe_lage_ergaenzende_adressinformation character varying(255),
    latitude double precision,
    longitude double precision,
    hausnummer character varying(255),
    ort character varying(255),
    plz character varying(255),
    strasse character varying(255),
    bebauungsplannummer character varying(255),
    frist_bearbeitung timestamp without time zone NOT NULL,
    offizielle_mitzeichnung character varying(255) NOT NULL,
    sobon_jahr character varying(255),
    sobon_relevant character varying(255) NOT NULL,
    stand_verfahren character varying(255) NOT NULL,
    stand_verfahren_freie_eingabe character varying(255),
    verortung jsonb,
    CONSTRAINT bauleitplanverfahren_sobon_relevant_check CHECK (((sobon_relevant)::text <> 'UNSPECIFIED'::text)),
    CONSTRAINT bauleitplanverfahren_sobon_relevant_check1 CHECK (((sobon_relevant)::text <> 'UNSPECIFIED'::text))
);


ALTER TABLE isidbuser.bauleitplanverfahren OWNER TO isidbuser;

--
-- TOC entry 231 (class 1259 OID 19690)
-- Name: baurate; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.baurate (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    bezeichnung character varying(80) NOT NULL,
    bezeichnung_jahr character varying(255) NOT NULL,
    gf_wohnen_geplant numeric(10,2),
    jahr integer NOT NULL,
    we_geplant integer,
    baugebiet_id character varying(36)
);


ALTER TABLE isidbuser.baurate OWNER TO isidbuser;

--
-- TOC entry 232 (class 1259 OID 19695)
-- Name: baurate_foerderarten; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.baurate_foerderarten (
    baurate_id character varying(36) NOT NULL,
    anteil_prozent numeric(19,2),
    bezeichnung character varying(255)
);


ALTER TABLE isidbuser.baurate_foerderarten OWNER TO isidbuser;

--
-- TOC entry 233 (class 1259 OID 19698)
-- Name: bauvorhaben; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.bauvorhaben (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    angabe_lage_ergaenzende_adressinformation character varying(255),
    latitude double precision,
    longitude double precision,
    hausnummer character varying(255),
    ort character varying(255),
    plz character varying(255),
    strasse character varying(255),
    anmerkung character varying(255),
    bauvorhaben_nummer character varying(255),
    bebauungsplannummer character varying(255),
    fis_nummer character varying(255),
    grundstuecksgroesse numeric(10,2),
    name_vorhaben character varying(255) NOT NULL,
    sobon_jahr character varying(255),
    sobon_relevant character varying(255) NOT NULL,
    stand_verfahren character varying(255) NOT NULL,
    stand_verfahren_freie_eingabe character varying(255),
    verortung jsonb,
    wesentliche_rechtsgrundlage_freie_eingabe character varying(255),
    relevante_abfragevariante_id character varying(36),
    CONSTRAINT bauvorhaben_sobon_relevant_check CHECK (((sobon_relevant)::text <> 'UNSPECIFIED'::text))
);


ALTER TABLE isidbuser.bauvorhaben OWNER TO isidbuser;

--
-- TOC entry 234 (class 1259 OID 19706)
-- Name: bauvorhaben_art_fnp; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.bauvorhaben_art_fnp (
    bauvorhaben_id character varying(36) NOT NULL,
    art_fnp character varying(255)
);


ALTER TABLE isidbuser.bauvorhaben_art_fnp OWNER TO isidbuser;

--
-- TOC entry 235 (class 1259 OID 19709)
-- Name: bauvorhaben_wesentliche_rechtsgrundlage; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.bauvorhaben_wesentliche_rechtsgrundlage (
    bauvorhaben_id character varying(36) NOT NULL,
    wesentliche_rechtsgrundlage character varying(255)
);


ALTER TABLE isidbuser.bauvorhaben_wesentliche_rechtsgrundlage OWNER TO isidbuser;

--
-- TOC entry 236 (class 1259 OID 19712)
-- Name: bedarfsmeldung_fachreferate; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.bedarfsmeldung_fachreferate (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    anzahl_einrichtungen integer NOT NULL,
    anzahl_grundschulzuege integer,
    anzahl_hortgruppen integer,
    anzahl_kindergartengruppen integer,
    anzahl_kinderkrippengruppen integer,
    infrastruktureinrichtung_typ integer NOT NULL,
    abfragevariante_weiteres_verfahren_id character varying(36),
    abfragevariante_bauleitplanverfahren_id character varying(36),
    abfragevariante_baugenehmigungsverfahren_id character varying(36)
);


ALTER TABLE isidbuser.bedarfsmeldung_fachreferate OWNER TO isidbuser;

--
-- TOC entry 237 (class 1259 OID 19717)
-- Name: dokument; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.dokument (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    art_dokument character varying(255) NOT NULL,
    path_to_file character varying(255) NOT NULL,
    size_in_bytes bigint NOT NULL,
    typ_dokument character varying(255) NOT NULL,
    kommentar_id character varying(36),
    weiteres_verfahren_id character varying(36),
    bauvorhaben_id character varying(36),
    bauleitplanverfahren_id character varying(36),
    baugenehmigungsverfahren_id character varying(36)
);


ALTER TABLE isidbuser.dokument OWNER TO isidbuser;

--
-- TOC entry 215 (class 1259 OID 18620)
-- Name: flurstueck; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.flurstueck (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    eigentumsart bigint,
    eigentumsart_bedeutung character varying(255),
    flaeche_qm numeric(19,2),
    gemarkung_nummer numeric(19,2) NOT NULL,
    coordinates json NOT NULL,
    type character varying(255) NOT NULL,
    nenner bigint,
    nummer character varying(255),
    zaehler bigint,
    gemarkung_id character varying(36)
);


ALTER TABLE isidbuser.flurstueck OWNER TO isidbuser;

--
-- TOC entry 238 (class 1259 OID 19724)
-- Name: foerdermix_stamm; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.foerdermix_stamm (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    bezeichnung character varying(80) NOT NULL,
    bezeichnung_jahr character varying(255) NOT NULL
);


ALTER TABLE isidbuser.foerdermix_stamm OWNER TO isidbuser;

--
-- TOC entry 239 (class 1259 OID 19729)
-- Name: foerdermix_stamm_foerderarten; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.foerdermix_stamm_foerderarten (
    foerdermix_stamm_id character varying(36) NOT NULL,
    anteil_prozent numeric(19,2),
    bezeichnung character varying(255)
);


ALTER TABLE isidbuser.foerdermix_stamm_foerderarten OWNER TO isidbuser;

--
-- TOC entry 216 (class 1259 OID 18635)
-- Name: gemarkung; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.gemarkung (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    coordinates json NOT NULL,
    type character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    nummer numeric(19,2) NOT NULL,
    verortung_id character varying(36)
);


ALTER TABLE isidbuser.gemarkung OWNER TO isidbuser;

--
-- TOC entry 240 (class 1259 OID 19732)
-- Name: global_counter; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.global_counter (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    counter bigint NOT NULL,
    counter_type character varying(255) NOT NULL
);


ALTER TABLE isidbuser.global_counter OWNER TO isidbuser;

--
-- TOC entry 241 (class 1259 OID 19737)
-- Name: grundschule; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.grundschule (
    anzahl_klassen integer NOT NULL,
    anzahl_plaetze integer NOT NULL,
    einrichtungstraeger character varying(255),
    id character varying(36) NOT NULL
);


ALTER TABLE isidbuser.grundschule OWNER TO isidbuser;

--
-- TOC entry 242 (class 1259 OID 19742)
-- Name: gs_nachmittag_betreuung; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.gs_nachmittag_betreuung (
    anzahl_hort_gruppen integer NOT NULL,
    anzahl_hort_plaetze integer NOT NULL,
    art_gs_nachmittag_betreuung character varying(255),
    einrichtungstraeger character varying(255),
    wohnungsnahe_hort_plaetze integer,
    id character varying(36) NOT NULL
);


ALTER TABLE isidbuser.gs_nachmittag_betreuung OWNER TO isidbuser;

--
-- TOC entry 243 (class 1259 OID 19749)
-- Name: haus_fuer_kinder; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.haus_fuer_kinder (
    anzahl_hort_gruppen integer,
    anzahl_hort_plaetze integer,
    anzahl_kindergarten_gruppen integer,
    anzahl_kindergarten_plaetze integer,
    anzahl_kinderkrippe_gruppen integer,
    anzahl_kinderkrippe_plaetze integer,
    einrichtungstraeger character varying(255),
    wohnungsnahe_hort_plaetze integer,
    wohnungsnahe_kindergarten_plaetze integer,
    wohnungsnahe_kinderkrippe_plaetze integer,
    id character varying(36) NOT NULL
);


ALTER TABLE isidbuser.haus_fuer_kinder OWNER TO isidbuser;

--
-- TOC entry 244 (class 1259 OID 19754)
-- Name: idealtypische_baurate; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.idealtypische_baurate (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    bis_exklusiv numeric(19,2) NOT NULL,
    jahresraten jsonb NOT NULL,
    typ character varying(255) NOT NULL,
    von numeric(19,2) NOT NULL
);


ALTER TABLE isidbuser.idealtypische_baurate OWNER TO isidbuser;

--
-- TOC entry 220 (class 1259 OID 19456)
-- Name: infrastrukturabfrage; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.infrastrukturabfrage (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    latitude double precision,
    longitude double precision,
    hausnummer character varying(255),
    ort character varying(255),
    plz character varying(255),
    strasse character varying(255),
    allgemeine_ortsangabe character varying(255),
    anmerkung character varying(255),
    bebauungsplannummer character varying(255),
    frist_stellungnahme timestamp without time zone NOT NULL,
    name_abfrage character varying(70) NOT NULL,
    stand_vorhaben character varying(255) NOT NULL,
    status_abfrage character varying(255) NOT NULL,
    verortung jsonb,
    aktenzeichen_pro_lbk character varying(255),
    offizieller_verfahrensschritt character varying(255) NOT NULL,
    sobon_jahr character varying(255),
    sobon_relevant character varying(255) NOT NULL,
    bauvorhaben_id character varying(36),
    CONSTRAINT infrastrukturabfrage_offizieller_verfahrensschritt_check CHECK (((offizieller_verfahrensschritt)::text <> 'UNSPECIFIED'::text)),
    CONSTRAINT infrastrukturabfrage_sobon_relevant_check CHECK (((sobon_relevant)::text <> 'UNSPECIFIED'::text))
);


ALTER TABLE isidbuser.infrastrukturabfrage OWNER TO isidbuser;

--
-- TOC entry 246 (class 1259 OID 19762)
-- Name: infrastruktureinrichtung; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.infrastruktureinrichtung (
    infrastruktureinrichtung_typ character varying(31) NOT NULL,
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    angabe_lage_ergaenzende_adressinformation character varying(255),
    latitude double precision,
    longitude double precision,
    hausnummer character varying(255),
    ort character varying(255),
    plz character varying(255),
    strasse character varying(255),
    fertigstellungsjahr integer,
    flaeche_gesamtgrundstueck numeric(10,2),
    flaeche_teilgrundstueck numeric(10,2),
    lfd_nr integer NOT NULL,
    name_einrichtung character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    bauvorhaben_id character varying(36)
);


ALTER TABLE isidbuser.infrastruktureinrichtung OWNER TO isidbuser;

--
-- TOC entry 245 (class 1259 OID 19761)
-- Name: infrastruktureinrichtung_lfd_nr_seq; Type: SEQUENCE; Schema: isidbuser; Owner: isidbuser
--

CREATE SEQUENCE isidbuser.infrastruktureinrichtung_lfd_nr_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE isidbuser.infrastruktureinrichtung_lfd_nr_seq OWNER TO isidbuser;

--
-- TOC entry 3727 (class 0 OID 0)
-- Dependencies: 245
-- Name: infrastruktureinrichtung_lfd_nr_seq; Type: SEQUENCE OWNED BY; Schema: isidbuser; Owner: isidbuser
--

ALTER SEQUENCE isidbuser.infrastruktureinrichtung_lfd_nr_seq OWNED BY isidbuser.infrastruktureinrichtung.lfd_nr;


--
-- TOC entry 247 (class 1259 OID 19770)
-- Name: kindergarten; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.kindergarten (
    anzahl_kindergarten_gruppen integer NOT NULL,
    anzahl_kindergarten_plaetze integer NOT NULL,
    einrichtungstraeger character varying(255),
    wohnungsnahe_kindergarten_plaetze integer,
    id character varying(36) NOT NULL
);


ALTER TABLE isidbuser.kindergarten OWNER TO isidbuser;

--
-- TOC entry 248 (class 1259 OID 19775)
-- Name: kinderkrippe; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.kinderkrippe (
    anzahl_kinderkrippe_gruppen integer NOT NULL,
    anzahl_kinderkrippe_plaetze integer NOT NULL,
    einrichtungstraeger character varying(255),
    wohnungsnahe_kinderkrippe_plaetze integer,
    id character varying(36) NOT NULL
);


ALTER TABLE isidbuser.kinderkrippe OWNER TO isidbuser;

--
-- TOC entry 249 (class 1259 OID 19780)
-- Name: kommentar; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.kommentar (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    datum character varying(32),
    text text,
    bauvorhaben_id character varying(36),
    infrastruktureinrichtung_id character varying(36)
);


ALTER TABLE isidbuser.kommentar OWNER TO isidbuser;

--
-- TOC entry 250 (class 1259 OID 19787)
-- Name: mittelschule; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.mittelschule (
    anzahl_klassen integer NOT NULL,
    anzahl_plaetze integer NOT NULL,
    einrichtungstraeger character varying(255),
    id character varying(36) NOT NULL
);


ALTER TABLE isidbuser.mittelschule OWNER TO isidbuser;

--
-- TOC entry 251 (class 1259 OID 19792)
-- Name: sobon_orientierungswert_soziale_infrastruktur; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.sobon_orientierungswert_soziale_infrastruktur (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    altersklasse character varying(255) NOT NULL,
    einrichtungstyp character varying(255) NOT NULL,
    einwohner_jahr10nach_ersterstellung numeric(10,4) NOT NULL,
    einwohner_jahr1nach_ersterstellung numeric(10,4) NOT NULL,
    einwohner_jahr2nach_ersterstellung numeric(10,4) NOT NULL,
    einwohner_jahr3nach_ersterstellung numeric(10,4) NOT NULL,
    einwohner_jahr4nach_ersterstellung numeric(10,4) NOT NULL,
    einwohner_jahr5nach_ersterstellung numeric(10,4) NOT NULL,
    einwohner_jahr6nach_ersterstellung numeric(10,4) NOT NULL,
    einwohner_jahr7nach_ersterstellung numeric(10,4) NOT NULL,
    einwohner_jahr8nach_ersterstellung numeric(10,4) NOT NULL,
    einwohner_jahr9nach_ersterstellung numeric(10,4) NOT NULL,
    foerderart_bezeichnung character varying(255) NOT NULL,
    gueltig_ab timestamp without time zone NOT NULL,
    stammwert_arbeitsgruppe numeric(10,4) NOT NULL
);


ALTER TABLE isidbuser.sobon_orientierungswert_soziale_infrastruktur OWNER TO isidbuser;

--
-- TOC entry 217 (class 1259 OID 18712)
-- Name: stadtbezirk; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.stadtbezirk (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    coordinates json NOT NULL,
    type character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    nummer character varying(255) NOT NULL,
    verortung_id character varying(36)
);


ALTER TABLE isidbuser.stadtbezirk OWNER TO isidbuser;

--
-- TOC entry 252 (class 1259 OID 19799)
-- Name: staedtebauliche_orientierungswert; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.staedtebauliche_orientierungswert (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    belegungsdichte numeric(10,2) NOT NULL,
    durchschnittliche_grundflaeche bigint NOT NULL,
    foerderart_bezeichnung character varying(255) NOT NULL,
    gueltig_ab timestamp without time zone NOT NULL
);


ALTER TABLE isidbuser.staedtebauliche_orientierungswert OWNER TO isidbuser;

--
-- TOC entry 218 (class 1259 OID 18726)
-- Name: verortung; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.verortung (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    coordinates json NOT NULL,
    type character varying(255) NOT NULL
);


ALTER TABLE isidbuser.verortung OWNER TO isidbuser;

--
-- TOC entry 253 (class 1259 OID 19804)
-- Name: versorgungsquote_gruppenstaerke; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.versorgungsquote_gruppenstaerke (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    gruppenstaerke integer NOT NULL,
    gueltig_ab timestamp without time zone NOT NULL,
    infrastruktureinrichtung_typ character varying(255) NOT NULL,
    versorgungsquote_planungsursaechlich numeric(4,3) NOT NULL,
    versorgungsquote_sobon_ursaechlich numeric(4,3) NOT NULL,
    CONSTRAINT versorgungsquote_gruppenstae_versorgungsquote_planungsurs_check CHECK (((versorgungsquote_planungsursaechlich >= (0)::numeric) AND (versorgungsquote_planungsursaechlich <= (1)::numeric))),
    CONSTRAINT versorgungsquote_gruppenstae_versorgungsquote_sobon_ursae_check CHECK (((versorgungsquote_sobon_ursaechlich >= (0)::numeric) AND (versorgungsquote_sobon_ursaechlich <= (1)::numeric)))
);


ALTER TABLE isidbuser.versorgungsquote_gruppenstaerke OWNER TO isidbuser;

--
-- TOC entry 254 (class 1259 OID 19811)
-- Name: weiteres_verfahren; Type: TABLE; Schema: isidbuser; Owner: isidbuser
--

CREATE TABLE isidbuser.weiteres_verfahren (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    anmerkung character varying(255),
    name character varying(70) NOT NULL,
    status_abfrage character varying(255) NOT NULL,
    sub character varying(255) NOT NULL,
    bauvorhaben_id character varying(36),
    angabe_lage_ergaenzende_adressinformation character varying(255),
    latitude double precision,
    longitude double precision,
    hausnummer character varying(255),
    ort character varying(255),
    plz character varying(255),
    strasse character varying(255),
    aktenzeichen_pro_lbk character varying(255),
    bebauungsplannummer character varying(255),
    frist_bearbeitung timestamp without time zone NOT NULL,
    offizielle_mitzeichnung character varying(255) NOT NULL,
    sobon_jahr character varying(255),
    sobon_relevant character varying(255) NOT NULL,
    stand_verfahren character varying(255) NOT NULL,
    stand_verfahren_freie_eingabe character varying(255),
    verortung jsonb,
    CONSTRAINT weiteres_verfahren_sobon_relevant_check CHECK (((sobon_relevant)::text <> 'UNSPECIFIED'::text)),
    CONSTRAINT weiteres_verfahren_sobon_relevant_check1 CHECK (((sobon_relevant)::text <> 'UNSPECIFIED'::text))
);


ALTER TABLE isidbuser.weiteres_verfahren OWNER TO isidbuser;

--
-- TOC entry 3402 (class 2604 OID 19765)
-- Name: infrastruktureinrichtung lfd_nr; Type: DEFAULT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.infrastruktureinrichtung ALTER COLUMN lfd_nr SET DEFAULT nextval('isidbuser.infrastruktureinrichtung_lfd_nr_seq'::regclass);


--
-- TOC entry 3430 (class 2606 OID 19640)
-- Name: abfragevariante_baugenehmigungsverfahren abfragevariante_baugenehmigungsverfahren_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_baugenehmigungsverfahren
    ADD CONSTRAINT abfragevariante_baugenehmigungsverfahren_pkey PRIMARY KEY (id);


--
-- TOC entry 3436 (class 2606 OID 19650)
-- Name: abfragevariante_bauleitplanverfahren abfragevariante_bauleitplanverfahren_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_bauleitplanverfahren
    ADD CONSTRAINT abfragevariante_bauleitplanverfahren_pkey PRIMARY KEY (id);


--
-- TOC entry 3421 (class 2606 OID 19384)
-- Name: abfragevariante abfragevariante_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante
    ADD CONSTRAINT abfragevariante_pkey PRIMARY KEY (id);


--
-- TOC entry 3442 (class 2606 OID 19660)
-- Name: abfragevariante_weiteres_verfahren abfragevariante_weiteres_verfahren_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_weiteres_verfahren
    ADD CONSTRAINT abfragevariante_weiteres_verfahren_pkey PRIMARY KEY (id);


--
-- TOC entry 3451 (class 2606 OID 19668)
-- Name: bauabschnitt bauabschnitt_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bauabschnitt
    ADD CONSTRAINT bauabschnitt_pkey PRIMARY KEY (id);


--
-- TOC entry 3454 (class 2606 OID 19673)
-- Name: baugebiet baugebiet_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.baugebiet
    ADD CONSTRAINT baugebiet_pkey PRIMARY KEY (id);


--
-- TOC entry 3456 (class 2606 OID 19680)
-- Name: baugenehmigungsverfahren baugenehmigungsverfahren_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.baugenehmigungsverfahren
    ADD CONSTRAINT baugenehmigungsverfahren_pkey PRIMARY KEY (id);


--
-- TOC entry 3461 (class 2606 OID 19689)
-- Name: bauleitplanverfahren bauleitplanverfahren_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bauleitplanverfahren
    ADD CONSTRAINT bauleitplanverfahren_pkey PRIMARY KEY (id);


--
-- TOC entry 3467 (class 2606 OID 19694)
-- Name: baurate baurate_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.baurate
    ADD CONSTRAINT baurate_pkey PRIMARY KEY (id);


--
-- TOC entry 3470 (class 2606 OID 19705)
-- Name: bauvorhaben bauvorhaben_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bauvorhaben
    ADD CONSTRAINT bauvorhaben_pkey PRIMARY KEY (id);


--
-- TOC entry 3477 (class 2606 OID 19716)
-- Name: bedarfsmeldung_fachreferate bedarfsmeldung_fachreferate_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bedarfsmeldung_fachreferate
    ADD CONSTRAINT bedarfsmeldung_fachreferate_pkey PRIMARY KEY (id);


--
-- TOC entry 3483 (class 2606 OID 19723)
-- Name: dokument dokument_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.dokument
    ADD CONSTRAINT dokument_pkey PRIMARY KEY (id);


--
-- TOC entry 3413 (class 2606 OID 18626)
-- Name: flurstueck flurstueck_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.flurstueck
    ADD CONSTRAINT flurstueck_pkey PRIMARY KEY (id);


--
-- TOC entry 3486 (class 2606 OID 19728)
-- Name: foerdermix_stamm foerdermix_stamm_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.foerdermix_stamm
    ADD CONSTRAINT foerdermix_stamm_pkey PRIMARY KEY (id);


--
-- TOC entry 3415 (class 2606 OID 18641)
-- Name: gemarkung gemarkung_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.gemarkung
    ADD CONSTRAINT gemarkung_pkey PRIMARY KEY (id);


--
-- TOC entry 3491 (class 2606 OID 19736)
-- Name: global_counter global_counter_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.global_counter
    ADD CONSTRAINT global_counter_pkey PRIMARY KEY (id);


--
-- TOC entry 3495 (class 2606 OID 19741)
-- Name: grundschule grundschule_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.grundschule
    ADD CONSTRAINT grundschule_pkey PRIMARY KEY (id);


--
-- TOC entry 3497 (class 2606 OID 19748)
-- Name: gs_nachmittag_betreuung gs_nachmittag_betreuung_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.gs_nachmittag_betreuung
    ADD CONSTRAINT gs_nachmittag_betreuung_pkey PRIMARY KEY (id);


--
-- TOC entry 3499 (class 2606 OID 19753)
-- Name: haus_fuer_kinder haus_fuer_kinder_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.haus_fuer_kinder
    ADD CONSTRAINT haus_fuer_kinder_pkey PRIMARY KEY (id);


--
-- TOC entry 3501 (class 2606 OID 19760)
-- Name: idealtypische_baurate idealtypische_baurate_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.idealtypische_baurate
    ADD CONSTRAINT idealtypische_baurate_pkey PRIMARY KEY (id);


--
-- TOC entry 3425 (class 2606 OID 19464)
-- Name: infrastrukturabfrage infrastrukturabfrage_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.infrastrukturabfrage
    ADD CONSTRAINT infrastrukturabfrage_pkey PRIMARY KEY (id);


--
-- TOC entry 3506 (class 2606 OID 19769)
-- Name: infrastruktureinrichtung infrastruktureinrichtung_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.infrastruktureinrichtung
    ADD CONSTRAINT infrastruktureinrichtung_pkey PRIMARY KEY (id);


--
-- TOC entry 3508 (class 2606 OID 19774)
-- Name: kindergarten kindergarten_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.kindergarten
    ADD CONSTRAINT kindergarten_pkey PRIMARY KEY (id);


--
-- TOC entry 3510 (class 2606 OID 19779)
-- Name: kinderkrippe kinderkrippe_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.kinderkrippe
    ADD CONSTRAINT kinderkrippe_pkey PRIMARY KEY (id);


--
-- TOC entry 3514 (class 2606 OID 19786)
-- Name: kommentar kommentar_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.kommentar
    ADD CONSTRAINT kommentar_pkey PRIMARY KEY (id);


--
-- TOC entry 3516 (class 2606 OID 19791)
-- Name: mittelschule mittelschule_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.mittelschule
    ADD CONSTRAINT mittelschule_pkey PRIMARY KEY (id);


--
-- TOC entry 3519 (class 2606 OID 19798)
-- Name: sobon_orientierungswert_soziale_infrastruktur sobon_orientierungswert_soziale_infrastruktur_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ADD CONSTRAINT sobon_orientierungswert_soziale_infrastruktur_pkey PRIMARY KEY (id);


--
-- TOC entry 3417 (class 2606 OID 18718)
-- Name: stadtbezirk stadtbezirk_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.stadtbezirk
    ADD CONSTRAINT stadtbezirk_pkey PRIMARY KEY (id);


--
-- TOC entry 3523 (class 2606 OID 19803)
-- Name: staedtebauliche_orientierungswert staedtebauliche_orientierungswert_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.staedtebauliche_orientierungswert
    ADD CONSTRAINT staedtebauliche_orientierungswert_pkey PRIMARY KEY (id);


--
-- TOC entry 3526 (class 2606 OID 19869)
-- Name: staedtebauliche_orientierungswert uk5x9a94ugolr44w0k6k66kud61; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.staedtebauliche_orientierungswert
    ADD CONSTRAINT uk5x9a94ugolr44w0k6k66kud61 UNIQUE (gueltig_ab, foerderart_bezeichnung);


--
-- TOC entry 3493 (class 2606 OID 19858)
-- Name: global_counter uk7rm8x0kx18f6whau8oms09ije; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.global_counter
    ADD CONSTRAINT uk7rm8x0kx18f6whau8oms09ije UNIQUE (counter_type);


--
-- TOC entry 3472 (class 2606 OID 19845)
-- Name: bauvorhaben uk_47ve8fqe1eiw2ct73kjpukn7n; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bauvorhaben
    ADD CONSTRAINT uk_47ve8fqe1eiw2ct73kjpukn7n UNIQUE (name_vorhaben);


--
-- TOC entry 3428 (class 2606 OID 19515)
-- Name: infrastrukturabfrage uk_bewwevn9yt1ovj5922ollt3xd; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.infrastrukturabfrage
    ADD CONSTRAINT uk_bewwevn9yt1ovj5922ollt3xd UNIQUE (name_abfrage);


--
-- TOC entry 3459 (class 2606 OID 19838)
-- Name: baugenehmigungsverfahren uk_jebiouc3rgyvww5my14ratjxk; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.baugenehmigungsverfahren
    ADD CONSTRAINT uk_jebiouc3rgyvww5my14ratjxk UNIQUE (name);


--
-- TOC entry 3464 (class 2606 OID 19841)
-- Name: bauleitplanverfahren uk_jnr63296n1hkn4qkoo7kvi4tu; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bauleitplanverfahren
    ADD CONSTRAINT uk_jnr63296n1hkn4qkoo7kvi4tu UNIQUE (name);


--
-- TOC entry 3533 (class 2606 OID 19875)
-- Name: weiteres_verfahren uk_pi2mh2r4x50x8eufgyj2j2wj8; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.weiteres_verfahren
    ADD CONSTRAINT uk_pi2mh2r4x50x8eufgyj2j2wj8 UNIQUE (name);


--
-- TOC entry 3488 (class 2606 OID 19855)
-- Name: foerdermix_stamm uka1uvagjduujdesyaquotyibfi; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.foerdermix_stamm
    ADD CONSTRAINT uka1uvagjduujdesyaquotyibfi UNIQUE (bezeichnung, bezeichnung_jahr);


--
-- TOC entry 3521 (class 2606 OID 19866)
-- Name: sobon_orientierungswert_soziale_infrastruktur ukehqrkyn8jho506swcldo2xc7j; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ADD CONSTRAINT ukehqrkyn8jho506swcldo2xc7j UNIQUE (gueltig_ab, einrichtungstyp, altersklasse, foerderart_bezeichnung);


--
-- TOC entry 3528 (class 2606 OID 19872)
-- Name: versorgungsquote_gruppenstaerke ukjf69cklrwiws07t3by33ikcig; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.versorgungsquote_gruppenstaerke
    ADD CONSTRAINT ukjf69cklrwiws07t3by33ikcig UNIQUE (gueltig_ab, infrastruktureinrichtung_typ);


--
-- TOC entry 3504 (class 2606 OID 19861)
-- Name: idealtypische_baurate ukqqep9tj7owsp9pywkxscmx4mo; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.idealtypische_baurate
    ADD CONSTRAINT ukqqep9tj7owsp9pywkxscmx4mo UNIQUE (von, bis_exklusiv, typ);


--
-- TOC entry 3423 (class 2606 OID 19504)
-- Name: abfragevariante uniquenameabfragevarianteperabfrage; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante
    ADD CONSTRAINT uniquenameabfragevarianteperabfrage UNIQUE (abfrage_abfragevarianten_id, abfrage_abfragevarianten_sachbearbeitung_id, abfragevarianten_name);


--
-- TOC entry 3434 (class 2606 OID 19823)
-- Name: abfragevariante_baugenehmigungsverfahren uniquenameabfragevarianteperbaugenehmigungsverfahren; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_baugenehmigungsverfahren
    ADD CONSTRAINT uniquenameabfragevarianteperbaugenehmigungsverfahren UNIQUE (abfragevarianten_baugenehmigungsverfahren_id, abfragevarianten_sachbearbeitung_baugenehmigungsverfahren_id, name);


--
-- TOC entry 3440 (class 2606 OID 19827)
-- Name: abfragevariante_bauleitplanverfahren uniquenameabfragevarianteperbauleitplanverfahren; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_bauleitplanverfahren
    ADD CONSTRAINT uniquenameabfragevarianteperbauleitplanverfahren UNIQUE (abfragevarianten_bauleitplanverfahren_id, abfragevarianten_sachbearbeitung_bauleitplanverfahren_id, name);


--
-- TOC entry 3446 (class 2606 OID 19831)
-- Name: abfragevariante_weiteres_verfahren uniquenameabfragevarianteperweiteresverfahren; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_weiteres_verfahren
    ADD CONSTRAINT uniquenameabfragevarianteperweiteresverfahren UNIQUE (abfragevarianten_weiteres_verfahren_id, abfragevarianten_sachbearbeitung_weiteres_verfahren_id, name);


--
-- TOC entry 3419 (class 2606 OID 18732)
-- Name: verortung verortung_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.verortung
    ADD CONSTRAINT verortung_pkey PRIMARY KEY (id);


--
-- TOC entry 3531 (class 2606 OID 19810)
-- Name: versorgungsquote_gruppenstaerke versorgungsquote_gruppenstaerke_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.versorgungsquote_gruppenstaerke
    ADD CONSTRAINT versorgungsquote_gruppenstaerke_pkey PRIMARY KEY (id);


--
-- TOC entry 3535 (class 2606 OID 19819)
-- Name: weiteres_verfahren weiteres_verfahren_pkey; Type: CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.weiteres_verfahren
    ADD CONSTRAINT weiteres_verfahren_pkey PRIMARY KEY (id);


--
-- TOC entry 3431 (class 1259 OID 19820)
-- Name: abfragevarianten_baugenehmigungsverfahren_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX abfragevarianten_baugenehmigungsverfahren_id_index ON isidbuser.abfragevariante_baugenehmigungsverfahren USING btree (abfragevarianten_baugenehmigungsverfahren_id);


--
-- TOC entry 3437 (class 1259 OID 19824)
-- Name: abfragevarianten_bauleitplanverfahren_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX abfragevarianten_bauleitplanverfahren_id_index ON isidbuser.abfragevariante_bauleitplanverfahren USING btree (abfragevarianten_bauleitplanverfahren_id);


--
-- TOC entry 3432 (class 1259 OID 19821)
-- Name: abfragevarianten_sachbearbeitung_baugenehmigungsverfahren_id_in; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX abfragevarianten_sachbearbeitung_baugenehmigungsverfahren_id_in ON isidbuser.abfragevariante_baugenehmigungsverfahren USING btree (abfragevarianten_sachbearbeitung_baugenehmigungsverfahren_id);


--
-- TOC entry 3438 (class 1259 OID 19825)
-- Name: abfragevarianten_sachbearbeitung_bauleitplanverfahren_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX abfragevarianten_sachbearbeitung_bauleitplanverfahren_id_index ON isidbuser.abfragevariante_bauleitplanverfahren USING btree (abfragevarianten_sachbearbeitung_bauleitplanverfahren_id);


--
-- TOC entry 3443 (class 1259 OID 19829)
-- Name: abfragevarianten_sachbearbeitung_weiteres_verfahren_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX abfragevarianten_sachbearbeitung_weiteres_verfahren_id_index ON isidbuser.abfragevariante_weiteres_verfahren USING btree (abfragevarianten_sachbearbeitung_weiteres_verfahren_id);


--
-- TOC entry 3444 (class 1259 OID 19828)
-- Name: abfragevarianten_weiteres_verfahren_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX abfragevarianten_weiteres_verfahren_id_index ON isidbuser.abfragevariante_weiteres_verfahren USING btree (abfragevarianten_weiteres_verfahren_id);


--
-- TOC entry 3447 (class 1259 OID 19833)
-- Name: bauabschnitt_abfragevariante_baugenehmigungsverfahren_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX bauabschnitt_abfragevariante_baugenehmigungsverfahren_id_index ON isidbuser.bauabschnitt USING btree (abfragevariante_baugenehmigungsverfahren_id);


--
-- TOC entry 3448 (class 1259 OID 19832)
-- Name: bauabschnitt_abfragevariante_bauleitplanverfahren_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX bauabschnitt_abfragevariante_bauleitplanverfahren_id_index ON isidbuser.bauabschnitt USING btree (abfragevariante_bauleitplanverfahren_id);


--
-- TOC entry 3449 (class 1259 OID 19834)
-- Name: bauabschnitt_abfragevariante_weiteres_verfahren_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX bauabschnitt_abfragevariante_weiteres_verfahren_id_index ON isidbuser.bauabschnitt USING btree (abfragevariante_weiteres_verfahren_id);


--
-- TOC entry 3452 (class 1259 OID 19835)
-- Name: baugebiet_bauabschnitt_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX baugebiet_bauabschnitt_id_index ON isidbuser.baugebiet USING btree (bauabschnitt_id);


--
-- TOC entry 3457 (class 1259 OID 19836)
-- Name: baugenehmigungsverfahrenabfrage_name_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX baugenehmigungsverfahrenabfrage_name_index ON isidbuser.baugenehmigungsverfahren USING btree (name);


--
-- TOC entry 3462 (class 1259 OID 19839)
-- Name: bauleitplanverfahrenabfrage_name_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX bauleitplanverfahrenabfrage_name_index ON isidbuser.bauleitplanverfahren USING btree (name);


--
-- TOC entry 3465 (class 1259 OID 19842)
-- Name: baurate_baugebiet_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX baurate_baugebiet_id_index ON isidbuser.baurate USING btree (baugebiet_id);


--
-- TOC entry 3468 (class 1259 OID 19843)
-- Name: bauvorhaben_name_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX bauvorhaben_name_index ON isidbuser.bauvorhaben USING btree (name_vorhaben);


--
-- TOC entry 3473 (class 1259 OID 19847)
-- Name: bedarfsmeldung_fachreferate_abfragevariante_baugenehmigungsverf; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX bedarfsmeldung_fachreferate_abfragevariante_baugenehmigungsverf ON isidbuser.bedarfsmeldung_fachreferate USING btree (abfragevariante_baugenehmigungsverfahren_id);


--
-- TOC entry 3474 (class 1259 OID 19846)
-- Name: bedarfsmeldung_fachreferate_abfragevariante_bauleitplanverfahre; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX bedarfsmeldung_fachreferate_abfragevariante_bauleitplanverfahre ON isidbuser.bedarfsmeldung_fachreferate USING btree (abfragevariante_bauleitplanverfahren_id);


--
-- TOC entry 3475 (class 1259 OID 19848)
-- Name: bedarfsmeldung_fachreferate_abfragevariante_weiteres_verfahren_; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX bedarfsmeldung_fachreferate_abfragevariante_weiteres_verfahren_ ON isidbuser.bedarfsmeldung_fachreferate USING btree (abfragevariante_weiteres_verfahren_id);


--
-- TOC entry 3489 (class 1259 OID 19856)
-- Name: counter_type_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX counter_type_index ON isidbuser.global_counter USING btree (counter_type);


--
-- TOC entry 3478 (class 1259 OID 19850)
-- Name: dokument_baugenehmigungsverfahren_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX dokument_baugenehmigungsverfahren_id_index ON isidbuser.dokument USING btree (baugenehmigungsverfahren_id);


--
-- TOC entry 3479 (class 1259 OID 19849)
-- Name: dokument_bauleitplanverfahren_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX dokument_bauleitplanverfahren_id_index ON isidbuser.dokument USING btree (bauleitplanverfahren_id);


--
-- TOC entry 3480 (class 1259 OID 19852)
-- Name: dokument_bauvorhaben_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX dokument_bauvorhaben_id_index ON isidbuser.dokument USING btree (bauvorhaben_id);


--
-- TOC entry 3481 (class 1259 OID 19853)
-- Name: dokument_kommentar_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX dokument_kommentar_id_index ON isidbuser.dokument USING btree (kommentar_id);


--
-- TOC entry 3484 (class 1259 OID 19851)
-- Name: dokument_weiteres_verfahren_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX dokument_weiteres_verfahren_id_index ON isidbuser.dokument USING btree (weiteres_verfahren_id);


--
-- TOC entry 3502 (class 1259 OID 19859)
-- Name: idealtypische_baurate_range_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX idealtypische_baurate_range_index ON isidbuser.idealtypische_baurate USING btree (typ, von, bis_exklusiv);


--
-- TOC entry 3511 (class 1259 OID 19862)
-- Name: kommentar_bauvorhaben_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX kommentar_bauvorhaben_id_index ON isidbuser.kommentar USING btree (bauvorhaben_id);


--
-- TOC entry 3512 (class 1259 OID 19863)
-- Name: kommentar_infrastruktureinrichtung_id_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX kommentar_infrastruktureinrichtung_id_index ON isidbuser.kommentar USING btree (infrastruktureinrichtung_id);


--
-- TOC entry 3426 (class 1259 OID 19513)
-- Name: name_abfrage_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX name_abfrage_index ON isidbuser.infrastrukturabfrage USING btree (name_abfrage);


--
-- TOC entry 3517 (class 1259 OID 19864)
-- Name: sobon_orientierungswert_soziale_infrastruktur_jahr_einrichtungs; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX sobon_orientierungswert_soziale_infrastruktur_jahr_einrichtungs ON isidbuser.sobon_orientierungswert_soziale_infrastruktur USING btree (gueltig_ab, einrichtungstyp, altersklasse, foerderart_bezeichnung);


--
-- TOC entry 3524 (class 1259 OID 19867)
-- Name: staedtebaulicher_orientierungswert_gueltigab_foerderartbezeichn; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX staedtebaulicher_orientierungswert_gueltigab_foerderartbezeichn ON isidbuser.staedtebauliche_orientierungswert USING btree (gueltig_ab, foerderart_bezeichnung);


--
-- TOC entry 3529 (class 1259 OID 19870)
-- Name: versorgungsqoute_gruppenstaerke; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX versorgungsqoute_gruppenstaerke ON isidbuser.versorgungsquote_gruppenstaerke USING btree (gueltig_ab, infrastruktureinrichtung_typ);


--
-- TOC entry 3536 (class 1259 OID 19873)
-- Name: weiteres_verfahrenabfrage_name_index; Type: INDEX; Schema: isidbuser; Owner: isidbuser
--

CREATE INDEX weiteres_verfahrenabfrage_name_index ON isidbuser.weiteres_verfahren USING btree (name);


--
-- TOC entry 3564 (class 2606 OID 19996)
-- Name: dokument fk1e7n3bdncu06saw2eyew3a8jt; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.dokument
    ADD CONSTRAINT fk1e7n3bdncu06saw2eyew3a8jt FOREIGN KEY (bauvorhaben_id) REFERENCES isidbuser.bauvorhaben(id);


--
-- TOC entry 3561 (class 2606 OID 19981)
-- Name: bedarfsmeldung_fachreferate fk1m9hfc9q0kwv2y86u05a6qmr8; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bedarfsmeldung_fachreferate
    ADD CONSTRAINT fk1m9hfc9q0kwv2y86u05a6qmr8 FOREIGN KEY (abfragevariante_baugenehmigungsverfahren_id) REFERENCES isidbuser.abfragevariante_baugenehmigungsverfahren(id);


--
-- TOC entry 3538 (class 2606 OID 18804)
-- Name: gemarkung fk255n7gr01646j8ulmf95qtqvi; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.gemarkung
    ADD CONSTRAINT fk255n7gr01646j8ulmf95qtqvi FOREIGN KEY (verortung_id) REFERENCES isidbuser.verortung(id);


--
-- TOC entry 3565 (class 2606 OID 20006)
-- Name: dokument fk2hfbju5538u0tn1cya5tnvtbq; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.dokument
    ADD CONSTRAINT fk2hfbju5538u0tn1cya5tnvtbq FOREIGN KEY (baugenehmigungsverfahren_id) REFERENCES isidbuser.baugenehmigungsverfahren(id);


--
-- TOC entry 3560 (class 2606 OID 19966)
-- Name: bauvorhaben_wesentliche_rechtsgrundlage fk30iwircdvex8fcbn8cegqx92e; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bauvorhaben_wesentliche_rechtsgrundlage
    ADD CONSTRAINT fk30iwircdvex8fcbn8cegqx92e FOREIGN KEY (bauvorhaben_id) REFERENCES isidbuser.bauvorhaben(id);


--
-- TOC entry 3539 (class 2606 OID 18879)
-- Name: stadtbezirk fk3vjnt20990q74ea0c7khwqw23; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.stadtbezirk
    ADD CONSTRAINT fk3vjnt20990q74ea0c7khwqw23 FOREIGN KEY (verortung_id) REFERENCES isidbuser.verortung(id);


--
-- TOC entry 3571 (class 2606 OID 20021)
-- Name: gs_nachmittag_betreuung fk434vr3fow5arbvxkgrn7egsw2; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.gs_nachmittag_betreuung
    ADD CONSTRAINT fk434vr3fow5arbvxkgrn7egsw2 FOREIGN KEY (id) REFERENCES isidbuser.infrastruktureinrichtung(id);


--
-- TOC entry 3554 (class 2606 OID 19936)
-- Name: baugebiet fk43nuwbptaq4hshuv6lm6ilv6n; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.baugebiet
    ADD CONSTRAINT fk43nuwbptaq4hshuv6lm6ilv6n FOREIGN KEY (bauabschnitt_id) REFERENCES isidbuser.bauabschnitt(id);


--
-- TOC entry 3551 (class 2606 OID 19931)
-- Name: bauabschnitt fk44ecbhieyub1dss2q3c52e1ap; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bauabschnitt
    ADD CONSTRAINT fk44ecbhieyub1dss2q3c52e1ap FOREIGN KEY (abfragevariante_baugenehmigungsverfahren_id) REFERENCES isidbuser.abfragevariante_baugenehmigungsverfahren(id);


--
-- TOC entry 3576 (class 2606 OID 20051)
-- Name: kommentar fk45aq0h5o0qhl17ventkmpmpf3; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.kommentar
    ADD CONSTRAINT fk45aq0h5o0qhl17ventkmpmpf3 FOREIGN KEY (infrastruktureinrichtung_id) REFERENCES isidbuser.infrastruktureinrichtung(id);


--
-- TOC entry 3566 (class 2606 OID 19991)
-- Name: dokument fk47liq4jx6eqybm2flvd1asdcd; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.dokument
    ADD CONSTRAINT fk47liq4jx6eqybm2flvd1asdcd FOREIGN KEY (weiteres_verfahren_id) REFERENCES isidbuser.weiteres_verfahren(id);


--
-- TOC entry 3542 (class 2606 OID 19881)
-- Name: abfragevariante_baugenehmigungsverfahren fk49c0wy3kjevklo5cbrjkrus48; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_baugenehmigungsverfahren
    ADD CONSTRAINT fk49c0wy3kjevklo5cbrjkrus48 FOREIGN KEY (abfragevarianten_baugenehmigungsverfahren_id) REFERENCES isidbuser.baugenehmigungsverfahren(id);


--
-- TOC entry 3547 (class 2606 OID 19901)
-- Name: abfragevariante_bauleitplanverfahren_wesentliche_rechtsgrundlag fk69xibgnrhtrdr6s1i5i1fv8i1; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_bauleitplanverfahren_wesentliche_rechtsgrundlag
    ADD CONSTRAINT fk69xibgnrhtrdr6s1i5i1fv8i1 FOREIGN KEY (abfragevariante_bauleitplanverfahren_id) REFERENCES isidbuser.abfragevariante_bauleitplanverfahren(id);


--
-- TOC entry 3567 (class 2606 OID 20001)
-- Name: dokument fk6pyd1ysqilkq4psswakb8m1e6; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.dokument
    ADD CONSTRAINT fk6pyd1ysqilkq4psswakb8m1e6 FOREIGN KEY (bauleitplanverfahren_id) REFERENCES isidbuser.bauleitplanverfahren(id);


--
-- TOC entry 3543 (class 2606 OID 19876)
-- Name: abfragevariante_baugenehmigungsverfahren fk76qfwbhrg88v1h2rkkbj27baj; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_baugenehmigungsverfahren
    ADD CONSTRAINT fk76qfwbhrg88v1h2rkkbj27baj FOREIGN KEY (abfragevarianten_sachbearbeitung_baugenehmigungsverfahren_id) REFERENCES isidbuser.baugenehmigungsverfahren(id);


--
-- TOC entry 3577 (class 2606 OID 20046)
-- Name: kommentar fk9jia9xb9ovr7e1ad49l866a03; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.kommentar
    ADD CONSTRAINT fk9jia9xb9ovr7e1ad49l866a03 FOREIGN KEY (bauvorhaben_id) REFERENCES isidbuser.bauvorhaben(id);


--
-- TOC entry 3540 (class 2606 OID 19522)
-- Name: abfragevariante fk9rcewq7cmm0r0l1qcpgpoyrba; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante
    ADD CONSTRAINT fk9rcewq7cmm0r0l1qcpgpoyrba FOREIGN KEY (abfrage_abfragevarianten_sachbearbeitung_id) REFERENCES isidbuser.infrastrukturabfrage(id);


--
-- TOC entry 3579 (class 2606 OID 20061)
-- Name: weiteres_verfahren fk_b19uhd66b8hmhfelkk2fwey21; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.weiteres_verfahren
    ADD CONSTRAINT fk_b19uhd66b8hmhfelkk2fwey21 FOREIGN KEY (bauvorhaben_id) REFERENCES isidbuser.bauvorhaben(id);


--
-- TOC entry 3555 (class 2606 OID 19941)
-- Name: baugenehmigungsverfahren fk_elnxjn7cmqroe7molk8b401i6; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.baugenehmigungsverfahren
    ADD CONSTRAINT fk_elnxjn7cmqroe7molk8b401i6 FOREIGN KEY (bauvorhaben_id) REFERENCES isidbuser.bauvorhaben(id);


--
-- TOC entry 3556 (class 2606 OID 19946)
-- Name: bauleitplanverfahren fk_euc56ml5cphea0v9sq3jw7ttb; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bauleitplanverfahren
    ADD CONSTRAINT fk_euc56ml5cphea0v9sq3jw7ttb FOREIGN KEY (bauvorhaben_id) REFERENCES isidbuser.bauvorhaben(id);


--
-- TOC entry 3550 (class 2606 OID 19916)
-- Name: abfragevariante_weiteres_verfahren_wesentliche_rechtsgrundlage fkbd5tmreht948r6mpa6xmgw8iq; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_weiteres_verfahren_wesentliche_rechtsgrundlage
    ADD CONSTRAINT fkbd5tmreht948r6mpa6xmgw8iq FOREIGN KEY (abfragevariante_weiteres_verfahren_id) REFERENCES isidbuser.abfragevariante_weiteres_verfahren(id);


--
-- TOC entry 3541 (class 2606 OID 19527)
-- Name: abfragevariante fkctdgy29o7ql64rewjqbxp9ow2; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante
    ADD CONSTRAINT fkctdgy29o7ql64rewjqbxp9ow2 FOREIGN KEY (abfrage_abfragevarianten_id) REFERENCES isidbuser.infrastrukturabfrage(id);


--
-- TOC entry 3570 (class 2606 OID 20016)
-- Name: grundschule fkdhtho0m5d89vlbqdxy81hxkvb; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.grundschule
    ADD CONSTRAINT fkdhtho0m5d89vlbqdxy81hxkvb FOREIGN KEY (id) REFERENCES isidbuser.infrastruktureinrichtung(id);


--
-- TOC entry 3537 (class 2606 OID 18794)
-- Name: flurstueck fke0bdem97rk7pcawo2w0wy0q2n; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.flurstueck
    ADD CONSTRAINT fke0bdem97rk7pcawo2w0wy0q2n FOREIGN KEY (gemarkung_id) REFERENCES isidbuser.gemarkung(id);


--
-- TOC entry 3552 (class 2606 OID 19926)
-- Name: bauabschnitt fkeukua3uyee46psn0jy48tuy1g; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bauabschnitt
    ADD CONSTRAINT fkeukua3uyee46psn0jy48tuy1g FOREIGN KEY (abfragevariante_bauleitplanverfahren_id) REFERENCES isidbuser.abfragevariante_bauleitplanverfahren(id);


--
-- TOC entry 3559 (class 2606 OID 19961)
-- Name: bauvorhaben_art_fnp fkfewe3gv2mam520gk055dieg95; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bauvorhaben_art_fnp
    ADD CONSTRAINT fkfewe3gv2mam520gk055dieg95 FOREIGN KEY (bauvorhaben_id) REFERENCES isidbuser.bauvorhaben(id);


--
-- TOC entry 3575 (class 2606 OID 20041)
-- Name: kinderkrippe fkffagi8keite5kl9m99s4sq3pm; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.kinderkrippe
    ADD CONSTRAINT fkffagi8keite5kl9m99s4sq3pm FOREIGN KEY (id) REFERENCES isidbuser.infrastruktureinrichtung(id);


--
-- TOC entry 3568 (class 2606 OID 19986)
-- Name: dokument fkgb30ot3q37f8l5ch5q79jx44b; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.dokument
    ADD CONSTRAINT fkgb30ot3q37f8l5ch5q79jx44b FOREIGN KEY (kommentar_id) REFERENCES isidbuser.kommentar(id);


--
-- TOC entry 3558 (class 2606 OID 19956)
-- Name: baurate_foerderarten fkh21llfapa4es22vrwrdl6w9qe; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.baurate_foerderarten
    ADD CONSTRAINT fkh21llfapa4es22vrwrdl6w9qe FOREIGN KEY (baurate_id) REFERENCES isidbuser.baurate(id);


--
-- TOC entry 3574 (class 2606 OID 20036)
-- Name: kindergarten fki2ofvj80ehsggy9e43he3se0x; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.kindergarten
    ADD CONSTRAINT fki2ofvj80ehsggy9e43he3se0x FOREIGN KEY (id) REFERENCES isidbuser.infrastruktureinrichtung(id);


--
-- TOC entry 3557 (class 2606 OID 19951)
-- Name: baurate fkib3s1x9l19xir110y9wrug1qp; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.baurate
    ADD CONSTRAINT fkib3s1x9l19xir110y9wrug1qp FOREIGN KEY (baugebiet_id) REFERENCES isidbuser.baugebiet(id);


--
-- TOC entry 3578 (class 2606 OID 20056)
-- Name: mittelschule fkj9noj0fd74gl939rwf7o69vy8; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.mittelschule
    ADD CONSTRAINT fkj9noj0fd74gl939rwf7o69vy8 FOREIGN KEY (id) REFERENCES isidbuser.infrastruktureinrichtung(id);


--
-- TOC entry 3572 (class 2606 OID 20026)
-- Name: haus_fuer_kinder fkjdesrw0yr150s4vt6u3bp42b3; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.haus_fuer_kinder
    ADD CONSTRAINT fkjdesrw0yr150s4vt6u3bp42b3 FOREIGN KEY (id) REFERENCES isidbuser.infrastruktureinrichtung(id);


--
-- TOC entry 3548 (class 2606 OID 19911)
-- Name: abfragevariante_weiteres_verfahren fkjmlbae1y4q3weo38yvlb5d1do; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_weiteres_verfahren
    ADD CONSTRAINT fkjmlbae1y4q3weo38yvlb5d1do FOREIGN KEY (abfragevarianten_sachbearbeitung_weiteres_verfahren_id) REFERENCES isidbuser.weiteres_verfahren(id);


--
-- TOC entry 3562 (class 2606 OID 19971)
-- Name: bedarfsmeldung_fachreferate fko2jy96eb5dw4jluvwywjfyy2p; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bedarfsmeldung_fachreferate
    ADD CONSTRAINT fko2jy96eb5dw4jluvwywjfyy2p FOREIGN KEY (abfragevariante_weiteres_verfahren_id) REFERENCES isidbuser.abfragevariante_weiteres_verfahren(id);


--
-- TOC entry 3563 (class 2606 OID 19976)
-- Name: bedarfsmeldung_fachreferate fkp64d3rygwnu9il6mdirm8vyvg; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bedarfsmeldung_fachreferate
    ADD CONSTRAINT fkp64d3rygwnu9il6mdirm8vyvg FOREIGN KEY (abfragevariante_bauleitplanverfahren_id) REFERENCES isidbuser.abfragevariante_bauleitplanverfahren(id);


--
-- TOC entry 3544 (class 2606 OID 19886)
-- Name: abfragevariante_baugenehmigungsverfahren_wesentliche_rechtsgrun fkp9tsqaxla68l3x7cr4p1re2np; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_baugenehmigungsverfahren_wesentliche_rechtsgrun
    ADD CONSTRAINT fkp9tsqaxla68l3x7cr4p1re2np FOREIGN KEY (abfragevariante_baugenehmigungsverfahren_id) REFERENCES isidbuser.abfragevariante_baugenehmigungsverfahren(id);


--
-- TOC entry 3549 (class 2606 OID 19906)
-- Name: abfragevariante_weiteres_verfahren fkq0xachmbv8lm87stwplym77qq; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_weiteres_verfahren
    ADD CONSTRAINT fkq0xachmbv8lm87stwplym77qq FOREIGN KEY (abfragevarianten_weiteres_verfahren_id) REFERENCES isidbuser.weiteres_verfahren(id);


--
-- TOC entry 3573 (class 2606 OID 20031)
-- Name: infrastruktureinrichtung fkqpfrtp1svmrv7a250kmoybdp5; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.infrastruktureinrichtung
    ADD CONSTRAINT fkqpfrtp1svmrv7a250kmoybdp5 FOREIGN KEY (bauvorhaben_id) REFERENCES isidbuser.bauvorhaben(id);


--
-- TOC entry 3553 (class 2606 OID 19921)
-- Name: bauabschnitt fkr2fp34gvhd13wgtrj40bwv7xm; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.bauabschnitt
    ADD CONSTRAINT fkr2fp34gvhd13wgtrj40bwv7xm FOREIGN KEY (abfragevariante_weiteres_verfahren_id) REFERENCES isidbuser.abfragevariante_weiteres_verfahren(id);


--
-- TOC entry 3569 (class 2606 OID 20011)
-- Name: foerdermix_stamm_foerderarten fkrnj6wdw3auu5k4p02q56dskt2; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.foerdermix_stamm_foerderarten
    ADD CONSTRAINT fkrnj6wdw3auu5k4p02q56dskt2 FOREIGN KEY (foerdermix_stamm_id) REFERENCES isidbuser.foerdermix_stamm(id);


--
-- TOC entry 3545 (class 2606 OID 19896)
-- Name: abfragevariante_bauleitplanverfahren fks1ic7g4qf4kopwmn64fm53m1f; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_bauleitplanverfahren
    ADD CONSTRAINT fks1ic7g4qf4kopwmn64fm53m1f FOREIGN KEY (abfragevarianten_bauleitplanverfahren_id) REFERENCES isidbuser.bauleitplanverfahren(id);


--
-- TOC entry 3546 (class 2606 OID 19891)
-- Name: abfragevariante_bauleitplanverfahren fksainhxpux6r02lm1gajxl79nt; Type: FK CONSTRAINT; Schema: isidbuser; Owner: isidbuser
--

ALTER TABLE ONLY isidbuser.abfragevariante_bauleitplanverfahren
    ADD CONSTRAINT fksainhxpux6r02lm1gajxl79nt FOREIGN KEY (abfragevarianten_sachbearbeitung_bauleitplanverfahren_id) REFERENCES isidbuser.bauleitplanverfahren(id);


-- Completed on 2024-02-06 12:39:09 CET

--
-- PostgreSQL database dump complete
--


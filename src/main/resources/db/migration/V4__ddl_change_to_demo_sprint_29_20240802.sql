--
-- Anpassungen von Version 1.1.0 auf Stand Demo 08.02.2024
--
BEGIN;

CREATE TABLE IF NOT EXISTS isidbuser.bedarfsmeldung
(
    anzahl_einrichtungen integer NOT NULL,
    anzahl_grundschulzuege integer,
    anzahl_hortgruppen integer,
    anzahl_kindergartengruppen integer,
    anzahl_kinderkrippengruppen integer,
    infrastruktureinrichtung_typ integer NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    abfragevariante_baugenehmigungsverfahren_abfrageersteller_id character varying(36) COLLATE pg_catalog."default",
    abfragevariante_baugenehmigungsverfahren_fachreferate_id character varying(36) COLLATE pg_catalog."default",
    abfragevariante_bauleitplanverfahren_abfrageersteller_id character varying(36) COLLATE pg_catalog."default",
    abfragevariante_bauleitplanverfahren_fachreferate_id character varying(36) COLLATE pg_catalog."default",
    abfragevariante_weiteres_verfahren_abfrageersteller_id character varying(36) COLLATE pg_catalog."default",
    abfragevariante_weiteres_verfahren_fachreferate_id character varying(36) COLLATE pg_catalog."default",
    id character varying(36) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT bedarfsmeldung_pkey PRIMARY KEY (id),
    CONSTRAINT fk587ovhoo0122e788txb2hi6vt FOREIGN KEY (abfragevariante_weiteres_verfahren_abfrageersteller_id)
    REFERENCES isidbuser.abfragevariante_weiteres_verfahren (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fk5ot09g48mj8qf38s6bsjlkpbo FOREIGN KEY (abfragevariante_bauleitplanverfahren_abfrageersteller_id)
    REFERENCES isidbuser.abfragevariante_bauleitplanverfahren (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fk69cf65sqshohxarvm72oyf67q FOREIGN KEY (abfragevariante_weiteres_verfahren_fachreferate_id)
    REFERENCES isidbuser.abfragevariante_weiteres_verfahren (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fkhm023faq7ilg21ksdmupi1rh3 FOREIGN KEY (abfragevariante_baugenehmigungsverfahren_abfrageersteller_id)
    REFERENCES isidbuser.abfragevariante_baugenehmigungsverfahren (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fki8ri601u2kpotcm0ritgno6dw FOREIGN KEY (abfragevariante_baugenehmigungsverfahren_fachreferate_id)
    REFERENCES isidbuser.abfragevariante_baugenehmigungsverfahren (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fknhbgsnsn17wsxuha7lkguaswt FOREIGN KEY (abfragevariante_bauleitplanverfahren_fachreferate_id)
    REFERENCES isidbuser.abfragevariante_bauleitplanverfahren (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT bedarfsmeldung_infrastruktureinrichtung_typ_check CHECK (infrastruktureinrichtung_typ >= 0 AND infrastruktureinrichtung_typ <= 6)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS isidbuser.bedarfsmeldung
    OWNER to isidbuser;

--
-- Löschen der Indices für isidbuser.bedarfsmeldung_fachreferate um diese in isidbuser.bedarfsmeldung erstellen zu können.
--
DROP INDEX IF EXISTS isidbuser.bedarfsmeldung_fachreferate_abfragevariante_baugenehmigungsverf;
DROP INDEX IF EXISTS isidbuser.bedarfsmeldung_fachreferate_abfragevariante_bauleitplanverfahre;
DROP INDEX IF EXISTS isidbuser.bedarfsmeldung_fachreferate_abfragevariante_weiteres_verfahren_;

CREATE INDEX IF NOT EXISTS bedarfsmeldung_fachreferate_abfragevariante_baugenehmigungsverf
    ON isidbuser.bedarfsmeldung USING btree
    (abfragevariante_baugenehmigungsverfahren_fachreferate_id COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS bedarfsmeldung_fachreferate_abfragevariante_bauleitplanverfahre
    ON isidbuser.bedarfsmeldung USING btree
    (abfragevariante_bauleitplanverfahren_fachreferate_id COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS bedarfsmeldung_fachreferate_abfragevariante_weiteres_verfahren_
    ON isidbuser.bedarfsmeldung USING btree
    (abfragevariante_weiteres_verfahren_fachreferate_id COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS bedarfsmeldung_abfrageersteller_abfragevariante_baugenehmigungs
    ON isidbuser.bedarfsmeldung USING btree
    (abfragevariante_baugenehmigungsverfahren_abfrageersteller_id COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS bedarfsmeldung_abfrageersteller_abfragevariante_bauleitplanverf
    ON isidbuser.bedarfsmeldung USING btree
    (abfragevariante_bauleitplanverfahren_abfrageersteller_id COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;
CREATE INDEX IF NOT EXISTS bedarfsmeldung_abfrageersteller_abfragevariante_weiteres_verfah
    ON isidbuser.bedarfsmeldung USING btree
    (abfragevariante_weiteres_verfahren_abfrageersteller_id COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;

--
-- Befüllung isidbuser.bedarfsmeldung mit Daten aus isidbuser.bedarfsmeldung_fachreferate
--
INSERT INTO isidbuser.bedarfsmeldung(
    id,
    created_date_time,
    last_modified_date_time,
    version,
    anzahl_einrichtungen,
    anzahl_grundschulzuege,
    anzahl_hortgruppen,
    anzahl_kindergartengruppen,
    anzahl_kinderkrippengruppen,
    infrastruktureinrichtung_typ,
    abfragevariante_baugenehmigungsverfahren_fachreferate_id,
    abfragevariante_bauleitplanverfahren_fachreferate_id,
    abfragevariante_weiteres_verfahren_fachreferate_id
)
    SELECT
        id,
        created_date_time,
        last_modified_date_time,
        version,
        anzahl_einrichtungen,
        anzahl_grundschulzuege,
        anzahl_hortgruppen,
        anzahl_kindergartengruppen,
        anzahl_kinderkrippengruppen,
        infrastruktureinrichtung_typ,
        abfragevariante_baugenehmigungsverfahren_id,
        abfragevariante_bauleitplanverfahren_id,
        abfragevariante_weiteres_verfahren_id
    FROM isidbuser.bedarfsmeldung_fachreferate;

CREATE TABLE IF NOT EXISTS isidbuser.umlegung_foerderarten
(
    created_date_time timestamp without time zone,
    gueltig_ab timestamp(6) without time zone NOT NULL,
    last_modified_date_time timestamp without time zone,
    version bigint,
    id character varying(36) COLLATE pg_catalog."default" NOT NULL,
    bezeichnung character varying(255) COLLATE pg_catalog."default" NOT NULL,
    umlegungsschluessel jsonb,
    CONSTRAINT umlegung_foerderarten_pkey PRIMARY KEY (id),
    CONSTRAINT uniqueumlegungfoerderarten UNIQUE (bezeichnung, gueltig_ab)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS isidbuser.baurate_foerderarten
    ALTER COLUMN anteil_prozent TYPE numeric(5,2);

ALTER TABLE IF EXISTS isidbuser.umlegung_foerderarten
    OWNER to isidbuser;

DROP TABLE IF EXISTS isidbuser.flurstueck CASCADE;

DROP TABLE IF EXISTS isidbuser.verortung CASCADE;

DROP TABLE IF EXISTS isidbuser.gemarkung CASCADE;

DROP TABLE IF EXISTS isidbuser.stadtbezirk CASCADE;

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ALTER COLUMN frist_bearbeitung TYPE timestamp(6),
    ALTER COLUMN frist_bearbeitung SET NOT NULL;

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren DROP CONSTRAINT IF EXISTS uk_pi2mh2r4x50x8eufgyj2j2wj8;

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ADD CONSTRAINT weiteres_verfahren_name_key UNIQUE (name);

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ADD CONSTRAINT weiteres_verfahren_offizielle_mitzeichnung_check CHECK (offizielle_mitzeichnung::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'TRUE'::character varying, 'FALSE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ADD CONSTRAINT weiteres_verfahren_sobon_jahr_check CHECK (sobon_jahr::text = ANY (ARRAY['JAHR_1995'::character varying, 'JAHR_1997'::character varying, 'JAHR_2001'::character varying, 'JAHR_2006'::character varying, 'JAHR_2012'::character varying, 'JAHR_2017'::character varying, 'JAHR_2017_PLUS'::character varying, 'JAHR_2021'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ADD CONSTRAINT weiteres_verfahren_sobon_relevant_check2 CHECK (sobon_relevant::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'TRUE'::character varying, 'FALSE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ADD CONSTRAINT weiteres_verfahren_stand_verfahren_check CHECK (stand_verfahren::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying, 'AUFTEILUNGSPLAN'::character varying, 'VORBEREITUNG_VORBESCHEID'::character varying, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying, 'STRUKTURKONZEPT'::character varying, 'RAHMENPLANUNG'::character varying, 'POTENTIALUNTERSUCHUNG'::character varying, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying, 'STANDORTABFRAGE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ADD CONSTRAINT weiteres_verfahren_status_abfrage_check CHECK (status_abfrage::text = ANY (ARRAY['ANGELEGT'::character varying, 'OFFEN'::character varying, 'IN_BEARBEITUNG_SACHBEARBEITUNG'::character varying, 'IN_BEARBEITUNG_FACHREFERATE'::character varying, 'BEDARFSMELDUNG_ERFOLGT'::character varying, 'ERLEDIGT_MIT_FACHREFERAT'::character varying, 'ERLEDIGT_OHNE_FACHREFERAT'::character varying, 'ABBRUCH'::character varying]::text[]));
DROP INDEX IF EXISTS isidbuser.weiteres_verfahrenabfrage_name_index;

ALTER TABLE IF EXISTS isidbuser.idealtypische_baurate
    ALTER COLUMN von TYPE numeric(38,2),
    ALTER COLUMN bis_exklusiv TYPE numeric(38,2),
    ALTER COLUMN von SET NOT NULL,
    ALTER COLUMN bis_exklusiv SET NOT NULL;

ALTER TABLE IF EXISTS isidbuser.idealtypische_baurate DROP CONSTRAINT IF EXISTS ukqqep9tj7owsp9pywkxscmx4mo;

ALTER TABLE IF EXISTS isidbuser.idealtypische_baurate
    ADD CONSTRAINT idealtypische_baurate_von_bis_exklusiv_typ_key UNIQUE (von, bis_exklusiv, typ);

ALTER TABLE IF EXISTS isidbuser.idealtypische_baurate
    ADD CONSTRAINT idealtypische_baurate_typ_check CHECK (typ::text = ANY (ARRAY['ANZAHL_WOHNEINHEITEN_GESAMT'::character varying, 'GESCHOSSFLAECHE_WOHNEN_GESAMT'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.bauvorhaben DROP CONSTRAINT IF EXISTS uk_47ve8fqe1eiw2ct73kjpukn7n;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD CONSTRAINT bauvorhaben_name_vorhaben_key UNIQUE (name_vorhaben);

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD CONSTRAINT bauvorhaben_relevante_abfragevariante_id_key UNIQUE (relevante_abfragevariante_id);

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD CONSTRAINT bauvorhaben_sobon_jahr_check CHECK (sobon_jahr::text = ANY (ARRAY['JAHR_1995'::character varying, 'JAHR_1997'::character varying, 'JAHR_2001'::character varying, 'JAHR_2006'::character varying, 'JAHR_2012'::character varying, 'JAHR_2017'::character varying, 'JAHR_2017_PLUS'::character varying, 'JAHR_2021'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD CONSTRAINT bauvorhaben_sobon_relevant_check1 CHECK (sobon_relevant::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'TRUE'::character varying, 'FALSE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD CONSTRAINT bauvorhaben_stand_verfahren_check CHECK (stand_verfahren::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying, 'AUFTEILUNGSPLAN'::character varying, 'VORBEREITUNG_VORBESCHEID'::character varying, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying, 'STRUKTURKONZEPT'::character varying, 'RAHMENPLANUNG'::character varying, 'POTENTIALUNTERSUCHUNG'::character varying, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying, 'STANDORTABFRAGE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.bauvorhaben_art_fnp
    ADD CONSTRAINT bauvorhaben_art_fnp_art_fnp_check CHECK (art_fnp::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'WR'::character varying, 'WA'::character varying, 'MU'::character varying, 'MK'::character varying, 'MI'::character varying, 'GE'::character varying, 'INFO_FEHLT'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.kinderkrippe
    ADD CONSTRAINT kinderkrippe_einrichtungstraeger_check CHECK (einrichtungstraeger::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'STAEDTISCHE_EINRICHTUNG'::character varying, 'EINRICHTUNG_BETRIEBSTRAEGERSCHAFT'::character varying, 'FREIE_GEMEINNUETZIGE_SONSTIGE'::character varying, 'EINRICHTUNG_GESAMTSTAEDTISCH'::character varying, 'ELTERN_KIND_INITIATIVE'::character varying, 'STAATLICHE_EINRICHTUNG'::character varying, 'PRIVATE_TRAEGERSCHAFT'::character varying, 'KIRCHLICHE_TRAEGERSCHAFT'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ALTER COLUMN frist_bearbeitung TYPE timestamp(6),
    ALTER COLUMN frist_bearbeitung SET NOT NULL;

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    DROP CONSTRAINT IF EXISTS uk_jnr63296n1hkn4qkoo7kvi4tu;

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD CONSTRAINT bauleitplanverfahren_name_key UNIQUE (name);

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD CONSTRAINT bauleitplanverfahren_offizielle_mitzeichnung_check CHECK (offizielle_mitzeichnung::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'TRUE'::character varying, 'FALSE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD CONSTRAINT bauleitplanverfahren_sobon_jahr_check CHECK (sobon_jahr::text = ANY (ARRAY['JAHR_1995'::character varying, 'JAHR_1997'::character varying, 'JAHR_2001'::character varying, 'JAHR_2006'::character varying, 'JAHR_2012'::character varying, 'JAHR_2017'::character varying, 'JAHR_2017_PLUS'::character varying, 'JAHR_2021'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD CONSTRAINT bauleitplanverfahren_sobon_relevant_check2 CHECK (sobon_relevant::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'TRUE'::character varying, 'FALSE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD CONSTRAINT bauleitplanverfahren_stand_verfahren_check CHECK (stand_verfahren::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying, 'AUFTEILUNGSPLAN'::character varying, 'VORBEREITUNG_VORBESCHEID'::character varying, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying, 'STRUKTURKONZEPT'::character varying, 'RAHMENPLANUNG'::character varying, 'POTENTIALUNTERSUCHUNG'::character varying, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying, 'STANDORTABFRAGE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD CONSTRAINT bauleitplanverfahren_status_abfrage_check CHECK (status_abfrage::text = ANY (ARRAY['ANGELEGT'::character varying, 'OFFEN'::character varying, 'IN_BEARBEITUNG_SACHBEARBEITUNG'::character varying, 'IN_BEARBEITUNG_FACHREFERATE'::character varying, 'BEDARFSMELDUNG_ERFOLGT'::character varying, 'ERLEDIGT_MIT_FACHREFERAT'::character varying, 'ERLEDIGT_OHNE_FACHREFERAT'::character varying, 'ABBRUCH'::character varying]::text[]));
DROP INDEX IF EXISTS isidbuser.bauleitplanverfahrenabfrage_name_index;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben_wesentliche_rechtsgrundlage
    ADD CONSTRAINT bauvorhaben_wesentliche_recht_wesentliche_rechtsgrundlage_check CHECK (wesentliche_rechtsgrundlage::text = ANY (ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'INNENBEREICH'::character varying, 'AUSSENBEREICH'::character varying, 'BEFREIUNG'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.abfragevariante_bauleitplanverfahren_wesentliche_rechtsgrundlag
    ADD CONSTRAINT abfragevariante_bauleitplanve_wesentliche_rechtsgrundlage_check CHECK (wesentliche_rechtsgrundlage::text = ANY (ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'INNENBEREICH'::character varying, 'AUSSENBEREICH'::character varying, 'BEFREIUNG'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.infrastruktureinrichtung
    ADD CONSTRAINT infrastruktureinrichtung_status_check CHECK (status::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'UNGESICHERTE_PLANUNG'::character varying, 'GESICHERTE_PLANUNG_NEUE_EINR'::character varying, 'GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR'::character varying, 'GESICHERTE_PLANUNG_TF_KITA_STANDORT'::character varying, 'GESICHERTE_PLANUNG_REDUZIERUNG_PLAETZE'::character varying, 'GESICHERTE_PLANUNG_INTERIMSSTANDORT'::character varying, 'UNGESICHERTE_PLANUNG_TF_KITA_STANDORT'::character varying, 'BESTAND'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.foerdermix_stamm DROP CONSTRAINT IF EXISTS uka1uvagjduujdesyaquotyibfi;

ALTER TABLE IF EXISTS isidbuser.foerdermix_stamm
    ADD CONSTRAINT foerdermix_stamm_bezeichnung_bezeichnung_jahr_key UNIQUE (bezeichnung, bezeichnung_jahr);

ALTER TABLE IF EXISTS isidbuser.foerdermix_stamm_foerderarten
    ALTER COLUMN anteil_prozent  TYPE numeric(5,2);

ALTER TABLE IF EXISTS isidbuser.abfragevariante_weiteres_verfahren_wesentliche_rechtsgrundlage
    ADD CONSTRAINT abfragevariante_weiteres_verf_wesentliche_rechtsgrundlage_check CHECK (wesentliche_rechtsgrundlage::text = ANY (ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'INNENBEREICH'::character varying, 'AUSSENBEREICH'::character varying, 'BEFREIUNG'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD CONSTRAINT dokument_art_dokument_check CHECK (art_dokument::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'EMAIL'::character varying, 'BESCHLUSS'::character varying, 'ANLAGE'::character varying, 'ANTRAG'::character varying, 'KARTE'::character varying, 'STELLUNGNAHME'::character varying, 'DATEN_BAUVORHABEN'::character varying, 'GEBAEUDEPLAN'::character varying, 'BERECHNUNG'::character varying, 'INFOS_BAUGENEHMIGUNG'::character varying, 'PRESSEARTIKEL'::character varying, 'INFOS_ZU_SOZ_INFRASTRUKTUR'::character varying, 'PROTOKOLL'::character varying, 'SONSTIGES'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ALTER COLUMN einwohner_jahr1nach_ersterstellung TYPE numeric(20,15),
    ALTER COLUMN einwohner_jahr2nach_ersterstellung TYPE numeric(20,15),
    ALTER COLUMN einwohner_jahr3nach_ersterstellung TYPE numeric(20,15),
    ALTER COLUMN einwohner_jahr4nach_ersterstellung TYPE numeric(20,15),
    ALTER COLUMN einwohner_jahr5nach_ersterstellung TYPE numeric(20,15),
    ALTER COLUMN einwohner_jahr6nach_ersterstellung TYPE numeric(20,15),
    ALTER COLUMN einwohner_jahr7nach_ersterstellung TYPE numeric(20,15),
    ALTER COLUMN einwohner_jahr8nach_ersterstellung TYPE numeric(20,15),
    ALTER COLUMN einwohner_jahr9nach_ersterstellung TYPE numeric(20,15),
    ALTER COLUMN einwohner_jahr10nach_ersterstellung TYPE numeric(20,15),
    ALTER COLUMN stammwert_arbeitsgruppe TYPE numeric(20,15),
    ALTER COLUMN gueltig_ab TYPE timestamp(6) without time zone,
    ALTER COLUMN einwohner_jahr1nach_ersterstellung SET NOT NULL,
    ALTER COLUMN einwohner_jahr2nach_ersterstellung SET NOT NULL,
    ALTER COLUMN einwohner_jahr3nach_ersterstellung SET NOT NULL,
    ALTER COLUMN einwohner_jahr4nach_ersterstellung SET NOT NULL,
    ALTER COLUMN einwohner_jahr5nach_ersterstellung SET NOT NULL,
    ALTER COLUMN einwohner_jahr6nach_ersterstellung SET NOT NULL,
    ALTER COLUMN einwohner_jahr7nach_ersterstellung SET NOT NULL,
    ALTER COLUMN einwohner_jahr8nach_ersterstellung SET NOT NULL,
    ALTER COLUMN einwohner_jahr9nach_ersterstellung SET NOT NULL,
    ALTER COLUMN einwohner_jahr10nach_ersterstellung SET NOT NULL,
    ALTER COLUMN stammwert_arbeitsgruppe SET NOT NULL,
    ALTER COLUMN gueltig_ab SET NOT NULL;

UPDATE isidbuser.sobon_orientierungswert_soziale_infrastruktur
    SET einrichtungstyp = 'UNSPECIFIED'
    WHERE einrichtungstyp = 'N_N';

UPDATE isidbuser.sobon_orientierungswert_soziale_infrastruktur
    SET einrichtungstyp = 'GS_NACHMITTAG_BETREUUNG'
    WHERE einrichtungstyp = 'KINDERHORT';

ALTER TABLE IF EXISTS isidbuser.sobon_orientierungswert_soziale_infrastruktur DROP CONSTRAINT IF EXISTS ukehqrkyn8jho506swcldo2xc7j;

ALTER TABLE IF EXISTS isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ADD CONSTRAINT sobon_orientierungswert_sozia_gueltig_ab_einrichtungstyp_al_key UNIQUE (gueltig_ab, einrichtungstyp, altersklasse, foerderart_bezeichnung);

ALTER TABLE IF EXISTS isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ADD CONSTRAINT sobon_orientierungswert_soziale_infrastru_einrichtungstyp_check CHECK (einrichtungstyp::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'KINDERKRIPPE'::character varying, 'KINDERGARTEN'::character varying, 'GS_NACHMITTAG_BETREUUNG'::character varying, 'HAUS_FUER_KINDER'::character varying, 'GRUNDSCHULE'::character varying, 'MITTELSCHULE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ADD CONSTRAINT sobon_orientierungswert_soziale_infrastruktu_altersklasse_check CHECK (altersklasse::text = ANY (ARRAY['NULL_ZWEI'::character varying, 'DREI_SECHSEINHALB'::character varying, 'SECHSEINHALB_NEUNEINHALB'::character varying, 'ZEHNEINHALB_FUENFZEHN'::character varying, 'SECHSZEHN_ACHTZEHN'::character varying, 'ALLE_EWO'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.versorgungsquote_gruppenstaerke
    ALTER COLUMN gueltig_ab TYPE timestamp(6) without time zone,
    ALTER COLUMN gueltig_ab SET NOT NULL;

ALTER TABLE IF EXISTS isidbuser.versorgungsquote_gruppenstaerke DROP CONSTRAINT IF EXISTS ukjf69cklrwiws07t3by33ikcig;

ALTER TABLE IF EXISTS isidbuser.versorgungsquote_gruppenstaerke
    ADD CONSTRAINT versorgungsquote_gruppenstaer_gueltig_ab_infrastruktureinri_key UNIQUE (gueltig_ab, infrastruktureinrichtung_typ);

ALTER TABLE IF EXISTS isidbuser.versorgungsquote_gruppenstaerke
    ADD CONSTRAINT versorgungsquote_gruppenstae_infrastruktureinrichtung_typ_check CHECK (infrastruktureinrichtung_typ::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'KINDERKRIPPE'::character varying, 'KINDERGARTEN'::character varying, 'GS_NACHMITTAG_BETREUUNG'::character varying, 'HAUS_FUER_KINDER'::character varying, 'GRUNDSCHULE'::character varying, 'MITTELSCHULE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.global_counter DROP CONSTRAINT IF EXISTS uk7rm8x0kx18f6whau8oms09ije;

ALTER TABLE IF EXISTS isidbuser.global_counter
    ADD CONSTRAINT global_counter_counter_type_key UNIQUE (counter_type);

ALTER TABLE IF EXISTS isidbuser.global_counter
    ADD CONSTRAINT global_counter_counter_type_check CHECK (counter_type::text = 'NUMMER_BAUVORHABEN'::text);


ALTER TABLE IF EXISTS isidbuser.grundschule
    ADD CONSTRAINT grundschule_einrichtungstraeger_check CHECK (einrichtungstraeger::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'STAEDTISCHE_EINRICHTUNG'::character varying, 'EINRICHTUNG_BETRIEBSTRAEGERSCHAFT'::character varying, 'FREIE_GEMEINNUETZIGE_SONSTIGE'::character varying, 'EINRICHTUNG_GESAMTSTAEDTISCH'::character varying, 'ELTERN_KIND_INITIATIVE'::character varying, 'STAATLICHE_EINRICHTUNG'::character varying, 'PRIVATE_TRAEGERSCHAFT'::character varying, 'KIRCHLICHE_TRAEGERSCHAFT'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.abfragevariante DROP COLUMN IF EXISTS is_relevant;

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    ALTER COLUMN frist_bearbeitung  TYPE timestamp(6),
    ALTER COLUMN frist_bearbeitung SET NOT NULL;

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    DROP CONSTRAINT IF EXISTS uk_jebiouc3rgyvww5my14ratjxk;

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    ADD CONSTRAINT baugenehmigungsverfahren_name_key UNIQUE (name);

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    ADD CONSTRAINT baugenehmigungsverfahren_stand_verfahren_check CHECK (stand_verfahren::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying, 'AUFTEILUNGSPLAN'::character varying, 'VORBEREITUNG_VORBESCHEID'::character varying, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying, 'STRUKTURKONZEPT'::character varying, 'RAHMENPLANUNG'::character varying, 'POTENTIALUNTERSUCHUNG'::character varying, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying, 'STANDORTABFRAGE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    ADD CONSTRAINT baugenehmigungsverfahren_status_abfrage_check CHECK (status_abfrage::text = ANY (ARRAY['ANGELEGT'::character varying, 'OFFEN'::character varying, 'IN_BEARBEITUNG_SACHBEARBEITUNG'::character varying, 'IN_BEARBEITUNG_FACHREFERATE'::character varying, 'BEDARFSMELDUNG_ERFOLGT'::character varying, 'ERLEDIGT_MIT_FACHREFERAT'::character varying, 'ERLEDIGT_OHNE_FACHREFERAT'::character varying, 'ABBRUCH'::character varying]::text[]));
DROP INDEX IF EXISTS isidbuser.baugenehmigungsverfahrenabfrage_name_index;

ALTER TABLE IF EXISTS isidbuser.gs_nachmittag_betreuung
    ADD CONSTRAINT gs_nachmittag_betreuung_art_gs_nachmittag_betreuung_check CHECK (art_gs_nachmittag_betreuung::text = ANY (ARRAY['HORT'::character varying, 'KOOPERATIVER_GANZTAG_FLEXIBLE_VARIANTE'::character varying, 'KOOPERATIVER_GANZTAG_RHYTHMISIERTE_VARIANTE'::character varying, 'TAGESHEIM'::character varying, 'MITTAGSBETREUUNG'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.gs_nachmittag_betreuung
    ADD CONSTRAINT gs_nachmittag_betreuung_einrichtungstraeger_check CHECK (einrichtungstraeger::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'STAEDTISCHE_EINRICHTUNG'::character varying, 'EINRICHTUNG_BETRIEBSTRAEGERSCHAFT'::character varying, 'FREIE_GEMEINNUETZIGE_SONSTIGE'::character varying, 'EINRICHTUNG_GESAMTSTAEDTISCH'::character varying, 'ELTERN_KIND_INITIATIVE'::character varying, 'STAATLICHE_EINRICHTUNG'::character varying, 'PRIVATE_TRAEGERSCHAFT'::character varying, 'KIRCHLICHE_TRAEGERSCHAFT'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.abfragevariante_baugenehmigungsverfahren
    ADD COLUMN ausglstr_bdrf_im_bgbt_brckschtgn_kita boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_im_bgbt_brckschtgn_schule boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_im_bplan_kita boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_im_bplan_schule boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_kita boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_kita boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_schule boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_schule boolean DEFAULT FALSE,
    ADD COLUMN stammdaten_gueltig_ab timestamp(6) without time zone,
    ADD COLUMN hinweis_versorgung character varying(1000),
    ADD CONSTRAINT abfragevariante_baugenehmigu_sobon_orientierungswert_jahr_check CHECK (sobon_orientierungswert_jahr::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'JAHR_2014'::character varying, 'JAHR_2017'::character varying, 'JAHR_2022'::character varying, 'STANDORTABFRAGE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.kindergarten
    ADD CONSTRAINT kindergarten_einrichtungstraeger_check CHECK (einrichtungstraeger::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'STAEDTISCHE_EINRICHTUNG'::character varying, 'EINRICHTUNG_BETRIEBSTRAEGERSCHAFT'::character varying, 'FREIE_GEMEINNUETZIGE_SONSTIGE'::character varying, 'EINRICHTUNG_GESAMTSTAEDTISCH'::character varying, 'ELTERN_KIND_INITIATIVE'::character varying, 'STAATLICHE_EINRICHTUNG'::character varying, 'PRIVATE_TRAEGERSCHAFT'::character varying, 'KIRCHLICHE_TRAEGERSCHAFT'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.staedtebauliche_orientierungswert
    ALTER COLUMN gueltig_ab TYPE timestamp(6) without time zone,
    ALTER COLUMN gueltig_ab SET NOT NULL;

ALTER TABLE IF EXISTS isidbuser.staedtebauliche_orientierungswert DROP CONSTRAINT IF EXISTS uk5x9a94ugolr44w0k6k66kud61;

ALTER TABLE IF EXISTS isidbuser.staedtebauliche_orientierungswert
    ADD CONSTRAINT staedtebauliche_orientierungs_gueltig_ab_foerderart_bezeich_key UNIQUE (gueltig_ab, foerderart_bezeichnung);

ALTER TABLE IF EXISTS isidbuser.baugebiet
    ADD CONSTRAINT baugebiet_art_bauliche_nutzung_check CHECK (art_bauliche_nutzung >= 0 AND art_bauliche_nutzung <= 7);

ALTER TABLE IF EXISTS isidbuser.mittelschule
    ADD CONSTRAINT mittelschule_einrichtungstraeger_check CHECK (einrichtungstraeger::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'STAEDTISCHE_EINRICHTUNG'::character varying, 'EINRICHTUNG_BETRIEBSTRAEGERSCHAFT'::character varying, 'FREIE_GEMEINNUETZIGE_SONSTIGE'::character varying, 'EINRICHTUNG_GESAMTSTAEDTISCH'::character varying, 'ELTERN_KIND_INITIATIVE'::character varying, 'STAATLICHE_EINRICHTUNG'::character varying, 'PRIVATE_TRAEGERSCHAFT'::character varying, 'KIRCHLICHE_TRAEGERSCHAFT'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.abfragevariante_baugenehmigungsverfahren_wesentliche_rechtsgrun
    ADD CONSTRAINT abfragevariante_baugenehmigun_wesentliche_rechtsgrundlage_check CHECK (wesentliche_rechtsgrundlage::text = ANY (ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'INNENBEREICH'::character varying, 'AUSSENBEREICH'::character varying, 'BEFREIUNG'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.abfragevariante_weiteres_verfahren
    ADD COLUMN ausglstr_bdrf_im_bgbt_brckschtgn_kita boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_im_bgbt_brckschtgn_schule boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_im_bplan_kita boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_im_bplan_schule boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_kita boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_kita boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_schule boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_schule boolean DEFAULT FALSE,
    ADD COLUMN stammdaten_gueltig_ab timestamp(6) without time zone,
    ADD COLUMN hinweis_versorgung character varying(1000),
    ADD CONSTRAINT abfragevariante_weiteres_ver_sobon_orientierungswert_jahr_check CHECK (sobon_orientierungswert_jahr::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'JAHR_2014'::character varying, 'JAHR_2017'::character varying, 'JAHR_2022'::character varying, 'STANDORTABFRAGE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.bedarfsmeldung_fachreferate DROP CONSTRAINT IF EXISTS fk1m9hfc9q0kwv2y86u05a6qmr8;

ALTER TABLE IF EXISTS isidbuser.bedarfsmeldung_fachreferate DROP CONSTRAINT IF EXISTS fko2jy96eb5dw4jluvwywjfyy2p;

ALTER TABLE IF EXISTS isidbuser.bedarfsmeldung_fachreferate DROP CONSTRAINT IF EXISTS fkp64d3rygwnu9il6mdirm8vyvg;

ALTER TABLE IF EXISTS isidbuser.abfragevariante_bauleitplanverfahren
    ADD COLUMN ausglstr_bdrf_im_bgbt_brckschtgn_kita boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_im_bgbt_brckschtgn_schule boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_im_bplan_kita boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_im_bplan_schule boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_kita boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_kita boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_schule boolean DEFAULT FALSE,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_schule boolean DEFAULT FALSE,
    ADD COLUMN stammdaten_gueltig_ab timestamp(6) without time zone,
    ADD COLUMN hinweis_versorgung character varying(1000),
    ADD CONSTRAINT abfragevariante_bauleitplanv_sobon_orientierungswert_jahr_check CHECK (sobon_orientierungswert_jahr::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'JAHR_2014'::character varying, 'JAHR_2017'::character varying, 'JAHR_2022'::character varying, 'STANDORTABFRAGE'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.haus_fuer_kinder
    ADD CONSTRAINT haus_fuer_kinder_einrichtungstraeger_check CHECK (einrichtungstraeger::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'STAEDTISCHE_EINRICHTUNG'::character varying, 'EINRICHTUNG_BETRIEBSTRAEGERSCHAFT'::character varying, 'FREIE_GEMEINNUETZIGE_SONSTIGE'::character varying, 'EINRICHTUNG_GESAMTSTAEDTISCH'::character varying, 'ELTERN_KIND_INITIATIVE'::character varying, 'STAATLICHE_EINRICHTUNG'::character varying, 'PRIVATE_TRAEGERSCHAFT'::character varying, 'KIRCHLICHE_TRAEGERSCHAFT'::character varying]::text[]));


END;

--
-- Anpassungen von Version 1.1.0 auf Stand Demo 08.02.2024
--

ALTER TABLE isidbuser.mittelschule
    DROP COLUMN is_relevant;

ALTER TABLE isidbuser.abfragevariante_baugenehmigungsverfahren
    ADD COLUMN ausglstr_bdrf_im_bgbt_brckschtgn_kita boolean,
    ADD COLUMN ausglstr_bdrf_im_bgbt_brckschtgn_schule boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_im_bplan_kita boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_im_bplan_schule boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_kita boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_kita boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_schule boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_schule boolean,
    ADD COLUMN stammdaten_gueltig_ab timestamp(6) without time zone,
    ADD COLUMN hinweis_versorgung character varying(1000),
    ADD CONSTRAINT abfragevariante_baugenehmigu_sobon_orientierungswert_jahr_check CHECK (((sobon_orientierungswert_jahr)::text = ANY ((ARRAY['UNSPECIFIED'::character varying, 'JAHR_2014'::character varying, 'JAHR_2017'::character varying, 'JAHR_2022'::character varying, 'STANDORTABFRAGE'::character varying])::text[])));

ALTER TABLE isidbuser.abfragevariante_baugenehmigungsverfahren_wesentliche_rechtsgrun
    ADD CONSTRAINT abfragevariante_baugenehmigun_wesentliche_rechtsgrundlage_check CHECK (((wesentliche_rechtsgrundlage)::text = ANY ((ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'INNENBEREICH'::character varying, 'AUSSENBEREICH'::character varying, 'BEFREIUNG'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying])::text[])));

ALTER TABLE isidbuser.abfragevariante_bauleitplanverfahren
    ADD COLUMN ausglstr_bdrf_im_bgbt_brckschtgn_kita boolean,
    ADD COLUMN ausglstr_bdrf_im_bgbt_brckschtgn_schule boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_im_bplan_kita boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_im_bplan_schule boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_kita boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_kita boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_schule boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_schule boolean,
    ALTER COLUMN satzungsbeschluss timestamp(6) without time zone,
    ADD COLUMN stammdaten_gueltig_ab timestamp(6) without time zone,
    ADD COLUMN hinweis_versorgung character varying(1000),
    ADD CONSTRAINT abfragevariante_bauleitplanv_sobon_orientierungswert_jahr_check CHECK (((sobon_orientierungswert_jahr)::text = ANY ((ARRAY['UNSPECIFIED'::character varying, 'JAHR_2014'::character varying, 'JAHR_2017'::character varying, 'JAHR_2022'::character varying, 'STANDORTABFRAGE'::character varying])::text[])));

ALTER TABLE isidbuser.abfragevariante_bauleitplanverfahren_wesentliche_rechtsgrundlag
    ADD CONSTRAINT abfragevariante_bauleitplanve_wesentliche_rechtsgrundlage_check CHECK (((wesentliche_rechtsgrundlage)::text = ANY ((ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'INNENBEREICH'::character varying, 'AUSSENBEREICH'::character varying, 'BEFREIUNG'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying])::text[])));

ALTER TABLE isidbuser.abfragevariante_weiteres_verfahren
    ADD COLUMN ausglstr_bdrf_im_bgbt_brckschtgn_kita boolean,
    ADD COLUMN ausglstr_bdrf_im_bgbt_brckschtgn_schule boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_im_bplan_kita boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_im_bplan_schule boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_kita boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_kita boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_schule boolean,
    ADD COLUMN ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_schule boolean,
    ALTER COLUMN satzungsbeschluss timestamp(6) without time zone,
    ADD COLUMN stammdaten_gueltig_ab timestamp(6) without time zone,
    ADD COLUMN hinweis_versorgung character varying(1000),
    ADD CONSTRAINT abfragevariante_weiteres_ver_sobon_orientierungswert_jahr_check CHECK (((sobon_orientierungswert_jahr)::text = ANY ((ARRAY['UNSPECIFIED'::character varying, 'JAHR_2014'::character varying, 'JAHR_2017'::character varying, 'JAHR_2022'::character varying, 'STANDORTABFRAGE'::character varying])::text[])));

ALTER TABLE isidbuser.abfragevariante_weiteres_verfahren_wesentliche_rechtsgrundlage
    ADD CONSTRAINT abfragevariante_weiteres_verf_wesentliche_rechtsgrundlage_check CHECK (((wesentliche_rechtsgrundlage)::text = ANY ((ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'INNENBEREICH'::character varying, 'AUSSENBEREICH'::character varying, 'BEFREIUNG'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying])::text[])));

ALTER TABLE isidbuser.baugebiet
    ADD CONSTRAINT baugebiet_art_bauliche_nutzung_check CHECK (((art_bauliche_nutzung >= 0) AND (art_bauliche_nutzung <= 7)));

ALTER TABLE isidbuser.baugenehmigungsverfahren
    ALTER COLUMN  frist_bearbeitung timestamp(6) without time zone NOT NULL,
    ADD CONSTRAINT baugenehmigungsverfahren_stand_verfahren_check CHECK (((stand_verfahren)::text = ANY ((ARRAY['UNSPECIFIED'::character varying, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying, 'AUFTEILUNGSPLAN'::character varying, 'VORBEREITUNG_VORBESCHEID'::character varying, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying, 'STRUKTURKONZEPT'::character varying, 'RAHMENPLANUNG'::character varying, 'POTENTIALUNTERSUCHUNG'::character varying, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying, 'STANDORTABFRAGE'::character varying])::text[]))),
    ADD CONSTRAINT baugenehmigungsverfahren_status_abfrage_check CHECK (((status_abfrage)::text = ANY ((ARRAY['ANGELEGT'::character varying, 'OFFEN'::character varying, 'IN_BEARBEITUNG_SACHBEARBEITUNG'::character varying, 'IN_BEARBEITUNG_FACHREFERATE'::character varying, 'BEDARFSMELDUNG_ERFOLGT'::character varying, 'ERLEDIGT_MIT_FACHREFERAT'::character varying, 'ERLEDIGT_OHNE_FACHREFERAT'::character varying, 'ABBRUCH'::character varying])::text[])));

ALTER TABLE isidbuser.bauleitplanverfahren
    ALTER COLUMN  frist_bearbeitung timestamp(6) without time zone NOT NULL,
    ADD CONSTRAINT bauleitplanverfahren_sobon_relevant_check2 CHECK (((sobon_relevant)::text = ANY ((ARRAY['UNSPECIFIED'::character varying, 'TRUE'::character varying, 'FALSE'::character varying])::text[]))),
    ADD CONSTRAINT bauleitplanverfahren_stand_verfahren_check CHECK (((stand_verfahren)::text = ANY ((ARRAY['UNSPECIFIED'::character varying, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying, 'AUFTEILUNGSPLAN'::character varying, 'VORBEREITUNG_VORBESCHEID'::character varying, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying, 'STRUKTURKONZEPT'::character varying, 'RAHMENPLANUNG'::character varying, 'POTENTIALUNTERSUCHUNG'::character varying, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying, 'STANDORTABFRAGE'::character varying])::text[]))),
    ADD CONSTRAINT bauleitplanverfahren_status_abfrage_check CHECK (((status_abfrage)::text = ANY ((ARRAY['ANGELEGT'::character varying, 'OFFEN'::character varying, 'IN_BEARBEITUNG_SACHBEARBEITUNG'::character varying, 'IN_BEARBEITUNG_FACHREFERATE'::character varying, 'BEDARFSMELDUNG_ERFOLGT'::character varying, 'ERLEDIGT_MIT_FACHREFERAT'::character varying, 'ERLEDIGT_OHNE_FACHREFERAT'::character varying, 'ABBRUCH'::character varying])::text[])));

ALTER TABLE isidbuser.baurate_foerderarten
    ALTER COLUMN anteil_prozent TYPE numeric(5,2);

ALTER TABLE isidbuser.bauvorhaben
    ADD CONSTRAINT bauvorhaben_sobon_jahr_check CHECK (((sobon_jahr)::text = ANY ((ARRAY['JAHR_1995'::character varying, 'JAHR_1997'::character varying, 'JAHR_2001'::character varying, 'JAHR_2006'::character varying, 'JAHR_2012'::character varying, 'JAHR_2017'::character varying, 'JAHR_2017_PLUS'::character varying, 'JAHR_2021'::character varying])::text[]))),
    ADD CONSTRAINT bauvorhaben_sobon_relevant_check1 CHECK (((sobon_relevant)::text = ANY ((ARRAY['UNSPECIFIED'::character varying, 'TRUE'::character varying, 'FALSE'::character varying])::text[]))),
    ADD CONSTRAINT bauvorhaben_stand_verfahren_check CHECK (((stand_verfahren)::text = ANY ((ARRAY['UNSPECIFIED'::character varying, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying, 'AUFTEILUNGSPLAN'::character varying, 'VORBEREITUNG_VORBESCHEID'::character varying, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying, 'STRUKTURKONZEPT'::character varying, 'RAHMENPLANUNG'::character varying, 'POTENTIALUNTERSUCHUNG'::character varying, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying, 'STANDORTABFRAGE'::character varying])::text[])));

ALTER TABLE isidbuser.bauvorhaben_art_fnp
    ADD CONSTRAINT bauvorhaben_art_fnp_art_fnp_check CHECK (((art_fnp)::text = ANY ((ARRAY['UNSPECIFIED'::character varying, 'WR'::character varying, 'WA'::character varying, 'MU'::character varying, 'MK'::character varying, 'MI'::character varying, 'GE'::character varying, 'INFO_FEHLT'::character varying])::text[])));

ALTER TABLE isidbuser.bauvorhaben_wesentliche_rechtsgrundlage
    ADD CONSTRAINT bauvorhaben_wesentliche_recht_wesentliche_rechtsgrundlage_check CHECK (((wesentliche_rechtsgrundlage)::text = ANY ((ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'INNENBEREICH'::character varying, 'AUSSENBEREICH'::character varying, 'BEFREIUNG'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying])::text[])));

-- Bedarfsmeldung Fachrefereate

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
    ADD COLUMN stammdaten_gueltig_ab timestamp(6) without time zone,
    ADD COLUMN hinweis_versorgung character varying(1000),
    ADD CONSTRAINT abfragevariante_weiteres_ver_sobon_orientierungswert_jahr_check CHECK (((sobon_orientierungswert_jahr)::text = ANY ((ARRAY['UNSPECIFIED'::character varying, 'JAHR_2014'::character varying, 'JAHR_2017'::character varying, 'JAHR_2022'::character varying, 'STANDORTABFRAGE'::character varying])::text[])));

ALTER TABLE isidbuser.abfragevariante_weiteres_verfahren_wesentliche_rechtsgrundlage
    ADD CONSTRAINT abfragevariante_weiteres_verf_wesentliche_rechtsgrundlage_check CHECK (((wesentliche_rechtsgrundlage)::text = ANY ((ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying, 'INNENBEREICH'::character varying, 'AUSSENBEREICH'::character varying, 'BEFREIUNG'::character varying, 'INFO_FEHLT'::character varying, 'FREIE_EINGABE'::character varying])::text[])));

ALTER TABLE isidbuser.baugebiet
    ADD CONSTRAINT baugebiet_art_bauliche_nutzung_check CHECK (((art_bauliche_nutzung >= 0) AND (art_bauliche_nutzung <= 7)));



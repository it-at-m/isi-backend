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
    ADD COLUMN hinweis_versorgung character varying(1000),
    ADD CONSTRAINT abfragevariante_baugenehmigu_sobon_orientierungswert_jahr_check CHECK (((sobon_orientierungswert_jahr)::text = ANY ((ARRAY['UNSPECIFIED'::character varying, 'JAHR_2014'::character varying, 'JAHR_2017'::character varying, 'JAHR_2022'::character varying, 'STANDORTABFRAGE'::character varying])::text[])));



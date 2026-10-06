BEGIN;

--
-- Vereinheitlichung aller persönlichen Nutzerdaten in der Tabelle personal_filter.
-- Die Startseiteneinstellung ("Meine Vorgänge") wird zu einer systemeigenen Zeile,
-- die über das Flag ist_startseite markiert ist.
--

ALTER TABLE isidbuser.personal_filter
    ADD COLUMN ist_startseite BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN schnellfilter  character varying(255);

-- Diese Felder beschreiben ausschließlich echte Filter. Die Startseiten-Zeile hält sie nicht,
-- die Pflicht für echte Filter wird im API-Layer über FilterSettingsDto validiert.
ALTER TABLE isidbuser.personal_filter
    ALTER COLUMN select_bauleitplanverfahren DROP NOT NULL,
    ALTER COLUMN select_baugenehmigungsverfahren DROP NOT NULL,
    ALTER COLUMN select_weiteres_verfahren DROP NOT NULL,
    ALTER COLUMN select_bauvorhaben DROP NOT NULL,
    ALTER COLUMN select_grundschule DROP NOT NULL,
    ALTER COLUMN select_gs_nachmittag_betreuung DROP NOT NULL,
    ALTER COLUMN select_haus_fuer_kinder DROP NOT NULL,
    ALTER COLUMN select_kindergarten DROP NOT NULL,
    ALTER COLUMN select_kinderkrippe DROP NOT NULL,
    ALTER COLUMN select_mittelschule DROP NOT NULL,
    ALTER COLUMN sobon_relevant DROP NOT NULL;

-- Übernahme der bestehenden Startseiteneinstellungen inklusive ID und Zeitstempel.
INSERT INTO isidbuser.personal_filter (id,
                                       version,
                                       created_date_time,
                                       last_modified_date_time,
                                       personalid,
                                       filter_name,
                                       ist_startseite,
                                       schnellfilter,
                                       sort_by,
                                       sort_order)
SELECT id,
       version,
       created_date_time,
       last_modified_date_time,
       personalid,
       'Startseiteneinstellung',
       TRUE,
       schnellfilter,
       sort_by,
       sort_order
FROM isidbuser.startseiten_einstellung;

DROP TABLE isidbuser.startseiten_einstellung;

-- Je Nutzer darf höchstens eine Startseiten-Zeile existieren.
CREATE UNIQUE INDEX personal_filter_startseite_personalid_uidx
    ON isidbuser.personal_filter (personalid)
    WHERE ist_startseite;

END;

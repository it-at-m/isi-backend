BEGIN;

--
-- Zusammenführung aller persönlichen Nutzerdaten in einer Tabelle.
--
-- Bisher lagen sie in neun Tabellen: personal_filter mit sieben @ElementCollection-Tabellen für die
-- Listenfelder sowie startseiten_einstellung. Die Daten werden ausschließlich vollständig je Nutzer
-- gelesen und geschrieben, es wird nie über ihren Inhalt gesucht. Sie werden deshalb als JSON
-- abgelegt; weitere Profilattribute lassen sich danach ohne Datenmigration ergänzen.
--

CREATE TABLE isidbuser.benutzer
(
    id                      character varying(36) PRIMARY KEY,
    version                 BIGINT,
    created_date_time       TIMESTAMP              NOT NULL,
    last_modified_date_time TIMESTAMP              NOT NULL,

    personalid              character varying(255) NOT NULL UNIQUE,
    startseiten_einstellung JSONB,
    persoenliche_filter     JSONB
);

INSERT INTO isidbuser.benutzer (id,
                                version,
                                created_date_time,
                                last_modified_date_time,
                                personalid,
                                startseiten_einstellung,
                                persoenliche_filter)
SELECT gen_random_uuid()::character varying(36),
       0,
       now(),
       now(),
       u.personalid,
       -- Aus der bisherigen Tabelle startseiten_einstellung wird ein Attribut.
       (SELECT jsonb_build_object('schnellfilter', s.schnellfilter,
                                  'sortBy', s.sort_by,
                                  'sortOrder', s.sort_order)
          FROM isidbuser.startseiten_einstellung s
         WHERE s.personalid = u.personalid),
       -- Aus personal_filter samt Kindtabellen wird eine Liste, absteigend nach letzter Änderung.
       (SELECT jsonb_agg(
                   jsonb_build_object(
                       'id', f.id,
                       'filterName', f.filter_name,
                       'createdDateTime', to_jsonb(f.created_date_time),
                       'lastModifiedDateTime', to_jsonb(f.last_modified_date_time),
                       'filterSettings', jsonb_build_object(
                           'sortBy', f.sort_by,
                           'sortOrder', f.sort_order,
                           'selectBauleitplanverfahren', f.select_bauleitplanverfahren,
                           'selectBaugenehmigungsverfahren', f.select_baugenehmigungsverfahren,
                           'selectWeiteresVerfahren', f.select_weiteres_verfahren,
                           'selectBauvorhaben', f.select_bauvorhaben,
                           'selectGrundschule', f.select_grundschule,
                           'selectGsNachmittagBetreuung', f.select_gs_nachmittag_betreuung,
                           'selectHausFuerKinder', f.select_haus_fuer_kinder,
                           'selectKindergarten', f.select_kindergarten,
                           'selectKinderkrippe', f.select_kinderkrippe,
                           'selectMittelschule', f.select_mittelschule,
                           'realisierungsbeginnVon', f.realisierungsbeginn_von,
                           'realisierungsbeginnBis', f.realisierungsbeginn_bis,
                           'nurEigeneAbfragen', f.nur_eigene_abfragen,
                           'sobonRelevant', f.sobon_relevant,
                           'weGesamtVon', f.we_gesamt_von,
                           'weGesamtBis', f.we_gesamt_bis,
                           'gfWohnenGeplantVon', f.gf_wohnen_geplant_von,
                           'gfWohnenGeplantBis', f.gf_wohnen_geplant_bis,
                           'stadtbezirkNummer',
                           (SELECT jsonb_agg(x.stadtbezirk_nummer)
                              FROM isidbuser.personal_filter_stadtbezirk_nummer x
                             WHERE x.personal_filter_id = f.id),
                           'kitaplanungsbereichKitaPlbT',
                           (SELECT jsonb_agg(x.kitaplanungsbereich_kita_plbt)
                              FROM isidbuser.personal_filter_kitaplanungsbereich_kita_plbt x
                             WHERE x.personal_filter_id = f.id),
                           'grundschulsprengelNummer',
                           (SELECT jsonb_agg(x.grundschulsprengel_nummer)
                              FROM isidbuser.personal_filter_grundschulsprengel_nummer x
                             WHERE x.personal_filter_id = f.id),
                           'mittelschulsprengelNummer',
                           (SELECT jsonb_agg(x.mittelschulsprengel_nummer)
                              FROM isidbuser.personal_filter_mittelschulsprengel_nummer x
                             WHERE x.personal_filter_id = f.id),
                           'statusAbfrage',
                           (SELECT jsonb_agg(x.status_abfrage)
                              FROM isidbuser.personal_filter_status_abfrage x
                             WHERE x.personal_filter_id = f.id),
                           'verfahrensstand',
                           (SELECT jsonb_agg(x.verfahrensstand)
                              FROM isidbuser.personal_filter_verfahrensstand x
                             WHERE x.personal_filter_id = f.id),
                           'infrastruktureinrichtungStatus',
                           (SELECT jsonb_agg(x.infrastruktureinrichtung_status)
                              FROM isidbuser.personal_filter_infrastruktureinrichtung_status x
                             WHERE x.personal_filter_id = f.id)
                       ))
                   ORDER BY f.last_modified_date_time DESC)
          FROM isidbuser.personal_filter f
         WHERE f.personalid = u.personalid)
-- Ein Nutzer kann ausschließlich Filter oder ausschließlich Startseiteneinstellungen besitzen.
FROM (SELECT personalid FROM isidbuser.personal_filter
      UNION
      SELECT personalid FROM isidbuser.startseiten_einstellung) u;

DROP TABLE isidbuser.personal_filter_stadtbezirk_nummer;
DROP TABLE isidbuser.personal_filter_kitaplanungsbereich_kita_plbt;
DROP TABLE isidbuser.personal_filter_grundschulsprengel_nummer;
DROP TABLE isidbuser.personal_filter_mittelschulsprengel_nummer;
DROP TABLE isidbuser.personal_filter_status_abfrage;
DROP TABLE isidbuser.personal_filter_verfahrensstand;
DROP TABLE isidbuser.personal_filter_infrastruktureinrichtung_status;
DROP TABLE isidbuser.personal_filter;
DROP TABLE isidbuser.startseiten_einstellung;

END;

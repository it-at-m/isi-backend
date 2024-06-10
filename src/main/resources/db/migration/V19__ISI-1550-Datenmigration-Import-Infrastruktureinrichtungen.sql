BEGIN;

--
-- Hinzufügen des Feldes ID_KiBiG_Web bei der Kinderkrippe Tabelle
--
ALTER TABLE isidbuser.kinderkrippe ADD id_kibig_web character varying(255);

--
-- Hinzufügen des Feldes ID_KiBiG_Web bei der Kindergarten Tabelle
--
ALTER TABLE isidbuser.kindergarten ADD id_kibig_web character varying(255);

--
-- Hinzufügen des Feldes ID_KiBiG_Web bei der Haus für Kinder Tabelle
--
ALTER TABLE isidbuser.haus_fuer_kinder ADD id_kibig_web character varying(255);

--
-- Hinzufügen des Feldes ID_KiBiG_Web bei der Nachmittagsbetreuung für Grundschulkinder Tabelle
--
ALTER TABLE isidbuser.gs_nachmittag_betreuung ADD id_kibig_web character varying(255);

END;
BEGIN;

--
-- Hinzufügen des Feldes ID_KiBiG_Web bei der Infrastruktureinrichtung Tabelle
--
ALTER TABLE isidbuser.infrastruktureinrichtung ADD id_kibig_web character varying(255);

END;
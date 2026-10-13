BEGIN;

--
-- Neue Infrastruktureinrichtung "Nachbarschaftstreff" anlegen
--

CREATE TABLE isidbuser.nachbarschaftstreff (
   kooperation character varying(255),
   kooperation_freie_eingabe character varying(1000),
   sobon_relevant character varying(255) NOT NULL,
   CONSTRAINT nachbarschaftstreff_sobon_relevant_check CHECK ((sobon_relevant::text <> 'UNSPECIFIED'::text)),
   id character varying(36) NOT NULL,
   CONSTRAINT nachbarschaftstreff_pkey PRIMARY KEY (id),
   CONSTRAINT fk_id_infrastruktureinrichtung_id FOREIGN KEY (id) REFERENCES isidbuser.infrastruktureinrichtung(id)
);
ALTER TABLE isidbuser.nachbarschaftstreff OWNER TO isidbuser;

---
---  Refactoring anlass_planung: von Superklasse "Infrastruktureinrichtung" in die Subklassen verschieben
---

ALTER TABLE IF EXISTS isidbuser.infrastruktureinrichtung
    DROP COLUMN anlass_planung;
ALTER TABLE IF EXISTS isidbuser.kindergarten
    ADD COLUMN anlass_planung character varying(255) NOT NULL;
ALTER TABLE IF EXISTS isidbuser.kinderkrippe
    ADD COLUMN anlass_planung character varying(255) NOT NULL;
ALTER TABLE IF EXISTS isidbuser.haus_fuer_kinder
    ADD COLUMN anlass_planung character varying(255) NOT NULL;
ALTER TABLE IF EXISTS isidbuser.gs_nachmittag_betreuung
    ADD COLUMN anlass_planung character varying(255) NOT NULL;
ALTER TABLE IF EXISTS isidbuser.grundschule
    ADD COLUMN anlass_planung character varying(255) NOT NULL;
ALTER TABLE IF EXISTS isidbuser.mittelschule
    ADD COLUMN anlass_planung character varying(255) NOT NULL;

---
--- Persönlichen Filter um neue Infrastruktureinrichtung Nachbarschaftstreff erweitern
---

ALTER TABLE IF EXISTS isidbuser.personal_filter
    ADD COLUMN select_nachbarschaftstreff BOOLEAN NOT NULL;

COMMIT;

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
--- Persönlichen Filter um neue Infrastruktureinrichtung Nachbarschaftstreff erweitern
---

ALTER TABLE IF EXISTS isidbuser.personal_filter
    ADD COLUMN select_nachbarschaftstreff BOOLEAN NOT NULL;

COMMIT;

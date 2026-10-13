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
    ADD COLUMN anlass_planung character varying(255) NOT NULL,
    ADD CONSTRAINT kindergarten_status_check CHECK (
        status IN ('BESTAND', 'UNGESICHERTE_PLANUNG', 'GESICHERTE_PLANUNG', 'PLANUNG_ZURUECKGEZOGEN')
    );
ALTER TABLE IF EXISTS isidbuser.kinderkrippe
    ADD COLUMN anlass_planung character varying(255) NOT NULL,
    ADD CONSTRAINT kinderkrippe_status_check CHECK (
        status IN ('BESTAND', 'UNGESICHERTE_PLANUNG', 'GESICHERTE_PLANUNG', 'PLANUNG_ZURUECKGEZOGEN')
    );
ALTER TABLE IF EXISTS isidbuser.haus_fuer_kinder
    ADD COLUMN anlass_planung character varying(255) NOT NULL,
    ADD CONSTRAINT haus_fuer_kinder_status_check CHECK (
        status IN ('BESTAND', 'UNGESICHERTE_PLANUNG', 'GESICHERTE_PLANUNG', 'PLANUNG_ZURUECKGEZOGEN')
    );
ALTER TABLE IF EXISTS isidbuser.gs_nachmittag_betreuung
    ADD COLUMN anlass_planung character varying(255) NOT NULL,
    ADD CONSTRAINT gs_nachmittag_betreuung_status_check CHECK (
        status IN ('BESTAND', 'UNGESICHERTE_PLANUNG', 'GESICHERTE_PLANUNG', 'PLANUNG_ZURUECKGEZOGEN')
    );
ALTER TABLE IF EXISTS isidbuser.grundschule
    ADD COLUMN anlass_planung character varying(255) NOT NULL,
    ADD CONSTRAINT grundschule_status_check CHECK (
        status IN ('BESTAND', 'UNGESICHERTE_PLANUNG', 'GESICHERTE_PLANUNG', 'PLANUNG_ZURUECKGEZOGEN')
    );
ALTER TABLE IF EXISTS isidbuser.mittelschule
    ADD COLUMN anlass_planung character varying(255) NOT NULL DEFAULT 'UNSPECIFIED',
    ADD CONSTRAINT mittelschule_status_check CHECK (
        status IN ('BESTAND', 'UNGESICHERTE_PLANUNG', 'GESICHERTE_PLANUNG', 'PLANUNG_ZURUECKGEZOGEN')
    );

---
--- Persönlichen Filter um neue Infrastruktureinrichtung Nachbarschaftstreff erweitern
---

ALTER TABLE IF EXISTS isidbuser.personal_filter
    ADD COLUMN select_nachbarschaftstreff BOOLEAN NOT NULL;

COMMIT;

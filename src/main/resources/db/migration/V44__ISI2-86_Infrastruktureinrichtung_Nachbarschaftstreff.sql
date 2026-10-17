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

-- Add selectNachbarschaftstreff flag into persoenliche_filter JSON for filters migrated by V43
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='isidbuser' AND tablename='benutzer') THEN
    UPDATE isidbuser.benutzer b
    SET persoenliche_filter = COALESCE(
      (
        SELECT jsonb_agg(
          CASE
            WHEN (elem->'filterSettings') ? 'selectNachbarschaftstreff' THEN elem
            ELSE jsonb_set(elem, '{filterSettings,selectNachbarschaftstreff}', 'false'::jsonb, true)
          END
        )
        FROM jsonb_array_elements(b.persoenliche_filter) AS t(elem)
      ),
      '[]'::jsonb
    )
    WHERE b.persoenliche_filter IS NOT NULL;
  END IF;
END$$;

COMMIT;

--
-- Anpassungen von Version 1.1.0 auf Stand Demo 08.02.2024
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.infrastruktureinrichtung
    ADD COLUMN verortung jsonb;

END;

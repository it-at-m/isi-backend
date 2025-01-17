--
-- Umbenennung Fördermix "SoBoN-Ursächlichkeit" in "SoBoN 2017"
--
BEGIN;

UPDATE baurate
SET bezeichnung = 'SoBoN 2017'
WHERE bezeichnung = 'SoBoN-Ursächlichkeit';

END;
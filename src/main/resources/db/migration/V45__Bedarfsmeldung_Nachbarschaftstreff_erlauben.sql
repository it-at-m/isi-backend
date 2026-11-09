BEGIN;

--
-- Bedarfsmeldungen für Nachbarschaftstreffs ermöglichen.
--
-- Die Spalte infrastruktureinrichtung_typ speichert den Ordinalwert des Enums InfrastruktureinrichtungTyp.
-- Der mit V44 hinzugefügte Wert NACHBARSCHAFTSTREFF hat den Ordinalwert 7 und wurde bisher durch den
-- CHECK-Constraint (0 bis 6) beim Speichern einer Bedarfsmeldung abgelehnt.
--

ALTER TABLE isidbuser.bedarfsmeldung
    DROP CONSTRAINT bedarfsmeldung_infrastruktureinrichtung_typ_check;

ALTER TABLE isidbuser.bedarfsmeldung
    ADD CONSTRAINT bedarfsmeldung_infrastruktureinrichtung_typ_check
    CHECK (infrastruktureinrichtung_typ >= 0 AND infrastruktureinrichtung_typ <= 7);

COMMIT;

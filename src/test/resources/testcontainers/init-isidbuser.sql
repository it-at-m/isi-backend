-- Die Flyway-Migrationen (ab V3, pg_dump-Baseline) setzen die Rolle isidbuser voraus ("OWNER TO isidbuser").
-- Das Schema muss dieser Rolle gehören, da PostgreSQL Fremdschlüsselprüfungen mit den Rechten des Tabelleneigentümers ausführt.
CREATE ROLE isidbuser;
CREATE SCHEMA isidbuser AUTHORIZATION isidbuser;

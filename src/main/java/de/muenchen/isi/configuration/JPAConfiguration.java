package de.muenchen.isi.configuration;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA-Konfiguration.
 *
 * Das Paket "org.springframework.data.jpa.convert.threeten" wird mitgescannt, damit die Jsr310JpaConverters
 * (autoApply) greifen und z. B. LocalDate wie bisher auf die bestehenden timestamp-Spalten gemappt wird.
 * Die Konfiguration liegt bewusst nicht auf der Application-Klasse, damit Slice-Tests wie @WebMvcTest
 * keine JPA-Infrastruktur benötigen.
 */
@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = { "org.springframework.data.jpa.convert.threeten", "de.muenchen.isi" })
public class JPAConfiguration {}

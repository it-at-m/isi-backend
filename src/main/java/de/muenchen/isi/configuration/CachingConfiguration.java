/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides the caches.
 * To disable the caching functionality delete this class, remove the corresponding bean creation methods
 * or remove the annotation {@link EnableCaching} above the class definition.
 */
@Configuration
@EnableCaching
public class CachingConfiguration {

    public static final int AUTHENTICATION_CACHE_EXPIRATION_TIME_SECONDS = 60;
}

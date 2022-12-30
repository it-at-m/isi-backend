package de.muenchen.isi.configuration;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        // Für Generierung der Spring-relevanten Bestandteile in MapperImpl
        componentModel = MappingConstants.ComponentModel.SPRING
)
public class MapstructConfiguration {
}

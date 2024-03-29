/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteBaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteWeiteresVerfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt.AbfragevarianteBaugenehmigungsverfahrenBedarfsmeldungErfolgtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt.AbfragevarianteBauleitplanverfahrenBedarfsmeldungErfolgtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt.AbfragevarianteWeiteresVerfahrenBedarfsmeldungErfolgtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfragevarianteWeiteresVerfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteBaugenehmigungsverfahren;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteBauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteWeiteresVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfiguration.class, uses = { BauabschnittDomainMapper.class })
public interface AbfragevarianteDomainMapper {
    AbfragevarianteBauleitplanverfahrenModel entity2Model(final AbfragevarianteBauleitplanverfahren entity);

    AbfragevarianteBaugenehmigungsverfahrenModel entity2Model(final AbfragevarianteBaugenehmigungsverfahren entity);

    AbfragevarianteWeiteresVerfahrenModel entity2Model(final AbfragevarianteWeiteresVerfahren entity);

    AbfragevarianteBauleitplanverfahren model2Entity(final AbfragevarianteBauleitplanverfahrenModel model);

    AbfragevarianteBaugenehmigungsverfahren model2Entity(final AbfragevarianteBaugenehmigungsverfahrenModel model);

    AbfragevarianteWeiteresVerfahren model2Entity(final AbfragevarianteWeiteresVerfahrenModel model);

    @Mappings(
        {
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "sobonOrientierungswertJahr", ignore = true),
            @Mapping(target = "sobonBerechnung", ignore = true),
            @Mapping(target = "stammdatenGueltigAb", ignore = true),
            @Mapping(target = "anmerkung", ignore = true),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true),
            @Mapping(target = "hinweisVersorgung", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenAngelegtModel request,
        @MappingTarget final AbfragevarianteBauleitplanverfahrenModel model
    );

    @Mappings(
        {
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "sobonOrientierungswertJahr", ignore = true),
            @Mapping(target = "stammdatenGueltigAb", ignore = true),
            @Mapping(target = "anmerkung", ignore = true),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true),
            @Mapping(target = "hinweisVersorgung", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
        }
    )
    AbfragevarianteBaugenehmigungsverfahrenModel request2Model(
        final AbfragevarianteBaugenehmigungsverfahrenAngelegtModel request,
        @MappingTarget final AbfragevarianteBaugenehmigungsverfahrenModel model
    );

    @Mappings(
        {
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "sobonOrientierungswertJahr", ignore = true),
            @Mapping(target = "sobonBerechnung", ignore = true),
            @Mapping(target = "stammdatenGueltigAb", ignore = true),
            @Mapping(target = "anmerkung", ignore = true),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true),
            @Mapping(target = "hinweisVersorgung", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
        }
    )
    AbfragevarianteWeiteresVerfahrenModel request2Model(
        final AbfragevarianteWeiteresVerfahrenAngelegtModel request,
        @MappingTarget final AbfragevarianteWeiteresVerfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "sobonOrientierungswertJahr", ignore = false),
            @Mapping(target = "sobonBerechnung", ignore = false),
            @Mapping(target = "stammdatenGueltigAb", ignore = false),
            @Mapping(target = "anmerkung", ignore = false),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel request,
        @MappingTarget final AbfragevarianteBauleitplanverfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "sobonOrientierungswertJahr", ignore = false),
            @Mapping(target = "stammdatenGueltigAb", ignore = false),
            @Mapping(target = "anmerkung", ignore = false),
        }
    )
    AbfragevarianteBaugenehmigungsverfahrenModel request2Model(
        final AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel request,
        @MappingTarget final AbfragevarianteBaugenehmigungsverfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "sobonOrientierungswertJahr", ignore = false),
            @Mapping(target = "sobonBerechnung", ignore = false),
            @Mapping(target = "stammdatenGueltigAb", ignore = false),
            @Mapping(target = "anmerkung", ignore = false),
        }
    )
    AbfragevarianteWeiteresVerfahrenModel request2Model(
        final AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungModel request,
        @MappingTarget final AbfragevarianteWeiteresVerfahrenModel model
    );

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true),
            @Mapping(target = "hinweisVersorgung", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel request,
        @MappingTarget final AbfragevarianteBauleitplanverfahrenModel model
    );

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true),
            @Mapping(target = "hinweisVersorgung", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
        }
    )
    AbfragevarianteBaugenehmigungsverfahrenModel request2Model(
        final AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel request,
        @MappingTarget final AbfragevarianteBaugenehmigungsverfahrenModel model
    );

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true),
            @Mapping(target = "hinweisVersorgung", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
        }
    )
    AbfragevarianteWeiteresVerfahrenModel request2Model(
        final AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungModel request,
        @MappingTarget final AbfragevarianteWeiteresVerfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = false),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = false),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = false),
            @Mapping(target = "hinweisVersorgung", ignore = false),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel request,
        @MappingTarget final AbfragevarianteBauleitplanverfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = false),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = false),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = false),
            @Mapping(target = "hinweisVersorgung", ignore = false),
        }
    )
    AbfragevarianteBaugenehmigungsverfahrenModel request2Model(
        final AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatModel request,
        @MappingTarget final AbfragevarianteBaugenehmigungsverfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = false),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = false),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = false),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = false),
            @Mapping(target = "hinweisVersorgung", ignore = false),
        }
    )
    AbfragevarianteWeiteresVerfahrenModel request2Model(
        final AbfragevarianteWeiteresVerfahrenInBearbeitungFachreferatModel request,
        @MappingTarget final AbfragevarianteWeiteresVerfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = false),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenBedarfsmeldungErfolgtModel request,
        @MappingTarget final AbfragevarianteBauleitplanverfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = false),
        }
    )
    AbfragevarianteBaugenehmigungsverfahrenModel request2Model(
        final AbfragevarianteBaugenehmigungsverfahrenBedarfsmeldungErfolgtModel request,
        @MappingTarget final AbfragevarianteBaugenehmigungsverfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = false),
        }
    )
    AbfragevarianteWeiteresVerfahrenModel request2Model(
        final AbfragevarianteWeiteresVerfahrenBedarfsmeldungErfolgtModel request,
        @MappingTarget final AbfragevarianteWeiteresVerfahrenModel model
    );

    @AfterMapping
    default void afterMappingToModel(@MappingTarget final AbfragevarianteBaugenehmigungsverfahrenModel model) {
        model.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
    }

    @AfterMapping
    default void afterMappingToModel(@MappingTarget final AbfragevarianteBauleitplanverfahrenModel model) {
        model.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
    }

    @AfterMapping
    default void afterMappingToModel(@MappingTarget final AbfragevarianteWeiteresVerfahrenModel model) {
        model.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
    }
}

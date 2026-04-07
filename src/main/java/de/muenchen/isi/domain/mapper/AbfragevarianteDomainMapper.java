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
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.AbfragevarianteBaugenehmigungsverfahrenEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.AbfragevarianteBauleitplanverfahrenEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.AbfragevarianteWeiteresVerfahrenEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageEinplanungBedarfe.AbfragevarianteBaugenehmigungsverfahrenEinplanungBedarfeModel;
import de.muenchen.isi.domain.model.abfrageEinplanungBedarfe.AbfragevarianteBauleitplanverfahrenEinplanungBedarfeModel;
import de.muenchen.isi.domain.model.abfrageEinplanungBedarfe.AbfragevarianteWeiteresVerfahrenEinplanungBedarfeModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.AbfragevarianteBaugenehmigungsverfahrenStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.AbfragevarianteBauleitplanverfahrenSachbearbeitungStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.AbfragevarianteBauleitplanverfahrenStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.AbfragevarianteWeiteresVerfahrenSachbearbeitungStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.AbfragevarianteWeiteresVerfahrenStartBearbeitungModel;
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
            @Mapping(target = "sobonOrientierungswertJahrPlanungsursaechlich", ignore = true),
            @Mapping(target = "sobonBerechnung", ignore = true),
            @Mapping(target = "stammdatenGueltigAb", ignore = true),
            @Mapping(target = "anmerkung", ignore = true),
            @Mapping(target = "hasBauratendateiInput", ignore = true),
            @Mapping(target = "anmerkungBauratendateiInput", ignore = true),
            @Mapping(target = "bauratendateiInputBasis", ignore = true),
            @Mapping(target = "bauratendateiInput", ignore = true),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true),
            @Mapping(target = "anmerkungFachreferate", ignore = true),
            @Mapping(target = "anmerkungAbfrageersteller", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
            @Mapping(target = "dokumente", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = true),
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
            @Mapping(target = "sobonOrientierungswertJahrPlanungsursaechlich", ignore = true),
            @Mapping(target = "stammdatenGueltigAb", ignore = true),
            @Mapping(target = "anmerkung", ignore = true),
            @Mapping(target = "hasBauratendateiInput", ignore = true),
            @Mapping(target = "anmerkungBauratendateiInput", ignore = true),
            @Mapping(target = "bauratendateiInputBasis", ignore = true),
            @Mapping(target = "bauratendateiInput", ignore = true),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true),
            @Mapping(target = "anmerkungFachreferate", ignore = true),
            @Mapping(target = "anmerkungAbfrageersteller", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
            @Mapping(target = "dokumente", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = true),
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
            @Mapping(target = "sobonOrientierungswertJahrPlanungsursaechlich", ignore = true),
            @Mapping(target = "sobonBerechnung", ignore = true),
            @Mapping(target = "stammdatenGueltigAb", ignore = true),
            @Mapping(target = "anmerkung", ignore = true),
            @Mapping(target = "hasBauratendateiInput", ignore = true),
            @Mapping(target = "anmerkungBauratendateiInput", ignore = true),
            @Mapping(target = "bauratendateiInputBasis", ignore = true),
            @Mapping(target = "bauratendateiInput", ignore = true),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true),
            @Mapping(target = "anmerkungFachreferate", ignore = true),
            @Mapping(target = "anmerkungAbfrageersteller", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
            @Mapping(target = "dokumente", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = true),
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
            @Mapping(target = "sobonOrientierungswertJahrPlanungsursaechlich", ignore = false),
            @Mapping(target = "sobonBerechnung", ignore = false),
            @Mapping(target = "stammdatenGueltigAb", ignore = false),
            @Mapping(target = "anmerkung", ignore = false),
            @Mapping(target = "hasBauratendateiInput", ignore = false),
            @Mapping(target = "anmerkungBauratendateiInput", ignore = false),
            @Mapping(target = "bauratendateiInputBasis", ignore = false),
            @Mapping(target = "bauratendateiInput", ignore = false),
            @Mapping(target = "dokumente", ignore = false),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenSachbearbeitungStartBearbeitungModel request,
        @MappingTarget final AbfragevarianteBauleitplanverfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "sobonOrientierungswertJahrPlanungsursaechlich", ignore = false),
            @Mapping(target = "stammdatenGueltigAb", ignore = false),
            @Mapping(target = "anmerkung", ignore = false),
            @Mapping(target = "hasBauratendateiInput", ignore = false),
            @Mapping(target = "anmerkungBauratendateiInput", ignore = false),
            @Mapping(target = "bauratendateiInputBasis", ignore = false),
            @Mapping(target = "bauratendateiInput", ignore = false),
            @Mapping(target = "dokumente", ignore = false),
        }
    )
    AbfragevarianteBaugenehmigungsverfahrenModel request2Model(
        final AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungStartBearbeitungModel request,
        @MappingTarget final AbfragevarianteBaugenehmigungsverfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "sobonOrientierungswertJahrPlanungsursaechlich", ignore = false),
            @Mapping(target = "sobonBerechnung", ignore = false),
            @Mapping(target = "stammdatenGueltigAb", ignore = false),
            @Mapping(target = "anmerkung", ignore = false),
            @Mapping(target = "hasBauratendateiInput", ignore = false),
            @Mapping(target = "anmerkungBauratendateiInput", ignore = false),
            @Mapping(target = "bauratendateiInputBasis", ignore = false),
            @Mapping(target = "bauratendateiInput", ignore = false),
            @Mapping(target = "dokumente", ignore = false),
        }
    )
    AbfragevarianteWeiteresVerfahrenModel request2Model(
        final AbfragevarianteWeiteresVerfahrenSachbearbeitungStartBearbeitungModel request,
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
            @Mapping(target = "anmerkungFachreferate", ignore = true),
            @Mapping(target = "anmerkungAbfrageersteller", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = true),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenStartBearbeitungModel request,
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
            @Mapping(target = "anmerkungFachreferate", ignore = true),
            @Mapping(target = "anmerkungAbfrageersteller", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = true),
        }
    )
    AbfragevarianteBaugenehmigungsverfahrenModel request2Model(
        final AbfragevarianteBaugenehmigungsverfahrenStartBearbeitungModel request,
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
            @Mapping(target = "anmerkungFachreferate", ignore = true),
            @Mapping(target = "anmerkungAbfrageersteller", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = true),
        }
    )
    AbfragevarianteWeiteresVerfahrenModel request2Model(
        final AbfragevarianteWeiteresVerfahrenStartBearbeitungModel request,
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
            @Mapping(target = "anmerkungFachreferate", ignore = false),
            @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = false),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenEinpflegenBedarfsmeldungModel request,
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
            @Mapping(target = "anmerkungFachreferate", ignore = false),
            @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = false),
        }
    )
    AbfragevarianteBaugenehmigungsverfahrenModel request2Model(
        final AbfragevarianteBaugenehmigungsverfahrenEinpflegenBedarfsmeldungModel request,
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
            @Mapping(target = "anmerkungFachreferate", ignore = false),
            @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = false),
        }
    )
    AbfragevarianteWeiteresVerfahrenModel request2Model(
        final AbfragevarianteWeiteresVerfahrenEinpflegenBedarfsmeldungModel request,
        @MappingTarget final AbfragevarianteWeiteresVerfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = false),
            @Mapping(target = "anmerkungAbfrageersteller", ignore = false),
            @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = false),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel request2Model(
        final AbfragevarianteBauleitplanverfahrenEinplanungBedarfeModel request,
        @MappingTarget final AbfragevarianteBauleitplanverfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = false),
            @Mapping(target = "anmerkungAbfrageersteller", ignore = false),
            @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = false),
        }
    )
    AbfragevarianteBaugenehmigungsverfahrenModel request2Model(
        final AbfragevarianteBaugenehmigungsverfahrenEinplanungBedarfeModel request,
        @MappingTarget final AbfragevarianteBaugenehmigungsverfahrenModel model
    );

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "version", ignore = false),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = false),
            @Mapping(target = "anmerkungAbfrageersteller", ignore = false),
            @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = false),
        }
    )
    AbfragevarianteWeiteresVerfahrenModel request2Model(
        final AbfragevarianteWeiteresVerfahrenEinplanungBedarfeModel request,
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

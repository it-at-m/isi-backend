/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper.converter;

import de.muenchen.isi.domain.mapper.BauabschnittDomainMapper;
import de.muenchen.isi.domain.model.AbfragevarianteBaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import org.mapstruct.*;

@Mapper(config = AbfrageConverterMapperConfig.class, uses = { BauabschnittDomainMapper.class })
public interface AbfragevarianteConverterDomainMapper {
    /**
     * Die Methode führt die Konvertierung einer {@link AbfragevarianteWeiteresVerfahrenModel} Abfrage in eine {@link AbfragevarianteBauleitplanverfahrenModel} Abfragevariante durch
     * @param abfragevarianteWeiteresVerfahrenModel {link AbfragevarianteWeiteresVerfahrenModel}
     * @return {link AbfragevarianteBauleitplanverfahrenModel}
     */
    @Mapping(target = "planart", ignore = true)
    @Mapping(target = "planartFreieEingabe", ignore = true)
    @Mapping(target = "sobonBerechnung", ignore = true)
    @Mapping(target = "stammdatenGueltigAb", ignore = true)
    @Mapping(target = "hasBauratendateiInput", ignore = true)
    @Mapping(target = "anmerkungBauratendateiInput", ignore = true)
    @Mapping(target = "bauratendateiInputBasis", ignore = true)
    @Mapping(target = "bauratendateiInput", ignore = true)
    @Mapping(target = "bedarfsmeldungFachreferate", ignore = true)
    @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = true)
    @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true)
    @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = true)
    @Mapping(target = "anmerkungFachreferate", ignore = true)
    @Mapping(target = "anmerkungAbfrageersteller", ignore = true)
    @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true)
    @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true)
    @InheritConfiguration(name = "ignoreCommonFields") // MapStruct generiert keinen Code für die AbfrageDto Attribute in diesem Mapper
    AbfragevarianteBauleitplanverfahrenModel convertAbfragevarianteWeiteresVerfahrenModel2AbfragevarianteBauleitplanverfahrenModel(
        final AbfragevarianteWeiteresVerfahrenModel abfragevarianteWeiteresVerfahrenModel
    );

    /**
     * Die Methode führt weiterführende Mapping Aktionen nach dem Standard-Mapping von convertAbfragevarianteWeiteresVerfahrenModel2AbfragevarianteBauleitplanverfahrenModel durch
     *
     * @param abfragevarianteWeiteresVerfahrenModel {@link AbfragevarianteWeiteresVerfahrenModel}.
     * @param abfragevarianteBauleitplanverfahrenModel {@link AbfragevarianteBauleitplanverfahrenModel}.
     */
    @AfterMapping
    default void afterMapping(
        final AbfragevarianteWeiteresVerfahrenModel abfragevarianteWeiteresVerfahrenModel,
        @MappingTarget final AbfragevarianteBauleitplanverfahrenModel abfragevarianteBauleitplanverfahrenModel
    ) {
        abfragevarianteBauleitplanverfahrenModel.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
    }

    /**
     * Die Methode führt die Konvertierung einer {@link AbfragevarianteWeiteresVerfahrenModel} Abfrage in eine {@link AbfragevarianteBaugenehmigungsverfahrenModel} Abfragevariante durch
     * @param abfragevarianteWeiteresVerfahrenModel {link AbfragevarianteWeiteresVerfahrenModel}
     * @return {link AbfragevarianteBaugenehmigungsverfahrenModel}
     */
    @Mapping(target = "wesentlicheRechtsgrundlage", ignore = true)
    @Mapping(target = "wesentlicheRechtsgrundlageFreieEingabe", ignore = true)
    @Mapping(target = "wesentlicheRechtsgrundlageAngabenZurBefreiung", ignore = true)
    @Mapping(target = "stammdatenGueltigAb", ignore = true)
    @Mapping(target = "hasBauratendateiInput", ignore = true)
    @Mapping(target = "anmerkungBauratendateiInput", ignore = true)
    @Mapping(target = "bauratendateiInputBasis", ignore = true)
    @Mapping(target = "bauratendateiInput", ignore = true)
    @Mapping(target = "bedarfsmeldungFachreferate", ignore = true)
    @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = true)
    @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true)
    @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = true)
    @Mapping(target = "anmerkungFachreferate", ignore = true)
    @Mapping(target = "anmerkungAbfrageersteller", ignore = true)
    @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true)
    @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true)
    @InheritConfiguration(name = "ignoreCommonFields") // MapStruct generiert keinen Code für die AbfrageDto Attribute in diesem Mapper
    AbfragevarianteBaugenehmigungsverfahrenModel convertAbfragevarianteWeiteresVerfahrenModel2AbfragevarianteBaugenehmigungsverfahrenModel(
        final AbfragevarianteWeiteresVerfahrenModel abfragevarianteWeiteresVerfahrenModel
    );

    /**
     * Die Methode führt weiterführende Mapping Aktionen nach dem Standard-Mapping von convertAbfragevarianteWeiteresVerfahrenModel2AbfragevarianteBaugenehmigungsverfahrenModel durch
     *
     * @param abfragevarianteWeiteresVerfahrenModel {@link AbfragevarianteWeiteresVerfahrenModel}.
     * @param abfragevarianteBaugenehmigungsverfahrenModel {@link AbfragevarianteBaugenehmigungsverfahrenModel}.
     */
    @AfterMapping
    default void afterMapping(
        final AbfragevarianteWeiteresVerfahrenModel abfragevarianteWeiteresVerfahrenModel,
        @MappingTarget final AbfragevarianteBaugenehmigungsverfahrenModel abfragevarianteBaugenehmigungsverfahrenModel
    ) {
        abfragevarianteBaugenehmigungsverfahrenModel.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
    }

    /**
     * Die Methode führt die Konvertierung einer {@link AbfragevarianteBauleitplanverfahrenModel} Abfrage in eine {@link AbfragevarianteBaugenehmigungsverfahrenModel} Abfragevariante durch
     * @param abfragevarianteBauleitplanverfahrenModel {link AbfragevarianteBauleitplanverfahrenModel}
     * @return {link AbfragevarianteBaugenehmigungsverfahrenModel}
     */
    @Mapping(target = "wesentlicheRechtsgrundlage", ignore = true)
    @Mapping(target = "wesentlicheRechtsgrundlageFreieEingabe", ignore = true)
    @Mapping(target = "wesentlicheRechtsgrundlageAngabenZurBefreiung", ignore = true)
    @Mapping(target = "gfWohnenBaurechtlichGenehmigt", ignore = true)
    @Mapping(target = "gfWohnenBaurechtlichFestgesetzt", ignore = true)
    @Mapping(target = "weBaurechtlichGenehmigt", ignore = true)
    @Mapping(target = "weBaurechtlichFestgesetzt", ignore = true)
    @Mapping(target = "stammdatenGueltigAb", ignore = true)
    @Mapping(target = "hasBauratendateiInput", ignore = true)
    @Mapping(target = "anmerkungBauratendateiInput", ignore = true)
    @Mapping(target = "bauratendateiInputBasis", ignore = true)
    @Mapping(target = "bauratendateiInput", ignore = true)
    @Mapping(target = "bedarfsmeldungFachreferate", ignore = true)
    @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = true)
    @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true)
    @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = true)
    @Mapping(target = "anmerkungFachreferate", ignore = true)
    @Mapping(target = "anmerkungAbfrageersteller", ignore = true)
    @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true)
    @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true)
    @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true)
    @InheritConfiguration(name = "ignoreCommonFields") // MapStruct generiert keinen Code für die AbfrageDto Attribute in diesem Mapper
    AbfragevarianteBaugenehmigungsverfahrenModel convertAbfragevarianteBauleitplanverfahrenModel2AbfragevarianteBaugenehmigungsverfahrenModel(
        final AbfragevarianteBauleitplanverfahrenModel abfragevarianteBauleitplanverfahrenModel
    );

    /**
     * Die Methode führt weiterführende Mapping Aktionen nach dem Standard-Mapping von convertAbfragevarianteBauleitplanverfahrenModel2AbfragevarianteBaugenehmigungsverfahrenModel durch
     *
     * @param abfragevarianteBauleitplanverfahrenModel {@link AbfragevarianteBauleitplanverfahrenModel}.
     * @param abfragevarianteBaugenehmigungsverfahrenModel {@link AbfragevarianteBaugenehmigungsverfahrenModel}.
     */
    @AfterMapping
    default void afterMapping(
        final AbfragevarianteBauleitplanverfahrenModel abfragevarianteBauleitplanverfahrenModel,
        @MappingTarget final AbfragevarianteBaugenehmigungsverfahrenModel abfragevarianteBaugenehmigungsverfahrenModel
    ) {
        abfragevarianteBaugenehmigungsverfahrenModel.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
    }
}

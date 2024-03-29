/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.common.BearbeitendePersonDto;
import de.muenchen.isi.api.dto.common.VerortungPointDto;
import de.muenchen.isi.api.validation.EinrichtungstraegerValid;
import de.muenchen.isi.api.validation.FertigstellungsjahrValid;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FertigstellungsjahrValid
@EinrichtungstraegerValid
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "infrastruktureinrichtungTyp",
    visible = true
)
@JsonSubTypes(
    {
        @JsonSubTypes.Type(value = GrundschuleDto.class, name = InfrastruktureinrichtungTyp.Values.GRUNDSCHULE),
        @JsonSubTypes.Type(
            value = GsNachmittagBetreuungDto.class,
            name = InfrastruktureinrichtungTyp.Values.GS_NACHMITTAG_BETREUUNG
        ),
        @JsonSubTypes.Type(value = HausFuerKinderDto.class, name = InfrastruktureinrichtungTyp.Values.HAUS_FUER_KINDER),
        @JsonSubTypes.Type(value = KindergartenDto.class, name = InfrastruktureinrichtungTyp.Values.KINDERGARTEN),
        @JsonSubTypes.Type(value = KinderkrippeDto.class, name = InfrastruktureinrichtungTyp.Values.KINDERKRIPPE),
        @JsonSubTypes.Type(value = MittelschuleDto.class, name = InfrastruktureinrichtungTyp.Values.MITTELSCHULE),
    }
)
@Schema(
    description = "InfrastruktureinrichtungDto",
    discriminatorProperty = "infrastruktureinrichtungTyp",
    discriminatorMapping = {
        @DiscriminatorMapping(value = InfrastruktureinrichtungTyp.Values.GRUNDSCHULE, schema = GrundschuleDto.class),
        @DiscriminatorMapping(
            value = InfrastruktureinrichtungTyp.Values.GS_NACHMITTAG_BETREUUNG,
            schema = GsNachmittagBetreuungDto.class
        ),
        @DiscriminatorMapping(
            value = InfrastruktureinrichtungTyp.Values.HAUS_FUER_KINDER,
            schema = HausFuerKinderDto.class
        ),
        @DiscriminatorMapping(value = InfrastruktureinrichtungTyp.Values.KINDERGARTEN, schema = KindergartenDto.class),
        @DiscriminatorMapping(value = InfrastruktureinrichtungTyp.Values.KINDERKRIPPE, schema = KinderkrippeDto.class),
        @DiscriminatorMapping(value = InfrastruktureinrichtungTyp.Values.MITTELSCHULE, schema = MittelschuleDto.class),
    }
)
public abstract class InfrastruktureinrichtungDto extends BaseEntityDto {

    private BearbeitendePersonDto bearbeitendePerson;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    private Long lfdNr;

    private UUID bauvorhaben;

    @Valid
    private AdresseDto adresse;

    @Valid
    private VerortungPointDto verortung;

    @NotBlank
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String nameEinrichtung;

    @Min(1900)
    @Max(2100)
    private Integer fertigstellungsjahr; // JJJJ

    @NotNull
    @NotUnspecified
    private StatusInfrastruktureinrichtung status;

    private BigDecimal flaecheGesamtgrundstueck;

    private BigDecimal flaecheTeilgrundstueck;
}

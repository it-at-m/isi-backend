/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.validation.EinrichtungstraegerValid;
import de.muenchen.isi.api.validation.FertigstellungsjahrValid;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import java.math.BigDecimal;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@FertigstellungsjahrValid
@EinrichtungstraegerValid
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CUSTOM,
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
public abstract class InfrastruktureinrichtungDto extends BaseEntityDto {

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    private Long lfdNr;

    private UUID bauvorhaben;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String allgemeineOrtsangabe;

    @Valid
    private AdresseDto adresse;

    @NotBlank
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String nameEinrichtung;

    @Min(1900)
    @Max(2100)
    private Integer fertigstellungsjahr; // JJJJ

    @NotNull
    @NotUnspecified
    private StatusInfrastruktureinrichtung status;

    private Einrichtungstraeger einrichtungstraeger;

    private BigDecimal flaecheGesamtgrundstueck;

    private BigDecimal flaecheTeilgrundstueck;

    private UUID zugeordnetesBaugebiet;
}

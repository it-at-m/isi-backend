package de.muenchen.isi.api.dto.calculation;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class LangfristigerPlanungsursaechlicherBedarfDto {

    @NotNull
    private List<WohneinheitenProFoerderartProJahrDto> wohneinheiten;

    @NotNull
    private List<WohneinheitenProFoerderartProJahrDto> wohneinheitenSumme10Jahre;

    @NotNull
    private List<WohneinheitenProFoerderartProJahrDto> wohneinheitenSumme15Jahre;

    @NotNull
    private List<WohneinheitenProFoerderartProJahrDto> wohneinheitenSumme20Jahre;

    @NotNull
    private List<WohneinheitenProFoerderartProJahrDto> wohneinheitenGesamt;

    @NotNull
    private List<InfrastrukturbedarfProJahrDto> bedarfKinderkrippe;

    @NotNull
    private InfrastrukturbedarfProJahrDto bedarfKinderkrippeMittelwert10;

    @NotNull
    private InfrastrukturbedarfProJahrDto bedarfKinderkrippeMittelwert15;

    @NotNull
    private InfrastrukturbedarfProJahrDto bedarfKinderkrippeMittelwert20;

    @NotNull
    private List<InfrastrukturbedarfProJahrDto> bedarfKindergarten;

    @NotNull
    private InfrastrukturbedarfProJahrDto bedarfKindergartenMittelwert10;

    @NotNull
    private InfrastrukturbedarfProJahrDto bedarfKindergartenMittelwert15;

    @NotNull
    private InfrastrukturbedarfProJahrDto bedarfKindergartenMittelwert20;

    @NotNull
    private List<PersonenProJahrDto> alleEinwohner;

    @NotNull
    private PersonenProJahrDto alleEinwohnerMittelwert10;

    @NotNull
    private PersonenProJahrDto alleEinwohnerMittelwert15;

    @NotNull
    private PersonenProJahrDto alleEinwohnerMittelwert20;
}

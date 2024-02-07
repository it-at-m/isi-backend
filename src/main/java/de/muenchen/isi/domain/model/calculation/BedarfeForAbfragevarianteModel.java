package de.muenchen.isi.domain.model.calculation;

import lombok.Data;

@Data
public class BedarfeForAbfragevarianteModel {

    private LangfristigerBedarfModel langfristigerPlanungsursaechlicherBedarf;

    private LangfristigerBedarfModel langfristigerSobonursaechlicherBedarf;
}

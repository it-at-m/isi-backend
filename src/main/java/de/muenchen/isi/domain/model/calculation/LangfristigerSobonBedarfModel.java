package de.muenchen.isi.domain.model.calculation;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LangfristigerSobonBedarfModel extends LangfristigerBedarfModel {

    private List<InfrastrukturbedarfProJahrModel> bedarfGsNachmittagBetreuung;

    private List<InfrastrukturbedarfProJahrModel> bedarfGrundschule;
}

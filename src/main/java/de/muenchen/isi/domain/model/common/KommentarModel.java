package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.domain.model.common.BearbeitendePersonModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class KommentarModel extends BaseEntityModel {

    private LocalDate erstellungsdatum;

    private String text;

    private List<DokumentModel> dokumente;

    private BearbeitendePersonModel bearbeitendePerson;
}

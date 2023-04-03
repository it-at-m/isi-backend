package de.muenchen.isi.domain.model;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauabschnittModel extends BaseEntityModel {

    private String bezeichnung;

    private List<BaugebietModel> baugebiete;
}

package de.muenchen.isi.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauabschnittModel extends BaseEntityModel {

    private String bezeichnung;

    private List<BaugebietModel> baugebiete;

}

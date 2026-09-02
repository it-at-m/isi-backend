package de.muenchen.isi.domain.model.search.filter;

import de.muenchen.isi.domain.model.BaseEntityModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonalFilterResponseModel extends BaseEntityModel {

    private String personalID;

    private String filterName;

    private FilterSettingsModel filterSettings;
}

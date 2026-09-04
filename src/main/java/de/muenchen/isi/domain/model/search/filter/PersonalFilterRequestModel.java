package de.muenchen.isi.domain.model.search.filter;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class PersonalFilterRequestModel {

    private UUID id;

    private String personalID;

    private String filterName;

    private FilterSettingsModel filterSettings;
}

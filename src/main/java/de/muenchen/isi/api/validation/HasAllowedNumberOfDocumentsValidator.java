package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.domain.service.stammdaten.FileInformationStammService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Component
public class HasAllowedNumberOfDocumentsValidator implements ConstraintValidator<HasAllowedNumberOfDocuments, List<DokumentDto>> {

    private final Long maxNumberOfFiles;

    public HasAllowedNumberOfDocumentsValidator(final FileInformationStammService fileInformationStammService) {
        this.maxNumberOfFiles = fileInformationStammService
                .getFileInformation()
                .getMaxNumberOfFiles();
    }

    /**
     * Prüft ob die Anzahl der {@link DokumentDto}s die erlaubte Anzahl nicht überschreitet.
     *
     * @param value   {@link List<DokumentDto>} zum Validieren.
     * @param context in welchem die Validierung statt findet.
     * @return true falls die erlaubte Anzahl an {@link DokumentDto}s nicht überschritten ist, andernfalls false.
     */
    @Override
    public boolean isValid(final List<DokumentDto> value, final ConstraintValidatorContext context) {
        return CollectionUtils.isEmpty(value) || value.size() <= this.maxNumberOfFiles;
    }

}

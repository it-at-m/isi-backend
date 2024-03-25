package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.bauratendatei.WithBauratendateiInputDto;
import de.muenchen.isi.api.mapper.BauratendateiApiMapper;
import de.muenchen.isi.domain.service.BauratendateiInputService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BauratendateiInputsValidator
    implements ConstraintValidator<HasFoerdermixRequiredSum, WithBauratendateiInputDto> {

    private final BauratendateiInputService bauratendateiInputService;

    private final BauratendateiApiMapper bauratendateiApiMapper;

    @Override
    public boolean isValid(
        final WithBauratendateiInputDto value,
        final ConstraintValidatorContext constraintValidatorContext
    ) {
        if (BooleanUtils.isNotTrue(value.getHasBauratendateiInputs())) {
            return true;
        }

        return bauratendateiInputService.equals(
            bauratendateiApiMapper.dto2Model(value.getBauratendateiInputBasis()),
            bauratendateiApiMapper.dto2Model(value.getBauratendateiInputs())
        );
    }
}

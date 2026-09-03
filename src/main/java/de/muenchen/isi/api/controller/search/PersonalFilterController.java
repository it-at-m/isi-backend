package de.muenchen.isi.api.controller.search;

import de.muenchen.isi.api.dto.search.filter.PersonalFilterRequestDto;
import de.muenchen.isi.api.dto.search.filter.PersonalFilterResponseDto;
import de.muenchen.isi.api.mapper.PersonalFilterApiMapper;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.service.search.PersonalFilterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/personal-filter")
@Tag(name = "PersonalFilter", description = "API zum interagieren mit persönlichen Filtern")
@Validated
public class PersonalFilterController {

    private final PersonalFilterService personalFilterService;

    private final PersonalFilterApiMapper personalFilterApiMapper;

    @GetMapping
    public List<PersonalFilterResponseDto> getPersonalFilters() {
        var modles = personalFilterService.getPersonalFilters();
        return personalFilterApiMapper.models2Dtos(modles);
    }

    @GetMapping("/{filterId}")
    public PersonalFilterResponseDto getByFilterID(@PathVariable @NotNull UUID filterId)
        throws EntityNotFoundException, IllegalAccessException {
        var responseModel = personalFilterService.getByFilterID(filterId);
        return personalFilterApiMapper.model2Dto(responseModel);
    }

    @Transactional(rollbackFor = OptimisticLockingException.class)
    @PatchMapping("/edit")
    public PersonalFilterResponseDto editFilter(
        @RequestBody @Valid @NotNull PersonalFilterRequestDto personalFilterRequestDto
    ) throws OptimisticLockingException, EntityNotFoundException, IllegalAccessException {
        var requestModel = personalFilterApiMapper.dto2Model(personalFilterRequestDto);
        var responseModel = personalFilterService.update(requestModel);
        return personalFilterApiMapper.model2Dto(responseModel);
    }

    @Transactional(rollbackFor = OptimisticLockingException.class)
    @PostMapping("/create")
    public PersonalFilterResponseDto createFilter(
        @RequestBody @Valid @NotNull PersonalFilterRequestDto personalFilterRequestDto
    ) throws OptimisticLockingException {
        var requestModel = personalFilterApiMapper.dto2Model(personalFilterRequestDto);
        var responseModel = personalFilterService.save(requestModel);
        return personalFilterApiMapper.model2Dto(responseModel);
    }

    @PostMapping("/delete/{filterId}")
    public void deleteFilter(@PathVariable UUID filterId) throws EntityNotFoundException, IllegalAccessException {
        personalFilterService.delete(filterId);
    }
}

package de.muenchen.isi.api.controller.search;

import de.muenchen.isi.api.dto.search.filter.PersonalFilterRequestDto;
import de.muenchen.isi.api.dto.search.filter.PersonalFilterResponseDto;
import de.muenchen.isi.api.mapper.PersonalFilterApiMapper;
import de.muenchen.isi.domain.service.search.PersonalFilterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @GetMapping("/filterid")
    public PersonalFilterResponseDto getByFilterID(@RequestBody PersonalFilterRequestDto personalFilterRequestDto) {
        var requestModel = personalFilterApiMapper.dto2Model(personalFilterRequestDto);
        var responseModel = personalFilterService.getByFilterID(requestModel);
        return personalFilterApiMapper.model2Dto(responseModel);
    }

    @PatchMapping("/edit")
    public PersonalFilterResponseDto editFilter(@RequestBody PersonalFilterRequestDto personalFilterRequestDto) {
        var requestModel = personalFilterApiMapper.dto2Model(personalFilterRequestDto);
        var responseModel = personalFilterService.update(requestModel);
        return personalFilterApiMapper.model2Dto(responseModel);
    }

    @PostMapping("/create")
    public PersonalFilterResponseDto createFilter(@RequestBody PersonalFilterRequestDto personalFilterRequestDto) {
        var requestModel = personalFilterApiMapper.dto2Model(personalFilterRequestDto);
        var responseModel = personalFilterService.save(requestModel);
        return personalFilterApiMapper.model2Dto(responseModel);
    }

    @PostMapping("/delete")
    public void deleteFilter(@RequestBody PersonalFilterRequestDto personalFilterRequestDto) {
        var requestModel = personalFilterApiMapper.dto2Model(personalFilterRequestDto);
        personalFilterService.delete(requestModel);
    }
}

package de.muenchen.isi.domain.service.filehandling;

import de.muenchen.isi.domain.mapper.DokumentDomainMapper;
import de.muenchen.isi.domain.model.filehandling.DokumenteModel;
import de.muenchen.isi.infrastructure.repository.filehandling.DokumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DokumentService {

    private final DokumentRepository dokumentRepository;

    private final DokumentDomainMapper dokumentDomainMapper;

    public DokumenteModel getDokumente(final Integer pageNumber, final Integer pageSize) {
        final var pageable = PageRequest.of(pageNumber, pageSize);
        final var foundPage = this.dokumentRepository.findAll(pageable);
        final var dokumentEntities = foundPage.getContent();
        final var dokumentModels = this.dokumentDomainMapper.entity2Model(dokumentEntities);
        final var dokumenteModel = new DokumenteModel();
        dokumenteModel.setDokumente(dokumentModels);
        dokumenteModel.setPageNumber(foundPage.getNumber());
        dokumenteModel.setPageSize(foundPage.getSize());
        dokumenteModel.setTotalElements(foundPage.getTotalElements());
        dokumenteModel.setTotalPages(foundPage.getTotalPages());
        dokumenteModel.setLast(foundPage.isLast());
        return dokumenteModel;
    }

}

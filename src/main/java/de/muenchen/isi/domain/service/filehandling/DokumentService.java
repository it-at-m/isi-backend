package de.muenchen.isi.domain.service.filehandling;

import de.muenchen.isi.domain.mapper.DokumentDomainMapper;
import de.muenchen.isi.domain.model.filehandling.DokumenteModel;
import de.muenchen.isi.infrastructure.repository.filehandling.DokumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DokumentService {

    private final DokumentRepository dokumentRepository;

    private final DokumentDomainMapper dokumentDomainMapper;

    public DokumenteModel getDokumente() {
        final var dokumente = this.dokumentDomainMapper.entity2Model(this.dokumentRepository.findAll());
        final var dokumenteModel = new DokumenteModel();
        dokumenteModel.setDokumente(dokumente);
        return dokumenteModel;
    }

}

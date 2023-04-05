package de.muenchen.isi.domain.service.filehandling;

import de.muenchen.isi.domain.mapper.DokumentDomainMapper;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.domain.model.filehandling.DokumenteModel;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.infrastructure.repository.filehandling.DokumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DokumentService {

    private final DokumentRepository dokumentRepository;

    private final DokumentDomainMapper dokumentDomainMapper;

    public DokumenteModel getDokumente(final Integer pageNumber, final Integer pageSize) {
        final var foundPage = this.dokumentRepository.findAll(PageRequest.of(pageNumber, pageSize));
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

    /**
     *
     * @param adaptedDokumentenListe
     * @param originalDokumentenListe
     * @return
     */
    public List<DokumentModel> getDokumenteInOriginalDokumentenListWhichAreMissingInAdaptedDokumentenListe(final List<DokumentModel> adaptedDokumentenListe,
                                                                                                           final List<DokumentModel> originalDokumentenListe) {
        // Entfernen aller nicht persistierten Dokumente
        final Map<FilepathModel, DokumentModel> alreadyPersistedAdaptedDokumentenMap = adaptedDokumentenListe
                .stream()
                .filter(dokumentModel -> ObjectUtils.isNotEmpty(dokumentModel.getId()))
                .collect(Collectors.toMap(DokumentModel::getFilePath, Function.identity()));

        final Map<FilepathModel, DokumentModel> originalDokumentenMap = originalDokumentenListe
                .stream()
                .filter(dokumentModel -> ObjectUtils.isNotEmpty(dokumentModel.getId()))
                .collect(Collectors.toMap(DokumentModel::getFilePath, Function.identity()));

        /**
         * Entfernen der in der adaptierten Map vorhandenen Dokumente aus der Map der original Dokumente.
         * In der Map der original Dokumente bleiben die in der adaptierten Map nicht vorhandenen Dokumente übrig.
         */
        originalDokumentenMap.keySet().removeAll(alreadyPersistedAdaptedDokumentenMap.keySet());
        return new ArrayList<>(originalDokumentenMap.values());
    }

}

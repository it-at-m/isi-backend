package de.muenchen.isi.domain.service.filehandling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.domain.model.filehandling.DokumenteModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import de.muenchen.isi.infrastructure.repository.filehandling.DokumentRepository;
import de.muenchen.isi.rest.TestData;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DokumentServiceTest {

    private DokumentService dokumentService;

    @Mock
    private DokumentRepository dokumentRepository;

    @BeforeEach
    public void beforeEach() {
        this.dokumentService = new DokumentService(this.dokumentRepository, new DokumentDomainMapperImpl());
        Mockito.reset(this.dokumentRepository);
    }

    @Test
    void getDokumente() {
        final List<Dokument> dokumente = new ArrayList<>();
        dokumente.add(TestData.createDokument("filepath1/file1.txt", ArtDokument.ANLAGE));
        dokumente.add(TestData.createDokument("filepath2/file2.txt", ArtDokument.ANTRAG));
        dokumente.add(TestData.createDokument("filepath3/file3.txt", ArtDokument.BERECHNUNG));

        final var page = new PageImpl<>(dokumente, PageRequest.of(4, 3), 15L);

        Mockito.when(this.dokumentRepository.findAll(PageRequest.of(4, 3))).thenReturn(page);

        final DokumenteModel result = this.dokumentService.getDokumente(4, 3);

        final DokumenteModel expected = new DokumenteModel();
        final List<DokumentModel> exptectedDokumentModels = new ArrayList<>();
        exptectedDokumentModels.add(TestData.createDokumentModel("filepath1/file1.txt", ArtDokument.ANLAGE));
        exptectedDokumentModels.add(TestData.createDokumentModel("filepath2/file2.txt", ArtDokument.ANTRAG));
        exptectedDokumentModels.add(TestData.createDokumentModel("filepath3/file3.txt", ArtDokument.BERECHNUNG));
        expected.setDokumente(exptectedDokumentModels);
        expected.setPageNumber(4);
        expected.setPageSize(3);
        expected.setTotalPages(5);
        expected.setTotalElements(15L);
        expected.setLast(true);

        assertThat(result, is(expected));
    }
}

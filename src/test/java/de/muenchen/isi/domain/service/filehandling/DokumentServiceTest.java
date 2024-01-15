package de.muenchen.isi.domain.service.filehandling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.TestData;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.domain.model.filehandling.DokumenteModel;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import de.muenchen.isi.infrastructure.repository.filehandling.DokumentRepository;
import de.muenchen.oss.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import de.muenchen.oss.digiwf.s3.integration.client.exception.DocumentStorageException;
import de.muenchen.oss.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import de.muenchen.oss.digiwf.s3.integration.client.exception.PropertyNotSetException;
import de.muenchen.oss.digiwf.s3.integration.client.repository.DocumentStorageFileRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    @Mock
    private DocumentStorageFileRepository documentStorageFileRepository;

    @BeforeEach
    public void beforeEach() {
        this.dokumentService =
            new DokumentService(
                this.dokumentRepository,
                new DokumentDomainMapperImpl(),
                documentStorageFileRepository,
                1
            );
        Mockito.reset(this.dokumentRepository, documentStorageFileRepository);
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

    @Test
    void deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe()
        throws FileHandlingFailedException, FileHandlingWithS3FailedException, DocumentStorageException, PropertyNotSetException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final var originalDokument1 = new DokumentModel();
        originalDokument1.setFilePath(new FilepathModel("test/file1.txt"));
        originalDokument1.setId(UUID.randomUUID());
        final var originalDokument2 = new DokumentModel();
        originalDokument2.setFilePath(new FilepathModel("test/file2.txt"));
        originalDokument2.setId(UUID.randomUUID());
        final var originalDokument3 = new DokumentModel();
        originalDokument3.setFilePath(new FilepathModel("test/file3.txt"));
        originalDokument3.setId(UUID.randomUUID());
        final var originalDokument4 = new DokumentModel();
        originalDokument4.setFilePath(new FilepathModel("test/file4.txt"));
        originalDokument4.setId(UUID.randomUUID());
        final List<DokumentModel> originalDokumentModels = List.of(
            originalDokument1,
            originalDokument2,
            originalDokument3,
            originalDokument4
        );

        final var adaptedDokument1 = new DokumentModel();
        adaptedDokument1.setFilePath(new FilepathModel("test/file1.txt"));
        adaptedDokument1.setId(originalDokument1.getId());
        final var adaptedDokument2 = new DokumentModel();
        adaptedDokument2.setFilePath(new FilepathModel("test/file3.txt"));
        adaptedDokument2.setId(originalDokument3.getId());
        final var adaptedDokument3 = new DokumentModel();
        adaptedDokument3.setFilePath(new FilepathModel("test/fileNew.txt"));
        adaptedDokument3.setId(null);
        final List<DokumentModel> adaptedDokumentModels = List.of(adaptedDokument1, adaptedDokument2, adaptedDokument3);

        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
            adaptedDokumentModels,
            originalDokumentModels
        );

        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .deleteFile(originalDokument2.getFilePath().getPathToFile(), 1);
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(1))
            .deleteFile(originalDokument4.getFilePath().getPathToFile(), 1);
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(2))
            .deleteFile(Mockito.anyString(), Mockito.anyInt());

        Mockito.reset(this.dokumentRepository, documentStorageFileRepository);

        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
            null,
            null
        );
        Mockito
            .verify(this.documentStorageFileRepository, Mockito.times(0))
            .deleteFile(Mockito.anyString(), Mockito.anyInt());
    }

    @Test
    void getDokumenteInOriginalDokumentenListWhichAreMissingInAdaptedDokumentenListe() {
        final var originalDokument1 = new DokumentModel();
        originalDokument1.setFilePath(new FilepathModel("test/file1.txt"));
        originalDokument1.setId(UUID.randomUUID());
        final var originalDokument2 = new DokumentModel();
        originalDokument2.setFilePath(new FilepathModel("test/file2.txt"));
        originalDokument2.setId(UUID.randomUUID());
        final var originalDokument3 = new DokumentModel();
        originalDokument3.setFilePath(new FilepathModel("test/file3.txt"));
        originalDokument3.setId(UUID.randomUUID());
        final var originalDokument4 = new DokumentModel();
        originalDokument4.setFilePath(new FilepathModel("test/file4.txt"));
        originalDokument4.setId(UUID.randomUUID());
        final List<DokumentModel> originalDokumentModels = List.of(
            originalDokument1,
            originalDokument2,
            originalDokument3,
            originalDokument4
        );

        final var adaptedDokument1 = new DokumentModel();
        adaptedDokument1.setFilePath(new FilepathModel("test/file1.txt"));
        adaptedDokument1.setId(originalDokument1.getId());
        final var adaptedDokument2 = new DokumentModel();
        adaptedDokument2.setFilePath(new FilepathModel("test/file3.txt"));
        adaptedDokument2.setId(originalDokument3.getId());
        final var adaptedDokument3 = new DokumentModel();
        adaptedDokument3.setFilePath(new FilepathModel("test/fileNew.txt"));
        adaptedDokument3.setId(null);
        final List<DokumentModel> adaptedDokumentModels = List.of(adaptedDokument1, adaptedDokument2, adaptedDokument3);

        List<DokumentModel> result =
            dokumentService.getDokumenteInOriginalDokumentenListWhichAreMissingInAdaptedDokumentenListe(
                adaptedDokumentModels,
                originalDokumentModels
            );

        List<DokumentModel> expected = List.of(originalDokument4, originalDokument2);

        assertThat(result, is(expected));

        result =
            dokumentService.getDokumenteInOriginalDokumentenListWhichAreMissingInAdaptedDokumentenListe(
                new ArrayList<>(),
                originalDokumentModels
            );

        expected = List.of(originalDokument4, originalDokument2, originalDokument1, originalDokument3);

        assertThat(result, is(expected));

        result =
            dokumentService.getDokumenteInOriginalDokumentenListWhichAreMissingInAdaptedDokumentenListe(
                adaptedDokumentModels,
                new ArrayList<>()
            );

        expected = List.of();

        assertThat(result, is(expected));

        result =
            dokumentService.getDokumenteInOriginalDokumentenListWhichAreMissingInAdaptedDokumentenListe(
                new ArrayList<>(),
                new ArrayList<>()
            );

        expected = List.of();

        assertThat(result, is(expected));
    }
}

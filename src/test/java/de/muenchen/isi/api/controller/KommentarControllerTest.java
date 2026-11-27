package de.muenchen.isi.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.api.advice.RestExceptionHandler;
import de.muenchen.isi.api.dto.common.KommentarBauvorhabenDto;
import de.muenchen.isi.api.dto.common.KommentarInfrastruktureinrichtungDto;
import de.muenchen.isi.api.mapper.KommentarApiMapper;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.InvalidTypeException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.model.common.KommentarBauvorhabenModel;
import de.muenchen.isi.domain.model.common.KommentarInfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.stammdaten.FileInformationModel;
import de.muenchen.isi.domain.service.common.KommentarService;
import de.muenchen.isi.domain.service.stammdaten.FileInformationStammService;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Nachbarschaftstreff;
import de.muenchen.isi.security.AuthenticationUtils;
import io.micrometer.tracing.Tracer;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = de.muenchen.isi.api.controller.common.KommentarController.class)
@Import(RestExceptionHandler.class)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
public class KommentarControllerTest {

    @org.springframework.boot.test.context.TestConfiguration
    static class TestConfig {

        @org.springframework.context.annotation.Bean
        public com.fasterxml.jackson.databind.ObjectMapper objectMapper() {
            final var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            // auto-detect and register available modules (JSR310, Jdk8, ParameterNames, ...)
            mapper.findAndRegisterModules();
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper;
        }
    }

    // RestExceptionHandler depends on Tracer
    @MockitoBean
    private Tracer tracer;

    // RequestResponseLoggingFilter depends on AuthenticationUtils
    @MockitoBean
    private AuthenticationUtils authenticationUtils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private KommentarService kommentarService;

    @MockitoBean
    private FileInformationStammService fileInformationStammService;

    @MockitoBean
    private KommentarApiMapper kommentarApiMapper;

    // Controller used for type checks
    @MockitoBean
    private de.muenchen.isi.api.controller.InfrastruktureinrichtungController infrastruktureinrichtungController;

    @BeforeEach
    void setup() {
        FileInformationModel mockFileInfo = new FileInformationModel();
        mockFileInfo.setMaxNumberOfFiles(5L);
        when(fileInformationStammService.getFileInformation()).thenReturn(mockFileInfo);
    }

    @Test
    void getKommentareForBauvorhaben_shouldReturnOk() throws Exception {
        final var id = UUID.randomUUID();
        final KommentarBauvorhabenModel model = mock(KommentarBauvorhabenModel.class);
        final KommentarBauvorhabenDto dto = new KommentarBauvorhabenDto();
        dto.setId(UUID.randomUUID());
        dto.setBauvorhaben(id);

        when(this.kommentarService.getKommentareForBauvorhaben(eq(id))).thenReturn(List.of(model));
        when(this.kommentarApiMapper.model2Dto(eq(model))).thenReturn(dto);

        this.mockMvc
            .perform(get("/kommentar/all/bauvorhaben/" + id))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id").value(dto.getId().toString()))
            .andExpect(jsonPath("$.[0].bauvorhaben").value(id.toString()));
    }

    @Test
    void getKommentareForInfrastruktureinrichtung_shouldReturnOk() throws Exception {
        final var id = UUID.randomUUID();
        final KommentarInfrastruktureinrichtungModel model = mock(KommentarInfrastruktureinrichtungModel.class);
        final KommentarInfrastruktureinrichtungDto dto = new KommentarInfrastruktureinrichtungDto();
        dto.setId(UUID.randomUUID());
        dto.setInfrastruktureinrichtung(id);

        when(this.kommentarService.getKommentareForInfrastruktureinrichtung(eq(id))).thenReturn(List.of(model));
        when(this.kommentarApiMapper.model2Dto(eq(model))).thenReturn(dto);

        this.mockMvc
            .perform(get("/kommentar/all/infrastruktureinrichtung/" + id))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id").value(dto.getId().toString()))
            .andExpect(jsonPath("$.[0].infrastruktureinrichtung").value(id.toString()));
    }

    @Test
    void createKommentarForBauvorhaben_shouldReturnCreated() throws Exception {
        final KommentarBauvorhabenDto request = new KommentarBauvorhabenDto();
        request.setBauvorhaben(UUID.randomUUID());

        final KommentarBauvorhabenModel model = mock(KommentarBauvorhabenModel.class);
        final KommentarBauvorhabenDto savedDto = new KommentarBauvorhabenDto();
        savedDto.setId(UUID.randomUUID());
        savedDto.setBauvorhaben(request.getBauvorhaben());

        when(this.kommentarApiMapper.dto2Model(any(KommentarBauvorhabenDto.class))).thenReturn(model);
        when(this.kommentarService.saveKommentarForBauvorhaben(eq(model))).thenReturn(model);
        when(this.kommentarApiMapper.model2Dto(eq(model))).thenReturn(savedDto);

        this.mockMvc
            .perform(
                post("/kommentar/bauvorhaben")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(savedDto.getId().toString()))
            .andExpect(jsonPath("$.bauvorhaben").value(request.getBauvorhaben().toString()));
    }

    @Test
    void createKommentarForBauvorhaben_whenOptimisticLocking_shouldReturnPreconditionFailed() throws Exception {
        final KommentarBauvorhabenDto request = new KommentarBauvorhabenDto();
        request.setBauvorhaben(UUID.randomUUID());

        final KommentarBauvorhabenModel model = mock(KommentarBauvorhabenModel.class);
        when(this.kommentarApiMapper.dto2Model(any(KommentarBauvorhabenDto.class))).thenReturn(model);
        when(this.kommentarService.saveKommentarForBauvorhaben(eq(model))).thenThrow(
            new OptimisticLockingException("optimistic")
        );

        this.mockMvc
            .perform(
                post("/kommentar/bauvorhaben")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isPreconditionFailed());
    }

    @Test
    void createKommentarForInfrastruktureinrichtung_shouldReturnCreated() throws Exception {
        final KommentarInfrastruktureinrichtungDto request = new KommentarInfrastruktureinrichtungDto();
        request.setInfrastruktureinrichtung(UUID.randomUUID());

        final KommentarInfrastruktureinrichtungModel model = mock(KommentarInfrastruktureinrichtungModel.class);
        final KommentarInfrastruktureinrichtungDto savedDto = new KommentarInfrastruktureinrichtungDto();
        savedDto.setId(UUID.randomUUID());
        savedDto.setInfrastruktureinrichtung(request.getInfrastruktureinrichtung());

        when(this.kommentarApiMapper.dto2Model(any(KommentarInfrastruktureinrichtungDto.class))).thenReturn(model);
        when(this.kommentarService.saveKommentarForInfrastruktureinrichtung(eq(model))).thenReturn(model);
        when(this.kommentarApiMapper.model2Dto(eq(model))).thenReturn(savedDto);

        this.mockMvc
            .perform(
                post("/kommentar/infrastruktureinrichtung")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(savedDto.getId().toString()))
            .andExpect(jsonPath("$.infrastruktureinrichtung").value(request.getInfrastruktureinrichtung().toString()));
    }

    @Test
    void createKommentarForInfrastruktureinrichtung_whenOptimisticLocking_shouldReturnPreconditionFailed()
        throws Exception {
        final KommentarInfrastruktureinrichtungDto request = new KommentarInfrastruktureinrichtungDto();
        request.setInfrastruktureinrichtung(UUID.randomUUID());

        final KommentarInfrastruktureinrichtungModel model = mock(KommentarInfrastruktureinrichtungModel.class);
        when(this.kommentarApiMapper.dto2Model(any(KommentarInfrastruktureinrichtungDto.class))).thenReturn(model);
        when(this.kommentarService.saveKommentarForInfrastruktureinrichtung(eq(model))).thenThrow(
            new OptimisticLockingException("optimistic")
        );

        this.mockMvc
            .perform(
                post("/kommentar/infrastruktureinrichtung")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isPreconditionFailed());
    }

    @Test
    void createKommentarForInfrastruktureinrichtungBedarfsmeldungKita_shouldReturnCreated() throws Exception {
        final KommentarInfrastruktureinrichtungDto request = new KommentarInfrastruktureinrichtungDto();
        request.setInfrastruktureinrichtung(UUID.randomUUID());

        final KommentarInfrastruktureinrichtungModel model = mock(KommentarInfrastruktureinrichtungModel.class);
        final KommentarInfrastruktureinrichtungDto savedDto = new KommentarInfrastruktureinrichtungDto();
        savedDto.setId(UUID.randomUUID());
        savedDto.setInfrastruktureinrichtung(request.getInfrastruktureinrichtung());

        // allow the check to pass
        doNothing().when(this.infrastruktureinrichtungController).CheckInfrastruktureinrichtungTyp(any(), any(), any());

        when(this.kommentarApiMapper.dto2Model(any(KommentarInfrastruktureinrichtungDto.class))).thenReturn(model);
        when(this.kommentarService.saveKommentarForInfrastruktureinrichtung(eq(model))).thenReturn(model);
        when(this.kommentarApiMapper.model2Dto(eq(model))).thenReturn(savedDto);

        this.mockMvc
            .perform(
                post("/kommentar/infrastruktureinrichtung/bedarfsmeldung_kita")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(savedDto.getId().toString()))
            .andExpect(jsonPath("$.infrastruktureinrichtung").value(request.getInfrastruktureinrichtung().toString()));
    }

    @Test
    void createKommentarForInfrastruktureinrichtungBedarfsmeldungKita_throwsForbidden() throws Exception {
        final KommentarInfrastruktureinrichtungDto request = new KommentarInfrastruktureinrichtungDto();
        request.setInfrastruktureinrichtung(UUID.randomUUID());

        // make the check throw InvalidTypeException
        doThrow(new InvalidTypeException("invalid type"))
            .when(this.infrastruktureinrichtungController)
            .CheckInfrastruktureinrichtungTyp(any(), any(), any());

        this.mockMvc
            .perform(
                post("/kommentar/infrastruktureinrichtung/bedarfsmeldung_kita")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isForbidden())
            .andExpect(result ->
                Assertions.assertTrue(
                    result.getResolvedException() instanceof InvalidTypeException,
                    () -> "Expected InvalidTypeException but was: " + result.getResolvedException()
                )
            );
    }

    @Test
    void updateKommentarForBauvorhaben_shouldReturnOk() throws Exception {
        final KommentarBauvorhabenDto request = new KommentarBauvorhabenDto();
        final UUID id = UUID.randomUUID();
        final UUID bauId = UUID.randomUUID();
        request.setId(id);
        request.setBauvorhaben(bauId);

        final KommentarBauvorhabenModel model = mock(KommentarBauvorhabenModel.class);
        final KommentarBauvorhabenDto savedDto = new KommentarBauvorhabenDto();
        savedDto.setId(id);
        savedDto.setBauvorhaben(bauId);

        final Kommentar komentarEntity = new Kommentar();
        final Bauvorhaben bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(bauId);
        komentarEntity.setBauvorhaben(bauvorhaben);

        when(this.kommentarService.getKommentarEntityById(eq(id))).thenReturn(komentarEntity);
        when(this.kommentarApiMapper.dto2Model(any(KommentarBauvorhabenDto.class))).thenReturn(model);
        when(this.kommentarService.updateKommentarForBauvorhaben(eq(model))).thenReturn(model);
        when(this.kommentarApiMapper.model2Dto(eq(model))).thenReturn(savedDto);

        this.mockMvc
            .perform(
                put("/kommentar/bauvorhaben")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(savedDto.getId().toString()))
            .andExpect(jsonPath("$.bauvorhaben").value(bauId.toString()));
    }

    @Test
    void updateKommentarForBauvorhaben_whenNotFound_shouldReturn404() throws Exception {
        final KommentarBauvorhabenDto request = new KommentarBauvorhabenDto();
        request.setId(UUID.randomUUID());

        when(this.kommentarService.getKommentarEntityById(eq(request.getId()))).thenThrow(
            new EntityNotFoundException("not found")
        );

        this.mockMvc
            .perform(
                put("/kommentar/bauvorhaben")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteKommentarForBauvorhabenById_shouldReturnNoContent() throws Exception {
        final var id = UUID.randomUUID();
        final Kommentar komentarEntity = new Kommentar();
        final Bauvorhaben bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(UUID.randomUUID());
        komentarEntity.setBauvorhaben(bauvorhaben);

        when(this.kommentarService.getKommentarEntityById(eq(id))).thenReturn(komentarEntity);
        doNothing().when(this.kommentarService).deleteKommentarById(eq(id));

        this.mockMvc.perform(delete("/kommentar/bauvorhaben/" + id)).andExpect(status().isNoContent());
    }

    @Test
    void deleteKommentarForBauvorhabenById_whenBadRequest_shouldReturn400() throws Exception {
        final var id = UUID.randomUUID();
        final Kommentar komentarEntity = new Kommentar();
        // no bauvorhaben set -> should return bad request
        komentarEntity.setInfrastruktureinrichtung(new Kinderkrippe());

        when(this.kommentarService.getKommentarEntityById(eq(id))).thenReturn(komentarEntity);

        this.mockMvc.perform(delete("/kommentar/bauvorhaben/" + id)).andExpect(status().isBadRequest());
    }

    @Test
    void deleteKommentarForInfrastruktureinrichtungBedarfsmeldungKita_throwsForbidden() throws Exception {
        final var id = UUID.randomUUID();
        final Kommentar komentarEntity = new Kommentar();
        final Infrastruktureinrichtung infra = new Nachbarschaftstreff();
        infra.setId(UUID.randomUUID());
        komentarEntity.setInfrastruktureinrichtung(infra);

        when(this.kommentarService.getKommentarEntityById(eq(id))).thenReturn(komentarEntity);
        doThrow(new InvalidTypeException("invalid type"))
            .when(this.infrastruktureinrichtungController)
            .CheckInfrastruktureinrichtungTyp(any(), any(), any());

        this.mockMvc
            .perform(delete("/kommentar/infrastruktureinrichtung/bedarfsmeldung_kita/" + id))
            .andExpect(status().isForbidden())
            .andExpect(result ->
                Assertions.assertTrue(
                    result.getResolvedException() instanceof InvalidTypeException,
                    () -> "Expected InvalidTypeException but was: " + result.getResolvedException()
                )
            );
    }

    @Test
    void deleteKommentarForInfrastruktureinrichtungBedarfsmeldungKita_shouldReturnNoContent() throws Exception {
        final var id = UUID.randomUUID();
        final Kommentar komentarEntity = new Kommentar();
        final Infrastruktureinrichtung infra = new Kinderkrippe();
        infra.setId(UUID.randomUUID());
        komentarEntity.setInfrastruktureinrichtung(infra);

        when(this.kommentarService.getKommentarEntityById(eq(id))).thenReturn(komentarEntity);
        // allow check to pass
        doNothing().when(this.infrastruktureinrichtungController).CheckInfrastruktureinrichtungTyp(any(), any(), any());
        doNothing().when(this.kommentarService).deleteKommentarById(eq(id));

        this.mockMvc
            .perform(delete("/kommentar/infrastruktureinrichtung/bedarfsmeldung_kita/" + id))
            .andExpect(status().isNoContent());
    }
}

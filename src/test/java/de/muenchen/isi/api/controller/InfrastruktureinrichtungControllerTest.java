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
import de.muenchen.isi.api.dto.infrastruktureinrichtung.InfrastruktureinrichtungDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.NachbarschaftstreffDto;
import de.muenchen.isi.api.mapper.InfrastruktureinrichtungApiMapper;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.service.BauvorhabenService;
import de.muenchen.isi.domain.service.InfrastruktureinrichtungService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.AnlassPlanung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.security.AuthenticationUtils;
import io.micrometer.tracing.Tracer;
import java.util.UUID;
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
@WebMvcTest(controllers = InfrastruktureinrichtungController.class)
@Import(RestExceptionHandler.class)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
class InfrastruktureinrichtungControllerTest {

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
    private InfrastruktureinrichtungService infrastruktureinrichtungService;

    @MockitoBean
    private BauvorhabenService bauvorhabenService;

    @MockitoBean
    private InfrastruktureinrichtungApiMapper infrastruktureinrichtungApiMapper;

    @Test
    void getInfrastruktureinrichtungById_shouldReturnOk() throws Exception {
        final var id = UUID.randomUUID();
        final InfrastruktureinrichtungModel model = mock(InfrastruktureinrichtungModel.class);
        final InfrastruktureinrichtungDto dto = new NachbarschaftstreffDto();
        dto.setId(id);
        dto.setNameEinrichtung("Test-Einrichtung");

        when(this.infrastruktureinrichtungService.getInfrastruktureinrichtungById(eq(id))).thenReturn(model);
        when(this.infrastruktureinrichtungApiMapper.model2Dto(eq(model))).thenReturn(dto);

        this.mockMvc
            .perform(get("/infrastruktureinrichtung/" + id))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.nameEinrichtung").value("Test-Einrichtung"));
    }

    @Test
    void getInfrastruktureinrichtungById_whenNotFound_shouldReturn404() throws Exception {
        final var id = UUID.randomUUID();
        when(this.infrastruktureinrichtungService.getInfrastruktureinrichtungById(eq(id))).thenThrow(
            new EntityNotFoundException("not found")
        );

        this.mockMvc.perform(get("/infrastruktureinrichtung/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void createInfrastruktureinrichtung_shouldReturnCreated() throws Exception {
        final NachbarschaftstreffDto request = new NachbarschaftstreffDto();
        request.setSobonRelevant(UncertainBoolean.FALSE);
        request.setInfrastruktureinrichtungTyp(
            de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp.NACHBARSCHAFTSTREFF
        );
        request.setNameEinrichtung("Neu");
        request.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        request.setAnlassPlanung(AnlassPlanung.NEUBAU_BAU_GB);

        final InfrastruktureinrichtungModel model = mock(InfrastruktureinrichtungModel.class);
        final InfrastruktureinrichtungDto savedDto = new NachbarschaftstreffDto();
        savedDto.setId(UUID.randomUUID());
        savedDto.setNameEinrichtung("Neu");

        when(this.infrastruktureinrichtungApiMapper.dto2Model(any(InfrastruktureinrichtungDto.class))).thenReturn(
            model
        );
        when(this.infrastruktureinrichtungService.saveInfrastruktureinrichtung(eq(model))).thenReturn(model);
        when(this.infrastruktureinrichtungApiMapper.model2Dto(eq(model))).thenReturn(savedDto);

        this.mockMvc
            .perform(
                post("/infrastruktureinrichtung")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(savedDto.getId().toString()))
            .andExpect(jsonPath("$.nameEinrichtung").value("Neu"));
    }

    @Test
    void createInfrastruktureinrichtung_whenOptimisticLocking_shouldReturnPreconditionFailed() throws Exception {
        final NachbarschaftstreffDto request = new NachbarschaftstreffDto();
        request.setSobonRelevant(UncertainBoolean.FALSE);
        request.setInfrastruktureinrichtungTyp(
            de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp.NACHBARSCHAFTSTREFF
        );
        request.setNameEinrichtung("Neu");
        request.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG);
        request.setAnlassPlanung(AnlassPlanung.NEUBAU_BEBAUUNGSPLAN);

        final InfrastruktureinrichtungModel model = mock(InfrastruktureinrichtungModel.class);
        when(this.infrastruktureinrichtungApiMapper.dto2Model(any(InfrastruktureinrichtungDto.class))).thenReturn(
            model
        );
        when(this.infrastruktureinrichtungService.saveInfrastruktureinrichtung(eq(model))).thenThrow(
            new OptimisticLockingException("optimistic")
        );

        this.mockMvc
            .perform(
                post("/infrastruktureinrichtung")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isPreconditionFailed());
    }

    @Test
    void updateInfrastruktureinrichtung_shouldReturnOk() throws Exception {
        final NachbarschaftstreffDto request = new NachbarschaftstreffDto();
        request.setSobonRelevant(UncertainBoolean.FALSE);
        request.setInfrastruktureinrichtungTyp(
            de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp.NACHBARSCHAFTSTREFF
        );
        request.setNameEinrichtung("Update");
        request.setStatus(StatusInfrastruktureinrichtung.PLANUNG_ZURUECKGEZOGEN);
        request.setAnlassPlanung(AnlassPlanung.SCHLIESSUNG);

        final InfrastruktureinrichtungModel model = mock(InfrastruktureinrichtungModel.class);
        final InfrastruktureinrichtungDto savedDto = new NachbarschaftstreffDto();
        savedDto.setId(UUID.randomUUID());
        savedDto.setNameEinrichtung("Update");

        when(this.infrastruktureinrichtungApiMapper.dto2Model(any(InfrastruktureinrichtungDto.class))).thenReturn(
            model
        );
        when(this.infrastruktureinrichtungService.updateInfrastruktureinrichtung(eq(model))).thenReturn(model);
        when(this.infrastruktureinrichtungApiMapper.model2Dto(eq(model))).thenReturn(savedDto);

        this.mockMvc
            .perform(
                put("/infrastruktureinrichtung")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(savedDto.getId().toString()))
            .andExpect(jsonPath("$.nameEinrichtung").value("Update"));
    }

    @Test
    void updateInfrastruktureinrichtung_whenNotFound_shouldReturn404() throws Exception {
        final NachbarschaftstreffDto request = new NachbarschaftstreffDto();
        request.setSobonRelevant(UncertainBoolean.FALSE);
        request.setInfrastruktureinrichtungTyp(
            de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp.NACHBARSCHAFTSTREFF
        );
        request.setNameEinrichtung("Update");
        request.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG);
        request.setAnlassPlanung(AnlassPlanung.NEUE_EINRICHTUNG_PRIVATER_TRAEGER);

        final InfrastruktureinrichtungModel model = mock(InfrastruktureinrichtungModel.class);
        when(this.infrastruktureinrichtungApiMapper.dto2Model(any(InfrastruktureinrichtungDto.class))).thenReturn(
            model
        );
        when(this.infrastruktureinrichtungService.updateInfrastruktureinrichtung(eq(model))).thenThrow(
            new EntityNotFoundException("not found")
        );

        this.mockMvc
            .perform(
                put("/infrastruktureinrichtung")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteInfrastruktureinrichtungById_shouldReturnNoContent() throws Exception {
        final var id = UUID.randomUUID();
        doNothing().when(this.infrastruktureinrichtungService).deleteInfrastruktureinrichtungById(eq(id));

        this.mockMvc.perform(delete("/infrastruktureinrichtung/" + id)).andExpect(status().isNoContent());
    }

    @Test
    void deleteInfrastruktureinrichtungById_whenReferenced_shouldReturnConflict() throws Exception {
        final var id = UUID.randomUUID();
        doThrow(new EntityIsReferencedException("referenced"))
            .when(this.infrastruktureinrichtungService)
            .deleteInfrastruktureinrichtungById(eq(id));

        this.mockMvc.perform(delete("/infrastruktureinrichtung/" + id)).andExpect(status().isConflict());
    }

    // ----- Role specific endpoints tests -----

    @Test
    void createInfrastruktureinrichtungBedarfsmeldungKita_shouldReturnCreated() throws Exception {
        final NachbarschaftstreffDto request = new NachbarschaftstreffDto();
        request.setSobonRelevant(UncertainBoolean.FALSE);
        request.setInfrastruktureinrichtungTyp(
            de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp.NACHBARSCHAFTSTREFF
        );
        request.setNameEinrichtung("KitaNeu");
        request.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        request.setAnlassPlanung(AnlassPlanung.UNSPECIFIED);

        final InfrastruktureinrichtungModel model = mock(InfrastruktureinrichtungModel.class);
        final InfrastruktureinrichtungDto savedDto = new NachbarschaftstreffDto();
        savedDto.setId(UUID.randomUUID());
        savedDto.setNameEinrichtung("KitaNeu");

        when(this.infrastruktureinrichtungApiMapper.dto2Model(any(InfrastruktureinrichtungDto.class))).thenReturn(
            model
        );
        when(this.infrastruktureinrichtungService.saveInfrastruktureinrichtung(eq(model))).thenReturn(model);
        when(this.infrastruktureinrichtungApiMapper.model2Dto(eq(model))).thenReturn(savedDto);

        this.mockMvc
            .perform(
                post("/infrastruktureinrichtung/bedarfsmeldung_kita")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(savedDto.getId().toString()))
            .andExpect(jsonPath("$.nameEinrichtung").value("KitaNeu"));
    }

    @Test
    void createInfrastruktureinrichtungBedarfsmeldungKita_whenReportingException_shouldReturnBadRequest()
        throws Exception {
        final NachbarschaftstreffDto request = new NachbarschaftstreffDto();
        request.setSobonRelevant(UncertainBoolean.FALSE);
        request.setInfrastruktureinrichtungTyp(
            de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp.NACHBARSCHAFTSTREFF
        );
        request.setNameEinrichtung("KitaNeu");
        request.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG);
        request.setAnlassPlanung(AnlassPlanung.BAUPROGRAMM);

        final InfrastruktureinrichtungModel model = mock(InfrastruktureinrichtungModel.class);
        when(this.infrastruktureinrichtungApiMapper.dto2Model(any(InfrastruktureinrichtungDto.class))).thenReturn(
            model
        );
        when(this.infrastruktureinrichtungService.saveInfrastruktureinrichtung(eq(model))).thenThrow(
            new ReportingException("reporting failed")
        );

        this.mockMvc
            .perform(
                post("/infrastruktureinrichtung/bedarfsmeldung_kita")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void createInfrastruktureinrichtungBedarfsmeldungSozialplanung_shouldReturnCreated() throws Exception {
        final NachbarschaftstreffDto request = new NachbarschaftstreffDto();
        request.setSobonRelevant(UncertainBoolean.FALSE);
        request.setInfrastruktureinrichtungTyp(
            de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp.NACHBARSCHAFTSTREFF
        );
        request.setNameEinrichtung("SozNeu");
        request.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        request.setAnlassPlanung(AnlassPlanung.ERWEITERUNG_BESTEHENDER_EINRICHTUNG);

        final InfrastruktureinrichtungModel model = mock(InfrastruktureinrichtungModel.class);
        final InfrastruktureinrichtungDto savedDto = new NachbarschaftstreffDto();
        savedDto.setId(UUID.randomUUID());
        savedDto.setNameEinrichtung("SozNeu");

        when(this.infrastruktureinrichtungApiMapper.dto2Model(any(InfrastruktureinrichtungDto.class))).thenReturn(
            model
        );
        when(this.infrastruktureinrichtungService.saveInfrastruktureinrichtung(eq(model))).thenReturn(model);
        when(this.infrastruktureinrichtungApiMapper.model2Dto(eq(model))).thenReturn(savedDto);

        this.mockMvc
            .perform(
                post("/infrastruktureinrichtung/bedarfsmeldung_sozialplanung")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(savedDto.getId().toString()))
            .andExpect(jsonPath("$.nameEinrichtung").value("SozNeu"));
    }

    @Test
    void createInfrastruktureinrichtungBedarfsmeldungSozialplanung_whenOptimisticLocking_shouldReturnPreconditionFailed()
        throws Exception {
        final NachbarschaftstreffDto request = new NachbarschaftstreffDto();
        request.setSobonRelevant(UncertainBoolean.FALSE);
        request.setInfrastruktureinrichtungTyp(
            de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp.NACHBARSCHAFTSTREFF
        );
        request.setNameEinrichtung("SozNeu");
        request.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG);
        request.setAnlassPlanung(AnlassPlanung.BAUPROGRAMM);

        final InfrastruktureinrichtungModel model = mock(InfrastruktureinrichtungModel.class);
        when(this.infrastruktureinrichtungApiMapper.dto2Model(any(InfrastruktureinrichtungDto.class))).thenReturn(
            model
        );
        when(this.infrastruktureinrichtungService.saveInfrastruktureinrichtung(eq(model))).thenThrow(
            new OptimisticLockingException("optimistic")
        );

        this.mockMvc
            .perform(
                post("/infrastruktureinrichtung/bedarfsmeldung_sozialplanung")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isPreconditionFailed());
    }

    @Test
    void updateInfrastruktureinrichtungBedarfsmeldungKita_shouldReturnOk() throws Exception {
        final NachbarschaftstreffDto request = new NachbarschaftstreffDto();
        request.setSobonRelevant(UncertainBoolean.FALSE);
        request.setInfrastruktureinrichtungTyp(
            de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp.NACHBARSCHAFTSTREFF
        );
        request.setNameEinrichtung("KitaUpdate");
        request.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        request.setAnlassPlanung(AnlassPlanung.REDUZIERUNG_PLAETZE_BEI_BESTANDSEINRICHTUNG);

        final InfrastruktureinrichtungModel model = mock(InfrastruktureinrichtungModel.class);
        final InfrastruktureinrichtungDto savedDto = new NachbarschaftstreffDto();
        savedDto.setId(UUID.randomUUID());
        savedDto.setNameEinrichtung("KitaUpdate");

        when(this.infrastruktureinrichtungApiMapper.dto2Model(any(InfrastruktureinrichtungDto.class))).thenReturn(
            model
        );
        when(this.infrastruktureinrichtungService.updateInfrastruktureinrichtung(eq(model))).thenReturn(model);
        when(this.infrastruktureinrichtungApiMapper.model2Dto(eq(model))).thenReturn(savedDto);

        this.mockMvc
            .perform(
                put("/infrastruktureinrichtung/bedarfsmeldung_kita")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(savedDto.getId().toString()))
            .andExpect(jsonPath("$.nameEinrichtung").value("KitaUpdate"));
    }

    @Test
    void updateInfrastruktureinrichtungBedarfsmeldungSozialplanung_whenNotFound_shouldReturn404() throws Exception {
        final NachbarschaftstreffDto request = new NachbarschaftstreffDto();
        request.setSobonRelevant(UncertainBoolean.FALSE);
        request.setInfrastruktureinrichtungTyp(
            de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp.NACHBARSCHAFTSTREFF
        );
        request.setNameEinrichtung("SozUpdate");
        request.setStatus(StatusInfrastruktureinrichtung.UNGESICHERTE_PLANUNG);
        request.setAnlassPlanung(AnlassPlanung.NEUBAU_BAU_GB);

        final InfrastruktureinrichtungModel model = mock(InfrastruktureinrichtungModel.class);
        when(this.infrastruktureinrichtungApiMapper.dto2Model(any(InfrastruktureinrichtungDto.class))).thenReturn(
            model
        );
        when(this.infrastruktureinrichtungService.updateInfrastruktureinrichtung(eq(model))).thenThrow(
            new EntityNotFoundException("not found")
        );

        this.mockMvc
            .perform(
                put("/infrastruktureinrichtung/bedarfsmeldung_sozialplanung")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteInfrastruktureinrichtungBedarfsmeldungKitaById_shouldReturnNoContent() throws Exception {
        final var id = UUID.randomUUID();
        doNothing().when(this.infrastruktureinrichtungService).deleteInfrastruktureinrichtungById(eq(id));

        this.mockMvc
            .perform(delete("/infrastruktureinrichtung/bedarfsmeldung_kita/" + id))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteInfrastruktureinrichtungBedarfsmeldungSozialplanungById_whenReferenced_shouldReturnConflict()
        throws Exception {
        final var id = UUID.randomUUID();
        doThrow(new EntityIsReferencedException("referenced"))
            .when(this.infrastruktureinrichtungService)
            .deleteInfrastruktureinrichtungById(eq(id));

        this.mockMvc
            .perform(delete("/infrastruktureinrichtung/bedarfsmeldung_sozialplanung/" + id))
            .andExpect(status().isConflict());
    }
}

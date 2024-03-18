package de.muenchen.isi.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.service.transition.MockCustomUser;
import de.muenchen.isi.infrastructure.entity.common.BearbeitendePerson;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = { IsiBackendApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
class AuthenticationUtilsTest {

    @Autowired
    private AuthenticationUtils authenticationUtils;

    @Test
    @MockCustomUser
    void getUserAuthorities() {
        final var result = authenticationUtils.getUserAuthorities();

        final var expected = List.of(
            AuthoritiesEnum.ISI_BACKEND_FREIGABE_ABFRAGE,
            AuthoritiesEnum.ISI_BACKEND_ABBRECHEN_ABFRAGE,
            AuthoritiesEnum.ISI_BACKEND_ZURUECK_AN_ABFRAGEERSTELLUNG_ABFRAGE,
            AuthoritiesEnum.ISI_BACKEND_IN_BEARBEITUNG_SETZTEN_ABFRAGE,
            AuthoritiesEnum.ISI_BACKEND_ZURUECK_AN_SACHBEARBEITUNG_ABFRAGE,
            AuthoritiesEnum.ISI_BACKEND_KEINE_BEARBEITUNG_NOETIG_ABFRAGE,
            AuthoritiesEnum.ISI_BACKEND_VERSCHICKEN_DER_STELLUNGNAHME_ABFRAGE,
            AuthoritiesEnum.ISI_BACKEND_BEDARFSMELDUNG_ERFOLGTE_ABFRAGE,
            AuthoritiesEnum.ISI_BACKEND_SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG_ABFRAGE,
            AuthoritiesEnum.ISI_BACKEND_ERNEUTE_BEARBEITUNG_ABFRAGE
        );

        assertThat(result, is(expected));
    }

    @Test
    void getUserAuthoritiesUnauthenticated() {
        final var result = authenticationUtils.getUserAuthorities();

        assertThat(result, is(List.of()));
    }

    @Test
    @MockCustomUser
    void getUsername() {
        final var result = authenticationUtils.getUsername();

        assertThat(result, is("rob"));
    }

    @Test
    void getUsernameUnauthenticated() {
        final var result = authenticationUtils.getUsername();

        assertThat(result, is("unauthenticated"));
    }

    @Test
    @MockCustomUser
    void getSurname() {
        final var result = authenticationUtils.getSurname();

        assertThat(result, is("Winch"));
    }

    @Test
    void getSurnameUnauthenticated() {
        final var result = authenticationUtils.getSurname();

        assertThat(result, is("unauthenticated"));
    }

    @Test
    @MockCustomUser
    void getGivenname() {
        final var result = authenticationUtils.getGivenname();

        assertThat(result, is("Rob"));
    }

    @Test
    void getGivennameUnauthenticated() {
        final var result = authenticationUtils.getGivenname();

        assertThat(result, is("unauthenticated"));
    }

    @Test
    @MockCustomUser
    void getEmail() {
        final var result = authenticationUtils.getEmail();

        assertThat(result, is("rob@example.com"));
    }

    @Test
    void getEmailUnauthenticated() {
        final var result = authenticationUtils.getEmail();

        assertThat(result, is("unauthenticated"));
    }

    @Test
    @MockCustomUser
    void getOrganisationseinheit() {
        final var result = authenticationUtils.getOrganisationseinheit();

        assertThat(result, is("IT"));
    }

    @Test
    void getOrganisationseinheitUnauthenticated() {
        final var result = authenticationUtils.getOrganisationseinheit();

        assertThat(result, is("unauthenticated"));
    }

    @Test
    @MockCustomUser
    void getUserSub() {
        final var result = authenticationUtils.getUserSub();

        assertThat(result, is("12345678-1234-1234-1234-1234567890ab"));
    }

    @Test
    void getUserSubUnauthenticated() {
        final var result = authenticationUtils.getUserSub();

        assertThat(result, is("123456789"));
    }

    @Test
    @MockCustomUser
    void getUserRoles() {
        final var result = authenticationUtils.getUserRoles();

        assertThat(result, is(List.of("admin")));
    }

    @Test
    void getUserRolesUnauthenticated() {
        final var result = authenticationUtils.getUserRoles();

        assertThat(result, is(List.of()));
    }

    @Test
    @MockCustomUser
    void isOnlyRoleAnwenderFalse() {
        final var result = authenticationUtils.isOnlyRoleAnwender();

        assertThat(result, is(false));
    }

    @Test
    @MockCustomUser(roles = { "anwender" })
    void isOnlyRoleAnwenderTrue() {
        final var result = authenticationUtils.isOnlyRoleAnwender();

        assertThat(result, is(true));
    }

    @Test
    void isOnlyRoleAnwenderUnauthenticated() {
        final var result = authenticationUtils.isOnlyRoleAnwender();

        assertThat(result, is(true));
    }

    @Test
    @MockCustomUser(roles = { "anwender", "admin" })
    void getBearbeitendePerson() {
        final var result = authenticationUtils.getBearbeitendePerson();

        final var expected = new BearbeitendePerson();
        expected.setName("Rob Winch");
        expected.setEmail("rob@example.com");
        expected.setOrganisationseinheit("IT");

        assertThat(result, is(expected));
    }

    @Test
    void getBearbeitendePersonUnauthenticated() {
        final var result = authenticationUtils.getBearbeitendePerson();

        final var expected = new BearbeitendePerson();
        expected.setName("unauthenticated unauthenticated");
        expected.setEmail("unauthenticated");
        expected.setOrganisationseinheit("unauthenticated");

        assertThat(result, is(expected));
    }
}

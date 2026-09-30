package de.muenchen.isi.domain.service.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.security.AuthenticationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticatedUserServiceTest {

    private static final String USER_SUB = "user-sub";

    @Mock
    private AuthenticationUtils authenticationUtils;

    private AuthenticatedUserService authenticatedUserService;

    @BeforeEach
    void setUp() {
        authenticatedUserService = new AuthenticatedUserService(authenticationUtils);
        Mockito.reset(authenticationUtils);
    }

    @Test
    void getSubFromAuthenticatedUserLiefertDenUserSub() throws Exception {
        when(authenticationUtils.getUserSub()).thenReturn(USER_SUB);
        when(authenticationUtils.isSubFromUnauthenticatedUser(USER_SUB)).thenReturn(false);

        assertThat(authenticatedUserService.getSubFromAuthenticatedUser("egal"), is(USER_SUB));
    }

    @Test
    void getSubFromAuthenticatedUserWirftBeiFallbackSub() {
        when(authenticationUtils.getUserSub()).thenReturn(USER_SUB);
        when(authenticationUtils.isSubFromUnauthenticatedUser(USER_SUB)).thenReturn(true);

        assertThrows(UserRoleNotAllowedException.class, () ->
            authenticatedUserService.getSubFromAuthenticatedUser("egal")
        );
    }

    @Test
    void getSubFromAuthenticatedUserUebernimmtDieUebergebeneFehlermeldung() {
        when(authenticationUtils.getUserSub()).thenReturn(USER_SUB);
        when(authenticationUtils.isSubFromUnauthenticatedUser(USER_SUB)).thenReturn(true);

        final var meldungFilter = "Sie müssen authentifiziert sein, um mit persönlichen Filtern interagieren zu können";
        final var exceptionFilter = assertThrows(UserRoleNotAllowedException.class, () ->
            authenticatedUserService.getSubFromAuthenticatedUser(meldungFilter)
        );
        assertThat(exceptionFilter.getMessage(), is(meldungFilter));

        final var meldungStartseite = "Sie müssen authentifiziert sein, um die Startseiteneinstellungen zu verwenden";
        final var exceptionStartseite = assertThrows(UserRoleNotAllowedException.class, () ->
            authenticatedUserService.getSubFromAuthenticatedUser(meldungStartseite)
        );
        assertThat(exceptionStartseite.getMessage(), is(meldungStartseite));
    }
}

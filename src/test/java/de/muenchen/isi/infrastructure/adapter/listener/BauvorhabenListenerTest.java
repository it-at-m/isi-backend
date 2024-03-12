package de.muenchen.isi.infrastructure.adapter.listener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.service.transition.MockCustomUser;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.BearbeitendePerson;
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
class BauvorhabenListenerTest {

    @Autowired
    private BauvorhabenListener bauvorhabenListener;

    @Test
    @MockCustomUser
    void beforeSave() {
        final var entityToSave = new Bauvorhaben();
        bauvorhabenListener.beforeSave(entityToSave);

        final var expected = new Bauvorhaben();
        final var bearbeitendePerson = new BearbeitendePerson();
        bearbeitendePerson.setName("Rob Winch");
        bearbeitendePerson.setEmail("rob@example.com");
        bearbeitendePerson.setOrganisationseinheit("IT");
        expected.setBearbeitendePerson(bearbeitendePerson);

        assertThat(entityToSave, is(expected));
    }
}

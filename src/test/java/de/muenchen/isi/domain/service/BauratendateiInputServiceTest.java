package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.mapper.BauratendateiDomainMapperImpl;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiWohneinheitenModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BauratendateiInputServiceTest {

    private BauratendateiInputService bauratendateiInputService = new BauratendateiInputService(
        new BauratendateiDomainMapperImpl()
    );

    @Test
    void concatJahrAndFoerderart() {
        final var bauratendateiWohneinheiten = new BauratendateiWohneinheitenModel();
        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        var result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is("2024förderart1"));

        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart("");
        result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is("2024"));

        bauratendateiWohneinheiten.setJahr("2024");
        bauratendateiWohneinheiten.setFoerderart(null);
        result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is("2024null"));

        bauratendateiWohneinheiten.setJahr("");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is("förderart1"));

        bauratendateiWohneinheiten.setJahr("null");
        bauratendateiWohneinheiten.setFoerderart("förderart1");
        result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is("nullförderart1"));

        bauratendateiWohneinheiten.setJahr("");
        bauratendateiWohneinheiten.setFoerderart("");
        result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is(""));

        bauratendateiWohneinheiten.setJahr(null);
        bauratendateiWohneinheiten.setFoerderart(null);
        result = bauratendateiInputService.concatJahrAndFoerderart(bauratendateiWohneinheiten);
        assertThat(result, is("nullnull"));
    }
}

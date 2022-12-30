package de.muenchen.isi.domain.service;

import de.muenchen.isi.api.dto.common.UtmDto;
import de.muenchen.isi.api.dto.common.Wgs84Dto;
import de.muenchen.isi.domain.exception.KoordinatenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class KoordinatenServiceTest {

    private KoordinatenService koordinatenService;

    @BeforeEach
    public void beforeEach() {
        this.koordinatenService = new KoordinatenService();
    }

    @Test
    public void wgs84ToUtm32Test() throws KoordinatenException {
        final Wgs84Dto wgs84Dto = new Wgs84Dto();
        wgs84Dto.setLatitude(48.137237);
        wgs84Dto.setLongitude(11.575524);

        final UtmDto utmDto = this.koordinatenService.wgs84ToUtm32(wgs84Dto);

        assertEquals(utmDto.getNorth(), 5334761.975490341);
        assertEquals(utmDto.getEast(), 691605.42419006);
    }

    @Test
    public void utm32ToWgs84Test() throws KoordinatenException {
        final UtmDto utmDto = new UtmDto();
        utmDto.setNorth(5334530.596);
        utmDto.setEast(689878.435);
        final Wgs84Dto wgs84Dto = this.koordinatenService.utm32ToWgs84(utmDto);

        assertEquals(wgs84Dto.getLatitude(), 48.13567500095738);
        assertEquals(wgs84Dto.getLongitude(), 11.552230994968712);
    }

}

package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.csv.BerichtsdatenKitaPlbCsv;
import de.muenchen.isi.infrastructure.entity.enums.Altersgruppe;
import de.muenchen.isi.infrastructure.entity.stammdaten.BerichtsdatenKitaPlb;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StammdatenDomainMapperTest {

    private StammdatenDomainMapper stammdatenDomainMapper = new StammdatenDomainMapperImpl();

    @Test
    void csv2EntityForEachAltersgruppe() {
        final var prognosedatenKitaPlbCsv = new BerichtsdatenKitaPlbCsv();
        prognosedatenKitaPlbCsv.setKitaPlb(99L);
        prognosedatenKitaPlbCsv.setBerichtsstand(LocalDate.of(2024, 5, 1));
        prognosedatenKitaPlbCsv.setAnzahlNullBisZweiJaehrige(BigDecimal.ONE);
        prognosedatenKitaPlbCsv.setAnzahlDreiBisFuenfJaehrigeUndFuenfzigProzentSechsJaehrige(BigDecimal.TEN);

        final var result = stammdatenDomainMapper.csv2EntityForEachAltersgruppe(prognosedatenKitaPlbCsv);

        final var entity1 = new BerichtsdatenKitaPlb();
        entity1.setKitaPlb(99L);
        entity1.setBerichtsstand(LocalDate.of(2024, 5, 1));
        entity1.setAltersgruppe(Altersgruppe.NULL_ZWEI_JAEHRIGE);
        entity1.setAnzahlKinder(BigDecimal.ONE);
        final var entity2 = new BerichtsdatenKitaPlb();
        entity2.setKitaPlb(99L);
        entity2.setBerichtsstand(LocalDate.of(2024, 5, 1));
        entity2.setAltersgruppe(Altersgruppe.DREI_FUENF_UND_FUENFZIG_PROZENT_SECHS_JAEHRIGE);
        entity2.setAnzahlKinder(BigDecimal.TEN);

        final var expected = Stream.of(entity1, entity2);

        assertThat(result.toList(), is(expected.toList()));
    }
}

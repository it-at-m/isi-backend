package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.FoerdermixDto;
import de.muenchen.isi.api.dto.common.SobonBerechnungDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SobonBerechnungValidatorTest {

    private final SobonBerechnungValidator validator = new SobonBerechnungValidator();

    @Test
    void isValidAVSachbearbeitung() {
        SobonBerechnungDto dto = new SobonBerechnungDto();
        SobonOrientierungswertJahr sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;

        FoerdermixDto foerdermixDto = new FoerdermixDto();
        foerdermixDto.setBezeichnung("Bezeichnung");
        foerdermixDto.setBezeichnungJahr("BezeichnungJahr");
        foerdermixDto.setFoerderarten(List.of());

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(null);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        dto.setVersorgungsquoteHortSobon(null);
        assertThat(validator.isValid(dto, null), is(false));

        dto.setSobonOrientierungswertJahrSobonUrsaechlich(SobonOrientierungswertJahr.UNSPECIFIED);
        dto.setVersorgungsquoteHortSobon(null);
        assertThat(validator.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(null);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        dto.setVersorgungsquoteHortSobon(null);
        assertThat(validator.isValid(dto, null), is(true));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        dto.setVersorgungsquoteHortSobon(null);
        assertThat(validator.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(sobonOrientierungswertJahr);
        dto.setVersorgungsquoteHortSobon(null);
        assertThat(validator.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(sobonOrientierungswertJahr);
        dto.setVersorgungsquoteHortSobon(BigDecimal.valueOf(-0.1));
        assertThat(validator.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(sobonOrientierungswertJahr);
        dto.setVersorgungsquoteHortSobon(BigDecimal.valueOf(1.1));
        assertThat(validator.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(sobonOrientierungswertJahr);
        dto.setVersorgungsquoteHortSobon(BigDecimal.ZERO);
        assertThat(validator.isValid(dto, null), is(true));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(sobonOrientierungswertJahr);
        dto.setVersorgungsquoteHortSobon(BigDecimal.ONE);
        assertThat(validator.isValid(dto, null), is(true));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(sobonOrientierungswertJahr);
        dto.setVersorgungsquoteHortSobon(BigDecimal.valueOf(0.100));
        assertThat(validator.isValid(dto, null), is(true));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(sobonOrientierungswertJahr);
        dto.setVersorgungsquoteHortSobon(BigDecimal.valueOf(0.900));
        assertThat(validator.isValid(dto, null), is(true));

        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(sobonOrientierungswertJahr);
        dto.setVersorgungsquoteHortSobon(null);
        assertThat(validator.isValid(dto, null), is(false));

        foerdermixDto.setFoerderarten(null);
        foerdermixDto.setBezeichnung(null);
        foerdermixDto.setBezeichnungJahr(null);
        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        dto.setVersorgungsquoteHortSobon(null);
        assertThat(validator.isValid(dto, null), is(true));

        foerdermixDto.setFoerderarten(List.of());
        foerdermixDto.setBezeichnung("");
        foerdermixDto.setBezeichnungJahr("");
        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        dto.setVersorgungsquoteHortSobon(null);
        assertThat(validator.isValid(dto, null), is(true));
    }
}

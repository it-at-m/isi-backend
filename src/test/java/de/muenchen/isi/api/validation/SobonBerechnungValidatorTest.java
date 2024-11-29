package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.AbfragevarianteBauleitplanverfahrenDto;
import de.muenchen.isi.api.dto.FoerderartDto;
import de.muenchen.isi.api.dto.FoerdermixDto;
import de.muenchen.isi.api.dto.common.SobonBerechnungDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import java.util.ArrayList;
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
        foerdermixDto.setFoerderarten(new ArrayList<>());

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        assertThat(validator.isValid(dto, null), is(true));

        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(null);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        assertThat(validator.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        assertThat(validator.isValid(dto, null), is(false));

        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(null);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        assertThat(validator.isValid(dto, null), is(true));

        foerdermixDto.setFoerderarten(null);
        foerdermixDto.setBezeichnung(null);
        foerdermixDto.setBezeichnungJahr(null);
        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        assertThat(validator.isValid(dto, null), is(true));

        foerdermixDto.setFoerderarten(List.of());
        foerdermixDto.setBezeichnung("");
        foerdermixDto.setBezeichnungJahr("");
        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        assertThat(validator.isValid(dto, null), is(true));

        foerdermixDto.setFoerderarten(null);
        foerdermixDto.setBezeichnung(null);
        foerdermixDto.setBezeichnungJahr(null);
        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        assertThat(validator.isValid(dto, null), is(true));

        // Neuer Testfall für das Feld SobonOrientierungswertJahrSobonUrsaechlich
        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        assertThat(validator.isValid(dto, null), is(true)); // isASobonBerechnung ist true, sollte valid sein

        // Wenn SobonOrientierungswertJahrSobonUrsaechlich nicht null ist, dann wird valid sein, wenn andere Bedingungen auch stimmen
        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(null);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(sobonOrientierungswertJahr);
        assertThat(validator.isValid(dto, null), is(true));

        // Wenn SobonOrientierungswertJahrSobonUrsaechlich null ist und isASobonBerechnung false, dann sollte es ungültig sein
        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        assertThat(validator.isValid(dto, null), is(false)); // isASobonBerechnung ist false und Orientierungswert null

        // Fall, dass isASobonBerechnung false und SobonOrientierungswertJahrSobonUrsaechlich gesetzt ist
        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(sobonOrientierungswertJahr);
        assertThat(validator.isValid(dto, null), is(true)); // beide Felder valid

        // Fall, dass SobonOrientierungswertJahrSobonUrsaechlich nicht null ist, aber Foerdermix leer ist
        foerdermixDto.setFoerderarten(null);
        foerdermixDto.setBezeichnung(null);
        foerdermixDto.setBezeichnungJahr(null);
        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(sobonOrientierungswertJahr);
        assertThat(validator.isValid(dto, null), is(true)); // valid, weil alle Bedingungen erfüllt

        // Weitere Tests für verschiedene Kombinationen, in denen das zusätzliche Feld eine Rolle spielt
        dto.setIsASobonBerechnung(false);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(null);
        assertThat(validator.isValid(dto, null), is(false));

        foerdermixDto.setFoerderarten(List.of());
        foerdermixDto.setBezeichnung("");
        foerdermixDto.setBezeichnungJahr("");
        dto.setIsASobonBerechnung(true);
        dto.setSobonFoerdermix(foerdermixDto);
        dto.setSobonOrientierungswertJahrSobonUrsaechlich(sobonOrientierungswertJahr);
        assertThat(validator.isValid(dto, null), is(true));
    }
}

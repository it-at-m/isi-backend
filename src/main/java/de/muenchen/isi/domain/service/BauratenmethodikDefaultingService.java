package de.muenchen.isi.domain.service;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Bauratenmethodik;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class BauratenmethodikDefaultingService {

    static final LocalDate STICHTAG_NEUE_BAURATENMETHODIK = LocalDate.of(2026, 7, 1);

    /**
     * Ermittelt die Vorbelegung für Bauratenmethodik anhand des Start-4.2-Verfahren-Datums.
     * Liefert {@code null}, wenn das Datum unbekannt ist und somit von der Sachbearbeitung
     * manuell ausgewählt werden muss.
     *
     * @param start42Verfahren Das Datum des Start des 4.2-Verfahrens.
     * @param datumUnbekannt   Ob das Datum als unbekannt/nicht zutreffend markiert wurde.
     * @return Die Vorbelegung für Bauratenmethodik oder {@code null}.
     */
    public Bauratenmethodik determineDefault(final LocalDate start42Verfahren, final boolean datumUnbekannt) {
        if (datumUnbekannt || start42Verfahren == null) {
            return null;
        }
        return start42Verfahren.isBefore(STICHTAG_NEUE_BAURATENMETHODIK)
            ? Bauratenmethodik.ALTE_BAURATENMETHODIK
            : Bauratenmethodik.NEUE_BAURATENMETHODIK;
    }
}

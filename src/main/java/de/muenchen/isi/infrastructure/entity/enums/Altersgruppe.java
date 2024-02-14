package de.muenchen.isi.infrastructure.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Altersgruppe {
    NULL_ZWEI_JAEHRIGE("0-2 Jährige"),
    DREI_FUENF_UND_FUENFZIG_PROZENT_SECHS_JAEHRIGE("3-5 und 50 % 6 Jährige");

    @Getter
    private final String bezeichnung;

    public static class Values {

        public static final String NULL_ZWEI_JAEHRIGE = "NULL_ZWEI_JAEHRIGE";

        public static final String DREI_FUENF_UND_FUENFZIG_PROZENT_SECHS_JAEHRIGE =
            "DREI_FUENF_UND_FUENFZIG_PROZENT_SECHS_JAEHRIGE";
    }
}

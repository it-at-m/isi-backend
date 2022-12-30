package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ZustaendigeDienststelle implements ILookup {

    UNSPECIFIED(ILookup.UNSPECIFIED),
    PLAN_HA_2("PLAN-HA II Stadtplanung"),
    KR("Grundstücksverwaltung / -verkehr"),
    PLAN_HA_3("PLAN-HA III Stadtsanierung"),
    PLAN_HA_4("PLAN-HA IV Baugenehmigung"),
    PLAN_HA_1_11_2("PLAN-HA I/11-2 Koordinierung Stellungnahmen PLAN HA I"),
    PLAN_HA_1_4("PLAN-HA I/11-2 Koordinierung Stellungnahmen PLAN HA I"),
    RBS_SB("RBS-SB Schul- und Kitabedarfsplanung Stabsstelle Steuerungsunterstützung und Bedarfsplanung"),
    RBS_ZIM_N("RBS-ZIM-N Neubau Infrastruktureinrichtungen");

    @Getter
    private final String bezeichnung;

}

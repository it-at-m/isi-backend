package de.muenchen.isi.configuration;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import java.util.EnumSet;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfiguration extends StateMachineConfigurerAdapter<StatusAbfrage, StatusAbfrageEvents> {

    /**
     * Legt alle verfuegbaren Status fest. ANGELEGT ist der Anfangstatus.
     * <p>
     * Es wird bewusst kein Endstatus definiert, da aus jedem Status (auch ABBRUCH) eine erneute Bearbeitung möglich sein kann.
     * Eine State Machine in einem Endstatus nimmt keine Events mehr an.
     *
     * @param states konfiguriert alle Status der State Machine
     * @throws Exception wenn ein Fehler in der Configuration auftritt
     */
    @Override
    public void configure(StateMachineStateConfigurer<StatusAbfrage, StatusAbfrageEvents> states) throws Exception {
        states.withStates().initial(StatusAbfrage.ANGELEGT).states(EnumSet.allOf(StatusAbfrage.class));
    }

    /**
     * Beschreibt alle Statusänderungen mit ihren dazugehörgen Events.
     * Für mehr Informationen zu den Statusänderungen bitte auf Confluence nachschauen.
     *
     * @param transitions konfiguriert die Statusübergange bei der State Machine
     * @throws Exception wenn ein Fehler in der Configuration auftritt
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<StatusAbfrage, StatusAbfrageEvents> transitions)
        throws Exception {
        transitions
            .withExternal()
            .source(StatusAbfrage.ANGELEGT)
            .target(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG)
            .event(StatusAbfrageEvents.FREIGABE)
            .and()
            // Alle Events vom Status UEBERMITTELT_ZUR_BEARBEITUNG
            .withExternal()
            .source(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG)
            .target(StatusAbfrage.ANGELEGT)
            .event(StatusAbfrageEvents.ZURUECK_AN_ABFRAGEERSTELLUNG)
            .and()
            .withExternal()
            .source(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG)
            .target(StatusAbfrage.ABBRUCH)
            .event(StatusAbfrageEvents.ABBRECHEN)
            .and()
            .withExternal()
            .source(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG)
            .target(StatusAbfrage.START_BEARBEITUNG)
            .event(StatusAbfrageEvents.IN_BEARBEITUNG_SETZEN)
            .and()
            // Alle Events vom Status START_BEARBEITUNG
            .withExternal()
            .source(StatusAbfrage.START_BEARBEITUNG)
            .target(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT)
            .event(StatusAbfrageEvents.KEINE_BEARBEITUNG_NOETIG)
            .and()
            .withExternal()
            .source(StatusAbfrage.START_BEARBEITUNG)
            .target(StatusAbfrage.ANGELEGT)
            .event(StatusAbfrageEvents.ZURUECK_AN_ABFRAGEERSTELLUNG)
            .and()
            .withExternal()
            .source(StatusAbfrage.START_BEARBEITUNG)
            .target(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG)
            .event(StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME)
            .and()
            .withExternal()
            .source(StatusAbfrage.START_BEARBEITUNG)
            .target(StatusAbfrage.ABBRUCH)
            .event(StatusAbfrageEvents.ABBRECHEN)
            .and()
            //Alle Events vom Status EINPFLEGEN_BEDARFSMELDUNG
            .withExternal()
            .source(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG)
            .target(StatusAbfrage.EINPLANUNG_BEDARFE)
            .event(StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE)
            .and()
            .withExternal()
            .source(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG)
            .target(StatusAbfrage.START_BEARBEITUNG)
            .event(StatusAbfrageEvents.ZURUECK_AN_SACHBEARBEITUNG)
            .and()
            .withExternal()
            .source(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG)
            .target(StatusAbfrage.ABBRUCH)
            .event(StatusAbfrageEvents.ABBRECHEN)
            .and()
            // Alle Events vom Status EINPLANUNG_BEDARFE
            .withExternal()
            .source(StatusAbfrage.EINPLANUNG_BEDARFE)
            .target(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT)
            .event(StatusAbfrageEvents.SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG)
            .and()
            .withExternal()
            .source(StatusAbfrage.EINPLANUNG_BEDARFE)
            .target(StatusAbfrage.ABBRUCH)
            .event(StatusAbfrageEvents.ABBRECHEN)
            .and()
            // Alle Events der Status ERLEDIGT_OHNE_FACHREFERAT, ERLEDIGT_MIT_FACHREFERAT und ABBRUCH
            .withExternal()
            .source(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT)
            .target(StatusAbfrage.START_BEARBEITUNG)
            .event(StatusAbfrageEvents.ERNEUTE_BEARBEITUNG)
            .and()
            .withExternal()
            .source(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT)
            .target(StatusAbfrage.START_BEARBEITUNG)
            .event(StatusAbfrageEvents.ERNEUTE_BEARBEITUNG)
            .and()
            .withExternal()
            .source(StatusAbfrage.ABBRUCH)
            .target(StatusAbfrage.START_BEARBEITUNG)
            .event(StatusAbfrageEvents.ERNEUTE_BEARBEITUNG);
    }
}

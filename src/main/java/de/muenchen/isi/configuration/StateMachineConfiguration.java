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
     * Legt alle verfuegbaren Events fest. ANGELEGT ist der Anfangstatus und ABBRUCH ist der Endstatus.
     *
     * @param states konfiguriert alle Status der State Machine
     * @throws Exception wenn ein Fehler in der Configuration auftritt
     */
    @Override
    public void configure(StateMachineStateConfigurer<StatusAbfrage, StatusAbfrageEvents> states) throws Exception {
        states
            .withStates()
            .initial(StatusAbfrage.ANGELEGT)
            .states(EnumSet.allOf(StatusAbfrage.class))
            .end(StatusAbfrage.ABBRUCH);
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
            .target(StatusAbfrage.OFFEN)
            .event(StatusAbfrageEvents.FREIGABE)
            .and()
            // Alle Events vom Status OFFEN
            .withExternal()
            .and()
            .withExternal()
            .source(StatusAbfrage.OFFEN)
            .target(StatusAbfrage.ANGELEGT)
            .event(StatusAbfrageEvents.ZURUECK_AN_ABFRAGEERSTELLUNG)
            .and()
            .withExternal()
            .source(StatusAbfrage.OFFEN)
            .target(StatusAbfrage.ABBRUCH)
            .event(StatusAbfrageEvents.ABBRECHEN)
            .and()
            .withExternal()
            .source(StatusAbfrage.OFFEN)
            .target(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG)
            .event(StatusAbfrageEvents.IN_BEARBEITUNG_SETZEN)
            .and()
            // Alle Events vom Status IN_BEARBEITUNG_PLAN
            .withExternal()
            .source(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG)
            .target(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT)
            .event(StatusAbfrageEvents.KEINE_BEARBEITUNG_NOETIG)
            .and()
            .withExternal()
            .source(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG)
            .target(StatusAbfrage.ANGELEGT)
            .event(StatusAbfrageEvents.ZURUECK_AN_ABFRAGEERSTELLUNG)
            .and()
            .withExternal()
            .source(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG)
            .target(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE)
            .event(StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME)
            .and()
            .withExternal()
            .source(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG)
            .target(StatusAbfrage.ABBRUCH)
            .event(StatusAbfrageEvents.ABBRECHEN)
            .and()
            //Alle Events vom Status IN_BEARBEITUNG_FACHREFERATE
            .withExternal()
            .source(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE)
            .target(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT)
            .event(StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE)
            .and()
            .withExternal()
            .source(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE)
            .target(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG)
            .event(StatusAbfrageEvents.ZURUECK_AN_SACHBEARBEITUNG)
            .and()
            .withExternal()
            .source(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE)
            .target(StatusAbfrage.ABBRUCH)
            .event(StatusAbfrageEvents.ABBRECHEN)
            .and()
            // Alle Events vom Status BEDARFSMELDUNG_ERFOLGT
            .withExternal()
            .source(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT)
            .target(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT)
            .event(StatusAbfrageEvents.SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG)
            .and()
            .withExternal()
            .source(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT)
            .target(StatusAbfrage.ABBRUCH)
            .event(StatusAbfrageEvents.ABBRECHEN)
            .and()
            .withExternal()
            .source(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT)
            .target(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG)
            .event(StatusAbfrageEvents.ERNEUTE_BEARBEITUNG)
            .and()
            .withExternal()
            .source(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT)
            .target(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG)
            .event(StatusAbfrageEvents.ERNEUTE_BEARBEITUNG);
    }
}

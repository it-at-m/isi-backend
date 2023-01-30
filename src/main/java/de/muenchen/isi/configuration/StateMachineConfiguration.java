package de.muenchen.isi.configuration;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfiguration extends StateMachineConfigurerAdapter<StatusAbfrage, StatusAbfrageEvents> {

    /**
     * Legt fest welche Events es alle geben soll. ANGELEGT ist der Anfangstatus und ABBRUCH ist der Endstatus
     *
     * @param states konfiguriert alle Statis der State Machine
     * @throws Exception falls ein Fehler passiert
     */
    @Override
    public void configure(StateMachineStateConfigurer<StatusAbfrage, StatusAbfrageEvents> states) throws Exception {
        states.withStates()
                .initial(StatusAbfrage.ANGELEGT)
                .states(EnumSet.allOf(StatusAbfrage.class))
                .end(StatusAbfrage.ABBRUCH);
    }

    /**
     * Beschreibt alle Statusänderungen mit ihren dazugehörgen Events.
     * Für mehr Informationen zu den Statusänderungen bitte auf Confluence nachschauen
     *
     * @param transitions konfiguriert die Statusübergange bei der State Machine
     * @throws Exception falls ein Fehler passiert
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<StatusAbfrage, StatusAbfrageEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(StatusAbfrage.ANGELEGT).target(StatusAbfrage.OFFEN)
                .event(StatusAbfrageEvents.FREIGABE)
                .and()

                // Alle Events vom Status OFFEN
                .withExternal()
                .source(StatusAbfrage.OFFEN).target(StatusAbfrage.IN_ERFASSUNG)
                .event(StatusAbfrageEvents.ZUSAETZLICHE_ABFRAGEVARIANTE)
                .and()
                .withExternal()
                .source(StatusAbfrage.OFFEN).target(StatusAbfrage.ANGELEGT)
                .event(StatusAbfrageEvents.ANGABEN_ANPASSEN)
                .and()
                .withExternal()
                .source(StatusAbfrage.OFFEN).target(StatusAbfrage.ABBRUCH)
                .event(StatusAbfrageEvents.ABBRECHEN)
                .and()
                .withExternal()
                .source(StatusAbfrage.OFFEN).target(StatusAbfrage.IN_BEARBEITUNG_PLAN)
                .event(StatusAbfrageEvents.KEINE_ZUSAEZTLICHE_ABFRAGEVARIANTE)
                .and()

                // Alle Events vom Status IN_ERFASSUNG
                .withExternal()
                .source(StatusAbfrage.IN_ERFASSUNG).target(StatusAbfrage.IN_BEARBEITUNG_PLAN)
                .event(StatusAbfrageEvents.SPEICHERN_DER_VARIANTEN)
                .and()
                .withExternal()
                .source(StatusAbfrage.IN_ERFASSUNG).target(StatusAbfrage.ABBRUCH)
                .event(StatusAbfrageEvents.ABBRECHEN)
                .and()
                .withExternal()
                .source(StatusAbfrage.IN_ERFASSUNG).target(StatusAbfrage.ANGELEGT)
                .event(StatusAbfrageEvents.ANGABEN_ANPASSEN)
                .and()

                // Alle Events vom Status IN_BEARBEITUNG_PLAN
                .withExternal()
                .source(StatusAbfrage.IN_BEARBEITUNG_PLAN).target(StatusAbfrage.ERLEDIGT)
                .event(StatusAbfrageEvents.KEINE_BEARBEITUNG_NOETIG)
                .and()
                .withExternal()
                .source(StatusAbfrage.IN_BEARBEITUNG_PLAN).target(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE)
                .event(StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME)
                .and()
                .withExternal()
                .source(StatusAbfrage.IN_BEARBEITUNG_PLAN).target(StatusAbfrage.ABBRUCH)
                .event(StatusAbfrageEvents.ABBRECHEN)
                .and()
                .withExternal()
                .source(StatusAbfrage.IN_BEARBEITUNG_PLAN).target(StatusAbfrage.IN_ERFASSUNG)
                .event(StatusAbfrageEvents.WEITERE_ABFRAVARIANTEN_ANLEGEN)
                .and()

                //Alle Events vom Status IN_BEARBEITUNG_FACHREFERATE
                .withExternal()
                .source(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE).target(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT)
                .event(StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE)
                .and()
                .withExternal()
                .source(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE).target(StatusAbfrage.ABBRUCH)
                .event(StatusAbfrageEvents.ABBRECHEN)
                .and()
                .withExternal()
                .source(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE).target(StatusAbfrage.IN_ERFASSUNG)
                .event(StatusAbfrageEvents.WEITERE_ABFRAVARIANTEN_ANLEGEN)
                .and()

                // Alle Events vom Status BEDARFSMELDUNG_ERFOLGT
                .withExternal()
                .source(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT).target(StatusAbfrage.ERLEDIGT)
                .event(StatusAbfrageEvents.SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG)
                .and()
                .withExternal()
                .source(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT).target(StatusAbfrage.ABBRUCH)
                .event(StatusAbfrageEvents.ABBRECHEN)
                .and()
                .withExternal()
                .source(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT).target(StatusAbfrage.IN_ERFASSUNG)
                .event(StatusAbfrageEvents.WEITERE_ABFRAVARIANTEN_ANLEGEN)
                .and()
                // Alle Events von ERLDEDIGT
                .withExternal()
                .source(StatusAbfrage.ERLEDIGT).target(StatusAbfrage.IN_ERFASSUNG)
                .event(StatusAbfrageEvents.WEITERE_ABFRAVARIANTEN_ANLEGEN);
    }
}

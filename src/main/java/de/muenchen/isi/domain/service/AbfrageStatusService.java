package de.muenchen.isi.domain.service;

import de.muenchen.isi.configuration.StateMachineConfiguration;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.StateMachineTransitionFailedException;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Der Zustandsautomat ist in {@link  StateMachineConfiguration} konfiguriert.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageStatusService {

    private static final String ABFRAGE_ID_HEADER = "abfrage_id";

    private final AbfrageService abfrageService;

    private final StateMachineFactory<StatusAbfrage, StatusAbfrageEvents> stateMachineFactory;

    /**
     * Ändert den Status auf {@link StatusAbfrage#OFFEN}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void freigabeAbfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.FREIGABE, stateMachine);
    }


    /**
     * Ändert den Status auf {@link StatusAbfrage#ABBRUCH}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void abbrechenAbfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.ABBRECHEN, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#ANGELEGT}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void angabenAnpassenAbfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.ANGABEN_ANPASSEN, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#IN_ERFASSUNG}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void weitereAbfragevariantenAnlegen(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.WEITERE_ABFRAVARIANTEN_ANLEGEN, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#IN_BEARBEITUNG_PLAN}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void keineZusaetzlicheAbfragevariante(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.KEINE_ZUSAEZTLICHE_ABFRAGEVARIANTE, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#IN_ERFASSUNG}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void zusaetzlicheAbfragevarianteAnlegen(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.ZUSAETZLICHE_ABFRAGEVARIANTE, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#IN_BEARBEITUNG_PLAN}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void speichernDerVarianten(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.SPEICHERN_DER_VARIANTEN, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#ERLEDIGT}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void keineBearbeitungNoetig(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.KEINE_BEARBEITUNG_NOETIG, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#IN_BEARBEITUNG_FACHREFERATE}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void verschickenDerStellungnahme(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#BEDARFSMELDUNG_ERFOLGT}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void bedarfsmeldungErfolgt(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#ERLEDIGT}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void speichernVonSozialinfrastrukturVersorgung(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG, stateMachine);
    }


    /**
     * Erstellt die Statemachine und initialisiert diese mit dem Status der Abfrage aus der DB.
     * <p>
     * Ausserdem wird ein preTransition-Listener definiert, der bei einer Statusänderung den Status in der DB aktualisiert.
     *
     * @param id vom Typ {@link UUID}  um die Abfrage aus der DB zu holen.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    private StateMachine<StatusAbfrage, StatusAbfrageEvents> build(final UUID id) throws EntityNotFoundException {
        final InfrastrukturabfrageModel abfrage = this.abfrageService.getInfrastrukturabfrageById(id);
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.stateMachineFactory.getStateMachine(abfrage.getId());

        stateMachine.stopReactively().block();

        stateMachine.getStateMachineAccessor().doWithAllRegions(stateMachineAccess -> {

            // Setzt den Status der Abfrage aus der DB in der StateMachine.
            stateMachineAccess.resetStateMachineReactively(new DefaultStateMachineContext<>(abfrage.getAbfrage().getStatusAbfrage(), null, null, null)).block();

            // Der Interceptor aktualisiert bei einer Statusänderung den Status der in der DB gespeicherten Entität.
            stateMachineAccess.addStateMachineInterceptor(new StateMachineInterceptorAdapter<>() {

                /**
                 * Verhalten siehe {@link StateMachineInterceptor#preTransition(StateContext)}
                 *
                 * Die Rückgabe des Wertes null führt in der StateMachine zum Ergebnis {@link StateMachineEventResult.ResultType#DENIED}
                 */
                @Override
                public StateContext<StatusAbfrage, StatusAbfrageEvents> preTransition(final StateContext<StatusAbfrage, StatusAbfrageEvents> stateContext) {
                    final State<StatusAbfrage, StatusAbfrageEvents> state = stateContext.getTransition().getTarget();
                    final MessageHeaders messageHeaders = stateContext.getMessageHeaders();
                    try {
                        final UUID abfrageId = AbfrageStatusService.this.getAbfrageId(messageHeaders);
                        final InfrastrukturabfrageModel abfrage = AbfrageStatusService.this.abfrageService.getInfrastrukturabfrageById(abfrageId);
                        abfrage.getAbfrage().setStatusAbfrage(state.getId());
                        AbfrageStatusService.this.abfrageService.updateInfrastrukturabfrage(abfrage);
                    } catch (final Exception exception) {
                        log.error("Die vom Statuswechsel betroffene Infrastrukturabfrage wurde nicht gefunden.", exception);
                        return null;
                    }
                    return stateContext;
                }

            });

        });

        stateMachine.startReactively().block();
        return stateMachine;
    }

    /**
     * Leitet eine Statusänderung ein mit dem übergebenem Event
     * <p>
     *
     * @param id           vom Typ {@link UUID} um die Abfrage aus der DB zu holen.
     * @param event        vom Typ {@link StatusAbfrageEvents} um eine Statusänderung durchzuführen
     * @param stateMachine vom Typ {@link StateMachine} welches den aktuellen Status plus alle Statis enthält
     * @throws AbfrageStatusNotAllowedException falls die Statusänderung nicht erlaubt nicht
     */
    private void sendEvent(final UUID id,
                           final StatusAbfrageEvents event,
                           final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine) throws AbfrageStatusNotAllowedException {
        final Mono<Message<StatusAbfrageEvents>> message = Mono.just(MessageBuilder.withPayload(event).setHeader(ABFRAGE_ID_HEADER, id).build());
        final Flux<StateMachineEventResult<StatusAbfrage, StatusAbfrageEvents>> result = stateMachine.sendEvent(message);
        try {
            result.toStream().forEach(stateMachineEventResult -> {
                if (stateMachineEventResult.getResultType() == StateMachineEventResult.ResultType.DENIED) {
                    final var errorMessage = String.format("Status Änderung ist nicht möglich. Aktueller Status: %s.", stateMachine.getState().getId());
                    log.error(errorMessage);
                    throw new StateMachineTransitionFailedException(errorMessage);
                }
            });
        } catch (final StateMachineTransitionFailedException exception) {
            throw new AbfrageStatusNotAllowedException(exception.getMessage(), exception);
        }
    }

    /**
     * Entimmt der Message die ID der Abfrage.
     *
     * @param messageHeaders mit dem Event und der AbfrageId
     * @return Gibt die UUID aus der Message zurück
     * @throws EntityNotFoundException falls kein AbfrageId Header gefunden wurde
     */
    public UUID getAbfrageId(final MessageHeaders messageHeaders) throws EntityNotFoundException {
        final UUID abfrageId;
        if (messageHeaders.containsKey(ABFRAGE_ID_HEADER)) {
            abfrageId = (UUID) messageHeaders.get(ABFRAGE_ID_HEADER);
        } else {
            throw new EntityNotFoundException("Header der Abfrage-ID wurde nicht gefunden");
        }
        return abfrageId;
    }

}

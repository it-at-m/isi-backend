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
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

/**
 * Genaue Statusänderungdefinition in {@link  StateMachineConfiguration} nachzusehen
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageStatusService {

    private static final String ABFRAGE_ID_HEADER = "abfrage_id";
    private final AbfrageService abfrageService;
    private final StateMachineFactory<StatusAbfrage, StatusAbfrageEvents> stateMachineFactory;

    /**
     * Ändert den Status auf OFFEN
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
     * Ändert den Status auf ABBRUCH
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
     * Ändert den Status auf ANGELEGT
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
     * Ändert den Status auf IN_ERFASSUNG
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
     * Ändert den Status auf IN_BEARBEITUNG_PLAN
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
     * Ändert den Status auf IN_ERFASSUNG
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
     * Ändert den Status auf IN_BEARBEITUNG_PLAN
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
     * Ändert den Status auf ERLEDIGT
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
     * Ändert den Status auf IN_BEARBEITUNG_FACHREFERATE
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
     * Ändert den Status auf BEDARFSMELDUNG_ERFOLGT
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
     * Ändert den Status auf ERLEDIGT
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
     * Erstellt die Statemachine und initialisiert sie mit dem Status der Abfrage aus der DB.
     * <p>
     * Ausserdem definieren wir eine preStateChange Listener der bei einer Statusänderung den Status in der DB aktualisiert.
     *
     * @param id vom Typ {@link UUID}  um die Abfrage aus der DB zu holen.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    private StateMachine<StatusAbfrage, StatusAbfrageEvents> build(final UUID id) throws EntityNotFoundException {
        final InfrastrukturabfrageModel abfrage = this.abfrageService.getInfrastrukturabfrageById(id);
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.stateMachineFactory.getStateMachine(abfrage.getId());

        stateMachine.stopReactively().block();

        try {
            stateMachine.getStateMachineAccessor().doWithAllRegions(stateMachineAccess -> {

                stateMachineAccess.addStateMachineInterceptor(new StateMachineInterceptorAdapter<>() {

                    @Override
                    public void preStateChange(final State<StatusAbfrage, StatusAbfrageEvents> state, final Message<StatusAbfrageEvents> message,
                                               final Transition<StatusAbfrage, StatusAbfrageEvents> transition, final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine,
                                               final StateMachine<StatusAbfrage, StatusAbfrageEvents> rootStateMachine) {
                        Optional.ofNullable(message).ifPresent(msg -> {
                            try {
                                final UUID abfrageId = AbfrageStatusService.this.getAbfrageId(msg);
                                final InfrastrukturabfrageModel abfrage = AbfrageStatusService.this.abfrageService.getInfrastrukturabfrageById(abfrageId);
                                abfrage.getAbfrage().setStatusAbfrage(state.getId());
                                AbfrageStatusService.this.abfrageService.updateInfrastrukturabfrage(abfrage);
                            } catch (final EntityNotFoundException exception) {
                                final var errorMessage = "Infrastrukturabfrage wurde nicht gefunden";
                                log.error(errorMessage);
                                throw new StateMachineTransitionFailedException(errorMessage, exception);
                            }
                        });
                    }
                });

                // Setzt den Status der Abfrage aus der DB in der StateMachine
                stateMachineAccess.resetStateMachineReactively(new DefaultStateMachineContext<>(abfrage.getAbfrage().getStatusAbfrage(), null, null, null)).block();
            });
        } catch (final StateMachineTransitionFailedException exception) {
            throw (EntityNotFoundException) exception.getCause();
        }

        stateMachine.startReactively().block();
        return stateMachine;
    }

    /**
     * Leitet eine Statusänderung ein mit dem übergebenem Event
     * <p>
     *
     * @param id           vom Typ {@link UUID} um die Abfrage aus der DB zu hollen.
     * @param event        vom Typ {@link StatusAbfrageEvents} um eine Statusänderung durchzuführen
     * @param stateMachine vom Typ {@link StateMachine} welches den aktuellen Status plus alle Statis enthält
     * @throws AbfrageStatusNotAllowedException falls die Statusänderung nicht erlaubt nicht
     */
    private void sendEvent(final UUID id, final StatusAbfrageEvents event, final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine) throws AbfrageStatusNotAllowedException {
        final Mono<Message<StatusAbfrageEvents>> message = Mono.just(MessageBuilder.withPayload(event).setHeader(ABFRAGE_ID_HEADER, id).build());
        final Flux<StateMachineEventResult<StatusAbfrage, StatusAbfrageEvents>> result = stateMachine.sendEvent(message);
        try {
            result.subscribe(stateMachineEventResultConsumer -> {
                if (stateMachineEventResultConsumer.getResultType() == StateMachineEventResult.ResultType.DENIED) {
                    final var errorMessage = String.format("Status Änderung ist nicht erlaubt. Aktueller Status: %s.", stateMachine.getState().getId());
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
     * @param msg die Message mit dem Event und der AbfrageId
     * @return Gibt die UUID aus der Message zurürck
     * @throws EntityNotFoundException falls kein AbfrageId Header gefunden wurde
     */
    public UUID getAbfrageId(final Message<StatusAbfrageEvents> msg) throws EntityNotFoundException {
        final UUID abfrageId;
        final MessageHeaders headers = msg.getHeaders();
        if (headers.containsKey(ABFRAGE_ID_HEADER)) {
            abfrageId = (UUID) headers.get(ABFRAGE_ID_HEADER);
        } else {
            throw new EntityNotFoundException("Abfrage ID Header wurde nicht gefunden");
        }
        return abfrageId;
    }

}

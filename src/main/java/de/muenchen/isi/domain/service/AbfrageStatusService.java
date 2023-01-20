package de.muenchen.isi.domain.service;

import de.muenchen.isi.configuration.StateMachineConfiguration;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageStatusService {

    private final AbfrageService abfrageService;
    private final StateMachineFactory<StatusAbfrage, StatusAbfrageEvents> stateMachineFactory;

    private static final String ABFRAGE_ID_HEADER = "abfrage_id";

    /**
     * Ändert den Status anhand der Definition {@link  StateMachineConfiguration}
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    public void freigabeAbfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(id);
        this.sendEvent(id, StatusAbfrageEvents.FREIGABE, stateMachine);
        System.out.println(stateMachine);
    }


    /**
     * Ändert den Status anhand der Definition {@link  StateMachineConfiguration}
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    public void abbrechenAbfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(id);
        this.sendEvent(id, StatusAbfrageEvents.ABBRECHEN, stateMachine);
    }

    /**
     * Ändert den Status anhand der Definition {@link  StateMachineConfiguration}
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    public void angabenAnpassenAbfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(id);
        this.sendEvent(id, StatusAbfrageEvents.ANGABEN_ANPASSEN, stateMachine);
    }

    /**
     * Ändert den Status anhand der Definition {@link  StateMachineConfiguration}
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    public void weitereAbfragevariantenAnlegen(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(id);
        this.sendEvent(id, StatusAbfrageEvents.WEITERE_ABFRAVARIANTEN_ANLEGEN, stateMachine);
    }

    /**
     * Ändert den Status anhand der Definition {@link  StateMachineConfiguration}
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    public void keineZusaetzlicheAbfragevariante(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(id);
        this.sendEvent(id, StatusAbfrageEvents.KEINE_ZUSAEZTLICHE_ABFRAGEVARIANTE, stateMachine);
    }

    /**
     * Ändert den Status anhand der Definition {@link  StateMachineConfiguration}
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    public void zusaetzlicheAbfragevarianteAnlegen(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(id);
        this.sendEvent(id, StatusAbfrageEvents.ZUSAETZLICHE_ABFRAGEVARIANTE, stateMachine);
    }

    /**
     * Ändert den Status anhand der Definition {@link  StateMachineConfiguration}
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    public void speichernDerVarianten(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(id);
        this.sendEvent(id, StatusAbfrageEvents.SPEICHERN_DER_VARIANTEN, stateMachine);
    }

    /**
     * Ändert den Status anhand der Definition {@link  StateMachineConfiguration}
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    public void keineBearbeitungNoetig(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(id);
        this.sendEvent(id, StatusAbfrageEvents.KEINE_BEARBEITUNG_NOETIG, stateMachine);
    }

    /**
     * Ändert den Status anhand der Definition {@link  StateMachineConfiguration}
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    public void verschickenDerStellungnahme(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(id);
        this.sendEvent(id, StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME, stateMachine);
    }

    /**
     * Ändert den Status anhand der Definition {@link  StateMachineConfiguration}
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    public void bedarfsmeldungErfolgt(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(id);
        this.sendEvent(id, StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE, stateMachine);
    }

    /**
     * Ändert den Status anhand der Definition {@link  StateMachineConfiguration}
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    public void speichernVonSozialinfrastrukturVersorgung(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(id);
        this.sendEvent(id, StatusAbfrageEvents.SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG, stateMachine);
    }


    /**
     * Erstellt die Statemachine und initialisiert sie mit dem Status der Abfrage aus der DB.
     * <p>
     * Ausserdem definieren wir eine preStateChange Listener der bei einer Statusänderung den Status in der DB aktualiesiert.
     *
     * @param id vom Typ {@link UUID}  um die Abfrage aus der DB zu hollen.
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden werden kann.
     */
    private StateMachine<StatusAbfrage, StatusAbfrageEvents> build(final UUID id) throws EntityNotFoundException {
        final InfrastrukturabfrageModel abfrage = abfrageService.getInfrastrukturabfrageById(id);
        // Hier habe ich ein EntityNotFoundExeption Array definiert damit ich die Fehlermeldung im preStateChange Listener abfangen kann und
        // danach werfen kann. Da man in den Lambdas keine Variablen zuweisen kann.
        final EntityNotFoundException[] entityNotFoundException = new EntityNotFoundException[1];
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = stateMachineFactory.getStateMachine(abfrage.getId());

        stateMachine.stopReactively().block();

        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(sma -> {

                    sma.addStateMachineInterceptor(new StateMachineInterceptorAdapter<StatusAbfrage, StatusAbfrageEvents>() {

                        @Override
                        public void preStateChange(State<StatusAbfrage, StatusAbfrageEvents> state, Message<StatusAbfrageEvents> message,
                                                   Transition<StatusAbfrage, StatusAbfrageEvents> transition, StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine,
                                                   StateMachine<StatusAbfrage, StatusAbfrageEvents> rootStateMachine) {
                            Optional.ofNullable(message).ifPresent(msg -> {
                                Optional.ofNullable(
                                                UUID.class.cast(msg.getHeaders().getOrDefault(ABFRAGE_ID_HEADER, UUID.randomUUID())))
                                        .ifPresent(fahrzeugId -> {
                                            try {
                                                final InfrastrukturabfrageModel abfrage = abfrageService.getInfrastrukturabfrageById(id);
                                                abfrage.getAbfrage().setStatusAbfrage(state.getId());
                                                abfrageService.updateInfrastrukturabfrage(abfrage);
                                            } catch (EntityNotFoundException e) {
                                                entityNotFoundException[0] = e;
                                            }
                                        });
                            });
                        }
                    });


                    // Setzt den Status der Abfrage aus der DB in der StateMachine
                    sma.resetStateMachineReactively(new DefaultStateMachineContext<>(abfrage.getAbfrage().getStatusAbfrage(), null, null, null)).block();
                    ;
                });
        // Wirft einen Fehler wenn die Entity nicht gefunden wurde
        if (entityNotFoundException[0] != null) {
            throw new EntityNotFoundException("Abfrage mit der ID: " + id + " wurde nicht gefunden");
        }
        stateMachine.startReactively().block();
        return stateMachine;
    }

    private void sendEvent(UUID id, StatusAbfrageEvents event, StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine) throws AbfrageStatusNotAllowedException {
        Mono<Message<StatusAbfrageEvents>> message = Mono.just(MessageBuilder.withPayload(event).setHeader(ABFRAGE_ID_HEADER, id).build());
        Flux<StateMachineEventResult<StatusAbfrage, StatusAbfrageEvents>> result = stateMachine.sendEvent(message);
        final AbfrageStatusNotAllowedException[] abfrageStatusNotAllowedException = new AbfrageStatusNotAllowedException[1];

        result.subscribe(smer -> {
            if (smer.getResultType() == StateMachineEventResult.ResultType.DENIED) {
                abfrageStatusNotAllowedException[0] = new AbfrageStatusNotAllowedException("Status Änderung ist nicht erlaubt. Aktueller Status: " + stateMachine.getState().getId());
            }
        });

        if (abfrageStatusNotAllowedException[0] != null) {
            throw abfrageStatusNotAllowedException[0];
        }
    }
}

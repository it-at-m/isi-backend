package de.muenchen.isi.domain.service;

import de.muenchen.isi.configuration.StateMachineConfiguration;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.StateMachineTransitionFailedException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.common.TransitionModel;
import de.muenchen.isi.domain.service.util.AuthenticationUtils;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import de.muenchen.isi.security.AuthoritiesEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
     * @param id vom Typ {@link UUID} um die Abfrage zu finden
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void freigabeAbfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.FREIGABE, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#IN_BEARBEITUNG_PLAN}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void inBearbeitungSetzenAbfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.IN_BEARBEITUNG_SETZEN, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#ABBRUCH}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void abbrechenAbfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.ABBRECHEN, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#ANGELEGT}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void angabenAnpassenAbfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.ANGABEN_ANPASSEN, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#IN_BEARBEITUNG_PLAN}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void korrigierenAbfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.KORRIGIEREN, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#ERLEDIGT}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void keineBearbeitungNoetig(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.KEINE_BEARBEITUNG_NOETIG, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#IN_BEARBEITUNG_FACHREFERATE}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void verschickenDerStellungnahme(final UUID id)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#BEDARFSMELDUNG_ERFOLGT}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void bedarfsmeldungErfolgt(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE, stateMachine);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#ERLEDIGT}.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     */
    public void speichernVonSozialinfrastrukturVersorgung(final UUID id)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        this.sendEvent(id, StatusAbfrageEvents.SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG, stateMachine);
    }

    /**
     * Erstellt die Statemachine und initialisiert diese mit dem Status der Abfrage aus der DB.
     * <p>
     * Ausserdem wird ein preTransition-Listener definiert, der bei einer Statusänderung den Status in der DB aktualisiert.
     *
     * @param id vom Typ {@link UUID}  um die Abfrage aus der DB zu holen
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden wird
     */
    private StateMachine<StatusAbfrage, StatusAbfrageEvents> build(final UUID id) throws EntityNotFoundException {
        final InfrastrukturabfrageModel abfrage = this.abfrageService.getInfrastrukturabfrageById(id);
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine =
            this.stateMachineFactory.getStateMachine(abfrage.getId());

        stateMachine.stopReactively().block();

        stateMachine
            .getStateMachineAccessor()
            .doWithAllRegions(stateMachineAccess -> {
                // Setzt den Status der Abfrage aus der DB in der StateMachine.
                stateMachineAccess
                    .resetStateMachineReactively(
                        new DefaultStateMachineContext<>(abfrage.getAbfrage().getStatusAbfrage(), null, null, null)
                    )
                    .block();

                // Der Interceptor aktualisiert bei einer Statusänderung den Status der in der DB gespeicherten Entität.
                stateMachineAccess.addStateMachineInterceptor(
                    new StateMachineInterceptorAdapter<>() {
                        /**
                         * Verhalten siehe {@link StateMachineInterceptor#preTransition(StateContext)}
                         *
                         * Die Rückgabe des Wertes null führt in der StateMachine zum Ergebnis {@link StateMachineEventResult.ResultType#DENIED}
                         */
                        @Override
                        public StateContext<StatusAbfrage, StatusAbfrageEvents> preTransition(
                            final StateContext<StatusAbfrage, StatusAbfrageEvents> stateContext
                        ) {
                            final State<StatusAbfrage, StatusAbfrageEvents> state = stateContext
                                .getTransition()
                                .getTarget();
                            final MessageHeaders messageHeaders = stateContext.getMessageHeaders();
                            try {
                                final UUID abfrageId = AbfrageStatusService.this.getAbfrageId(messageHeaders);
                                final InfrastrukturabfrageModel abfrage =
                                    AbfrageStatusService.this.abfrageService.getInfrastrukturabfrageById(abfrageId);
                                abfrage.getAbfrage().setStatusAbfrage(state.getId());
                                abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
                            } catch (
                                final EntityNotFoundException
                                | OptimisticLockingException
                                | UniqueViolationException exception
                            ) {
                                log.error(exception.getMessage(), exception);
                                return null;
                            } catch (final Exception exception) {
                                log.error("Bei der Statusänderung ist ein Fehler aufgetreten.", exception);
                                return null;
                            }
                            return stateContext;
                        }
                    }
                );
            });

        stateMachine.startReactively().block();
        return stateMachine;
    }

    /**
     * Leitet mit dem übergebenem Event eine Statusänderung ein.
     * <p>
     *
     * @param id           vom Typ {@link UUID} um die Abfrage aus der DB zu holen.
     * @param event        vom Typ {@link StatusAbfrageEvents} um eine Statusänderung durchzuführen
     * @param stateMachine vom Typ {@link StateMachine} welche den aktuellen Status plus alle Status enthält
     * @throws AbfrageStatusNotAllowedException falls die Statusänderung nicht erlaubt nicht
     */
    private void sendEvent(
        final UUID id,
        final StatusAbfrageEvents event,
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine
    ) throws AbfrageStatusNotAllowedException {
        final Mono<Message<StatusAbfrageEvents>> message = Mono.just(
            MessageBuilder.withPayload(event).setHeader(ABFRAGE_ID_HEADER, id).build()
        );
        final Flux<StateMachineEventResult<StatusAbfrage, StatusAbfrageEvents>> result = stateMachine.sendEvent(
            message
        );
        try {
            result
                .toStream()
                .forEach(stateMachineEventResult -> {
                    if (stateMachineEventResult.getResultType() == StateMachineEventResult.ResultType.DENIED) {
                        final var errorMessage = String.format(
                            "Status Änderung ist nicht möglich. Aktueller Status: %s.",
                            stateMachine.getState().getId()
                        );
                        log.error(errorMessage);
                        throw new StateMachineTransitionFailedException(errorMessage);
                    }
                });
        } catch (final StateMachineTransitionFailedException exception) {
            throw new AbfrageStatusNotAllowedException(exception.getMessage(), exception);
        }
    }

    /**
     * Entnimmt den MessageHeaders die ID der Abfrage.
     *
     * @param messageHeaders mit dem Event und der AbfrageId
     * @return Gibt die UUID aus der Message zurück
     * @throws EntityNotFoundException falls kein AbfrageId Header gefunden wird
     */
    public UUID getAbfrageId(final MessageHeaders messageHeaders) throws EntityNotFoundException {
        final UUID abfrageId;
        if (messageHeaders.containsKey(ABFRAGE_ID_HEADER)) {
            abfrageId = (UUID) messageHeaders.get(ABFRAGE_ID_HEADER);
        } else {
            final var errorMessage = "Header der Abfrage-ID wurde nicht gefunden";
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }
        return abfrageId;
    }

    /**
     * Entnimmt alle möglichen Statusänderungen aus dem aktuellen Status.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden
     * @return Liste von {@link StatusAbfrageEvents} welche möglich sind
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden wird
     */
    public List<StatusAbfrageEvents> getStatusAbfrageEventsBasedOnState(final UUID id) throws EntityNotFoundException {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(id);
        return stateMachine.getTransitions().stream()
                .filter(transition -> transition.getSource().getId().equals(stateMachine.getState().getId()))
                .map(transition -> transition.getTrigger().getEvent())
                .collect(Collectors.toList());

    }

    /**
     * Entnimmt alle möglichen Statusänderungen aus dem aktuellen Status und der aktuellen Rolle des Nutzers.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden
     * @return Liste von {@link TransitionModel} welche möglich sind
     * @throws EntityNotFoundException falls die Abfrage nicht gefunden wird
     */
    public List<TransitionModel> getStatusAbfrageEventsBasedOnStateAndRole(final UUID id) throws EntityNotFoundException {
        List<AuthoritiesEnum> userRolesAuthorities = AuthenticationUtils.getUserAuthorities();
        List<StatusAbfrageEvents> possibleAbfrageEventsBasedOnRole = new ArrayList<>();
        Map<AuthoritiesEnum, StatusAbfrageEvents> rolesAndMatchingEvent = getRolesAndEventsMap();
        List<TransitionModel> possibleTransitionsModel = new ArrayList<>();
        for (AuthoritiesEnum userRole : userRolesAuthorities) {
            possibleAbfrageEventsBasedOnRole.add(rolesAndMatchingEvent.get(userRole));
        }
        List<StatusAbfrageEvents> matchingAbfrageEvents = new ArrayList<>(getStatusAbfrageEventsBasedOnState(id));
        matchingAbfrageEvents.retainAll(possibleAbfrageEventsBasedOnRole);
        for (StatusAbfrageEvents event : matchingAbfrageEvents) {
            TransitionModel transitionModel = new TransitionModel();
            transitionModel.setUrl(event.getUrl());
            transitionModel.setButtonName(event.getButtonName());
            transitionModel.setIndex(event.getIndex());
            possibleTransitionsModel.add(transitionModel);
        }
        possibleTransitionsModel.sort((Comparator.comparingInt(TransitionModel::getIndex)));
        return possibleTransitionsModel;
    }

    /**
     * Definiert eine {@link MultiValuedMap} welche die Nutzerrolle mit den dazugehörigten {@link StatusAbfrageEvents} verknüpft.
     *
     * @return {@link MultiValuedMap}
     */
    private Map<AuthoritiesEnum, StatusAbfrageEvents> getRolesAndEventsMap() {
        final Map<AuthoritiesEnum, StatusAbfrageEvents> rolesAndEvents = new HashMap<>();
        rolesAndEvents.put(AuthoritiesEnum.ISI_BACKEND_FREIGABE_ABFRAGE, StatusAbfrageEvents.FREIGABE);
        rolesAndEvents.put(AuthoritiesEnum.ISI_BACKEND_ABBRECHEN_ABFRAGE, StatusAbfrageEvents.ABBRECHEN);
        rolesAndEvents.put(AuthoritiesEnum.ISI_BACKEND_ANGABEN_ANPASSEN_ABFRAGE, StatusAbfrageEvents.ANGABEN_ANPASSEN);
        rolesAndEvents.put(AuthoritiesEnum.ISI_BACKEND_IN_BEARBEITUNG_SETZTEN, StatusAbfrageEvents.IN_BEARBEITUNG_SETZEN);
        rolesAndEvents.put(AuthoritiesEnum.ISI_BACKEND_KORRIGIEREN, StatusAbfrageEvents.KORRIGIEREN);
        rolesAndEvents.put(AuthoritiesEnum.ISI_BACKEND_KEINE_BEARBEITUNG_NOETIG_ABFRAGE, StatusAbfrageEvents.KEINE_BEARBEITUNG_NOETIG);
        rolesAndEvents.put(AuthoritiesEnum.ISI_BACKEND_VERSCHICKEN_DER_STELLUNGNAHME_ABFRAGE, StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME);
        rolesAndEvents.put(AuthoritiesEnum.ISI_BACKEND_BEDARFSMELDUNG_ERFOLGT_ABFRAGE, StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE);
        rolesAndEvents.put(AuthoritiesEnum.ISI_BACKEND_SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG_ABFRAGE, StatusAbfrageEvents.SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG);

        return rolesAndEvents;
    }
}

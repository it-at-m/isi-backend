package de.muenchen.isi.domain.service;

import de.muenchen.isi.configuration.StateMachineConfiguration;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.StateMachineTransitionFailedException;
import de.muenchen.isi.domain.exception.StringLengthExceededException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.common.TransitionModel;
import de.muenchen.isi.domain.service.common.BearbeitungshistorieService;
import de.muenchen.isi.domain.service.email.SendWorkAssignmentInformationService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import de.muenchen.isi.security.AuthenticationUtils;
import de.muenchen.isi.security.AuthoritiesEnum;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Der Zustandsautomat ist in {@link  StateMachineConfiguration} konfiguriert.
 */
@Slf4j
@Service
public class AbfrageStatusService {

    private static final int MAX_LENGTH_ANMERKUNG = 2000;

    /**
     * Zentrale Zuordnung der {@link AuthoritiesEnum} zu den dazugehörigen {@link StatusAbfrageEvents}.
     */
    private static final Map<AuthoritiesEnum, StatusAbfrageEvents> EVENT_BY_AUTHORITY = Map.ofEntries(
        Map.entry(AuthoritiesEnum.ISI_BACKEND_FREIGABE_ABFRAGE, StatusAbfrageEvents.FREIGABE),
        Map.entry(AuthoritiesEnum.ISI_BACKEND_ABBRECHEN_ABFRAGE, StatusAbfrageEvents.ABBRECHEN),
        Map.entry(
            AuthoritiesEnum.ISI_BACKEND_ZURUECK_AN_ABFRAGEERSTELLUNG_ABFRAGE,
            StatusAbfrageEvents.ZURUECK_AN_ABFRAGEERSTELLUNG
        ),
        Map.entry(
            AuthoritiesEnum.ISI_BACKEND_IN_BEARBEITUNG_SETZTEN_ABFRAGE,
            StatusAbfrageEvents.IN_BEARBEITUNG_SETZEN
        ),
        Map.entry(
            AuthoritiesEnum.ISI_BACKEND_ZURUECK_AN_SACHBEARBEITUNG_ABFRAGE,
            StatusAbfrageEvents.ZURUECK_AN_SACHBEARBEITUNG
        ),
        Map.entry(
            AuthoritiesEnum.ISI_BACKEND_KEINE_BEARBEITUNG_NOETIG_ABFRAGE,
            StatusAbfrageEvents.KEINE_BEARBEITUNG_NOETIG
        ),
        Map.entry(
            AuthoritiesEnum.ISI_BACKEND_VERSCHICKEN_DER_STELLUNGNAHME_ABFRAGE,
            StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME
        ),
        Map.entry(
            AuthoritiesEnum.ISI_BACKEND_BEDARFSMELDUNG_ERFOLGTE_ABFRAGE,
            StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE
        ),
        Map.entry(
            AuthoritiesEnum.ISI_BACKEND_SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG_ABFRAGE,
            StatusAbfrageEvents.SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG
        ),
        Map.entry(AuthoritiesEnum.ISI_BACKEND_ERNEUTE_BEARBEITUNG_ABFRAGE, StatusAbfrageEvents.ERNEUTE_BEARBEITUNG)
    );

    private final AbfrageService abfrageService;

    private final StateMachineFactory<StatusAbfrage, StatusAbfrageEvents> stateMachineFactory;

    private final AuthenticationUtils authenticationUtils;

    private final BearbeitungshistorieService abfrageBearbeitungshistorieService;

    private final SendWorkAssignmentInformationService sendWorkAssignmentInformationService;

    private final BauratenmethodikDefaultingService bauratenmethodikDefaultingService;

    /**
     * Die je Status möglichen {@link StatusAbfrageEvents}. Da die Statusübergänge statisch konfiguriert sind,
     * wird die Zuordnung einmalig aus der {@link StateMachineConfiguration} ermittelt.
     */
    private final Map<StatusAbfrage, Set<StatusAbfrageEvents>> eventsByStatus;

    public AbfrageStatusService(
        final AbfrageService abfrageService,
        final StateMachineFactory<StatusAbfrage, StatusAbfrageEvents> stateMachineFactory,
        final AuthenticationUtils authenticationUtils,
        final BearbeitungshistorieService abfrageBearbeitungshistorieService,
        final SendWorkAssignmentInformationService sendWorkAssignmentInformationService,
        final BauratenmethodikDefaultingService bauratenmethodikDefaultingService
    ) {
        this.abfrageService = abfrageService;
        this.stateMachineFactory = stateMachineFactory;
        this.authenticationUtils = authenticationUtils;
        this.abfrageBearbeitungshistorieService = abfrageBearbeitungshistorieService;
        this.sendWorkAssignmentInformationService = sendWorkAssignmentInformationService;
        this.bauratenmethodikDefaultingService = bauratenmethodikDefaultingService;
        this.eventsByStatus = getEventsByStatus(stateMachineFactory);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#UEBERMITTELT_ZUR_BEARBEITUNG}.
     *
     * @param id        vom Typ {@link UUID} um die Abfrage zu finden
     * @param anmerkung falls ein Nutzer eine Anmerkung bei einem Statusübergang macht wird diese an die Abfrage Anmerkung angehängt
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     * @throws StringLengthExceededException    wenn die Anmerkung zur Statusänderung die max. Länge überschreitet
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    public void freigabeAbfrage(final UUID id, String anmerkung)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.changeStatus(id, anmerkung, StatusAbfrageEvents.FREIGABE);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#START_BEARBEITUNG}.
     * Bei einem {@link BauleitplanverfahrenModel} wird zusätzlich die Bauratenmethodik vorbelegt.
     *
     * @param id        vom Typ {@link UUID} um die Abfrage zu finden
     * @param anmerkung falls ein Nutzer eine Anmerkung bei einem Statusübergang macht wird diese an die Abfrage Anmerkung angehängt
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     * @throws StringLengthExceededException    wenn die Anmerkung zur Statusänderung die max. Länge überschreitet
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    public void inBearbeitungSetzenAbfrage(final UUID id, String anmerkung)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.changeStatus(
            id,
            anmerkung,
            StatusAbfrageEvents.IN_BEARBEITUNG_SETZEN,
            this::setBauratenmethodikVorbelegungIfBauleitplanverfahren
        );
    }

    /**
     * Ermittelt für ein {@link BauleitplanverfahrenModel} die Vorbelegung für Bauratenmethodik
     * anhand des Feldes {@code start42Verfahren}, setzt diese auf der Abfrage (zur späteren
     * Vorbelegung neu angelegter Sachbearbeitungs-Abfragevarianten durch das Frontend) und
     * zusätzlich direkt auf der SoBoN-Berechnung jeder bereits durch die Abfrageerstellung
     * angelegten Abfragevariante, sofern dort noch keine Bauratenmethodik gesetzt ist. Für andere
     * Abfragearten passiert nichts.
     * <p>
     * Die Abfrage wird hier nicht gespeichert, sondern gemeinsam mit dem neuen Status.
     *
     * @param abfrage die vorzubelegende Abfrage
     */
    private void setBauratenmethodikVorbelegungIfBauleitplanverfahren(final AbfrageModel abfrage) {
        if (abfrage instanceof BauleitplanverfahrenModel bauleitplanverfahren) {
            final var vorbelegung = this.bauratenmethodikDefaultingService.determineDefault(
                bauleitplanverfahren.getStart42Verfahren(),
                Boolean.TRUE.equals(bauleitplanverfahren.getStart42VerfahrenDatumUnbekannt())
            );
            bauleitplanverfahren.setBauratenmethodikVorbelegung(vorbelegung);
            CollectionUtils.emptyIfNull(bauleitplanverfahren.getAbfragevariantenBauleitplanverfahren())
                .stream()
                .map(AbfragevarianteBauleitplanverfahrenModel::getSobonBerechnung)
                .filter(sobonBerechnung -> sobonBerechnung != null && sobonBerechnung.getBauratenmethodik() == null)
                .forEach(sobonBerechnung -> sobonBerechnung.setBauratenmethodik(vorbelegung));
        }
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#ABBRUCH}.
     *
     * @param id        vom Typ {@link UUID} um die Abfrage zu finden
     * @param anmerkung falls ein Nutzer eine Anmerkung bei einem Statusübergang macht wird diese an die Abfrage Anmerkung angehängt
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     * @throws StringLengthExceededException    wenn die Anmerkung zur Statusänderung die max. Länge überschreitet
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    public void abbrechenAbfrage(final UUID id, String anmerkung)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.changeStatus(id, anmerkung, StatusAbfrageEvents.ABBRECHEN);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#ANGELEGT}.
     *
     * @param id        vom Typ {@link UUID} um die Abfrage zu finden
     * @param anmerkung falls ein Nutzer eine Anmerkung bei einem Statusübergang macht wird diese an die Abfrage Anmerkung angehängt
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     * @throws StringLengthExceededException    wenn die Anmerkung zur Statusänderung die max. Länge überschreitet
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    public void zurueckAnAbfrageerstellungAbfrage(final UUID id, String anmerkung)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.changeStatus(id, anmerkung, StatusAbfrageEvents.ZURUECK_AN_ABFRAGEERSTELLUNG);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#START_BEARBEITUNG}.
     *
     * @param id        vom Typ {@link UUID} um die Abfrage zu finden
     * @param anmerkung falls ein Nutzer eine Anmerkung bei einem Statusübergang macht wird diese an die Abfrage Anmerkung angehängt
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     * @throws StringLengthExceededException    wenn die Anmerkung zur Statusänderung die max. Länge überschreitet
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    public void zurueckAnSachbearbeitungAbfrage(final UUID id, String anmerkung)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.changeStatus(id, anmerkung, StatusAbfrageEvents.ZURUECK_AN_SACHBEARBEITUNG);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#ERLEDIGT_OHNE_FACHREFERAT}.
     *
     * @param id        vom Typ {@link UUID} um die Abfrage zu finden
     * @param anmerkung falls ein Nutzer eine Anmerkung bei einem Statusübergang macht wird diese an die Abfrage Anmerkung angehängt
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     * @throws StringLengthExceededException    wenn die Anmerkung zur Statusänderung die max. Länge überschreitet
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    public void erledigtOhneFachreferat(final UUID id, String anmerkung)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.changeStatus(id, anmerkung, StatusAbfrageEvents.KEINE_BEARBEITUNG_NOETIG);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#EINPFLEGEN_BEDARFSMELDUNG}.
     *
     * @param id        vom Typ {@link UUID} um die Abfrage zu finden
     * @param anmerkung falls ein Nutzer eine Anmerkung bei einem Statusübergang macht wird diese an die Abfrage Anmerkung angehängt
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     * @throws StringLengthExceededException    wenn die Anmerkung zur Statusänderung die max. Länge überschreitet
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    public void verschickenDerStellungnahme(final UUID id, String anmerkung)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.changeStatus(id, anmerkung, StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#EINPLANUNG_BEDARFE}.
     *
     * @param id        vom Typ {@link UUID} um die Abfrage zu finden
     * @param anmerkung falls ein Nutzer eine Anmerkung bei einem Statusübergang macht wird diese an die Abfrage Anmerkung angehängt
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     * @throws StringLengthExceededException    wenn die Anmerkung zur Statusänderung die max. Länge überschreitet
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    public void bedarfsmeldungErfolgt(final UUID id, String anmerkung)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.changeStatus(id, anmerkung, StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE);
    }

    /**
     * Ändert den Status auf {@link StatusAbfrage#ERLEDIGT_MIT_FACHREFERAT}.
     *
     * @param id        vom Typ {@link UUID} um die Abfrage zu finden
     * @param anmerkung falls ein Nutzer eine Anmerkung bei einem Statusübergang macht wird diese an die Abfrage Anmerkung angehängt
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     * @throws StringLengthExceededException    wenn die Anmerkung zur Statusänderung die max. Länge überschreitet
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    public void erledigtMitFachreferat(final UUID id, String anmerkung)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.changeStatus(id, anmerkung, StatusAbfrageEvents.SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG);
    }

    /**
     * Ändert den Status von {@link StatusAbfrage#ERLEDIGT_OHNE_FACHREFERAT}, {@link StatusAbfrage#ERLEDIGT_MIT_FACHREFERAT}
     * oder {@link StatusAbfrage#ABBRUCH} auf {@link StatusAbfrage#START_BEARBEITUNG}.
     *
     * @param id        vom Typ {@link UUID} um die Abfrage zu finden
     * @param anmerkung falls ein Nutzer eine Anmerkung bei einem Statusübergang macht wird diese an die Abfrage Anmerkung angehängt
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     * @throws StringLengthExceededException    wenn die Anmerkung zur Statusänderung die max. Länge überschreitet
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    public void erneuteBearbeitungSachbearbeitung(final UUID id, String anmerkung)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.changeStatus(id, anmerkung, StatusAbfrageEvents.ERNEUTE_BEARBEITUNG);
    }

    /**
     * Lädt die Abfrage einmalig und leitet mit dem übergebenem Event eine Statusänderung ein.
     *
     * @param id        vom Typ {@link UUID} um die Abfrage zu finden
     * @param anmerkung falls ein Nutzer eine Anmerkung bei einem Statusübergang macht wird diese an die Abfrage Anmerkung angehängt
     * @param event     vom Typ {@link StatusAbfrageEvents} um eine Statusänderung durchzuführen
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden wird
     * @throws AbfrageStatusNotAllowedException wenn die Statusänderung nicht erlaubt ist
     * @throws StringLengthExceededException    wenn die Anmerkung zur Statusänderung die max. Länge überschreitet
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    private void changeStatus(final UUID id, final String anmerkung, final StatusAbfrageEvents event)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.changeStatus(id, anmerkung, event, abfrage -> {});
    }

    /**
     * Wie {@link #changeStatus(UUID, String, StatusAbfrageEvents)}. Zusätzlich werden die Änderungen aus
     * {@code additionalChanges} an der Abfrage vorgenommen und gemeinsam mit dem neuen Status gespeichert.
     * Ist die Statusänderung nicht erlaubt, wird nichts gespeichert.
     */
    private void changeStatus(
        final UUID id,
        final String anmerkung,
        final StatusAbfrageEvents event,
        final Consumer<AbfrageModel> additionalChanges
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        final var abfrage = this.abfrageService.getById(id);
        this.throwStringLengthExceededExceptionWhenAnmerkungExceedsLength(abfrage, anmerkung);
        additionalChanges.accept(abfrage);
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.build(abfrage, anmerkung);
        this.sendEvent(event, stateMachine);
    }

    /**
     * Erstellt die Statemachine und initialisiert diese mit dem Status der übergebenen Abfrage.
     * <p>
     * Ausserdem wird ein preTransition-Listener definiert, der bei einer Statusänderung die Abfrage
     * mit dem neuen Status in der DB speichert.
     *
     * @param abfrage   die bereits geladene Abfrage
     * @param anmerkung welche bei einer Statusänderung an die Anmerkung der Abfrage angehängt wird
     */
    private StateMachine<StatusAbfrage, StatusAbfrageEvents> build(final AbfrageModel abfrage, final String anmerkung) {
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = this.stateMachineFactory.getStateMachine(
            abfrage.getId()
        );

        stateMachine.stopReactively().block();

        stateMachine.getStateMachineAccessor().doWithAllRegions(stateMachineAccess -> {
            // Setzt den Status der Abfrage in der StateMachine.
            stateMachineAccess
                .resetStateMachineReactively(
                    new DefaultStateMachineContext<>(abfrage.getStatusAbfrage(), null, null, null)
                )
                .block();

            // Der Interceptor speichert bei einer Statusänderung die Abfrage mit dem neuen Status.
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
                        try {
                            // Setzen des neuen Status
                            abfrage.setStatusAbfrage(stateContext.getTransition().getTarget().getId());
                            // Anfügen der Anmerkung
                            abfrage.setAnmerkung(appendAnmerkung(abfrage.getAnmerkung(), anmerkung));
                            // Erweitern der Bearbeitungshistorie
                            final var abfrageWithHistorie =
                                abfrageBearbeitungshistorieService.appendBearbeitungshistorieToAbfrage(abfrage);
                            // Speichern der Abfrage
                            AbfrageStatusService.this.abfrageService.save(abfrageWithHistorie);
                            // Asynchrones Versenden der Information für anstehende Aufgabe
                            sendWorkAssignmentInformationService.sendWorkAssignmentInformationAsync(
                                abfrageWithHistorie,
                                stateContext.getEvent()
                            );
                        } catch (final EntityNotFoundException | OptimisticLockingException exception) {
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
     *
     * @param event        vom Typ {@link StatusAbfrageEvents} um eine Statusänderung durchzuführen
     * @param stateMachine vom Typ {@link StateMachine} welche den aktuellen Status plus alle Status enthält
     * @throws AbfrageStatusNotAllowedException falls die Statusänderung nicht erlaubt nicht
     */
    private void sendEvent(
        final StatusAbfrageEvents event,
        final StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine
    ) throws AbfrageStatusNotAllowedException {
        final Mono<Message<StatusAbfrageEvents>> message = Mono.just(MessageBuilder.withPayload(event).build());
        final Flux<StateMachineEventResult<StatusAbfrage, StatusAbfrageEvents>> result = stateMachine.sendEvent(
            message
        );
        try {
            result.toStream().forEach(stateMachineEventResult -> {
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
     * Ermittelt mögliche Statusübergänge basierend auf Benutzer Authorities und aktuellen Status der Abfrage.
     * <p>
     * Hierzu wird nur der Status der Abfrage geladen und keine State Machine erstellt.
     *
     * @param id vom Typ {@link UUID} um die Abfrage zu finden
     * @return Liste von {@link TransitionModel} welche möglich sind
     * @throws EntityNotFoundException     falls die Abfrage nicht gefunden wird
     * @throws UserRoleNotAllowedException falls der User keine Berechtigung für die Abfrage hat.
     */
    public List<TransitionModel> getStatusAbfrageEventsBasedOnStateAndAuthorities(final UUID id)
        throws EntityNotFoundException, UserRoleNotAllowedException {
        final Set<StatusAbfrageEvents> eventsForAuthorities = getStatusAbfrageEventsForAuthorities(
            authenticationUtils.getUserAuthorities()
        );
        final StatusAbfrage statusAbfrage = this.abfrageService.getStatusAbfrageById(id);
        return eventsByStatus
            .getOrDefault(statusAbfrage, Set.of())
            .stream()
            .filter(eventsForAuthorities::contains)
            .map(event -> {
                TransitionModel transitionModel = new TransitionModel();
                transitionModel.setUrl(event.getUrl());
                transitionModel.setButtonName(event.getButtonName());
                transitionModel.setIndex(event.getIndex());
                transitionModel.setDialogText(event.getDialogText());
                return transitionModel;
            })
            .sorted(Comparator.comparingInt(TransitionModel::getIndex))
            .collect(Collectors.toList());
    }

    /**
     * Ermittelt anhand der im Security Context vorhandenen Authorities, welche {@link StatusAbfrageEvents} erlaubt sind.
     *
     * @param authorities Die Liste der Authorities des Benutzers.
     * @return Die erlaubten {@link StatusAbfrageEvents} basierend auf den Authorities des Benutzers.
     */
    private static Set<StatusAbfrageEvents> getStatusAbfrageEventsForAuthorities(
        final List<AuthoritiesEnum> authorities
    ) {
        return authorities
            .stream()
            .map(EVENT_BY_AUTHORITY::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(() -> EnumSet.noneOf(StatusAbfrageEvents.class)));
    }

    /**
     * Ermittelt aus den konfigurierten Statusübergängen der State Machine je Status die möglichen {@link StatusAbfrageEvents}.
     *
     * @param stateMachineFactory zum Erstellen einer State Machine mit den konfigurierten Statusübergängen
     * @return die je Status möglichen {@link StatusAbfrageEvents}
     */
    private static Map<StatusAbfrage, Set<StatusAbfrageEvents>> getEventsByStatus(
        final StateMachineFactory<StatusAbfrage, StatusAbfrageEvents> stateMachineFactory
    ) {
        return stateMachineFactory
            .getStateMachine()
            .getTransitions()
            .stream()
            .collect(
                Collectors.groupingBy(
                    transition -> transition.getSource().getId(),
                    () -> new EnumMap<>(StatusAbfrage.class),
                    Collectors.mapping(
                        transition -> transition.getTrigger().getEvent(),
                        Collectors.toCollection(() -> EnumSet.noneOf(StatusAbfrageEvents.class))
                    )
                )
            );
    }

    /**
     * Überprüft, ob die Anmerkung der Abfrage nach dem Anhängen der neuen Anmerkung die maximale Länge überschreitet.
     *
     * @param abfrage   an deren Anmerkung die neue Anmerkung angehängt werden soll.
     * @param anmerkung die zur Abfrage hinzugefügt werden soll.
     * @throws StringLengthExceededException Wenn die Anmerkung die maximale Länge von 2000 Zeichen überschreitet.
     */
    private void throwStringLengthExceededExceptionWhenAnmerkungExceedsLength(
        final AbfrageModel abfrage,
        final String anmerkung
    ) throws StringLengthExceededException {
        if (
            StringUtils.isNotEmpty(anmerkung) &&
            appendAnmerkung(abfrage.getAnmerkung(), anmerkung).length() > MAX_LENGTH_ANMERKUNG
        ) {
            throw new StringLengthExceededException("Es sind maximal " + MAX_LENGTH_ANMERKUNG + " Zeichen erlaubt");
        }
    }

    /**
     * Hängt die neue Anmerkung durch einen Zeilenumbruch getrennt an die bestehende Anmerkung an.
     *
     * @param bestehendeAnmerkung die bisherige Anmerkung der Abfrage. Kann null sein.
     * @param anmerkung           die neue Anmerkung. Kann null oder leer sein.
     * @return die zusammengesetzte Anmerkung
     */
    private static String appendAnmerkung(final String bestehendeAnmerkung, final String anmerkung) {
        if (StringUtils.isEmpty(anmerkung)) {
            return bestehendeAnmerkung;
        }
        return bestehendeAnmerkung == null ? anmerkung : bestehendeAnmerkung.concat("\n").concat(anmerkung);
    }
}

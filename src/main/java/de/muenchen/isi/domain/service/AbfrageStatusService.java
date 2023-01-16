package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageStatusService {

    private final AbfrageService abfrageService;
    private final StateMachineFactory<StatusAbfrage, StatusAbfrageEvents> stateMachineFactory;

    /**
     * Gibt ein {@link InfrastrukturabfrageModel} identifiziert durch die ID frei.
     *
     * @param id zur Identifzierung des {@link InfrastrukturabfrageModel}s
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException falls die Abfrage nicht freigegeben werden kann.
     */
    public void freigabeInfrastrukturabfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final InfrastrukturabfrageModel abfrage = this.abfrageService
                .getInfrastrukturabfrageById(id);
        abfrage.setAbfrage(this.freigabeAbfrage(abfrage));
        this.abfrageService.updateInfrastrukturabfrage(abfrage);

    }

    /**
     * Gibt das {@link AbfrageModel} im Parameter frei falls möglich.
     *
     * @param abfrage vom Typ {@link AbfrageModel} zur Freigabe.
     * @return das freigegebene {@link AbfrageModel}.
     * @throws AbfrageStatusNotAllowedException falls die Abfrage nicht freigegeben werden kann.
     */
    protected AbfrageModel freigabeAbfrage(final InfrastrukturabfrageModel abfrage) throws AbfrageStatusNotAllowedException {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(abfrage);
        stateMachine.sendEvent(StatusAbfrageEvents.FREIGABE);

        abfrage.getAbfrage().setStatusAbfrage(stateMachine.getState().getId());

        return abfrage.getAbfrage();
    }


    /**
     *
     * @param abfrage
     * @return
     */
    protected AbfrageModel abbrechenAbfrage(final InfrastrukturabfrageModel abfrage) {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(abfrage);
        stateMachine.sendEvent(StatusAbfrageEvents.ABBRECHEN);

        abfrage.getAbfrage().setStatusAbfrage(stateMachine.getState().getId());

        return abfrage.getAbfrage();
    }

    /**
     *
     * @param abfrage
     * @return
     */
    protected AbfrageModel angabenAnpassenAbfrage(final InfrastrukturabfrageModel abfrage) {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(abfrage);
        stateMachine.sendEvent(StatusAbfrageEvents.ANGABEN_ANPASSEN);

        abfrage.getAbfrage().setStatusAbfrage(stateMachine.getState().getId());

        return abfrage.getAbfrage();
    }

    /**
     *
     * @param abfrage
     * @return
     */
    protected AbfrageModel weitereAbfragevariantenAnlegen(final InfrastrukturabfrageModel abfrage) {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(abfrage);
        stateMachine.sendEvent(StatusAbfrageEvents.WEITERE_ABFRAVARIANTEN_ANLEGEN);

        abfrage.getAbfrage().setStatusAbfrage(stateMachine.getState().getId());

        return abfrage.getAbfrage();
    }

    /**
     *
     * @param abfrage
     * @return
     */
    protected AbfrageModel keineZusätzlicheAbfragevariante(final InfrastrukturabfrageModel abfrage) {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(abfrage);
        stateMachine.sendEvent(StatusAbfrageEvents.KEINE_ZUSAEZTLICHE_ABFRAGEVARIANTE);

        abfrage.getAbfrage().setStatusAbfrage(stateMachine.getState().getId());

        return abfrage.getAbfrage();
    }

    /**
     *
     * @param abfrage
     * @return
     */
    protected AbfrageModel zusaetzlicheAbfragevarianteAnlegen(final InfrastrukturabfrageModel abfrage) {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(abfrage);
        stateMachine.sendEvent(StatusAbfrageEvents.ZUSAETZLICHE_ABFRAGEVARIANTE);

        abfrage.getAbfrage().setStatusAbfrage(stateMachine.getState().getId());

        return abfrage.getAbfrage();
    }

    /**
     *
     * @param abfrage
     * @return
     */
    protected AbfrageModel speichernDerVarianten(final InfrastrukturabfrageModel abfrage) {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(abfrage);
        stateMachine.sendEvent(StatusAbfrageEvents.SPEICHERN_DER_VARIANTEN);

        abfrage.getAbfrage().setStatusAbfrage(stateMachine.getState().getId());

        return abfrage.getAbfrage();
    }

    /**
     *
     * @param abfrage
     * @return
     */
    protected AbfrageModel keineBearbeitungNötig(final InfrastrukturabfrageModel abfrage) {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(abfrage);
        stateMachine.sendEvent(StatusAbfrageEvents.KEINE_BEARBEITUNG_NÖTIG);

        abfrage.getAbfrage().setStatusAbfrage(stateMachine.getState().getId());

        return abfrage.getAbfrage();
    }

    /**
     *
     * @param abfrage
     * @return
     */
    protected AbfrageModel verschickenDerStellungnahme(final InfrastrukturabfrageModel abfrage) {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(abfrage);
        stateMachine.sendEvent(StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME);

        abfrage.getAbfrage().setStatusAbfrage(stateMachine.getState().getId());

        return abfrage.getAbfrage();
    }

    /**
     *
     * @param abfrage
     * @return
     */
    protected AbfrageModel bedarfsmeldungErfolgt(final InfrastrukturabfrageModel abfrage) {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(abfrage);
        stateMachine.sendEvent(StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE);

        abfrage.getAbfrage().setStatusAbfrage(stateMachine.getState().getId());

        return abfrage.getAbfrage();
    }

    /**
     *
     * @param abfrage
     * @return
     */
    protected AbfrageModel speichernVonSozialinfrastrukturVersorgung(final InfrastrukturabfrageModel abfrage) {
        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = build(abfrage);
        stateMachine.sendEvent(StatusAbfrageEvents.SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG);

        abfrage.getAbfrage().setStatusAbfrage(stateMachine.getState().getId());

        return abfrage.getAbfrage();
    }


    /**
     * Setzt den Status der Abfrage aus der Datenbank in der {@link StateMachine}
     *
     * @param abfrage vom Typ {@link InfrastrukturabfrageModel} mit ihrem aktuellen Status.
     * @return die {@link StateMachine} mit initialiertem Status.
     */
    private StateMachine<StatusAbfrage, StatusAbfrageEvents> build (final InfrastrukturabfrageModel abfrage) {

        StateMachine<StatusAbfrage, StatusAbfrageEvents> stateMachine = stateMachineFactory.getStateMachine(abfrage.getId());

        stateMachine.stopReactively().block();

        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(sma ->
                        sma.resetStateMachineReactively(new DefaultStateMachineContext<>(abfrage.getAbfrage().getStatusAbfrage(), null, null, null)));

        stateMachine.startReactively().block();

        return stateMachine;
    }
}

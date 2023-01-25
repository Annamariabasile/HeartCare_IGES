package c15.dev.gestioneUtente.service;

import c15.dev.model.dto.ModificaPazienteDTO;
import c15.dev.model.dto.UtenteRegistratoDTO;
import c15.dev.model.entity.Admin;
import c15.dev.model.entity.Medico;
import c15.dev.model.entity.Paziente;
import c15.dev.model.entity.UtenteRegistrato;
import c15.dev.utils.AuthenticationRequest;
import c15.dev.utils.AuthenticationResponse;

import java.util.List;
import java.util.Optional;

/**
 * @author Leopoldo Todisco, Carlo Venditto.
 * Creato il 03/01/2023.
 * Interfaccia Service per Gestione Utente.
 * Questa interfaccia è quella che espone i metodi che saranno usati
 * dal controller del package, ma anche da altri controller e services.
 */
public interface GestioneUtenteService {
    /**
     * Firma del metodo di login.
     *
     * @return UtenteRegistrato loggato
     */
    public AuthenticationResponse login(AuthenticationRequest request);

    /**
     * Firma del metodo di assegnaCaregiver.
     *
     * @param idPaziente       del paziente a cui si vuole assegnare il caregiver
     * @param emailCaregiver   email del caregiver
     * @param nomeCaregiver    nome del caregiver
     * @param cognomeCaregiver nome del caregiver
     */
    void assegnaCaregiver(Long idPaziente,
                          String emailCaregiver,
                          String nomeCaregiver,
                          String cognomeCaregiver);

    /**
     * Firma del metodo rimuoviPaziente.
     *
     * @param idUtente id del paziente che deve essere rimosso
     */
    void rimuoviPaziente(Long idUtente);

    /**
     * Firma del metodo rimuoviMedico.
     *
     * @param idUtente id del medico che deve essere rimosso
     */
    void rimuoviMedico(Long idUtente);

    /**
     * Firma del metodo isPaziente.
     *
     * @param idUtente id dell'utente che vogliamo controllare che sia un paziente
     * @return
     */
    boolean isPaziente(long idUtente);

    /**
     * Firma del metodo isMedico.
     *
     * @param idUtente id dell'utente che vogliamo controllare che sia un medico
     * @return
     */
    boolean isMedico(long idUtente);

    /**
     * Firma del metodo isAdmin
     *
     * @param idUtente id dell'utente che vogliamo controllare che sia un admin
     * @return
     */
    boolean isAdmin(long idUtente);

    /**
     * Firma del metodo assegnaPaziente.
     *
     * @param idMedico   id del medico a cui assegnamo il paziente
     * @param idPaziente id del paziente che viene assegnato al medico
     * @return
     */
    boolean assegnaPaziente(long idMedico, long idPaziente);

    /**
     * Firma del metodo findPazienteById
     *
     * @param id id del paziente da ricercare
     * @return
     */
    Paziente findPazienteById(Long id);

    /**
     * Firma del metodo findMedicoById
     *
     * @param id id del medico da ricercare
     * @return
     */
    Medico findMedicoById(Long id);

    boolean findUtenteByCf(String cf);

    boolean findUtenteByEmail(String email);

    /**
     * Firma del metodo updatePaziente
     *
     * @param paz paziente da aggiornare
     */
    void updatePaziente(Paziente paz);

    /**
     * Firma del metodo updateMedico
     *
     * @param med medico da aggiornare
     */
    void updateMedico(Medico med);

    /**
     * Firma del metodo getTuttiMedici
     */
    List<UtenteRegistrato> getTuttiMedici();

    /**
     * Firma del metodo getTuttiPazienti
     */
    List<UtenteRegistrato> getTuttiPazienti();

    /**
     * Firma del metodo getPazientiByMedico
     */
    List<Paziente> getPazientiByMedico(long idMedico);

    /**
     * Firma del metodo modificaDatiPaziente
     * @param pazienteDTO
     * @param idUtente
     */
    void modificaDatiPaziente(ModificaPazienteDTO pazienteDTO, long idUtente) throws Exception;
    void modificaDatiMedico(UtenteRegistratoDTO dto, long idUtente) throws Exception;

    public UtenteRegistrato findUtenteById(Long id);

    public UtenteRegistrato findUtenteByEmail(String email);

}

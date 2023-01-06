package c15.dev.gestioneUtente.controller;

import c15.dev.gestioneUtente.service.GestioneUtenteService;
import c15.dev.model.dto.ModificaPazienteDTO;
import c15.dev.model.entity.Medico;
import c15.dev.model.entity.Paziente;
import c15.dev.model.entity.UtenteRegistrato;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Leopoldo Todisco, Carlo Venditto.
 * Crato il: 03/01/2023.
 * Classe controller.
 */
@RestController
@SessionAttributes("utenteLoggato")
public class GestioneUtenteController {
    /**
     * Service per le operazioni di accesso
     */
    @Autowired
    private GestioneUtenteService service;
    /**
     * Sessione
     */
    @Autowired
    private HttpSession session;

    /**
     * Metodo di login.
     *
     * @param body è il body della richiesta.
     *             Al suo interno vi si trovano i valori di password ed email.
     */
    @PostMapping(value = "/login")
    public void login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        System.out.println(email + password);

        Optional<UtenteRegistrato> utente = service.login(email, password);
        utente.ifPresent(
                utenteRegistrato ->
                {
                    session.setAttribute("utenteLoggato", utente.get());
                });
    }

    /**
     * Metodo di logout.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout() {
        System.out.println(((UtenteRegistrato) session.getAttribute("utenteLoggato")).getNome());
        session.invalidate();
    }

    /**
     * Metodo per assegnare un caregiver.
     *
     * @param idPaziente
     * @param emailCaregiver
     * @param nomeCaregiver
     * @param cognomeCaregiver
     */
    @RequestMapping(value = "/assegnaCaregiver", method = RequestMethod.POST)
    public void assegnaCaregiver(@RequestParam Long idPaziente,
                                 @RequestParam String emailCaregiver,
                                 @RequestParam String nomeCaregiver,
                                 @RequestParam String cognomeCaregiver) {
        if (service.isPaziente(idPaziente)) {
            service.assegnaCaregiver(idPaziente,
                    emailCaregiver,
                    nomeCaregiver,
                    cognomeCaregiver);
        }
    }

    /**
     * Metodo per rimuovere un Paziente o un Medico.
     *
     * @param idUtente
     */
    @RequestMapping(value = "/rimuoviUtente", method = RequestMethod.POST)
    public boolean rimuoviUtente(@RequestParam Long idUtente) {
        if (service.isPaziente(idUtente)) {
            service.rimuoviPaziente(idUtente);
            return true;
        } else if (service.isMedico(idUtente)) {
            service.rimuoviPaziente(idUtente);
            return true;
        }
        return false;
    }

    /**
     * Metodo che assegna un paziente a un medico.
     *
     * @param idMedico
     * @param idPaziente
     */
    @RequestMapping(value = "/assegnaPaziente", method = RequestMethod.POST)
    public void assegnaPaziente(@RequestParam long idMedico,
                                @RequestParam long idPaziente) {
        if (service.isMedico(idMedico) && service.isPaziente(idPaziente)) {
            service.assegnaPaziente(idMedico, idPaziente);
        }
    }

    /**
     * Metodo che restituisce tutti i medici
     */
    @RequestMapping(value = "/getTuttiMedici", method = RequestMethod.POST)
    public List<UtenteRegistrato> getTuttiMedici() {
        UtenteRegistrato u = (UtenteRegistrato)
                session.getAttribute("utenteLoggato");
        if (service.isAdmin(u.getId())) {
            return service.getTuttiMedici()
                    .stream()
                    .filter((utente)
                            -> utente.getClass()
                            .getSimpleName()
                            .equals("Medico"))
                    .toList();
        }
        return null;
    }

    /**
     * Metodo che restituisce tutti i pazienti
     */
    @RequestMapping(value = "/getTuttiPazienti", method = RequestMethod.POST)
    public List<UtenteRegistrato> getTuttiPazienti() {
        UtenteRegistrato u = (UtenteRegistrato)
                session.getAttribute("utenteLoggato");
        if (service.isAdmin(u.getId())) {
            return service.getTuttiPazienti()
                    .stream()
                    .filter((utente)
                            -> utente.getClass()
                            .getSimpleName()
                            .equals("Paziente"))
                    .toList();
        }
        return null;
    }

    /**
     * Metodo che restituisce tutti i pazienti
     *
     * @param idMedico id del medico
     */
    @RequestMapping(value = "/getPazientiByMedico", method = RequestMethod.POST)
    public List<Paziente> getPazientiByMedico(@RequestParam long idMedico) {

        return service.getPazientiByMedico(idMedico);
    }


    /**
     * Metodo per modificare i dati di un utente.
     * @param pazienteDTO
     * @return
     */
    //TODO usare optional per vedere solo quali campi modificare
    @PostMapping("/modificaDatiUtente")
    public boolean modificaDatiPaziente(@Valid @RequestBody
                                           ModificaPazienteDTO pazienteDTO) {
        UtenteRegistrato utente = (UtenteRegistrato)
                session.getAttribute("utenteLoggato");

        long id = utente.getId();

        if (service.isPaziente(id)) {
            if (Arrays.equals(pazienteDTO.getConfermaPassword(), utente.getPassword())) {
                service.modificaDatiPaziente(pazienteDTO, id);
                return true;
            } else if (service.isMedico(id) || service.isAdmin(id)) {

                //TODO da modificare con generics per permettere ad un medico ad un utente di essere modificati
                if(Arrays.equals(pazienteDTO.getConfermaPassword(), utente.getPassword())) {
                    service.modificaDatiPaziente(pazienteDTO, id);
                    return true;
                }
            }

        }

        return false;
    }

    /**
     * @author Leopoldo Todisco.
     * Metodo che permette di ottenere i dati relativi a un utente qualsiasi.
     * @param idUtente
     * @return ResponseEntity è la response che sarà fetchata dal frontend.
     * Essa comprende una Map con i dati utente e lo stato della rispostama.
     */
    @PostMapping("/utente/{id}")
    public ResponseEntity<Object>
        getDatiProfiloUtente(@PathVariable("id") final Long idUtente){
        HashMap<String, Object> map = new HashMap<>();

        if(service.isPaziente(idUtente)){
            Paziente paziente = service.findPazienteById(idUtente);
            map.put("nome", paziente.getNome());
            map.put("cognome", paziente.getCognome());
            map.put("email", paziente.getEmail());
            map.put("nTelefono", paziente.getNumeroTelefono());
            map.put("emailCaregiver", paziente.getEmailCaregiver());
            map.put("nomeCaregiver", paziente.getNomeCaregiver());
            map.put("cognomeCaregiver", paziente.getCognomeCaregiver());
        }

        else if(service.isMedico(idUtente) || service.isAdmin(idUtente)){
            Medico medico = service.findMedicoById(idUtente);
            map.put("nome", medico.getNome());
            map.put("cognome", medico.getCognome());
            map.put("email", medico.getEmail());
            map.put("nTelefono", medico.getNumeroTelefono());
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}


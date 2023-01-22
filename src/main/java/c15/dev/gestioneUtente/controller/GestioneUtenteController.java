package c15.dev.gestioneUtente.controller;

import c15.dev.gestioneUtente.service.GestioneUtenteService;
import c15.dev.model.dto.ModificaPazienteDTO;
import c15.dev.model.dto.UtenteRegistratoDTO;
import c15.dev.model.entity.Medico;
import c15.dev.model.entity.Paziente;
import c15.dev.model.entity.UtenteRegistrato;
import c15.dev.model.entity.enumeration.StatoNotifica;
import c15.dev.model.entity.enumeration.StatoVisita;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import java.util.GregorianCalendar;
import java.util.Arrays;

/**
 * @author Leopoldo Todisco, Carlo Venditto.
 * Crato il: 03/01/2023.
 * Classe controller.
 */
@RestController
@CrossOrigin
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
    public ResponseEntity<Object>
                login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Optional<UtenteRegistrato> utente = service.login(email, password);
        UtenteRegistrato user;
        HashMap<String, Object> userInfo = new HashMap<>();

        if(utente.isPresent()) {
            user = utente.get();
            session.setAttribute("utenteLoggato", user.getId());
            userInfo.put("nome", user.getNome());
            userInfo.put("id", user.getId());
            userInfo.put("genere", user.getGenere());
            String ruolo = user.getClass().getSimpleName();
            userInfo.put("ruolo", ruolo);
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Metodo di logout.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout() {
        System.out.println(((UtenteRegistrato) session.getAttribute("utenteLoggato")));
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
     * Metodo che restituisce tutti i medici.
     * Invariante: il metodo può essere chiamato solo da admin.
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
     * Metodo che restituisce tutti i pazienti.
     * Invariante: il metodo può essere chiamato solo da admin.
     */
    @RequestMapping(value = "/getTuttiPazienti", method = RequestMethod.POST)
    public List<UtenteRegistrato> getTuttiPazienti() {
        UtenteRegistrato u = (UtenteRegistrato)
                session.getAttribute("utenteLoggato");
        return service.getTuttiPazienti()
                .stream()
                .filter((utente)
                        -> utente.getClass()
                        .getSimpleName()
                        .equals("Paziente"))
                .toList();
    }

    /**
     * Metodo che restituisce tutti i pazienti di un medico.
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
       /* UtenteRegistrato utente = (UtenteRegistrato)
                session.getAttribute("utenteLoggato");*/

        long id = 1L;//utente.getId();
        UtenteRegistrato utente = service.findUtenteById(id);
        if (service.isPaziente(id)) {
            if (Arrays.equals(pazienteDTO.getConfermaPassword(), utente.getPassword())) {
                service.modificaDatiPaziente(pazienteDTO, id);
                return true;
            }

        }
        return false;
    }

    //TODO usare generics se possibile

    /**
     * Metodo per modificare i dati di un medico.
     * @param pazienteDTO
     * @return
     */
    @PostMapping("/modificaDatiMedico")
    public boolean modificaDatiPaziente(@Valid @RequestBody
                                        UtenteRegistratoDTO pazienteDTO) {
       /* UtenteRegistrato utente = (UtenteRegistrato)
                session.getAttribute("utenteLoggato");*/

        long id = 4L;//utente.getId();
        UtenteRegistrato utente = service.findUtenteById(id);
        if (service.isMedico(id)) {
            if (Arrays.equals(pazienteDTO.getConfermaPassword(), utente.getPassword())) {
                service.modificaDatiMedico(pazienteDTO, id);
                return true;
            }

        }
        return false;
    }


    /**
     * @author Leopoldo Todisco.
     * Metodo che permette di ottenere i dati relativi a un utente qualsiasi.
     * @param idUtente
     * @return ResponseEntity è la response che sarà fetchata dal frontend.
     * Essa comprende una Map con i dati utente e lo stato della risposta.
     */
    @PostMapping("/utente")
    public ResponseEntity<Object>
        getDatiProfiloUtente(@RequestBody final Long idUtente){
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

    /**
     * @author Paolo Carmine Valletta.
     * Metodo che permette di visualizzare la home di un Medico o Paziente.
     * @param idUtente
     * @return ResponseEntity è la response che sarà fetchata dal frontend.
     * Essa comprende una Map con i dati della home e lo stato della risposta.
     */
    @PostMapping("/Home/{id}")
    public ResponseEntity<Object>
        visualizzazioneHomeUtente(@PathVariable("id") final long idUtente) {
        HashMap<String, Object> map = new HashMap<>();

        if(service.isPaziente(idUtente)){
            Paziente paz = service.findPazienteById(idUtente);
            map.put("numeroMisurazioni", paz.getMisurazione().size());
            map.put("appuntamentiInProgramma", paz.getElencoVisite()
                    .stream()
                    .filter(visita -> visita.getStatoVisita()
                            .equals(StatoVisita.PROGRAMMATA))
                    .toList().size());
            map.put("nuoveNote", paz.getNote()
                    .stream()
                    .filter(nota -> nota.getStatoNota()
                            .equals(StatoNotifica.NON_LETTA))
                    .toList().size());
            map.put("listaNote", paz.getNote());
            map.put("listaVisite", paz.getElencoVisite());
            map.put("listaMisurazioni", paz.getMisurazione());
        } else if (service.isMedico(idUtente)) {
            Medico med = service.findMedicoById(idUtente);
            map.put("pazientiTotali", med.getElencoPazienti());
            map.put("appuntamentiInProgramma", med.getElencoVisite()
                    .stream()
                    .filter(visita -> visita.getStatoVisita()
                            .equals(StatoVisita.PROGRAMMATA))
                    .toList().size());
            map.put("nuoveNote", med.getNote()
                    .stream()
                    .filter(note -> note.getStatoNota()
                            .equals(StatoNotifica.NON_LETTA))
                    .toList().size());
            map.put("listaNote", med.getNote());
            map.put("visiteInProgrammaOggi", med.getElencoVisite()
                    .stream()
                    .filter(visita -> visita.getData()
                            .equals(new GregorianCalendar()))
                    .toList());
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping(value = "/getByCodice")
    public ResponseEntity<Object> getByCodice(@RequestBody String codiceFiscale){
        HashMap<String, Object> map = new HashMap<>();
        System.out.println(codiceFiscale);

        System.out.println(service.findUtenteByCf(codiceFiscale));
        map.put("codiceFiscale", service.findUtenteByCf(codiceFiscale));

        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @PostMapping(value = "/getByEmail")
    public ResponseEntity<Object> getByEmail(@RequestBody String email){
        HashMap<String, Object> map = new HashMap<>();
        System.out.println(email);

        System.out.println(service.findUtenteByEmail(email));
        map.put("email", service.findUtenteByEmail(email));

        return new ResponseEntity<>(map,HttpStatus.OK);
    }


}



package c15.dev.gestioneUtente.service;

import c15.dev.model.dao.*;
import c15.dev.model.entity.*;

import c15.dev.utils.JwtService;

import c15.dev.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Carlo.
 *  Creato il : 03/01/2023.
 * Questa classe rappresenta il Service utilizzato per la gestione utenti.
 */
@Service
@RequiredArgsConstructor
public class GestioneUtenteServiceImpl implements GestioneUtenteService {
    /**
     * Service per le operazioni che riguardano Jwt.
     */
    @Autowired
    private final JwtService jwtService;
    /**
     * provvede alle operazioni legate all'autenticazione.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Provvede ad accedere al db per il dispositivo medico.
     */
    @Autowired
    private DispositivoMedicoDAO daoM;

    /**
     * Provvede ad accedere al db per il paziente.
     */
    @Autowired
    private PazienteDAO paziente;
    /**
     * Provvede ad accedere al db per l'admin.
     */
    @Autowired
    private AdminDAO admin;
    /**
     * Provvede ad accedere al db per il medico.
     */
    @Autowired
    private MedicoDAO medico;

    /**
     * Provvede ad accedere al db per l'indirizzo.
     */
    @Autowired
    private IndirizzoDAO indirizzo;

    @Autowired
    private CaregiverDAO caregiver;

    /**
     * Provvede ad accedere al db per l'utente.
     */
    @Qualifier("utenteRegistratoDAO")
    @Autowired
    private UtenteRegistratoDAO utente;


    /**
     * Oggetto che provvede alla criptazione della password.
     */
    @Autowired
    private PasswordEncoder pwdEncoder;



    /**
     * pre L'utente deve essere un pazienete.
     * Metodo che assegna un caregiver a un paziente.
     * @param idPaziente del paziente a cui si vuole assegnare il caregiver.
     * post Viene assegnato il caregiver.
     */
    @Override
    public boolean assegnaCaregiver(final Long idPaziente, final Long idCaregiver) {
        Optional<UtenteRegistrato> pz =  paziente.findById(idPaziente);
        if (pz.isEmpty()) {
            return false;
        }
        Paziente tmp = (Paziente) pz.get();
        tmp.setCaregiver((Caregiver) caregiver.findById(idCaregiver).get());
        paziente.save(tmp);
        return true;
    }



    /**
     * pre idUtente != null,
     * ind != null
     * Metodo che assegna indirizzo ad utente.
     * @param idUtente id utente.
     * @param ind indirizzo da assegnare.
     * @return true o false.
     * post L'indirizzo viene assegnato correttamente.
     */
    @Override
    public boolean assegnaIndirizzoAdUtente(final long idUtente,
                                            final Indirizzo ind) {
        Optional<UtenteRegistrato> user = utente.findById(idUtente);
        if (user.isEmpty()) {
            return false;
        }

        user.get().setIndirizzoResidenza(ind);
        utente.save(user.get());
        return true;
    }

    /**
     * pre idMedico != null e idPaziente != null.
     * Metodo che assegna medico a paziente.
     * @param idMedico id medico.
     * @param idPaziente id paziente.
     * @return true o false.
     * post Il medico viene assegnato correttamente.
     */
    @Override
    public boolean assegnaMedicoAPaziente(final long idMedico,
                                          final long idPaziente) {
        Medico med = findMedicoById(idMedico);
        Paziente paz = findPazienteById(idPaziente);

        paz.setMedico(med);
        paziente.saveAndFlush(paz);

        return true;
    }

    /**
     * pre idPaziente != null.
     * Metodo che trova tutti i dispositivi di un paziente.
     * @param idPaziente id paziente.
     * @return insieme dispositivi medici.
     * post hashSet != null.
     */
    @Override
    public Set<DispositivoMedico>
    getDispositiviByPaziente(final long idPaziente) {
        //Paziente pz = this.findPazienteById(idPaziente);
        Set<DispositivoMedico> res = new HashSet<>();
        res.addAll(daoM.findByPaziente(idPaziente));


        res.forEach(s -> System.out.println(s.getId()));
        return res;
    }

    /**
     * pre L'id deve corrispondere ad un medico.
     * Metodo che elimina un medico.
     * @param idUtente id dell'utente.
     * post Il medico viene eliminato.
     */
    @Override
    public void rimuoviMedico(final Long idUtente) {
        Optional<UtenteRegistrato> u = medico.findById(idUtente);
        medico.delete(u.get());
    }


    @Override
    public void rimuoviCaregiver(final Long idCaregiver) {
        Optional<UtenteRegistrato> u = caregiver.findById(idCaregiver);
        caregiver.delete(u.get());
    }



    /**pre Il paziente deve esistere.
     * Metodo che verifica se un utente è un paziente.
     * @param idUtente id dell'utente.
     * @return true o false.
     * post
     */
    @Override
    public boolean isPaziente(final long idUtente) {
        Optional<UtenteRegistrato> u = paziente.findById(idUtente);

        if (u.isEmpty()) {
            return false;
        } else if (u.get().getClass().getSimpleName().equals("Paziente")) {
            return true;
        }

        return false;
    }

    /**pre Il medico  deve esistere.
     * Metodo che verifica se un utente è un medico.
     * @param idUtente id dell'utente.
     * @return true o false.
     */
    @Override
    public boolean isMedico(final long idUtente) {
        Optional<UtenteRegistrato> u = medico.findById(idUtente);

        if (u.isEmpty()) {
            return false;
        } else if (u.get().getClass().getSimpleName().equals("Medico")) {
            return true;
        }
        return false;
    }

    /**
     * pre L'admin deve esistere.
     * Implementazione del metodo che verifica se un utente è un admin.
     * @param idUtente id dell'utente che vogliamo controllare sia un admin.
     * @return true o false.
     */
    @Override
    public boolean isAdmin(final long idUtente) {
        Optional<UtenteRegistrato> u = admin.findById(idUtente);

        if (u.isEmpty()) {
            return false;
        } else if (u.get().getClass().getSimpleName().equals("Admin")) {
            return true;
        }
        return false;
    }

    /**
     * pre idMedico != null e idPaziente != null.
     * Metodo che assegna un paziente ad un medico.
     * @param idMedico id del medico.
     * @param idPaziente id del paziente.
     * @return true o false.
     * post Il paziente viene assegnato correttamente.
     */
    @Override
    public boolean assegnaPaziente(final long idMedico,
                                   final long idPaziente) {
        Optional<UtenteRegistrato> med = medico.findById(idMedico);
        if (med.isEmpty()) {
            return false;
        }
        Optional<UtenteRegistrato> paz = paziente.findById(idPaziente);
        if(paz.isEmpty()) {
            return false;
        }
        Medico m = (Medico) med.get();
        Paziente pz = (Paziente) paz.get();
        m.getElencoPazienti().add(pz);
        pz.setMedico(m);
        paziente.save(pz);
        return true;
    }

    /**
     * pre id != null.
     * Metodo che trova un Paziente tramite id.
     * @param id id del paziente.
     * @return Paziente.
     * post Il paziente viene trovato.
     */
    @Override
    public Paziente findPazienteById(final Long id) {
        Optional<UtenteRegistrato> paz = paziente.findById(id);
        if (paz.isEmpty()) {
            return null;
        }

        return (Paziente) paz.get();
    }

    /**
     * pre id != null.
     * Metodo che trova Medico tramite id.
     * @param id id del medico.
     * @return Medico.
     * post Il medico viene trovato.
     */
    @Override
    public Medico findMedicoById(final Long id) {
        Optional<UtenteRegistrato> paz = medico.findById(id);
        if (paz.isEmpty()) {
            return null;
        }

        return (Medico) paz.get();
    }


   /* @Override
    public boolean findMedicoByCf(final String codiceFiscale) {
        Medico u = medico.findBycodiceFiscale(codiceFiscale);

        if (u == null) {
            return false;
        }
        return true;
    } */
    /**
     * pre codiceFiscale != null
     * Metodo che restituisce un paziente tramite il codice fiscale.
     * @param codiceFiscale codice fiscale utente.
     * @return true o false.
     * post Il paziente viene trovato.
     */
    @Override
    public boolean findUtenteByCf(final String codiceFiscale) {
            Paziente u = paziente.findBycodiceFiscale(codiceFiscale);

        if (u == null) {
            return false;
        }
        return true;
    }
    /**
     * pre: email non deve essere null.
     * Metodo che restituisce un paziente tramite la sua email.
     * @param email email del paziente.
     * @return true o false.
     * post: email del paziente esiste.
     */
    @Override
    public boolean checkByEmail(final String email) {
        Paziente u = paziente.findByEmail(email);
        if (u == null) {
            return false;
        }
        return true;
    }
    /**
     * pre: ind non deve essere null.
     * Metodo per registrare indirizzo nel DB.
     * @param ind è l'indirizzo da aggiungere.
     * @return true o false.
     * post: ind viene salvato nel db.
     */
    @Override
    public boolean registraIndirizzo(final Indirizzo ind) {
        indirizzo.save(ind);
        return true;
    }

    /**
     * pre: id non deve essere null.
     * Metodo che restituisce un utente tramite il suo id.
     * @param id id dell'utente.
     * @return UtenteRegistrato.
     * post: utenteRegistrato viene trovato.
     */
    @Override
    public UtenteRegistrato findUtenteById(final Long id) {
        Optional<UtenteRegistrato> u = utente.findById(id);
        if (u.isEmpty()) {
            return null;
        }
         return u.get();
    }

    /**
     * pre: email non deve essere null.
     * Metodo che restituisce un utente tramite la sua mail.
     * @param email email dell'utente.
     * @return UtenteRegistrato.
     * post: utenteRegistrato viene trovato tramite mail.
     */
    @Override
    public UtenteRegistrato findUtenteByEmail(final String email) {
        UtenteRegistrato result;

        if ((result = paziente.findByEmail(email)) != null) {
            return result;
        } else if ((result = medico.findByEmail(email)) != null) {
            return result;
        } else if ((result = admin.findByEmail(email)) != null) {
            return result;
        } else if ((result = caregiver.findByEmail(email)) != null) {
            return result;
        }
        return null;
    }

    /**
     * pre: u non deve essere null.
     * Metodo per fare update di un utente nel DB.
     * @param u utente da aggiornare.
     * post: u viene modificato.
     */
    public void updateUtente(final UtenteRegistrato u) {
        this.utente.save(u);
    }

    /**
     * Metodo per ottenere tutti i medici del db.
     * @return lista di tutti i medici.
     * post: lista di medici.
     */
    @Override
    public List<UtenteRegistrato> getTuttiMedici() {
        return medico.findAll();
    }

    /**
     * Metodo per ottenere tutti i pazienti del db.
     * @return Lista di tutti i pazienti.
     * post: lista di pazienti.
     */
    @Override
    public List<UtenteRegistrato> getTuttiPazienti() {
        return paziente.findAll();
    }

    /**
     * Metodo che restituisce tutti gli utenti dal db.
     * @return Lista di tutti gli utenti.
     * post: lista di utenti.
     */
    @Override
    public List<UtenteRegistrato> getTuttiUtenti() {
        return utente.findAll();
    }

    /**
     * pre: idMedico non deve essere null.
     * Metodo per ottenere tutti i pazienti del db di un medico.
     * @param idMedico id del medico.
     * @return Lista di tutti i pazienti asscociati al medico.
     * post: list di paziente non è null.
     */
    @Override
    public List<Paziente> getPazientiByMedico(final long idMedico) {

        paziente.findAll().stream().filter((p) -> p.getClass()
                        .getSimpleName().equals("Paziente"))
                .map(Paziente.class::cast)
                .forEach((p) -> System.out.println(p));

        return paziente.findAll().stream()
                .filter((p) -> p.getClass().getSimpleName().equals("Paziente"))
                .map(Paziente.class::cast)
                .filter(p -> p.getMedico().getId() == (idMedico))
                .toList();
    }



    /**
     * pre: idPaziente non deve essere null.
     * Metodo per la ricerca di un medico tramite il suo paziente.
     * @param idPaziente id del paziente.
     * @return Long.
     * post: idMedico.
     */
    @Override
    public  Long findMedicoByPaziente(final long idPaziente) {
        Medico m = findPazienteById(idPaziente).getMedico();
        return m.getId();
    }

    /**
     * metodo che restituisce gli indirizzi dal db.
     * @return Lista di indirizzi.
     * post: list di indirizzo non è null.
     */
    @Override
    public List<Indirizzo> findAllIndirizzi() {
        return indirizzo.findAll();
    }

    /**
     * pre: idUtente non deve essere null.
     * Metodo che rimuove un utente dal db.
     * @param idUtente id dell'utente.
     * post: utente viene eliminato dal db.
     */
    @Override
    public void rimuoviUtente(final Long idUtente) {
        Optional<UtenteRegistrato> u = utente.findById(idUtente);
        utente.delete(u.get());
    }

    /**
     * pre: pwd e idUtente non devono essere null.
     * metodo che controlla che la password passata
     * sia uguale a quella presente nel db.
     * @param pwd password passata.
     * @param idUtente id dell'utente.
     * @return true o false
     */
    @Override
    public boolean controllaPassword(final String pwd,
                                     final Long idUtente) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UtenteRegistrato u = utente.findById(idUtente).get();
        boolean isPasswordMatch = passwordEncoder.matches(pwd, u.getPassword());

        if (isPasswordMatch) {
            return true;
        }

        return false;
    }

    /**
     * pre: nuovaPassword non deve essere null.
     * Metodo che decripta la nuova password.
     * @param nuovaPassword password nuova che si vuole inserire.
     * @return String.
     * post: nuovaPassword criptata.
     */
    @Override
    public String encryptPassword(final String nuovaPassword) {
        return pwdEncoder.encode(nuovaPassword);
    }

    @Override
    public Caregiver findCaregiverById(Long id) {
        Optional<UtenteRegistrato> car = caregiver.findById(id);
        return (Caregiver) car.orElse(null);

    }

    @Override
    public List<UtenteRegistrato> getTuttiCaregiver() {
        return caregiver.findAll();
    }

    @Override
    public List<UtenteRegistrato> getTuttiCaregiverNonRegistrati() {
        Optional<List<UtenteRegistrato>> utentiNonRegistrati = Optional.of(utente.findAll()
                .stream()
                .filter(utente -> utente.getRuolo().equals(Role.CAREGIVER_NON_REGISTRATO))
                .toList());
        return utentiNonRegistrati.get();
    }

    @Override
    public Long findCaregiverByIdPaziente(Long idPaziente) {
        Caregiver c = findPazienteById(idPaziente).getCaregiver();
        return c.getId();
    }

    @Override
    public boolean isCaregiver(Long idUtente) {
        Optional<UtenteRegistrato> u = caregiver.findById(idUtente);

        if (u.isEmpty()) {
            return false;
        } else if (u.get().getClass().getSimpleName().equals("Caregiver")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isCaregiverNonRegistrato(Long idUtente) {
        Optional<UtenteRegistrato> u = utente.findById(idUtente);

        if (u.isEmpty()) {
            return false;
        } else if (u.get().getRuolo().equals(Role.CAREGIVER_NON_REGISTRATO)) {
            return true;
        }
        return false;
    }

    @Override
    public Long generaNuovoCaregiverNonRegistrato(String emailNuovoCaregiver) throws Exception {
        Caregiver nuovoCaregiver = new Caregiver(
                LocalDate.of(1970, 1, 1),
                generaCodiceFiscaleCasuale(),
                generaNumeroTelefonoCasuale(),
                "Password123!",
                emailNuovoCaregiver,
                "Caregiver di Prova",
                "Caregiver di Prova",
                "F",
                Role.CAREGIVER_NON_REGISTRATO);
        caregiver.save(nuovoCaregiver);
        return caregiver.findByEmail(emailNuovoCaregiver).getId();
    }

    private static String generaNumeroTelefonoCasuale() {
        Random RANDOM = new Random();
        StringBuilder numeroTelefono = new StringBuilder();

        numeroTelefono.append("+");
        numeroTelefono.append("39");

        // Aggiungi il prefisso per i cellulari italiani (3xx)
        numeroTelefono.append("3");
        numeroTelefono.append(RANDOM.nextInt(10));  // Seconda cifra del prefisso (3xx)
        numeroTelefono.append(RANDOM.nextInt(10));  // Terza cifra del prefisso (3xx)

        for (int i = 0; i < 7; i++) {
            numeroTelefono.append(RANDOM.nextInt(10));
        }

        return numeroTelefono.toString();
    }


    private static String generaCodiceFiscaleCasuale() {
        String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String LETTERS_AND_DIGITS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random RANDOM = new Random();
        StringBuilder codiceFiscale = new StringBuilder();

        // Aggiungi le prime 6 lettere
        for (int i = 0; i < 6; i++) {
            codiceFiscale.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }

        // Aggiungi 2 caratteri alfanumerici
        for (int i = 0; i < 2; i++) {
            codiceFiscale.append(LETTERS_AND_DIGITS.charAt(RANDOM.nextInt(LETTERS_AND_DIGITS.length())));
        }

        // Aggiungi 1 lettera
        codiceFiscale.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));

        // Aggiungi 2 caratteri alfanumerici
        for (int i = 0; i < 2; i++) {
            codiceFiscale.append(LETTERS_AND_DIGITS.charAt(RANDOM.nextInt(LETTERS_AND_DIGITS.length())));
        }

        // Aggiungi 1 lettera
        codiceFiscale.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));

        // Aggiungi 3 caratteri alfanumerici
        for (int i = 0; i < 3; i++) {
            codiceFiscale.append(LETTERS_AND_DIGITS.charAt(RANDOM.nextInt(LETTERS_AND_DIGITS.length())));
        }

        // Aggiungi 1 lettera finale
        codiceFiscale.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));

        return codiceFiscale.toString();
    }


    /**
     * Metodo che aggiorna un indirizzo.
     * @param ind indirizzo che si vuole aggiornare.
     */
    public void updateIndirizzo(final Indirizzo ind) {
        indirizzo.save(ind);
    }

}

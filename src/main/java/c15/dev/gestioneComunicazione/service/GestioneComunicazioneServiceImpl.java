package c15.dev.gestioneComunicazione.service;

import c15.dev.gestioneUtente.service.GestioneUtenteService;
import c15.dev.model.dao.NotaDAO;
import c15.dev.model.dto.NotaCaregiverDTO;
import c15.dev.model.dto.NotaDTO;
import c15.dev.model.dto.NotificaDTO;
import c15.dev.model.entity.Caregiver;
import c15.dev.model.entity.Medico;
import c15.dev.model.entity.Nota;
import c15.dev.model.entity.Paziente;
import c15.dev.model.entity.enumeration.StatoNota;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * @author: Leopoldo todisco, Carlo Venditto.
 *
 */
@Service
@Transactional
public class GestioneComunicazioneServiceImpl implements GestioneComunicazioneService {
    /**
     * Oggetto per invio notifiche.
     */
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * Service per le operazioni legate all'utente.
     */
    @Autowired
    private GestioneUtenteService utenteService;

    /**
     * Provvede alle operazioni del db delle note.
     */
    @Autowired
    private NotaDAO notaDAO;

    /**
     * Provvede all'invio delle email.
     */
    @Autowired
    private JavaMailSender mailSender;


    /**
     * pre La taglia del messaggio deve essre > 0,
     * emailDestinatario deve essere >0.
     * Metodo per inviare una email.
     * @param messaggio messaggio da inviare nell'email.
     * @param emailDestinatario mail del destinatario.
     */
    @Override
    @Async
    public void invioEmail(final String messaggio,
                           final String oggetto,
                           final String emailDestinatario) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("paolocarmine1201@gmail.com");
        message.setTo(emailDestinatario);
        message.setSubject(oggetto);
        message.setText(messaggio);
        mailSender.send(message);
        System.out.println("email inviata");


    }

    @Override
    public void invioEmailRegistrazioneCaregiver(String messaggio, String oggetto, String emailDestinatario, Long idPaziente, Long idCaregiver) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("paolocarmine1201@gmail.com");
        message.setTo(emailDestinatario);
        message.setSubject(oggetto);
        message.setText(messaggio);
        mailSender.send(message);
        System.out.println("Email inviata");
    }

    /**
     * pre Messaggio deve essere diverso da null,
     * idDestinario deve essere diverso da null,
     * idMittente deve essere diverso da null.
     * Metodo per inviare una nota.
     * @param messaggio messaggio da inviare.
     * @param idDestinatario id del destinatario della mail.
     * @param idMittente id del mittente della mail.
     */
    @Override
    public boolean invioNota(final String messaggio,
                          final Long idDestinatario,
                          final Long idMittente,
                          final Long idAutore) {

        if(idMittente == null || idDestinatario == null){
            return false;
        } else if(messaggio.equals("")){
            return false;
        } else {
            if (utenteService.isMedico(idAutore)) {

                Medico m =  utenteService.findMedicoById(idMittente);
                Paziente p = utenteService.findPazienteById(idDestinatario);
                Nota nota = new Nota(messaggio,
                        LocalDate.now(),
                        idMittente,
                        StatoNota.NON_LETTA,
                        m,
                        p);
                notaDAO.save(nota);
                return true;
            } else if(utenteService.isPaziente(idAutore)){

                Medico m = (Medico) utenteService.findMedicoById(idDestinatario);
                Paziente p = (Paziente) utenteService.findPazienteById(idMittente);

                Nota nota =
                        new Nota(messaggio,
                                LocalDate.now(),
                                idMittente,
                                StatoNota.NON_LETTA,
                                m,
                                p);
                notaDAO.save(nota);
                return true;
            }




            Medico m = (Medico) utenteService.findMedicoById(idDestinatario);
            Paziente p = (Paziente) utenteService.findPazienteById(idMittente);

            Nota nota =
                    new Nota(messaggio,
                            LocalDate.now(),
                            idAutore,
                            StatoNota.NON_LETTA,
                            m,
                            p);
            notaDAO.save(nota);
            return true;
        }
    }

    /**
     * Implementazione del metodo che cerca tutte le note.
     * @return lista di tutte le note.
     * post List di nota deve essere diversa da null.
     */
    public List<Nota> findAllNote() {
        return notaDAO.findAll();
    }

    /**
     * pre Id deve essre diverso da null.
     * Firma del metodo che ricerca tutte le note di un utente.
     * @param id identificativo dell'utente.
     * @return lista delle note dell'utente.
     * post La lista deve essere diversa da null.
     */
    @Override
    public List<NotaDTO> findNoteByIdUtente(final long id) {
        System.out.println(id);
        List<Nota> note = notaDAO.findNoteByIdUtente(id);
        List<NotaDTO> dto = note
                .stream()
                .map(e ->
                        new NotaDTO(utenteService
                                .findUtenteById(e.getAutore())
                                .getNome()
                                + " "
                                + utenteService
                                .findUtenteById(e.getAutore())
                                .getCognome(),
                e.getContenuto())).toList();

        return dto;
    }

    @Override
    public List<NotaCaregiverDTO> findNoteInviateByIdUtente(long id) {
        List<Nota> note = notaDAO.findNoteInviateByIdUtente(id);
        List<NotaCaregiverDTO> dto = note
                .stream()
                .map(e ->
                        new NotaCaregiverDTO(utenteService.findUtenteById(e.getAutore()).getNome() + " " + utenteService.findUtenteById(e.getAutore()).getCognome(),
                                e.getContenuto(),
                                utenteService.findUtenteById(e.getMedico().getId()).getNome() + " " + utenteService.findUtenteById(e.getMedico().getId()).getCognome()
                                )).toList();

        return dto;
    }

    /**
     * pre email diversa da null.
     * Firma del metodo che restituisce tutte le note non lette di un utente.
     * @param email email dell'utente.
     * @return lista delle note non lette dell'utente.
     * post La lista deve essere diversa da null.
     */
    @Override
    public List<Nota> findNoteNonLetteByUser(final String email) {
        var user = utenteService.findUtenteByEmail(email);
        long idUser = user.getId();

        var list = notaDAO.findNoteByIdUtente(idUser);
        return list.stream().filter(n -> n.getStatoNota()
                .equals(StatoNota.NON_LETTA))
                .toList();

    }


    /**pre Il messaggio non deve essere vuoto e l'utente deve esistere.
     * Metodo per inviare una notifica al frontend.
     * @param message è il messaggio che viene passato al frontend.
     */
    @Override
    @SendTo("/topic/notifica")
    public void sendNotifica(final String message, final Long idDest) {
        NotificaDTO n = NotificaDTO.builder()
                .messagio(message)
                .idPaziente(idDest)
                .build();
        template.convertAndSend("/topic/notifica", n);
    }

    @Override
    public List<NotaDTO> findNoteInviateERicevuteByIdUtente(long id) {
        List<Nota> note = notaDAO.findNoteInviateERicevuteByIdUtente(id);
        List<NotaDTO> dto = note
                .stream()
                .map(e ->
                        new NotaDTO(utenteService.findUtenteById(e.getAutore()).getNome() + " " + utenteService.findUtenteById(e.getAutore()).getCognome(),
                                e.getContenuto())).toList();

        return dto;
    }
}

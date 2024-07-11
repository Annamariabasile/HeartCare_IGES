package c15.dev.integrationTesting.gestioneComunicazione;

import c15.dev.gestioneComunicazione.service.GestioneComunicazioneService;
import c15.dev.gestioneUtente.service.GestioneUtenteService;
import c15.dev.model.dao.CaregiverDAO;
import c15.dev.model.dao.MedicoDAO;
import c15.dev.model.dao.NotaDAO;
import c15.dev.model.dao.PazienteDAO;
import c15.dev.model.entity.Caregiver;
import c15.dev.model.entity.Medico;
import c15.dev.model.entity.Nota;
import c15.dev.model.entity.Paziente;
import c15.dev.utils.AuthenticationRequest;
import c15.dev.utils.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")  // Usa un profilo di test per configurare il database di test
public class GestioneComunicazioneServiceIntegrationTest {
    @Autowired
    private PazienteDAO pazienteDAO;

    @Autowired
    private NotaDAO notaDao;

    @Autowired
    private CaregiverDAO caregiverDAO;

    @Autowired
    private MedicoDAO medicoDAO;

    @Autowired
    private GestioneComunicazioneService gestioneComunicazioneService;

    @Autowired
    private GestioneUtenteService utenteService;

    private Paziente paziente;
    private Paziente pazienteErrato;
    private Medico medico;
    private Caregiver caregiver;

    @BeforeEach
    public void setUp() {
        medico = utenteService.findMedicoById(4L);
        paziente = utenteService.findPazienteById(1L);
        pazienteErrato = utenteService.findPazienteById(51L);
        caregiver = paziente.getCaregiver();
    }

    @Test
    @Transactional
    public void testInvioNotaCaregiverIntegrationTesting () throws Exception {


        List<Nota> notePaziente = notaDao.findNoteByIdUtente(paziente.getId());

        boolean result = gestioneComunicazioneService.invioNota("Prova", medico.getId(), paziente.getId(), caregiver.getId());

        assertEquals(true, result);
        List<Nota> note = notaDao.findNoteByIdUtente(paziente.getId());
        assertEquals(notePaziente.size()+1, note.size());
    }


    @Test
    @Transactional
    public void testInvioNotaCaregiver_TestoVuotoIntegrationTesting () throws Exception {
        List<Nota> notePaziente = notaDao.findNoteByIdUtente(paziente.getId());
        boolean result = gestioneComunicazioneService.invioNota("", medico.getId(), paziente.getId(), caregiver.getId());
        assertEquals(false, result);
        List<Nota> note = notaDao.findNoteByIdUtente(paziente.getId());
        assertEquals(notePaziente.size(), note.size());
    }

    @Test
    @Transactional
    public void testInvioNotaCaregiver_PazienteNonValidoIntegrationTesting () throws Exception {
        assertNull(pazienteErrato);

        boolean result = gestioneComunicazioneService.invioNota("prova", medico.getId(), null, caregiver.getId());
        assertEquals(false, result);
    }


}

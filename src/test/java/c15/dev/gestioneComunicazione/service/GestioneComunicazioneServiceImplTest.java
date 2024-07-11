package c15.dev.gestioneComunicazione.service;

import c15.dev.HeartCareApplicationTests;
import c15.dev.gestioneUtente.service.GestioneUtenteService;
import c15.dev.gestioneUtente.service.GestioneUtenteServiceImpl;
import c15.dev.model.dao.CaregiverDAO;
import c15.dev.model.dao.MedicoDAO;
import c15.dev.model.dao.NotaDAO;
import c15.dev.model.dao.PazienteDAO;
import c15.dev.model.entity.Caregiver;
import c15.dev.model.entity.Medico;
import c15.dev.model.entity.Nota;
import c15.dev.model.entity.Paziente;
import c15.dev.model.entity.enumeration.StatoNota;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = HeartCareApplicationTests.class)
public class GestioneComunicazioneServiceImplTest {

    @Mock
    private PazienteDAO pazienteDAO;

    @Mock
    private NotaDAO notaDao;

    @Mock
    private CaregiverDAO caregiverDAO;

    @Mock
    private MedicoDAO medicoDAO;

    @InjectMocks
    private GestioneComunicazioneServiceImpl service;

    @Mock
    private GestioneUtenteService utenteService;

    private Paziente paziente;
    private Medico medico;
    private Caregiver caregiver;
    private Nota nota;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this); // Inizializza i mock

        paziente = new Paziente(LocalDate.of(2000, 11, 11),
                "TDSLLD00E18C129Y",
                "+393887868300",
                "Wpasswd1!%",
                "leopoldo.todiscozte@gmail.com",
                "Leopoldo",
                "Todisco",
                "M");
        paziente.setId(1L);

        medico = new Medico(LocalDate.of(2000, 10, 10),
                "VDDCDD89L78I976V",
                "+393398765437",
                "Apasswd1!%",
                "leopoldo@libero.it",
                "Leopoldo",
                "Todisco",
                "M");
        medico.setId(2L);

        caregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "XZBWST56D49I927U",
                "+393242345617",
                "Ciaone12345!",
                "heartcare016@gmail.com",
                "Alessandro",
                "Basile",
                "M"
        );

        nota = new Nota("Prova",
                LocalDate.now(),
                caregiver.getId(),
                StatoNota.NON_LETTA,
                medico,
                paziente);

        caregiver.setId(11L);

    }

    @Test
    public void testInviaNotaDaParteDelCaregiver() throws Exception {

        when(utenteService.findMedicoById(anyLong())).thenAnswer(invocationOnMock -> {
            return medico;
        });


        when(utenteService.findPazienteById(anyLong())).thenAnswer(invocationOnMock -> {
            return paziente;
        });

        when(utenteService.isMedico(anyLong())).thenAnswer(invocationOnMock -> {
            return false;
        });

        when(utenteService.isPaziente(anyLong())).thenAnswer(invocationOnMock -> {
            return false;
        });


        // Preparazione dei dati
        paziente.setMedico(medico);
        medico.getElencoPazienti().add(paziente);
        paziente.setCaregiver(caregiver);
        caregiver.getElencoPazienti().add(paziente);

        // Esegui il metodo da testare
        boolean result = service.invioNota("Prova", medico.getId(), paziente.getId(), caregiver.getId());

        // Verifica delle aspettative
        assertEquals(true, result);
    }

    @Test
    public void testInviaNotaDaParteDelCaregiver_TestoVuoto() throws Exception {

        // Esegui il metodo da testare
        boolean result = service.invioNota("", medico.getId(), paziente.getId(), caregiver.getId());

        // Verifica delle aspettative
        assertEquals(false, result);
    }

    @Test
    public void testInviaNotaDaParteDelCaregiver_PazienteNonValido() throws Exception {
        // Esegui il metodo da testare
        boolean result = service.invioNota("Prova", medico.getId(), null, caregiver.getId());
        // Verifica delle aspettative
        assertEquals(false, result);
    }


}

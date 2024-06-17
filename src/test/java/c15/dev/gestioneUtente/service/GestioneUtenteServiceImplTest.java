package c15.dev.gestioneUtente.service;


import c15.dev.HeartCareApplication;
import c15.dev.HeartCareApplicationTests;
import c15.dev.model.dao.AdminDAO;
import c15.dev.model.dao.CaregiverDAO;
import c15.dev.model.dao.PazienteDAO;

import c15.dev.model.dao.MedicoDAO;

import c15.dev.model.entity.Caregiver;
import c15.dev.model.entity.Medico;
import c15.dev.model.entity.Paziente;
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
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * @author Paolo Carmine Valletta, Carlo Venditto.
 *  * Creato il 03/02/2023.
 *  * Classe di test per la gestione service di utente.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = HeartCareApplicationTests.class)
public class GestioneUtenteServiceImplTest {


    /**
     *  Mocking del MedicoDAO per accedere al DB da parte del medico.
     */
    @Mock
    private MedicoDAO daoMedico;

    @Mock
    private CaregiverDAO daoCaregiver;

    @Mock
    private AdminDAO adminDAO;
    /**
     *  Mocking del MedicoDAO per accedere al DB da parte del paziente.
     */
    @Mock
    private PazienteDAO pazienteDAO;
    /**
     *  Inject mocking dell'implementazione del service.
     */
    @InjectMocks
    private GestioneUtenteServiceImpl service;

    private Paziente paz;
    private Medico med;
    private Caregiver car;
    private ArrayList<Paziente> listaPaz;



    private Paziente paziente;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        paziente = new Paziente(LocalDate.of(2000, 10, 10),
                "TDSLL00E18C129Y",
                "+393654563256",
                "Wpasswd1!%",
                "cicccio@libero.it",
                "Ciccio",
                "Pasticcio",
                "M");

        paziente.setId(10L);


        paz = new Paziente(LocalDate.of(2000, 10, 10),
                "VLLPCR01L12I234V",
                "+393381234568",
                "Wpasswd1!%",
                "paolo@libero.it",
                "Paolo",
                "Valletta",
                "M");

        med = new Medico(LocalDate.of(2000, 10, 10),
                "VDDCDD89L78I976V",
                "+393398765437",
                "Apasswd1!%",
                "leopoldo@libero.it",
                "Leopoldo",
                "Todisco",
                "M");

        car = new Caregiver(LocalDate.of(2000, 11, 18),
                "XZBWST56D49I927U",
                "+393242345617",
                "Ciaone12345!",
                "heartcare016@gmail.com",
                "Alessandro",
                "Basile",
                "M"
        );

        paz.setId(1L);
        med.setId(2L);
        car.setId(11L);

        listaPaz = new ArrayList<>();

        System.out.println(paziente.toString());
    }


    @Test
    public void testAssegnaCaregiver() throws Exception {
        // Mock per trovare il caregiver per email corretta
        /*when(daoCaregiver.findByEmail("heartcare016@gmail.com")).thenAnswer(invocationOnMock -> {
            return Optional.of(car);
        });

        when(pazienteDAO.findById(anyLong())).thenAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            if (id.equals(paz.getId())) {
                return Optional.of(paz);
            } else {
                return Optional.empty();
            }
        });

         */

        System.out.println("pazienteProva: " + paz.getNome());
        System.out.println("caregiverPROVA: " + car.getNome());

        paz.setCaregiver(car);
        car.getElencoPazienti().add(paz);

        System.out.println("caregiverDopoAverAssegnato:" + paz.getCaregiver().getNome());

        // Verifica il risultato del metodo assegnare caregiver
        assertEquals(true, service.assegnaCaregiver(paz.getId(), car.getId()));
    }

    @Test
    public void testAssegnaCaregiver_emailNonValida() throws Exception {
        // Mock per trovare il caregiver per email non valida
        //Long idCaregiver = service.findUtenteByEmail("heartcare016gmail.com").getId();

            when(daoCaregiver.findByEmail("heartcare016gmail.com")).thenAnswer(invocationOnMock -> {
                return Optional.ofNullable(null);
            });

        // Mock per trovare il paziente per id
        when(pazienteDAO.findById(anyLong())).thenAnswer(invocationOnMock -> {
            return Optional.of(paziente);
        });

        paziente.setCaregiver(car);
        car.getElencoPazienti().add(paziente);

        // Verifica il risultato del metodo assegnare caregiver per email non valida
        assertFalse(service.assegnaCaregiver(paziente.getId(), car.getId()));
    }



        /**
         *  Testing dell'assegnamento del paziente corretto.
         */
        @Test
        public void testAssegnaPazienteCorretto () {
            when(pazienteDAO.findById(anyLong())).thenAnswer(invocationOnMock -> {
                return Optional.of(paz);
            });

            when(daoMedico.findById(anyLong())).thenAnswer(invocationOnMock -> {
                return Optional.of(med);
            });

            paz.setMedico(med);
            med.getElencoPazienti().add(paz);


            assertEquals(true, service.assegnaPaziente(med.getId(), paz.getId()));

        }

        /**
         *  Testing dell'assegnamento fallito.
         *  causa: Medico non esistente.
         */
        @Test
        public void testAssegnaPazienteMedicoNonEsitente () {
            when(daoMedico.findById(anyLong())).thenAnswer(invocationOnMock -> {
                return Optional.ofNullable(null);
            });

            paz.setMedico(med);
            med.getElencoPazienti().add(paz);


            assertEquals(false, service.assegnaPaziente(med.getId(), paz.getId()));

        }

        /**
         *  Testing dell'assegnamento fallito.
         *  causa: Paziente non esistente.
         */
        @Test
        public void testAssegnaPazienteNonEsitente () {
            when(pazienteDAO.findById(anyLong())).thenAnswer(invocationOnMock -> {
                return Optional.ofNullable(null);
            });

            when(daoMedico.findById(anyLong())).thenAnswer(invocationOnMock -> {
                return Optional.of(med);
            });

            paz.setMedico(med);
            med.getElencoPazienti().add(paz);


            assertEquals(false, service.assegnaPaziente(med.getId(), paz.getId()));


        }

}

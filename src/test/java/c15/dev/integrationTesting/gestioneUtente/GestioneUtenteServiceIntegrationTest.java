package c15.dev.integrationTesting.gestioneUtente;

import c15.dev.HeartCareApplicationTests;
import c15.dev.gestioneUtente.service.GestioneUtenteService;
import c15.dev.model.dao.CaregiverDAO;
import c15.dev.model.dao.MedicoDAO;
import c15.dev.model.dao.PazienteDAO;
import c15.dev.model.entity.Caregiver;
import c15.dev.model.entity.Medico;
import c15.dev.model.entity.Paziente;
import c15.dev.model.entity.UtenteRegistrato;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")  // Usa un profilo di test per configurare il database di test
public class GestioneUtenteServiceIntegrationTest {
    @Autowired
    private MedicoDAO daoMedico;

    @Autowired
    private CaregiverDAO daoCaregiver;

    @Autowired
    private PazienteDAO daoPaziente;

    @Autowired
    private GestioneUtenteService utenteService;

    Caregiver car;
    Paziente paz;
    Medico med;

    @Test
    @Transactional
    public void testAssegnaCaregiver() {

        car = daoCaregiver.findByEmail("paolocarminevalletta@gmail.com");
        paz = Optional.of((Paziente) daoPaziente.findById(3L).get()).get();

        boolean result = utenteService.assegnaCaregiver(3L, "paolocarminevalletta@gmail.com");
        assertTrue(result);

        assertEquals(car.getEmail(), paz.getCaregiver().getEmail());
    }

    @Test
    @Transactional
    public void testAssegnaCaregiver_emailNonValida() throws Exception {
        car = daoCaregiver.findByEmail("paolocarminevallettagmail.com");
        paz = Optional.of((Paziente) daoPaziente.findById(3L).get()).get();

        boolean result = utenteService.assegnaCaregiver(3L, "paolocarminevallettagmail.com");
        assertFalse(result);

        assertThat(paz.getCaregiver().getEmail()).isNotEqualTo("paolocarminevallettagmail.com");
        //assertEquals(null, paz.getCaregiver().getEmail());
    }

}

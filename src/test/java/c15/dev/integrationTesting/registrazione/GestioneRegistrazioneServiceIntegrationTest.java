package c15.dev.integrationTesting.registrazione;

import c15.dev.gestioneUtente.service.GestioneUtenteService;
import c15.dev.model.dao.CaregiverDAO;
import c15.dev.model.dao.MedicoDAO;
import c15.dev.model.dao.PazienteDAO;
import c15.dev.model.entity.Caregiver;
import c15.dev.model.entity.Medico;
import c15.dev.model.entity.Paziente;
import c15.dev.registrazione.service.RegistrazioneServiceImpl;
import c15.dev.utils.AuthenticationRequest;
import c15.dev.utils.AuthenticationResponse;
import c15.dev.utils.JwtService;
import c15.dev.utils.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")  // Usa un profilo di test per configurare il database di test
public class GestioneRegistrazioneServiceIntegrationTest {

    @Autowired
    private CaregiverDAO daoCaregiver;

    @Autowired
    private RegistrazioneServiceImpl registrazioneService;

    private AuthenticationRequest request;

    @Autowired
    private PazienteDAO daoPaziente;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder pwdEncoder;

    Caregiver car;

    @Autowired
    private GestioneUtenteService gestioneUtenteService;

    @Test
    @Transactional
    public void testLoginCaregiverIntegrationTesting () throws Exception {
        request = new AuthenticationRequest(
                "Ciaone12345!",
                "paolocarminevalletta@gmail.com"
        );

        car = daoCaregiver.findByEmail("paolocarminevalletta@gmail.com");

        var jwtToken = jwtService.generateToken(car);
        assertEquals(AuthenticationResponse.builder().token(jwtToken).build(), registrazioneService.login(request));
    }

    @Test
    @Transactional
    public void testLoginCaregiver_PasswordSbagliataIntegrationTesting () throws Exception {
        request = new AuthenticationRequest(
                "Ciao12345!",
                "paolocarminevalletta@gmail.com"
        );

        car = daoCaregiver.findByEmail("paolocarminevalletta@gmail.com");
        String password = request.getPassword();
        assertFalse(pwdEncoder.matches(password, car.getPassword()));

        assertThrows(BadCredentialsException.class, () -> registrazioneService.login(request));
    }

    @Test
    @Transactional
    public void testLoginCaregiver_EmailSbagliataIntegrationTesting () throws Exception {
        request = new AuthenticationRequest(
                "Ciaone12345!",
                "paolocarminevallettagmail.com"
        );

        assertThrows(InternalAuthenticationServiceException.class, () -> registrazioneService.login(request));
    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverIntegrationTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L12I234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "Basile",
                "F"
        );
        car = daoCaregiver.findByEmail(savedCaregiver.getEmail());
        savedCaregiver.setId(car.getId());
        var jwtToken = jwtService.generateToken(savedCaregiver);

        assertEquals(AuthenticationResponse.builder()
                .token(jwtToken)
                .build(), this.registrazioneService.registraCaregiver( savedCaregiver, 1L,savedCaregiver.getPassword()));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverNomeMancanteIntegrationTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L12I234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "",
                "Basile",
                "F"
        );
        car = daoCaregiver.findByEmail(savedCaregiver.getEmail());
        savedCaregiver.setId(car.getId());
        assertThrows(Exception.class, () -> registrazioneService.registraCaregiver(savedCaregiver, 1L,"Ciaone12345!"));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverCognomeMancanteIntegrationTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L12I234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "",
                "F"
        );
        car = daoCaregiver.findByEmail(savedCaregiver.getEmail());
        savedCaregiver.setId(car.getId());
        assertThrows(Exception.class, () -> registrazioneService.registraCaregiver(savedCaregiver, 1L,"Ciaone12345!"));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverNumeroTelefonoNonValidoIntegrationTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L12I234V",
                "+3932425619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "Basile",
                "F"
        );
        car = daoCaregiver.findByEmail(savedCaregiver.getEmail());
        savedCaregiver.setId(car.getId());
        assertThrows(Exception.class, () -> registrazioneService.registraCaregiver(savedCaregiver, 1L,"Ciaone12345!"));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverCodiceFiscaleNonValidoIntegrationTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L1234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "Basile",
                "F"
        );
        car = daoCaregiver.findByEmail(savedCaregiver.getEmail());
        savedCaregiver.setId(car.getId());
        assertThrows(Exception.class, () -> registrazioneService.registraCaregiver(savedCaregiver, 1L,"Ciaone12345!"));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverDataNonValidaIntegrationTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L1234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "Basile",
                "F"
        );
        car = daoCaregiver.findByEmail(savedCaregiver.getEmail());
        savedCaregiver.setId(car.getId());
        LocalDate dataFutura = LocalDate.of(2025, 6, 20);
        savedCaregiver.setDataDiNascita(dataFutura);
        assertThrows(Exception.class, () -> registrazioneService.registraCaregiver(savedCaregiver, 1L,"Ciaone12345!"));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverGenereNonValidoIntegrationTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L1234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "Basile",
                "F"
        );
        car = daoCaregiver.findByEmail(savedCaregiver.getEmail());
        savedCaregiver.setId(car.getId());
        savedCaregiver.setGenere("V");
        assertThrows(Exception.class, () -> registrazioneService.registraCaregiver(savedCaregiver, 1L,"Ciaone12345!"));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverPasswordNonValidaIntegrationTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L1234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "Basile",
                "F"
        );
        car = daoCaregiver.findByEmail(savedCaregiver.getEmail());
        savedCaregiver.setId(car.getId());
        savedCaregiver.setPassword("blablabla");
        assertThrows(Exception.class, () -> registrazioneService.registraCaregiver(savedCaregiver, 1L,"Ciaone12345!"));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverPasswordNonCombacianoIntegrationTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L1234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "Basile",
                "F"
        );
        car = daoCaregiver.findByEmail(savedCaregiver.getEmail());
        savedCaregiver.setId(car.getId());
        assertThrows(Exception.class, () -> registrazioneService.registraCaregiver(savedCaregiver, 1L,"Ciaone123"));

    }


}

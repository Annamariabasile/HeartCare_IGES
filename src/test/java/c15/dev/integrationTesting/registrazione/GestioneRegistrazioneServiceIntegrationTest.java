package c15.dev.integrationTesting.registrazione;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")  // Usa un profilo di test per configurare il database di test
public class GestioneRegistrazioneServiceIntegrationTest {

    @Autowired
    private CaregiverDAO daoCaregiver;

    @Autowired
    private RegistrazioneServiceImpl registrazioneService;

    private AuthenticationRequest request;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder pwdEncoder;

    Caregiver car;


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

}

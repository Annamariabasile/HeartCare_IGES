package c15.dev.registrazione.service;

import c15.dev.HeartCareApplication;
import c15.dev.HeartCareApplicationTests;
import c15.dev.model.dao.*;
import c15.dev.model.entity.*;
import c15.dev.utils.AuthenticationRequest;
import c15.dev.utils.AuthenticationResponse;
import c15.dev.utils.JwtService;
import lombok.SneakyThrows;
import org.apache.catalina.Authenticator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HeartCareApplicationTests.class)
public class RegistrazioneServiceImplTest {

    @InjectMocks
    RegistrazioneServiceImpl registrazioneService;

    @InjectMocks
    private RegistrazioneServiceImpl rs;

    @Mock
    private AdminDAO adminDAO;

    @Mock
    private MedicoDAO medicoDAO;

    @Mock
    private PazienteDAO pazienteDAO;

    @Mock
    private AuthenticationRequest request;

    @Mock
   private AuthenticationManager authenticationManager;

    /**
     * Provvede alla criptazione della password.
     */
    @Mock
    private PasswordEncoder pwdEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationResponse authenticationResponse;

    @Mock
    private UtenteRegistratoDAO utenteRegistratoDAO;
    private Paziente paziente;
    private String confermaPassword;

    private Medico med1;

    private Caregiver car;
    @Mock
    private CaregiverDAO caregiverDAO;

    private Admin a1;

    @BeforeEach
    @SneakyThrows
    public void setup() throws Exception {
        paziente = new Paziente(
                LocalDate.parse("2001-06-15"),
                "CCLMRA02G14E321Q",
                "+393421234561",
                "Wpasswd1!%",
                "mario@gmail.com",
                "Mario",
                "Cicalese",
                "M"
        );
        confermaPassword = "Wpasswd1!%";

         med1 = new Medico(LocalDate.of(2000, 11, 18),
                "PDSLPD00E19C139A",
                "+393809123300",
                "Apasswd1!%",
                "alessandrozoccola@libero.it",
                "Alessandro",
                "Zoccola",
                "M");

          a1 = new Admin(LocalDate.of(2000, 11, 18),
                "PDSLPD08E18C129Q",
                "+393887124110",
                "Wpasswd1!%",
                "fabiola@libero.it",
                "Fabiola",
                "Valorant",
                "F");
          car = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L12I234V",
                "+393242345619",
                "Ciaone12345!",
                "heartcare016@gmail.com",
                "Annamaria",
                  "Basile",
                "F"
          );

    }

    @Test
    public void TestLoginPaziente() throws Exception {
        request = new AuthenticationRequest(
                "Wpasswd1!%",
                "mario@gmail.com"
        );

        when(this.pazienteDAO.findByEmail(any())).thenReturn(paziente);
       when(this.adminDAO.findByEmail(any())).thenReturn(null);
       when(this.medicoDAO.findByEmail(any())).thenReturn(null);
       when(this.caregiverDAO.findByEmail(any())).thenReturn(null);

       var jwtToken = jwtService.generateToken(paziente);
        assertEquals(AuthenticationResponse.builder()
               .token(jwtToken)
               .build(), this.rs.login(request));
   }

    @Test
    public void TestLoginCaregiver() throws Exception {
        request = new AuthenticationRequest(
                "Ciaone12345!",
                "heartcare016@gmail.com"
        );

        when(this.pazienteDAO.findByEmail(any())).thenReturn(null);
        when(this.adminDAO.findByEmail(any())).thenReturn(null);
        when(this.medicoDAO.findByEmail(any())).thenReturn(null);
        when(this.caregiverDAO.findByEmail(any())).thenReturn(car);

        var jwtToken = jwtService.generateToken(car);
        assertEquals(AuthenticationResponse.builder()
                .token(jwtToken)
                .build(), this.rs.login(request));
    }
    @Test
    public void TestLoginMedico() throws Exception {
        request = new AuthenticationRequest(
                "Apasswd1!%",
                "alessandrozoccola@libero.it"
        );

        when(this.pazienteDAO.findByEmail(any())).thenReturn(null);
        when(this.adminDAO.findByEmail(any())).thenReturn(null);
        when(this.medicoDAO.findByEmail(any())).thenReturn(med1);
        when(this.caregiverDAO.findByEmail(any())).thenReturn(null);


        var jwtToken = jwtService.generateToken(med1);
        assertEquals(AuthenticationResponse.builder()
                .token(jwtToken)
                .build(), this.rs.login(request));
    }

    @Test
    public void TestLoginAdmin() throws Exception {
        request = new AuthenticationRequest(
                "Wpasswd1!%",
                "fabiola@libero.it"
        );

        when(this.pazienteDAO.findByEmail(any())).thenReturn(null);
        when(this.adminDAO.findByEmail(any())).thenReturn(a1);
        when(this.medicoDAO.findByEmail(any())).thenReturn(null);
        when(this.caregiverDAO.findByEmail(any())).thenReturn(null);


        var jwtToken = jwtService.generateToken(a1);
        assertEquals(AuthenticationResponse.builder()
                .token(jwtToken)
                .build(), this.rs.login(request));
    }

    /**
     * metood che si occupa di testare la registrazione del paziente
     * @throws Exception
     */
    @Test
    public void registraPaziente()
            throws Exception {
        Paziente SavedPaziente = new Paziente(
                LocalDate.parse("2001-06-15"),
                "CCLMRA02G14E321Q",
                "+393421234561",
                "Wpasswd1!%",
                "mario@gmail.com",
                "Mario",
                "Cicalese",
                "M"
        );
        SavedPaziente.setId(1L);
        String token = jwtService.generateToken(paziente);
        when(this.pazienteDAO.save(paziente)).thenReturn(SavedPaziente);
        when(this.utenteRegistratoDAO.findByEmail(any())).thenReturn(null);
        assertEquals(AuthenticationResponse.builder()
                .token(token)
                .build(), this.registrazioneService.registraPaziente(paziente,confermaPassword));
    }

    /**
     * metodo che si occupa di testare la registrazione di un paziente
     * con una mail già presente nel database
     * @throws Exception
     */
    @Test
    public void TestRegistrazioneEmailPresente() throws Exception {
        Paziente paziente = new Paziente(
                LocalDate.parse("2001-06-15"),
                "CCLMRA02G14E321Q",
                "+393421234561",
                "Wpasswd1!%",
                "mario@gmail.com",
                "Mario",
                "Cicalese",
                "M"
        );
        when(this.utenteRegistratoDAO.findByEmail(any())).thenReturn(paziente);
        assertThrows(IllegalArgumentException.class, () -> this.registrazioneService.registraPaziente(paziente,confermaPassword));
    }

    /**
     * metodo che si occupa di testare la registrazione del paziente.
     * quando viene passato passato un paziente come null.
     */
    @Test
    public void TestRegistrazionePazienteNull() {
        assertThrows(IllegalArgumentException.class,
                () -> this.registrazioneService.registraPaziente(null,null));
    }

    /**
     * metodo che si occupa di testare se la password inserita in fase di.
     * registrazione rispetta la regex definita.
     */
    @Test
    public void TestRegistrazionePasswordErrata() throws Exception {
        paziente.setPassword("passwordCheNonRispettaIlFormato");
        assertThrows(IllegalArgumentException.class,
                () -> this.registrazioneService.registraPaziente(paziente,confermaPassword));
    }

    /**
     * metodo che si occupa di testare se la password di conferma.
     * corrisponde con la prima inserita.
     */
    @Test
    public void TestRegistrazioneConfermaPasswordDiversa(){
        confermaPassword = "passwordDiversa";
        assertThrows(IllegalArgumentException.class,
                () -> this.registrazioneService.registraPaziente(paziente,confermaPassword));
    }

    @Test
    public void TestLoginEmailSbagliata() throws Exception {
        request = new AuthenticationRequest(
                "Wpasswd1!%",
                "fabiola2@libero.it"
        );


        Mockito.when(this.adminDAO.findByEmail(any())).thenReturn(null);
        Mockito.when(this.pazienteDAO.findByEmail(any())).thenReturn(null);
        Mockito.when(this.medicoDAO.findByEmail(any())).thenReturn(null);
        Mockito.when(this.caregiverDAO.findByEmail(any())).thenReturn(null);


        assertEquals(AuthenticationResponse.builder().token(null).build(), registrazioneService.login(request));

    }

    @Test
    public void TestLoginPasswordErrataAdmin() throws Exception {
        request = new AuthenticationRequest(
                "Wpasswd9!%",
                "fabiola@libero.it"
        );
        String password = request.getPassword();

        Mockito.when(this.adminDAO.findByEmail(any())).thenReturn(a1);
        Mockito.when(this.pwdEncoder.matches(password, a1.getPassword())).thenReturn(false);

        assertEquals(AuthenticationResponse.builder().token(null).build(), registrazioneService.login(request));


    }


    @Test
    public void TestLoginPasswordErrataPaziente() throws Exception {
        request = new AuthenticationRequest(
                "Wpasswd9!%",
                "mario@gmail.com"
        );

        String password = request.getPassword();
        Mockito.when(this.pazienteDAO.findByEmail(any())).thenReturn(paziente);
        Mockito.when(this.pwdEncoder.matches(password, paziente.getPassword())).thenReturn(false);
        assertEquals(AuthenticationResponse.builder().token(null).build(), registrazioneService.login(request));
    }

    @Test
    public void TestLoginPasswordErrataCaregiver() throws Exception {
        request = new AuthenticationRequest(
                "Wpasswd9!%",
                "heartcare016@gmail.com"
        );

        String password = request.getPassword();
        Mockito.when(this.caregiverDAO.findByEmail(any())).thenReturn(car);
        Mockito.when(this.pwdEncoder.matches(password, car.getPassword())).thenReturn(false);
        assertEquals(AuthenticationResponse.builder().token(null).build(), registrazioneService.login(request));
    }

    @Test
    public void TestLoginPasswordErrataMedico() throws Exception {
        request = new AuthenticationRequest(
                "Wpasswd9!%",
                "alessandrozoccola@libero.it"
        );

        String password = request.getPassword();
        Mockito.when(this.medicoDAO.findByEmail(any())).thenReturn(med1);
        Mockito.when(this.pwdEncoder.matches(password, med1.getPassword())).thenReturn(false);
        assertEquals(AuthenticationResponse.builder().token(null).build(), registrazioneService.login(request));
    }
}


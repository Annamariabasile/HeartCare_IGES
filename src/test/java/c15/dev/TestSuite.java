package c15.dev;


import c15.dev.gestioneComunicazione.service.GestioneComunicazioneServiceImplTest;
import c15.dev.gestioneMisurazione.service.GestioneMisurazioneServiceImplTest;
import c15.dev.gestioneUtente.service.GestioneUtenteServiceImplTest;
import c15.dev.gestioneVisita.service.GestioneVisitaServiceImplTesting;
import c15.dev.integrationTesting.gestioneComunicazione.GestioneComunicazioneServiceIntegrationTest;
import c15.dev.integrationTesting.gestioneUtente.GestioneUtenteServiceIntegrationTest;
import c15.dev.integrationTesting.registrazione.GestioneRegistrazioneServiceIntegrationTest;
import c15.dev.registrazione.service.RegistrazioneServiceImplTest;
import c15.dev.repositoryTesting.CaregiverDAOTest;
import c15.dev.repositoryTesting.NotaDAOTest;
import c15.dev.repositoryTesting.UtenteRegistratoDaoTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({GestioneVisitaServiceImplTesting.class,
                GestioneUtenteServiceImplTest.class,
                RegistrazioneServiceImplTest.class,
                GestioneMisurazioneServiceImplTest.class,
                GestioneUtenteServiceIntegrationTest.class,
                GestioneRegistrazioneServiceIntegrationTest.class,
                GestioneComunicazioneServiceIntegrationTest.class,
                GestioneComunicazioneServiceImplTest.class,
                CaregiverDAOTest.class,
                NotaDAOTest.class,
                UtenteRegistratoDaoTest.class})
public class TestSuite {

}
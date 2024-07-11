package c15.dev.repositoryTesting;

import c15.dev.model.dao.CaregiverDAO;
import c15.dev.model.entity.Caregiver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CaregiverDAOTest {

    @Autowired
    private CaregiverDAO caregiverDAO;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    public void testFindCaregiverById() {
        // Creazione di un caregiver di test
        Long id = 21L;

        // Ricerca del caregiver per ID
        Caregiver foundCaregiver = (Caregiver) caregiverDAO.findById(id).orElse(null);
        Assertions.assertNotNull(foundCaregiver);
    }

    @Test
    @Transactional
    public void testFindCaregiverByIdInesistente() {
        // Creazione di un caregiver di test
        Long id = 89L;

        // Ricerca del caregiver per ID
        Caregiver foundCaregiver = (Caregiver) caregiverDAO.findById(id).orElse(null);
        Assertions.assertNull(foundCaregiver);
    }

    @Test
    @Transactional
    public void testFindCaregiverByEmail() {
        // Creazione di un caregiver di test
        String email = "paolocarminevalletta@gmail.com";

        // Ricerca del caregiver per ID
        Caregiver foundCaregiver = caregiverDAO.findByEmail(email);
        Assertions.assertNotNull(foundCaregiver);
    }

    @Test
    @Transactional
    public void testFindCaregiverByEmailInesistente() {
        // Creazione di un caregiver di test
        String email = "paolocarminevgmail.com";

        // Ricerca del caregiver per ID
        Caregiver foundCaregiver = caregiverDAO.findByEmail(email);
        Assertions.assertNull(foundCaregiver);
    }

    @Test
    @Transactional
    public void testSaveCaregiver() throws Exception {
        // Creazione di un caregiver di test
        Caregiver caregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L12I234V",
                "+393242345619",
                "Ciaone12345!",
                "heartcare016@gmail.com",
                "Annamaria",
                "Basile",
                "F"
        );

        Long id = caregiver.getId();

        Assertions.assertEquals(0L,id);
        entityManager.persist(caregiver);
        Assertions.assertNotNull((Long) caregiver.getId());
    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverNomeMancanteDBTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L12I234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "",
                "Basile",
                "F"
        );

        Long id = savedCaregiver.getId();

        Assertions.assertEquals(0L,id);
        Assertions.assertThrows(Exception.class, () -> entityManager.persist(savedCaregiver));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverCognomeMancanteDBTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L12I234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "",
                "F"
        );

        Long id = savedCaregiver.getId();

        Assertions.assertEquals(0L,id);
        Assertions.assertThrows(Exception.class, () -> entityManager.persist(savedCaregiver));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverNumeroTelefonoNonValidoDBTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L12I234V",
                "+3932425619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "Basile",
                "F"
        );

        Long id = savedCaregiver.getId();

        Assertions.assertEquals(0L,id);
        Assertions.assertThrows(Exception.class, () -> entityManager.persist(savedCaregiver));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverCodiceFiscaleNonValidoDBTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L1234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "Basile",
                "F"
        );
        Long id = savedCaregiver.getId();

        Assertions.assertEquals(0L,id);
        Assertions.assertThrows(Exception.class, () -> entityManager.persist(savedCaregiver));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverDataNonValidaDBTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L1234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "Basile",
                "F"
        );
        LocalDate dataFutura = LocalDate.of(2025, 6, 20);
        savedCaregiver.setDataDiNascita(dataFutura);

        Long id = savedCaregiver.getId();

        Assertions.assertEquals(0L,id);
        Assertions.assertThrows(Exception.class, () -> entityManager.persist(savedCaregiver));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverGenereNonValidoDBTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L1234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "Basile",
                "F"
        );
        savedCaregiver.setGenere("V");
        Long id = savedCaregiver.getId();

        Assertions.assertEquals(0L,id);
        Assertions.assertThrows(Exception.class, () -> entityManager.persist(savedCaregiver));

    }

    @Test
    @Transactional
    public void testRegistrazioneCaregiverPasswordNonValidaDBTesting() throws Exception {

        Caregiver savedCaregiver = new Caregiver(LocalDate.of(2000, 11, 18),
                "VLSPCR01L1234V",
                "+393242345619",
                "Ciaone12345!",
                "caregiver1@libero.it",
                "Annamaria",
                "Basile",
                "F"
        );
        savedCaregiver.setPassword("blablabla");
        Long id = savedCaregiver.getId();

        Assertions.assertEquals(0L,id);

        Assertions.assertThrows(Exception.class, () -> entityManager.persist(savedCaregiver));

    }
}
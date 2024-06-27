package c15.dev.repositoryTesting;

import c15.dev.model.dao.UtenteRegistratoDAO;
import c15.dev.model.entity.Caregiver;
import c15.dev.model.entity.Paziente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UtenteRegistratoDaoTest {
    @Autowired
    private UtenteRegistratoDAO utenteRegistratoDAO;

    @Autowired
    private TestEntityManager entityManager;
    @Test
    @Transactional
    public void testFindCaregiverByIdPaziente() throws Exception {
        Long id = 1L;

        Paziente paziente = (Paziente) utenteRegistratoDAO.findById(id).get();
        Caregiver car = paziente.getCaregiver();

        Assertions.assertNotNull((Long) car.getId());
    }
}

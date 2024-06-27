package c15.dev.repositoryTesting;

import c15.dev.model.dao.NotaDAO;
import c15.dev.model.dao.UtenteRegistratoDAO;
import c15.dev.model.entity.Caregiver;
import c15.dev.model.entity.Nota;
import c15.dev.model.entity.Paziente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NotaDAOTest {
    @Autowired
    private NotaDAO notaDAO;

    @Autowired
    private TestEntityManager entityManager;
    @Test
    @Transactional
    public void testFindAllNote() throws Exception {
        List<Nota> note = notaDAO.findAll();
        Assertions.assertNotNull(note);
    }

    @Test
    @Transactional
    public void testFindNoteById() throws Exception {
        Long id = 1L;
        Nota nota = notaDAO.findById(id).get();
        Assertions.assertNotNull(nota);
    }

    @Test
    @Transactional
    public void testFindNoteByIdUtente() throws Exception {
        Long id = 1L;
        List<Nota> note = notaDAO.findNoteByIdUtente(id);
        Assertions.assertNotNull(note);
    }

    @Test
    @Transactional
    public void testFindNoteInviateByIdUtente() throws Exception {
        Long id = 1L;
        List<Nota> note = notaDAO.findNoteInviateByIdUtente(id);
        Assertions.assertNotNull(note);
    }

    @Test
    @Transactional
    public void testFindNoteInviateRicevuteByIdUtente() throws Exception {
        Long id = 1L;
        List<Nota> note = notaDAO.findNoteInviateERicevuteByIdUtente(id);
        Assertions.assertNotNull(note);
    }


}
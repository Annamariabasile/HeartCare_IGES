package c15.dev.model.dao;

import c15.dev.model.entity.Paziente;
import org.springframework.stereotype.Repository;

/**
 * @author carlo.
 * creato il 1/1/2023.
 * Questa classe rappresenta il DAO della classe Paziente.
 */
@Repository
public interface PazienteDAO extends UtenteRegistratoDAO {
    /**
     * Query che ci permette di ricercare un paziente tramite la sua email.
     * @param email email del paziente.
     * @return Paziente trovato nel db.
     */
     Paziente findByEmail(String email);

    /**
     * Query che ci permette di ricercare un paziente.
     * tramite il suo codiceFiscale.
     * @param codiceFiscale del paziente.
     * @return Paziente trovato nel db.
     */
     Paziente findBycodiceFiscale(String codiceFiscale);

}

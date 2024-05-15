package c15.dev.model.dao;

import c15.dev.model.entity.UtenteRegistrato;
import org.springframework.stereotype.Repository;

@Repository
public interface CaregiverDAO extends UtenteRegistratoDAO {
    /**
     * Metodo per la ricerca tramite una email.
     * @param email
     * @return utente trovato nel db.
     */
    UtenteRegistrato findByEmail(String email);
}

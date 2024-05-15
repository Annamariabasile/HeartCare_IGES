package c15.dev.model.dao;

import c15.dev.model.entity.Caregiver;
import c15.dev.utils.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface CaregiverDAO extends UtenteRegistratoDAO {
    /**
     * Metodo per la ricerca tramite una email.
     * @param email
     * @return utente trovato nel db.
     */
    Caregiver findByEmail(String email);

}

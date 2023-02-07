package c15.dev.model.dao;

import c15.dev.model.entity.UtenteRegistrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Leopoldo Todisco.
 * Data creazione: 31/12/2022.
 * Interfaccia che indica un DAO per l'utente registrato.
 * Si usa questa interfaccia come "base".
 * per creare i DAO delle classi figlie di UtenteRegistrato.
 * Per questo motivo si usa l'annotazione @NoRepositoryBean.
 */
@Repository
public interface UtenteRegistratoDAO
        extends JpaRepository<UtenteRegistrato, Long> {

    /**
     * Query che ci permette di ricercare un utente tramite la sua email.
     * @param email email dell'utente da ricercare.
     * @return Utente corrispondente.
     */
    UtenteRegistrato findByEmail(String email);
}

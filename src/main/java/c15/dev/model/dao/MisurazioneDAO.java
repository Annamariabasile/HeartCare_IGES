package c15.dev.model.dao;

import c15.dev.model.entity.Misurazione;
import c15.dev.model.entity.UtenteRegistrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


/**
 *
 * @author Alessandro Zoccola.
 * Data creazione: 2/1/2023.
 * Interfaccia che indica un DAO per la misurazione.
 * Si usa questa interfaccia come "base"
 * per creare i DAO delle classi figlie di Misurazione.
 * Per questo motivo si usa l'annotazione @NoRepositoryBean
 */
@NoRepositoryBean
public interface MisurazioneDAO extends JpaRepository<Misurazione, String> {
}

package c15.dev.registrazione.service;

import c15.dev.model.dao.PazienteDAO;
import c15.dev.model.entity.Medico;
import c15.dev.model.entity.Paziente;
import c15.dev.model.entity.UtenteRegistrato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Mario Cicalese.
 * Creato il : 03/01/2023.
 * Questa classe rappresenta il Service utilizzato per la registrazione.
 */
public interface RegistrazioneService {
    public Paziente registraPaziente(Paziente paziente);
    public Paziente findByemail(String email);
    public Paziente findBycodiceFiscale(String codiceFiscale);
    /**
     * Firma del metodo che consente la registrazione di un medico.
     * @param med è il medico che viene registrato.
     */
    void registraMedico(Medico med);
}

package c15.dev.gestioneMisurazione.service;


import c15.dev.model.dao.DispositivoMedicoDAO;
import c15.dev.model.dao.MisurazioneDAO;
import c15.dev.model.dao.PazienteDAO;
import c15.dev.model.entity.DispositivoMedico;
import c15.dev.model.entity.Misurazione;
import c15.dev.model.entity.Paziente;
import c15.dev.model.entity.UtenteRegistrato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * @author Paolo Carmine Valletta, Alessandro Zoccola.
 * creato il: 04/01/2023.
 * Per questo service si estende l'interfaccia GestioneMisurazioneService.
 */
@Service
public class GestioneMisurazioneServiceImpl implements GestioneMisurazioneService {

    /**
     * Provvede ad accedere al db per il dispositivo.
     */
    @Autowired
    private DispositivoMedicoDAO dispositivoDao;

    /**
     * Provvede ad accedere al db per il paziente.
     */
    @Autowired
    private PazienteDAO pazienteDao;
    /**
     * provvede ad accedere al db per effettuare operazioni sulla tabella misurazione
     */
    @Autowired
    private MisurazioneDAO misurazioneDAO;

    @Override
    public boolean registrazioneDispositivo(DispositivoMedico dispositivo,
                                            long idPaziente) {
        Optional<UtenteRegistrato> paziente = pazienteDao.findById(idPaziente);
        if(paziente.isEmpty()){
            return false;
        }
        dispositivo.setPaziente((Paziente) paziente.get());
        dispositivoDao.save(dispositivo);
        return true;
    }

    @Override
    public boolean rimozioneDispositivo(DispositivoMedico dispositivo,
                                        long idPaziente) {
        Optional<UtenteRegistrato> paziente = pazienteDao.findById(idPaziente);
        if(paziente.isEmpty()){
            return false;
        }

        dispositivo.setPaziente(null);
        dispositivoDao.save(dispositivo);
        return true;
    }

    @Override
    public List<Misurazione> getMisurazioniByPaziente(Long id) {
        return (List<Misurazione>) misurazioneDAO.findByPaziente(id);
    }


}

package c15.dev.model.listeners;

import c15.dev.gestioneComunicazione.service.GestioneComunicazioneService;
import c15.dev.gestioneMisurazione.misurazioneAdapter.ControlloMisurazioni;
import c15.dev.model.entity.Misurazione;
import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leopoldo Todisco
 * Listener che si occupa di inviare email quando la misurazione è sballata.
 */
@Component
public class MisurazioneEventListener implements PostInsertEventListener {
    /**
     * service documentazione.
     */
    @Autowired
    private GestioneComunicazioneService comunicazioneService;
    /**
     * sessione di hibernate.
     */
    @Autowired
    private SessionFactory session;

    /**
     * metodo di inizializzazione del listener.
     * Si esegue come post construct.
     */
    @PostConstruct
    protected void init() {
        SessionFactoryImpl sF = session.unwrap(SessionFactoryImpl.class);
        var reg = sF
                                .getServiceRegistry()
                                .getService(EventListenerRegistry.class);
        reg.getEventListenerGroup(EventType.POST_INSERT).appendListener(this);
    }

    /**
     * Metodo che richiama il service per inviare una notifica.
     * @param event coincide con l'avvenuto insrrimento nel db.
     */
    @Override
    public void onPostInsert(final PostInsertEvent event) {
        var eventEntity = event.getEntity();
        if (eventEntity instanceof Misurazione) {
            Misurazione misurazione = (Misurazione) eventEntity;
            var str = "La misurazione del paziente "+ misurazione.getPaziente().getNome() + " " + misurazione.getPaziente().getCognome() +" ha prodotto valori anomali.";
            String oggetto = "Misurazione anomala";
            //System.out.println("IDPaziente: " + misurazione.getPaziente().getId() + " IDCaregiver: " + misurazione.getPaziente().getCaregiver().getId());
            if (ControlloMisurazioni.chiamaControllo(misurazione)) {
                comunicazioneService.sendNotifica("Misurazione con valori anomali", misurazione.getPaziente().getId());
                // todo: verificare nel front-end se la notifica arriva anche al caregiver.
                comunicazioneService.sendNotifica("Misurazione con valori anomali", misurazione.getPaziente().getCaregiver().getId());
                if (misurazione.getPaziente().getCaregiver().getEmail() != null) {
                    comunicazioneService.invioEmail(str, oggetto, misurazione.getPaziente().getCaregiver().getEmail());
                }
            }
        }
    }

    /**
     *
     * @param persister The persister for the entity in question.
     *
     * @return true o false.
     */
    @Override
    public boolean requiresPostCommitHandling(final EntityPersister persister) {
        return false;
    }
}

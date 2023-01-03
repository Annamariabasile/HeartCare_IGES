package c15.dev.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * @author Paolo Carmine Valletta.
 * Creato il: 30/12/2022.
 * Questa è la classe relativa ad una Misurazione Coagulazione.
 * I campi sono: tempo di protrobina, inr.
 *
 */
@Entity
public class MisurazioneCoagulazione extends Misurazione implements Serializable {
    /**
     * Questo campo indica il tempo di protrombina.
     */
    @Column(name = "tempo_di_protrobina", nullable = false)
    private double tempoDiProtrobina;

    /**
     * questo campo indica inr
     */
    @Column(name = "inr", nullable = false)
    private int inr;

    /**
     *
     * Costruttore senza parametri per misurazione Coagulazione.
     */
    public MisurazioneCoagulazione() {
        super();
    }

    /**
     * @param dataMisurazione rappresenta la data della misurazione
     * @param paziente rappresenta il paziente della misurazione coagulazione
     * @param dispositivoMedico rappresenta il dispositivo medico
     *                          della misurazione coagulazione
     * @param tempoDiProtrobina rappresenta il valore del tempo di protrobina
     *                          della misurazione coagulazione
     * @param inr rappresenta il valore dell'inr della misurazione coagulazione
     *
     */
    public MisurazioneCoagulazione(final GregorianCalendar dataMisurazione,
                                   final Paziente paziente,
                                   final DispositivoMedico dispositivoMedico,
                                   final double tempoDiProtrobina,
                                   final int inr) {
        super(dataMisurazione, paziente, dispositivoMedico);
        this.tempoDiProtrobina = tempoDiProtrobina;
        this.inr = inr;
    }

    /**
     *
     * @return tempoDiProtrobina
     * metodo che restituisce il valore del tempo di protrobina.
     */
    public double getTempoDiProtrobina() {
        return tempoDiProtrobina;
    }

    /**
     *
     * @return inr
     * metodo che restituisce il valore dell'inr.
     */
    public int getInr() {
        return inr;
    }

    /**
     *
     * @param tempoDiProtrobina
     * metodo che permette di impostare il valore del tempo di protrobina
     * della misurazione coagulazione.
     */
    public void setTempoDiProtrobina(final double tempoDiProtrobina) {
        this.tempoDiProtrobina = tempoDiProtrobina;
    }

    /**
     *
     * @param inr
     * metodo che permette di impostare il valore dell'inr
     * della misurazione coagulazione.
     */
    public void setInr(final int inr) {
        this.inr = inr;
    }
}

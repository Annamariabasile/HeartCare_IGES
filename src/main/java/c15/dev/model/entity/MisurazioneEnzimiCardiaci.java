package c15.dev.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.GregorianCalendar;


/**
 * @author Alessandro Zoccola.
 * Creato il: 30/12/2022.
 * Questa è la classe relativa ad una Misurazione Enzimi Cardiaci.
 * I campi sono: data della misurazione,
 *               valore mioglobina,
 *               valore creatin Kinasi,
 *               troponina Cardiaca.
 */
@Entity
public class MisurazioneEnzimiCardiaci
        extends Misurazione implements Serializable {
    /**
     * questo campo indica il valore della mioglobina.
     */
    @Column(name = "mioglobina", nullable = false)
    private double mioglobina;

    /**
     * questo campo indica il valore della creatin Kinasi.
     */
    @Column(name = "creatin_kinasi", nullable = false)
    private double creatinKinasi;

    /**
     * questo campo indica il valore della troponina cardiaca.
     */
    @Column(name = "troponina_cardiaca", nullable = false)
    private double troponinaCardiaca;

    /**
     * Costruttore senza parametri per MisurazioneEnzimiCardiaci.
     */
    public MisurazioneEnzimiCardiaci() {
        super();
    }

    /**
     * @param dataMisurazione rappresenta la data della misurazione
     * @param paziente rappresenta il paziente coinvolto nella misurazione
     * @param dispositivoMedico rappresenta il dispositivo medico con cui
     *                          è stata effettuata la misurazione
     * @param creatinKinasi rappresenta il valore della creatin Kinasi
     * @param mioglobina rappresenta il valore della mioglobina
     * @param troponinaCardiaca rappresenta il valore della troponina cardiaca
     */
    public MisurazioneEnzimiCardiaci(final GregorianCalendar dataMisurazione,
                                     final Paziente paziente,
                                     final DispositivoMedico dispositivoMedico,
                                     final double mioglobina,
                                     final double creatinKinasi,
                                     final double troponinaCardiaca) {
        super(dataMisurazione, paziente, dispositivoMedico);
        this.creatinKinasi = creatinKinasi;
        this.mioglobina = mioglobina;
        this.troponinaCardiaca = troponinaCardiaca;
    }

    /**
     *
     * @return mioglobina
     * metodo che restituisce il valore della mioglobina.
     */
    public double getMioglobina() {
        return mioglobina;
    }

    /**
     *
     * @param mioglobina
     * metodo che permette settare la mioglobina di una misurazione.
     *
     */
    public void setMioglobina(final double mioglobina) {
        this.mioglobina = mioglobina;
    }

    /**
     *
     * @return creatinKinasi
     * metodo che restituisce il valore della creatin kinasi.
     */
    public double getCreatinKinasi() {
        return creatinKinasi;
    }

    /**
     *
     * @param creatinKinasi
     * metodo che permette settare la creatin kinasi di una misurazione.
     *
     */
    public void setCreatinKinasi(final double creatinKinasi) {
        this.creatinKinasi = creatinKinasi;
    }

    /**
     *
     * @return troponinaCardiaca
     * metodo che restituisce il valore della troponina Cardiaca.
     */
    public double getTroponinaCardiaca() {
        return troponinaCardiaca;
    }


    /**
     *
     * @param troponinaCardiaca
     * metodo che permette settare la troponina cardiaca di una misurazione.
     *
     */
    public void setTroponinaCardiaca(final double troponinaCardiaca) {
        this.troponinaCardiaca = troponinaCardiaca;
    }
}

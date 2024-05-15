package c15.dev.model.entity;

import c15.dev.utils.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Setter
@SuperBuilder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
public class Caregiver extends UtenteRegistrato{
    /**
     * Lista che contiene l'elenco dei pazienti che sono stati.
     * assegnati al caregiver in questione.
     */
    @OneToMany(mappedBy = "caregiver", fetch = FetchType.LAZY)
    @JsonManagedReference("paziente-caregiver")
    private List<Paziente> elencoPazienti = new ArrayList<>();

    public Caregiver() throws Exception {
        super();
    }

    /**
     *
     * @param dataDiNascita
     * @param codiceFiscale
     * @param numeroTelefono
     * @param password
     * @param email
     * @param nome
     * @param cognome
     * @param genere
     *
     * costruttore per Caregiver.
     */
    public Caregiver(final LocalDate dataDiNascita,
                  final String codiceFiscale,
                  final String numeroTelefono,
                  final String password,
                  final String email,
                  final String nome,
                  final String cognome,
                  final String genere) throws Exception {
        super(dataDiNascita,
                codiceFiscale,
                numeroTelefono,
                password,
                email,
                nome,
                cognome,
                genere,
                Role.CAREGIVER);
    }

    /**
     * Metodo equals.
     * @param o oggetto da confrontare.
     * @return booleano.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Caregiver caregiver)) {
            return false;
        }

        return Objects.equals(getElencoPazienti(), caregiver.getElencoPazienti());
    }

    /**
     * Metodo hashCode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getElencoPazienti());
    }
}

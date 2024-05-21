package c15.dev.model.dto;

import c15.dev.model.entity.Misurazione;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MisurazioneCaregiverDTO {
    /**
     * Parametro misurazione che contiene tutti i dati di una misurazione.
     * @param misurazione.
     *
     */
    private Misurazione misurazione;

    /**
     * Parametro che contiene la categoria della misurazione.
     * @param categoria.
     */
    private String categoria;

    /**
     * Parametro che contiene il nome del paziente.
     * @param nomeCompletoPaziente.
     */
    private String nomeCompletoPaziente;
}

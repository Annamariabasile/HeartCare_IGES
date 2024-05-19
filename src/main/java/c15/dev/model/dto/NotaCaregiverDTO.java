package c15.dev.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class NotaCaregiverDTO {

    /**
     *  Parametro nome della nota.
     */
    private String nomeMittente;

    /**
     * Parametro che contiene il messaggio della nota.
     */
    private String messaggio;


    /**
     * Parametro che contiene il destinatario della nota.
     */
    private String nomeDestinatario;
}

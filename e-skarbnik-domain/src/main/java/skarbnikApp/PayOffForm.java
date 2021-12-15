package skarbnikApp;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class PayOffForm {

    @NotNull(message = "Musisz podać opis wypłaty")
    @Size(min = 1, max = 20,
            message = "Opis musi mieć od 1 do 20 znaków")
    private String name;

    @NotNull(message = "Musisz podać kwotę")
    @Positive(message="Podaj dodatnią kwotę")
    @Pattern(regexp="^\\d+(\\.\\d\\d?)?$",
            message="Podaj kwotę w formacie liczbowym w przypadku części dziesiętnych użyj < . > np. 5.55")
    private String value;

    public PayOff toPayOff(Long gradeId) {
        return new PayOff(gradeId, name, new BigDecimal(value));
    }
}


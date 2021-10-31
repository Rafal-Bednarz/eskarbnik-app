package skarbnikApp;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class PayOffForm {

    @NotNull(message = "musisz podać opis wypłaty")
    @Size(min = 1, max = 20,
            message = "opis musi mieć od 1 do 20 znaków")
    private String name;

    @NotNull(message = "musisz podać kwotę")
    @Positive(message="podaj dodatnią kwotę")
    @Pattern(regexp="^\\d+(\\.\\d\\d?)?$",
            message="podaj kwotę w formacie liczbowym w przypadku części dziesiętnych użyj < . > np. 5.55")
    private String value;

    public PayOff toPayOff(Long gradeId) {
        return new PayOff(gradeId, name, new BigDecimal(value));
    }
}


package skarbnikApp;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class PayOffForm {

    @NotNull(message = "Pole nie może być puste")
    @Size(min = 1, max = 20, message = "nazwa klasy musi mieć od 1 do 20 znaków")
    private String name;

    @NotNull(message = "Pole nie może być puste")
    @Positive(message="Podaj dodatnią kwotę")
    @Pattern(regexp="^\\d+(\\.\\d\\d?)?$",
            message="Podaj kwotę w formacie liczbowym w przypadku części dziesiętnych użyj < . > np. 5.55")
    private String value;

    public PayOff toPayOff(Long gradeId) {
        return new PayOff(gradeId, name, new BigDecimal(value));
    }
}


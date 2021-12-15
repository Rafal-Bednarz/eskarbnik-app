package skarbnikApp;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class PaymentForm {

    @NotNull(message="Musisz podać kwotę")
    @Positive(message = "Podaj dodatnią kwotę")
    @Max(value = 999, message="zbyt duża wpłata.")
    @Pattern(regexp="^\\d+(\\.\\d\\d?)?$",
            message="Podaj kwotę w formacie liczbowym w przypadku części dziesiętnych użyj < . > np. 5.55")
    private String value;

    public Payment toPayment(Long studentId) {
        return new Payment(studentId, new BigDecimal(value));
    }
}

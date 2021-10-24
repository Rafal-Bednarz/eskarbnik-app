package skarbnikApp;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class PayOffForm {

    @NotBlank(message="Pole nie może być puste")
    @NotNull(message = "Pole nie może być puste")
    private String name;

    @NotNull(message = "pole nie może być puste")
    @NotBlank(message="Pole nie może być puste")
    @Positive(message="Podaj dodatnią kwotę")
    @Pattern(regexp="^\\d+(\\.\\d\\d?)?$", message="Podaj prawidłową kwotę")
    private String value;

    public PayOff toPayOff(Long gradeId) {
        return new PayOff(gradeId, name, new BigDecimal(value));
    }
}


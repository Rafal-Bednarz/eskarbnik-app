package skarbnikApp.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class PaymentDTO {

    @Getter
    private final String placedAt;

    @Getter
    private final BigDecimal value;

}

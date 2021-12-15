package skarbnikApp.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@RequiredArgsConstructor
public class PaymentDTO {

    @Getter
    private final Date placedAt;

    @Getter
    private final BigDecimal value;

}

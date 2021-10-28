package skarbnikApp.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class StudentDTO {

    @Getter
    private final String id;
    @Getter
    private final String fullname;
    @Getter
    private final BigDecimal paymentSum;
    @Getter
    private final List<PaymentDTO> payments;

}

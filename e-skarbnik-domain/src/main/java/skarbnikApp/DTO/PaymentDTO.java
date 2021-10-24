package skarbnikApp.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentDTO {

    @Getter
    private final String placedAt;

    @Getter
    private final String value;

}

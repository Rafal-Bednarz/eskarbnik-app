package skarbnikApp.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@RequiredArgsConstructor
public class PayOffDTO {
    @Getter
    private final Date createdAt;
    @Getter
    private final String name;
    @Getter
    private final BigDecimal value;

}

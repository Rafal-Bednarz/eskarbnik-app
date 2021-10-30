package skarbnikApp.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class GradeDTO {
    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final BigDecimal budget;
    @Getter
    private final BigDecimal paymentsSum;
    @Getter
    private final BigDecimal payOffsSum;
    @Getter
    private final List<StudentDTO> students;
    @Getter
    private final List<PayOffDTO> payOffs;

}

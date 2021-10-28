package skarbnikApp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import skarbnikApp.DTO.PaymentDTO;
import skarbnikApp.DTO.StudentDTO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED, force = true)
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private final Long gradeId;

    private final String fullname;

    private BigDecimal paymentsSum = new BigDecimal(0);

    @ManyToMany(targetEntity = Payment.class)
    private List<Payment> payments = new ArrayList<>();

    public StudentDTO toDTO() {
        List<PaymentDTO> payments = new ArrayList<>();
        if(!this.payments.isEmpty()) {
            this.payments.forEach(payment -> payments.add(payment.toDTO()));
        }
        return new StudentDTO(id.toString(), fullname,
                paymentsSum, payments);
    }
}

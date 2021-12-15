package skarbnikApp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import skarbnikApp.DTO.PaymentDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor(access= AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private final Long studentId;

    private Date placedAt;

    private final BigDecimal value;

    @PrePersist
    public void placedAt() {
        this.placedAt = new Date();
    }

    public PaymentDTO toDTO() {
        return new PaymentDTO(placedAt, value);
    }

}

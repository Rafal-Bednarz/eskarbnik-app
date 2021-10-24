package skarbnikApp;


import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import skarbnikApp.DTO.PayOffDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor(access= AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Entity
public class PayOff {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private final Long gradeId;

    private Date createdAt;

    private final String name;

    private final BigDecimal value;

    @PrePersist
    public void createdAt() {
        this.createdAt = new Date();
    }

    public PayOffDTO toDTO() {
        return new PayOffDTO(DateService.dateConverter(createdAt),
                name, value.toString());
    }
}


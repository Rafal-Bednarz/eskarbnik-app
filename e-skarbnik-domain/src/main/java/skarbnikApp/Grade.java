package skarbnikApp;

import lombok.*;
import skarbnikApp.DTO.GradeDTO;
import skarbnikApp.DTO.PayOffDTO;
import skarbnikApp.DTO.StudentDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private String name;

    private BigDecimal budget = new BigDecimal(0);

    @ManyToMany(targetEntity = Student.class)
    private List<Student> students = Collections.emptyList();

    @ManyToMany(targetEntity = PayOff.class)
    private List<PayOff> payOffs = Collections.emptyList();

    public Grade(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public GradeDTO toDTO() {
        List<StudentDTO> students = new ArrayList<>();
        List<PayOffDTO> payOffs = new ArrayList<>();
        if (!this.students.isEmpty()) {
            this.students.forEach(student -> {
                students.add(student.toDTO());
            });
        }
        if(!this.payOffs.isEmpty()) {
            this.payOffs.forEach(payOff ->  {
                payOffs.add(payOff.toDTO());
            });
        }
        return new GradeDTO(id.toString(), name, budget.toString(),
                students, payOffs);
    }
}

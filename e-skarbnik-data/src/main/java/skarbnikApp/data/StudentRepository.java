package skarbnikApp.data;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import skarbnikApp.Student;

import java.math.BigDecimal;


@Transactional
public interface StudentRepository extends CrudRepository<Student, Long>{

    void deleteAllByGradeId(Long gradeId);

    @Modifying
    @Query("update Student student " +
            "set student.paymentsSum = :payments_sum  where student.id = :id")
    void updateStudent(@Param(value = "payments_sum")BigDecimal payments_sum,
                       @Param(value = "id")Long id);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into student_payments (student_id, payments_id) values (:student_id, :payments_id)")
    void updateStudentPayments(@Param(value = "student_id")Long student_id,
                               @Param(value = "payments_id") Long payments_id);

}

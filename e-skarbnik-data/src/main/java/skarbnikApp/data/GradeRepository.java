package skarbnikApp.data;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import skarbnikApp.Grade;

import java.math.BigDecimal;

@Transactional
public interface GradeRepository extends CrudRepository<Grade, Long> {

    void deleteAllByUserId(Long userId);

    @Modifying
    @Query("update Grade grade " +
            "set grade.paymentsSum = :newPaymentsSum, " +
            "grade.budget = :newGradeBudget where grade.id = :id")
    void updateGradeWhenAddPayments(@Param(value = "id") Long id,
                                    @Param(value = "newPaymentsSum") BigDecimal newPaymentsSum,
                                    @Param(value = "newGradeBudget") BigDecimal newGradeBudget);

    @Modifying
    @Query(nativeQuery = true, value = "update grade set pay_offs_sum = :newPayOffsSum, " +
            "budget = :newGradeBudget where id = :grade_id")
    void updateGradeWhenAddPayOff(@Param(value = "grade_id") Long grade_id,
                                  @Param(value = "newGradeBudget") BigDecimal newGradeBudget,
                                  @Param(value = "newPayOffsSum") BigDecimal newPayOffsSum);
    @Modifying
    @Query(nativeQuery = true, value = "insert into grade_pay_offs (grade_id, pay_offs_id) " +
                                    "values (:grade_id, :pay_offs_id)")
    void updateGradePayOffs(@Param(value = "grade_id") Long grade_id,
                                  @Param(value = "pay_offs_id") Long pay_offs_id);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into grade_students (grade_id, students_id) values (:grade_id, :students_id)")
    void updateGradeStudentsWhenAddStudent(@Param(value = "grade_id") Long grade_id,
                             @Param(value = "students_id") Long students_id);

    @Modifying
    @Query(nativeQuery = true, value = "delete from grade_students " +
            "where grade_id = :grade_id and students_id = :students_id")
    void updateGradeStudentsWhenDeleteStudent(@Param(value = "grade_id") Long grade_id,
                                              @Param(value = "students_id") Long students_id);
}

package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.DTO.PaymentDTO;
import skarbnikApp.data.GradeRepository;
import skarbnikApp.data.PaymentRepository;
import skarbnikApp.data.StudentRepository;
import skarbnikApp.services.CustomNoSuchElementException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping(path = "/payments", produces = "application/json")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepo;
    private final StudentRepository studentRepo;
    private final GradeRepository gradeRepo;

    @PostMapping(path = "/{gradeId}/{studentId}", consumes = "application/json")
    public ResponseEntity<PaymentDTO> addPayment(@PathVariable("studentId") Long studentId,
                                                 @PathVariable("gradeId") Long gradeId,
                                                 @RequestBody @Valid PaymentForm form) {
        Grade grade = gradeRepo.findById(gradeId).orElseThrow(
                () -> new CustomNoSuchElementException(gradeId.toString())
        );
        Student student = studentRepo.findById(studentId).orElseThrow(
                () -> new CustomNoSuchElementException(studentId.toString())
        );
        if(!grade.getStudents().contains(student)) {
            throw new CustomNoSuchElementException("/" + gradeId + "/" + studentId);
        }
        Payment payment = form.toPayment(studentId);
        paymentRepo.save(payment);

        BigDecimal newStudentPaymentSum = student.getPaymentsSum().add(payment.getValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal newGradeBudget =
                grade.getBudget().add(payment.getValue()).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal newPaymentsSum = grade.getPaymentsSum().add(payment.getValue())
                .setScale(2, RoundingMode.CEILING);
        gradeRepo.updateGradeWhenAddPayments(gradeId, newPaymentsSum, newGradeBudget);
        studentRepo.updateStudent(newStudentPaymentSum, studentId);
        studentRepo.updateStudentPayments(studentId, payment.getId());
        return new ResponseEntity<PaymentDTO>(payment.toDTO(), HttpStatus.CREATED);
    }
}

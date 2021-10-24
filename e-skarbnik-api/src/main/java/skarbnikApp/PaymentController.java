package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.DTO.PaymentDTO;
import skarbnikApp.data.GradeRepository;
import skarbnikApp.data.PaymentRepository;
import skarbnikApp.data.StudentRepository;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@CrossOrigin(origins = "*")
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
                                                 @RequestBody @Valid PaymentForm form, Errors errors) {
        if(errors.hasErrors() || !studentRepo.existsById(studentId)
                || !gradeRepo.existsById(gradeId) || form.getValue() == null) {
            return new ResponseEntity<PaymentDTO>(new PaymentDTO(null, null),
                    HttpStatus.BAD_REQUEST);
        }
        Student student = studentRepo.findById(studentId).get();
        Payment payment = form.toPayment(studentId);
        List<Payment> payments = student.getPayments();
        payments.add(paymentRepo.save(payment));

        BigDecimal newStudentPaymentSum = student.getPaymentsSum().add(payment.getValue())
                .setScale(2, RoundingMode.HALF_EVEN);
        student.setPaymentsSum(newStudentPaymentSum);
        studentRepo.save(student);

        Grade grade = gradeRepo.findById(gradeId).get();
        BigDecimal newGradeBudget =
                grade.getBudget().add(payment.getValue()).setScale(2, RoundingMode.HALF_EVEN);
        grade.setBudget(newGradeBudget);
        gradeRepo.save(grade);

        return new ResponseEntity<PaymentDTO>(payment.toDTO(), HttpStatus.CREATED);

    }
}

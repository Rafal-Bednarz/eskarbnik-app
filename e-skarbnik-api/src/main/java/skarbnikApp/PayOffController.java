package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.DTO.PayOffDTO;
import skarbnikApp.data.GradeRepository;
import skarbnikApp.data.PayOffRepository;
import skarbnikApp.services.CustomNoSuchElementException;
import skarbnikApp.services.RequestException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping(path = "/payOffs", produces = "application/json")
@RequiredArgsConstructor
public class PayOffController {

    private final GradeRepository gradeRepo;
    private final PayOffRepository payOffRepo;

    @PostMapping(path = "/{gradeId}", consumes = "application/json")
    public ResponseEntity<PayOffDTO> addPayOff(@PathVariable("gradeId") Long gradeId,
                                               @Valid @RequestBody PayOffForm payOffForm) {
        Grade grade = gradeRepo.findById(gradeId).orElseThrow(
                () -> new CustomNoSuchElementException(gradeId.toString())
        );
        if(grade.getBudget().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RequestException("Brak środków do wypłaty", "");
        }
        if (grade.getBudget().compareTo(BigDecimal.ZERO) > 0
                && grade.getBudget().compareTo(new BigDecimal(payOffForm.getValue())) >= 0) {
            PayOff payOff = payOffRepo.save(payOffForm.toPayOff(gradeId));
            BigDecimal newGradeBudget = grade.getBudget().subtract(payOff.getValue())
                    .setScale(2, RoundingMode.CEILING);
            BigDecimal newPayOffsSum = grade.getPayOffsSum().add(payOff.getValue())
                    .setScale(2, RoundingMode.CEILING);
            gradeRepo.updateGradeWhenAddPayOff(gradeId, newGradeBudget, newPayOffsSum);
            gradeRepo.updateGradePayOffs(gradeId, payOff.getId());
            return new ResponseEntity<PayOffDTO>(payOff.toDTO(), HttpStatus.CREATED);
        }
        throw new RequestException("Zbyt duża kwota wypłaty. Kwota nie może być wyższa niż: " + grade.getBudget(),
                "value: nieprawidłowa wartość <- podaj mniejszą kwotę");
    }
}

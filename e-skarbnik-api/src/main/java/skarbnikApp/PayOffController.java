package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.DTO.PayOffDTO;
import skarbnikApp.data.GradeRepository;
import skarbnikApp.data.PayOffRepository;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping(path = "/payOffs", produces = "application/json")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PayOffController {

    private final GradeRepository gradeRepo;
    private final PayOffRepository payOffRepo;

    @PostMapping(path = "/{gradeId}", consumes = "application/json")
    public ResponseEntity<PayOffDTO> addPayOff(@PathVariable("gradeId") Long gradeId,
                                               @Valid @RequestBody PayOffForm payOffForm, Errors errors) {
        if(!errors.hasErrors() && gradeRepo.existsById(gradeId) && payOffForm.getValue() != null) {
            Grade grade = gradeRepo.findById(gradeId).get();
            PayOff payOff = payOffForm.toPayOff(gradeId);
            if (grade.getBudget().compareTo(BigDecimal.ZERO) > 0 && grade.getBudget().compareTo(payOff.getValue()) >= 0) {
                List<PayOff> payOffList = grade.getPayOffs();
                payOffList.add(payOffRepo.save(payOff));
                grade.setPayOffs(payOffList);
                BigDecimal newGradeBudget = grade.getBudget().subtract(payOff.getValue())
                        .setScale(2, RoundingMode.CEILING);
                grade.setBudget(newGradeBudget);
                gradeRepo.save(grade);
                return new ResponseEntity<PayOffDTO>(payOff.toDTO(), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<PayOffDTO>(
                new PayOffDTO(null, null, null), HttpStatus.BAD_REQUEST);
    }
}

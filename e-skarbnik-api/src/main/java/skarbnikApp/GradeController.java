package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.DTO.GradeDTO;
import skarbnikApp.DTO.PayOffDTO;
import skarbnikApp.DTO.StudentDTO;
import skarbnikApp.data.*;
import skarbnikApp.services.CustomNoSuchElementException;
import skarbnikApp.services.RequestException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/grades", produces = "application/json")
public class GradeController {

    private final GradeRepository gradeRepo;
    private final UserRepository userRepo;
    private final StudentRepository studentRepo;
    private final PaymentRepository paymentRepo;
    private final PayOffRepository payOffRepo;

    @GetMapping
    public ResponseEntity<List<GradeDTO>> getUserGrades(@AuthenticationPrincipal User user) {

        User usr = userRepo.findByUsername(user.getUsername());
        List<GradeDTO> grades = new ArrayList<>();
        usr.getGrades().forEach(grade -> grades.add(grade.toDTO()));
        return new ResponseEntity<List<GradeDTO>>(grades, HttpStatus.OK);
    }
    @GetMapping(path = "/{gradeId}")
    public ResponseEntity<GradeDTO> getGrade(@PathVariable("gradeId") Long gradeId) {

        Grade grade = gradeRepo.findById(gradeId).orElseThrow(
                () -> new CustomNoSuchElementException(gradeId.toString()));
            return new ResponseEntity<GradeDTO>(grade.toDTO(), HttpStatus.OK);

    }
    @PostMapping(consumes = "application/json")
    public ResponseEntity<GradeDTO> addGrade(@Valid @RequestBody GradeForm gradeForm,
                                             @AuthenticationPrincipal User user) {

        Grade grade = gradeRepo.save(gradeForm.toGrade(user.getId()));
        userRepo.updateUserWhenAddGrade(user.getId(), grade.getId());

        return new ResponseEntity<GradeDTO>(grade.toDTO(), HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/{gradeId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGrade(@AuthenticationPrincipal User user, @PathVariable("gradeId") Long gradeId) {
        User usr = userRepo.findByUsername(user.getUsername());
        List<Grade> grades = usr.getGrades();
        Grade grade = gradeRepo.findById(gradeId).orElseThrow(
                () -> new CustomNoSuchElementException(gradeId.toString())
        );
        if (!grades.contains(grade)) {
            throw new RequestException("Nieprawid??owa ??cie??ka", gradeId + " <- nieprawid??owa warto????");
        }
        grade.getStudents().forEach(student -> {
            student.setPayments(Collections.emptyList());
            paymentRepo.deleteAllByStudentId(student.getId());
        });
        grade.setStudents(Collections.emptyList());
        studentRepo.deleteAllByGradeId(gradeId);
        grade.setPayOffs(Collections.emptyList());
        payOffRepo.deleteAllByGradeId(gradeId);
        userRepo.updateUserWhenDeleteGrade(user.getId(), gradeId);
        gradeRepo.deleteById(gradeId);
    }
    @PutMapping(path = "/{gradeId}/change")
    public ResponseEntity<GradeDTO> changeGradeName(@Valid @RequestBody GradeForm newName,
                                                    @PathVariable("gradeId") Long gradeId){

        Grade grade = gradeRepo.findById(gradeId).orElseThrow(
                () -> new CustomNoSuchElementException(gradeId.toString())
        );
        grade.setName(newName.getName());
        return new ResponseEntity<GradeDTO>(grade.toDTO(), HttpStatus.CREATED);
    }
    @GetMapping(path = "/{gradeId}/students")
    public ResponseEntity<List<StudentDTO>> getStudentsByGradeId(@PathVariable("gradeId") Long gradeId) {
        Grade grade = gradeRepo.findById(gradeId).orElseThrow(
                () -> new CustomNoSuchElementException(gradeId.toString())
        );
        List<StudentDTO> students = grade.toDTO().getStudents();
        return new ResponseEntity<List<StudentDTO>>(students, HttpStatus.OK);
    }
    @GetMapping(path = "/{gradeId}/payOffs")
    public ResponseEntity<List<PayOffDTO>> getPayOffsByGradeId(@PathVariable("gradeId") Long gradeId) {
        Grade grade = gradeRepo.findById(gradeId).orElseThrow(
                () -> new CustomNoSuchElementException(gradeId.toString())
        );
        List<PayOffDTO> payOffs = grade.toDTO().getPayOffs();
        return new ResponseEntity<List<PayOffDTO>>(payOffs, HttpStatus.OK);
    }
}

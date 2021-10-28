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
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
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
    @GetMapping(path = "/{id}")
    public ResponseEntity<GradeDTO> getGrade(@PathVariable("id") Long id) {

        Grade grade = gradeRepo.findById(id).get();
            return new ResponseEntity<GradeDTO>(grade.toDTO(), HttpStatus.OK);

    }
    @PostMapping(consumes = "application/json")
    public ResponseEntity<GradeDTO> addGrade(@Valid @RequestBody GradeForm gradeForm,
                                             @AuthenticationPrincipal User user) {

        User usr = userRepo.findByUsername(user.getUsername());
        Grade grade = gradeRepo.save(gradeForm.toGrade(usr.getId()));
        List<Grade> grades = usr.getGrades();
        grades.add(grade);
        usr.setGrades(grades);
        userRepo.save(usr);
        return new ResponseEntity<GradeDTO>(grade.toDTO(), HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/{gradeId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGrade(@AuthenticationPrincipal User user, @PathVariable("gradeId") Long gradeId){
            User usr = userRepo.findByUsername(user.getUsername());
            List<Grade> grades = usr.getGrades();
            Grade grade = gradeRepo.findById(gradeId).orElseThrow();
            grade.getStudents().forEach(student -> {
                student.setPayments(Collections.emptyList());
                paymentRepo.deleteAllByStudentId(student.getId());
            });
            grade.setStudents(Collections.emptyList());
            studentRepo.deleteAllByGradeId(gradeId);
            grade.setPayOffs(Collections.emptyList());
            payOffRepo.deleteAllByGradeId(gradeId);
            grades.remove(grade);
            userRepo.save(usr);
            gradeRepo.deleteById(gradeId);
    }
    @PutMapping(path = "/{gradeId}/change")
    public ResponseEntity<GradeDTO> changeGradeName(@Valid @RequestBody GradeForm newName,
                                                    @PathVariable("gradeId") Long gradeId){

        Grade grade = gradeRepo.findById(gradeId).orElseThrow();
        grade.setName(newName.getName());
        gradeRepo.save(grade);
        return new ResponseEntity<GradeDTO>(grade.toDTO(), HttpStatus.CREATED);
    }
    @GetMapping(path = "/{gradeId}/students")
    public ResponseEntity<List<StudentDTO>> getStudentsByGradeId(@PathVariable("gradeId") Long id) {
        Grade grade = gradeRepo.findById(id).orElseThrow();
        List<StudentDTO> students = grade.toDTO().getStudents();
        return new ResponseEntity<List<StudentDTO>>(students, HttpStatus.OK);
    }
    @GetMapping(path = "/{gradeId}/payOffs")
    public ResponseEntity<List<PayOffDTO>> getPayOffsByGradeId(@PathVariable("gradeId") Long gradeId) {
        Grade grade = gradeRepo.findById(gradeId).orElseThrow();
        List<PayOffDTO> payOffs = grade.toDTO().getPayOffs();
        return new ResponseEntity<List<PayOffDTO>>(payOffs, HttpStatus.OK);
    }
}

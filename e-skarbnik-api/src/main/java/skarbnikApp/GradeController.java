package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.DTO.GradeDTO;
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
        if(gradeRepo.existsById(id)) {
            Grade grade = gradeRepo.findById(id).get();
            return new ResponseEntity<GradeDTO>(grade.toDTO(), HttpStatus.OK);
        }
        return new ResponseEntity<GradeDTO>
                (new GradeDTO(null, null, null, null, null),
                        HttpStatus.NOT_FOUND);
    }
    @PostMapping(consumes = "application/json")
    public ResponseEntity<GradeDTO> addGrade(@Valid @RequestBody GradeForm gradeForm, Errors errors,
                                             @AuthenticationPrincipal User user) {
        if(errors.hasErrors()) {
            return new ResponseEntity<GradeDTO>(
                    new GradeDTO(null, null, null, null, null),
                    HttpStatus.BAD_REQUEST);
        }
        User usr = userRepo.findByUsername(user.getUsername());
        Grade grade = gradeRepo.save(gradeForm.toGrade(usr.getId()));
        List<Grade> grades = usr.getGrades();
        grades.add(grade);
        usr.setGrades(grades);
        userRepo.save(usr);
        return new ResponseEntity<GradeDTO>(grade.toDTO(), HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGrade(@AuthenticationPrincipal User user, @PathVariable("id") Long id){
        if(gradeRepo.existsById(id)) {
            User usr = userRepo.findByUsername(user.getUsername());
            List<Grade> grades = usr.getGrades();
            Grade grade = gradeRepo.findById(id).get();
            grade.getStudents().forEach(student -> {
                student.setPayments(Collections.emptyList());
                paymentRepo.deleteAllByStudentId(student.getId());
            });
            grade.setStudents(Collections.emptyList());
            studentRepo.deleteAllByGradeId(id);
            grade.setPayOffs(Collections.emptyList());
            payOffRepo.deleteAllByGradeId(id);
            grades.remove(grade);
            gradeRepo.deleteById(id);
            userRepo.save(usr);
        }
    }
    @PutMapping(path = "/{gradeId}/change")
    public ResponseEntity<GradeDTO> changeGradeName(@Valid @RequestBody GradeForm newName,
                                                    @PathVariable("gradeId") Long gradeId, Errors errors){
        if(errors.hasErrors() || !gradeRepo.existsById(gradeId)) {
            return new ResponseEntity<GradeDTO>
                    (new GradeDTO(null, null, null, null, null),
                            HttpStatus.NOT_FOUND);
        }
        Grade grade = gradeRepo.findById(gradeId).get();
        grade.setName(newName.getName());
        gradeRepo.save(grade);
        return new ResponseEntity<GradeDTO>(grade.toDTO(), HttpStatus.CREATED);

    }
}

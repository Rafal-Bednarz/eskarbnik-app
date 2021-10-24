package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.DTO.StudentDTO;
import skarbnikApp.data.GradeRepository;
import skarbnikApp.data.PaymentRepository;
import skarbnikApp.data.StudentRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/students", produces = "application/json")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepo;
    private final GradeRepository gradeRepo;
    private final PaymentRepository paymentRepo;

    @PostMapping(path = "/{gradeId}", consumes = "application/json")
    public ResponseEntity<StudentDTO> addStudent(@PathVariable("gradeId") Long gradeId,
                                                 @Valid @RequestBody StudentForm studentForm, Errors errors) {
        if(errors.hasErrors() || !gradeRepo.existsById(gradeId)) {
            return  new ResponseEntity<StudentDTO>(
                    new StudentDTO(null, null, null, null),
                    HttpStatus.BAD_REQUEST);
        }
            Grade grade = gradeRepo.findById(gradeId).get();
            Student student = studentRepo.save(studentForm.toStudent(gradeId));
            grade.getStudents().add(student);
            gradeRepo.save(grade);
            return new ResponseEntity<StudentDTO>(student.toDTO(), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{gradeId}/{studentId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable("studentId") Long studentId, @PathVariable("gradeId") Long gradeId) {
        if (gradeRepo.existsById(gradeId)) {
            Grade grade = gradeRepo.findById(gradeId).get();
            List<Student> students = grade.getStudents();
            if(studentRepo.existsById(studentId)) {
              Student student = studentRepo.findById(studentId).get();
              student.setPayments(Collections.emptyList());
              paymentRepo.deleteAllByStudentId(studentId);
              students.remove(student);
              studentRepo.deleteById(studentId);
              gradeRepo.save(grade);
            }
        }
    }
}

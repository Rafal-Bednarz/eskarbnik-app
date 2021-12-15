package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.DTO.StudentDTO;
import skarbnikApp.data.GradeRepository;
import skarbnikApp.data.PaymentRepository;
import skarbnikApp.data.StudentRepository;
import skarbnikApp.services.CustomNoSuchElementException;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/students", produces = "application/json")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepo;
    private final GradeRepository gradeRepo;
    private final PaymentRepository paymentRepo;

    @PostMapping(path = "/{gradeId}", consumes = "application/json")
    public ResponseEntity<StudentDTO> addStudent(@PathVariable("gradeId") Long gradeId,
                                                 @Valid @RequestBody StudentForm studentForm) {
        if(gradeRepo.existsById(gradeId)) {
            Student student = studentForm.toStudent(gradeId);
            gradeRepo.updateGradeStudentsWhenAddStudent(gradeId, studentRepo.save(student).getId());

            return new ResponseEntity<StudentDTO>(student.toDTO(), HttpStatus.CREATED);
        } else {
            throw new CustomNoSuchElementException(gradeId.toString());
        }
    }
    @GetMapping(path = "/{studentId}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable("studentId") Long studentId) {
        Student student = studentRepo.findById(studentId).orElseThrow(
                () -> new CustomNoSuchElementException(studentId.toString()));

        return new ResponseEntity<StudentDTO>(student.toDTO(), HttpStatus.OK);
    }


    @DeleteMapping(path = "/{gradeId}/{studentId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable("studentId") Long studentId, @PathVariable("gradeId") Long gradeId) {
        Grade grade = gradeRepo.findById(gradeId).orElseThrow(
                () -> new CustomNoSuchElementException(gradeId.toString())
        );
        List<Student> students = grade.getStudents();
        Student student = studentRepo.findById(studentId).orElseThrow(
                () -> new CustomNoSuchElementException(studentId.toString())
        );
        if (!students.contains(student)) {
            throw new CustomNoSuchElementException("/" + gradeId + "/" + studentId);
        }
        student.setPayments(Collections.emptyList());
        paymentRepo.deleteAllByStudentId(studentId);

        gradeRepo.updateGradeStudentsWhenDeleteStudent(gradeId, studentId);
        studentRepo.deleteById(studentId);
    }
}

package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import skarbnikApp.DTO.UserDTO;
import skarbnikApp.data.*;

import java.util.Collections;


@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/users", produces = "application/json")
public class UserController {

    private final UserRepository userRepo;
    private final StudentRepository studentRepo;
    private final GradeRepository gradeRepo;
    private final PaymentRepository paymentRepo;
    private final PayOffRepository payOffRepo;

    @GetMapping
    public ResponseEntity<UserDTO> getUser(@AuthenticationPrincipal User user) {
        User usr = userRepo.findByUsername(user.getUsername());
        return new ResponseEntity<UserDTO>(usr.toDTO(), HttpStatus.OK);
    }
    @DeleteMapping(path = "/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@AuthenticationPrincipal User user) {
        User usr = userRepo.findByUsername(user.getUsername());

        usr.getGrades().forEach(grade -> {
            grade.getStudents().forEach(student -> {
                student.setPayments(Collections.emptyList());
                paymentRepo.deleteAllByStudentId(student.getId());
            });
            grade.setPayOffs(Collections.emptyList());
            payOffRepo.deleteAllByGradeId(grade.getId());
            grade.setStudents(Collections.emptyList());
            studentRepo.deleteAllByGradeId(grade.getId());
        });
        user.setGrades(Collections.emptyList());
        userRepo.save(user);
        gradeRepo.deleteAllByUserId(user.getId());
        userRepo.deleteById(user.getId());

        //reset current session
        SecurityContext session = SecurityContextHolder.getContext();
        session.setAuthentication(null);
    }
}

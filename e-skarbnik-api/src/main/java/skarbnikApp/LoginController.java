package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.data.UserRepository;

import javax.validation.Valid;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public boolean login(@Valid @RequestBody UserFormLogin userForm, Errors errors) {
        if(errors.hasErrors()) {
            return false;
        }
        if (repo.existsByUsername(userForm.getUsername())) {
            User user = repo.findByUsername(userForm.getUsername());
            return  passwordEncoder.matches(userForm.getPassword(), user.getPassword())
                    && user.isEnabled();
        }
        return false;
    }
}

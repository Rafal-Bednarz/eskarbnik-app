package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.data.UserRepository;
import skarbnikApp.services.RequestException;

import javax.validation.Valid;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Boolean> login(@Valid @RequestBody UserFormLogin userForm) {

        if (!repo.existsByUsername(userForm.getUsername())) {
            throw new RequestException("Nieprawidłowy login", "username: nieprawidłowa wartość");
        }
        User user = repo.findByUsername(userForm.getUsername());
        if(!passwordEncoder.matches(userForm.getPassword(), user.getPassword())) {
            throw new RequestException("Nieprawidłowe hasło","password: nieprawidłowa wartość");
        }
        if(!user.isEnabled()) {
            throw new RequestException("Konto nieaktywne", "");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Boolean.TRUE);
    }
}

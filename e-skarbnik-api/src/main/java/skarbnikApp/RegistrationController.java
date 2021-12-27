package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.DTO.UserDTO;
import skarbnikApp.data.UserRepository;
import skarbnikApp.services.CustomNoSuchElementException;
import skarbnikApp.services.RequestException;
import skarbnikApp.services.SpringMailService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/registration", produces = "application/json")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final SpringMailService mailService;

    @Value("${allowed.origins}")
    private String frontUrl;


    @PostMapping(consumes = "application/json")
    public ResponseEntity<UserDTO> registrationUser(@Valid @RequestBody UserFormRegistration userForm) {

        if (userRepo.existsByUsername(userForm.getUsername())) {
            throw new RequestException("Nazwa użytkownika jest już zajęta", "username: nieprawidłowa wartość");
        }
        if(userRepo.existsByEmail(userForm.getEmail())) {
            throw new RequestException("Istnieje konto z podanym adresem email", "email: nieprawidłowa wartość");
        }
        User user = userForm.toUser(passwordEncoder);
        Map<String, Object> model = new HashMap<>();
        model.put("username", user.getUsername());
        model.put("link", frontUrl + "/e-skarbnik-ui/registration/" + user.getUsername() + "/" + user.getRegistrationToken());
        try {
            mailService.sendMailWithTemplate(user.getEmail(), "Link aktywacyjny", model);
        } catch (Exception e) {
            throw e;
        }
        userRepo.save(user);
        return new ResponseEntity<UserDTO>(user.toDTO(), HttpStatus.CREATED);
    }
    @GetMapping(path = "/{username}/{registrationToken}")
    public ResponseEntity<UserDTO> confirmRegistration(@PathVariable("username") String username,
                                           @PathVariable("registrationToken") String registrationToken) {
        if(!userRepo.existsByUsername(username)) {
            throw new CustomNoSuchElementException(username);
        }
        User user = userRepo.findByUsername(username);
        if (user.getRegistrationToken().equals(registrationToken)) {
            user.setActivated(true);
            userRepo.save(user);
            return new ResponseEntity<UserDTO>(user.toDTO(), HttpStatus.CREATED);
        }
        throw new CustomNoSuchElementException(registrationToken);
    }
}

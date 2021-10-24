package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.DTO.UserDTO;
import skarbnikApp.data.UserRepository;
import skarbnikApp.services.SpringMailService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/registration", produces = "application/json")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final SpringMailService mailService;


    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> registrationUser(@Valid @RequestBody UserFormRegistration userForm, Errors errors) {
        String token = "";
        if (!errors.hasErrors() && userForm != null && !userRepo.existsByUsername(userForm.getUsername())
                && !userRepo.existsByEmail(userForm.getEmail()))
          {
            User user = userRepo.save(userForm.toUser(passwordEncoder));
            token = user.getRegistrationToken();
            mailService.sendMail(user.getEmail(), "Link aktywacyjny",
                    "https://eskarbnik-app.herokuapp.com/registration/" + user.getUsername() + "/" + token);
            return new ResponseEntity<String>(token, HttpStatus.CREATED);
        }
        return new ResponseEntity<String>(token, HttpStatus.BAD_REQUEST);
    }
    @GetMapping(path = "/{username}/{registrationToken}")
    public ResponseEntity<UserDTO> confirmRegistration(@PathVariable("username") String username,
                                           @PathVariable("registrationToken") String registrationToken) {
        if(userRepo.existsByUsername(username)) {
            User user = userRepo.findByUsername(username);
            if (user.getRegistrationToken().equals(registrationToken)) {
                user.setActivated(true);
                userRepo.save(user);
                return new ResponseEntity<UserDTO>(user.toDTO(), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<UserDTO>(new UserDTO(null, null), HttpStatus.BAD_REQUEST);
    }
}

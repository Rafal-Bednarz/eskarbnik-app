package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.DTO.JwtResponse;
import skarbnikApp.data.UserRepository;
import skarbnikApp.security.JwtTokenUtil;
import skarbnikApp.security.UserRepoUserDetailsService;
import skarbnikApp.services.RequestException;

import java.util.Objects;

@RestController
@RequestMapping(path = "/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserRepoUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<JwtResponse> login(@RequestBody UserFormLogin userForm) {

        authenticate(userForm.getUsername(), userForm.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(userForm.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }
    private void authenticate(String username, String password) {
        try {
            Objects.requireNonNull(username);
            Objects.requireNonNull(password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch(BadCredentialsException e) {
            throw new RequestException("Nieprawidłowy login lub hasło", "");
        } catch(NullPointerException e) {
            throw new RequestException("Pole nie może być puste", "");
        } catch(DisabledException e) {
            throw new RequestException("Konto nieaktywne", "");
        }
    }
}

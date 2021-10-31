package skarbnikApp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.services.RequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/logout")
@CrossOrigin(origins = "*")
public class LogoutController {

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request) {

        try {
            request.logout();
        } catch (ServletException e) {
            throw new RequestException("błąd serwera", "");
        }
    }
}

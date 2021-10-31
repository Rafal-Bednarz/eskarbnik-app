package skarbnikApp;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/logout")
public class LogoutController {

    @PostMapping
    public void logout() {
        //reset session
        SecurityContext session = SecurityContextHolder.getContext();
        session.setAuthentication(null);
    }
}

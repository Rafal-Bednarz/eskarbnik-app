package skarbnikApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import skarbnikApp.services.SpringMailService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/contact", produces = "application/json")
public class ContactController {

    private final SpringMailService mailService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(consumes = "application/json")
    public void contactUs(@Valid @RequestBody MailMessage message) {
        String sendTo = "rmb@vp.pl";
            mailService.sendMail(sendTo,
                    message.getSubject(), message.getMessage());
    }
}

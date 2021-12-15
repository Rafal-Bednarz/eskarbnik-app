package skarbnikApp;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class MailMessage {

    @NotNull(message = "Musisz podać temat")
    @NotEmpty(message = "Musisz podać temat")
    private String subject;
    @NotEmpty(message = "Musisz podać treść wiadomości")
    @NotNull(message = "Musisz podać treść wiadomości")
    private String message;

}

package skarbnikApp;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MailMessage {

    @NotNull(message = "musisz podać temat")
    private String subject;
    @NotNull(message = "musisz podać treść wiadomości")
    private String message;

}

package skarbnikApp;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MailMessage {

    @NotNull(message = "pole nie może być puste")
    private String subject;
    @NotNull(message = "pole nie może być puste")
    private String message;

}

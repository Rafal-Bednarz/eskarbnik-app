package skarbnikApp;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class GradeForm {

    @NotBlank(message="pole nie może być puste")
    @NotNull(message="pole nie może być puste")
    @Pattern(regexp = "^[A-ZŁŚĆŻŹÓĄĘŃa-złąęćśźżńó0-9]+$")
    private String name;

    public Grade toGrade(Long userId) {
        return new Grade(userId, name);
    }
}

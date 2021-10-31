package skarbnikApp;

import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class GradeForm {

    @NotNull(message="musisz podać nazwę")
    @Size(min = 1, max = 10, message = "nazwa klasy musi mieć od 1 do 10 znaków")
    private String name;

    public Grade toGrade(Long userId) {
        return new Grade(userId, name);
    }
}

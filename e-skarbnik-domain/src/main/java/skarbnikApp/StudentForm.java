package skarbnikApp;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class StudentForm {

    @NotNull(message = "Musisz podać Imię i Nazwisko")
    @Pattern(regexp = "^[A-ZŻŁĆŚŹ][a-złąęńóśćźż]+(\\s(-\\s)?[A-ZŻŁĆŚŹ][a-złąęńóśćźż]+)+$",
            message="Nieprawidłowy format imienia i nazwiska")
    private String fullname;

    public Student toStudent(Long gradeId) {
        return new Student(gradeId, fullname);
    }
}

package skarbnikApp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@RequiredArgsConstructor
public class UserFormLogin {
    @Getter
    @NotNull(message = "pole nie może być puste")
    @Pattern(regexp = "^[A-Za-z0-9]{3}[A-Za-z0-9]*$")
    private final String username;

    @Getter
    @NotNull(message = "pole nie może być puste")
    @Pattern(regexp = "^[A-Za-z0-9!@#$%&?]{5}[A-Za-z0-9!@#$%&?]*$",
            message = "zbyt mało znaków, lub użyto niedopuszczalnych znaków")
    private final String password;
}

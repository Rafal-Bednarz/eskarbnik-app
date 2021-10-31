package skarbnikApp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@RequiredArgsConstructor
public class UserFormLogin {
    @Getter
    @NotNull(message = "musisz podać nazwę użytkownika")
    @Pattern(regexp = "^[A-Za-z0-9]{3}[A-Za-z0-9]*$",
            message = "nazwa musi mieć conajmniej 3 znaki, lub użyto niedopuszczalnych znaków")
    private final String username;

    @Getter
    @NotNull(message = "musisz podać hasło")
    @Pattern(regexp = "^[A-Za-z0-9!@#$%&?]{5}[A-Za-z0-9!@#$%&?]*$",
            message = "hasło musi mieć conajmniej 5 znaków, lub użyto niedopuszczalnych znaków")
    private final String password;
}

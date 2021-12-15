package skarbnikApp;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Random;

@Data
@RequiredArgsConstructor
public class UserFormRegistration {

    @NotNull(message = "Musisz podać nazwę użytkownika")
    @Pattern(regexp = "^[A-Za-z0-9]{3}[A-Za-z0-9]*$",
            message = "Nazwa musi mieć conajmniej 3 znaki, lub użyto niedopuszczalnych znaków")
    private final String username;

    @NotNull(message = "Musisz podac email")
    @Email(message = "Nieprawidłowy format adresu email")
    private final String email;

    @NotNull(message = "Musisz podać hasło")
    @Pattern(regexp = "^[A-Za-z0-9!@#$%&?]{5}[A-Za-z0-9!@#$%&?]*$",
            message = "Hasło musi mieć conajmniej 5 znaków, lub użyto niedopuszczalnych znaków")
    private final String password;

    public User toUser(PasswordEncoder passwordEncoder) {

        String generatedString = generateToken();
        return new User(username, email, passwordEncoder.encode(password), generatedString);
    }
    public String generateToken() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        int length = 30;
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < length; i++) {
            token.append(characters.charAt(random.nextInt(characters.length())));
        }
        return token.toString();
    }
}

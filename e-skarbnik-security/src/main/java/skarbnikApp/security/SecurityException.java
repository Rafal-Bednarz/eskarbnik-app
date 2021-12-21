package skarbnikApp.security;

import lombok.Getter;

public class SecurityException extends RuntimeException {

    @Getter
    public String error;

    public SecurityException(String message, String error) {
        super(message);
        this.error = error;
    }
}

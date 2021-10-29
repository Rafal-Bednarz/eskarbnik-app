package skarbnikApp.services;

import lombok.Getter;

public class RequestException extends RuntimeException {
    @Getter
    String error;

    public RequestException(String message, String error) {
        super(message);
        this.error = error;
    }
}

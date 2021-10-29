package skarbnikApp.services;

import lombok.Getter;

import java.util.NoSuchElementException;

public class CustomNoSuchElementException extends NoSuchElementException {

    @Getter
    private String error;

    public CustomNoSuchElementException(String error) {
        super();
        this.error = "'" + error + "'" + " <- nieprawidłowa wartość";
    }
}

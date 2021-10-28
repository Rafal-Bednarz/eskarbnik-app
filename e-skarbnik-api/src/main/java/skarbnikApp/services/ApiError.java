package skarbnikApp.services;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class ApiError {

    private HttpStatus status;
    private List<String> messages;

    public ApiError(HttpStatus status, List<String> messages) {
        super();
        this.status = status;
        this.messages = messages;
    }
}

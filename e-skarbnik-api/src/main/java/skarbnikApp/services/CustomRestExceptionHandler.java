package skarbnikApp.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<String>();
        List<String> messages = new ArrayList<>();
        for (FieldError error : ex.getFieldErrors()) {
            errors.add(error.getField() + ": "+ error.getDefaultMessage() + " <- błąd walidacji");
        }
        for (FieldError error : ex.getFieldErrors()) {
            messages.add(error.getDefaultMessage());
        }

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, !messages.isEmpty() ? messages.get(0) : "",
                        !errors.isEmpty() ? errors.get(0) : "");
        return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);
    }
    @ExceptionHandler(CustomNoSuchElementException.class)
    public ResponseEntity<Object> handleMethodNoSuchElement(CustomNoSuchElementException ex,
                                                            WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST
                , "Nieprawidłowa ścieżka" , ex.getError());
        return handleExceptionInternal(ex, apiError, HttpHeaders.EMPTY, apiError.getStatus(), request);
    }
    @ExceptionHandler(RequestException.class)
    public ResponseEntity<Object> handleMethodRequest(RequestException ex, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,  ex.getMessage(), ex.getError());
        return handleExceptionInternal(ex, apiError, HttpHeaders.EMPTY, apiError.getStatus(), request);
    }
}

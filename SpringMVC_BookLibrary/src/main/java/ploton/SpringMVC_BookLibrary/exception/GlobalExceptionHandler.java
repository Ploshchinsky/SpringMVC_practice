package ploton.SpringMVC_BookLibrary.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private Error error;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> allExceptionHandler(Exception ex) {
        error = new Error("Internal Server Error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityValidationException.class)
    public ResponseEntity<Error> entityValidationExceptionHandler(EntityValidationException ex) {
        error = new Error("Entity Validation Exception", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        error = new Error("Illegal Argument Exception", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Error> noSuchElementExceptionHandler(NoSuchElementException ex) {
        error = new Error("No Such Element Exception", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Error {
        private String name;
        private String message;
    }
}

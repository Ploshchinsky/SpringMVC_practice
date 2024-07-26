package ploton.SpringMVC_ProductOrderCustomer.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
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
        error = new Error("Internal Server Error", ex.toString());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JsonEntityException.class)
    public ResponseEntity<Error> jsonEntityExceptionHandler(JsonEntityException ex) {
        error = new Error("JsonEntityException", ex.toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Error> noSuchElementExceptionHandler(NoSuchElementException ex) {
        error = new Error("NoSuchElementException", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Data
    @AllArgsConstructor
    public static class Error {
        private String name;
        private String message;
    }
}

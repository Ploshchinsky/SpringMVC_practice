package ploton.SpringMVCJsonView.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    //Global handling Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleAllException(Exception ex) {
        Error error = new Error("Internal Server Error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Custom handling Exception

    //Исключение - Пользователь не найден
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> handleUserNotFoundException(UserNotFoundException ex) {
        Error error = new Error("User Not Found Exception", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Исключение - Проверка вводимых полей пользователя не пройдена
    @ExceptionHandler(WrongUserFieldsException.class)
    public ResponseEntity<Error> handleWrongUserFieldsException(WrongUserFieldsException ex) {
        Error error = new Error("Wrong User Fields Exception", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @Data
    @AllArgsConstructor
    public static class Error {
        private String name;
        private String message;
    }
}

package ploton.SpringMVC_BookLibrary.exception;

public class EntityValidationException extends IllegalArgumentException {
    public EntityValidationException(String message) {
        super(message);
    }
}

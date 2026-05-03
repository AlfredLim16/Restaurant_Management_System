package exception;

/**
 *
 * @author admin
 */
public class ValidationException extends BusinessException {

    public ValidationException(String message){
        super(message);
    }

    public ValidationException(String message, Throwable cause){
        super(message, cause);
    }
}

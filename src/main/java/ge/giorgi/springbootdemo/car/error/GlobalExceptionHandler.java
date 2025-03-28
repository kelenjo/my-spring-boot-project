package ge.giorgi.springbootdemo.car.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationError(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        logger.warn("Validation error: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("invalid-request", errorMessage));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(NotFoundException exception) {
        logger.warn("Resource not found: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO("not-found", exception.getMessage()));
    }

    @ExceptionHandler(InvalidPaginationException.class)
    public ResponseEntity<ErrorDTO> handleInvalidPagination(InvalidPaginationException ex) {
        logger.warn("Invalid pagination request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("invalid-request", ex.getMessage()));
    }

    @ExceptionHandler(EngineInUseException.class)
    public ResponseEntity<ErrorDTO> handleEngineInUseException(EngineInUseException ex) {
        logger.warn("Engine in use: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorDTO("conflict", ex.getMessage()));
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorDTO> handleInvalidLoginException(InvalidLoginException ex) {
        logger.warn("Invalid login attempt: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDTO("invalid-login", ex.getMessage()));
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<ErrorDTO> handleNotEnoughMoney(NotEnoughMoneyException ex) {
        logger.warn("Not enough money: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("money-shortage", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAccessDeniedException(AccessDeniedException ex) {
        logger.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorDTO("access-denied", ex.getMessage()));
    }

    @ExceptionHandler(InvalidPurchasingException.class)
    public ResponseEntity<ErrorDTO> handleInvalidPurchasingException(InvalidPurchasingException ex) {
        logger.warn("Invalid purchase attempt: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("invalid-purchase", ex.getMessage()));
    }

    @ExceptionHandler(PatternSyntaxException.class)
    public ResponseEntity<ErrorDTO> handlePatterSyntaxException(PatternSyntaxException ex){
        logger.warn("Pattern syntax is invalid: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorDTO("Invalid-syntax", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex){
        logger.warn("Illegal argument detected: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("Illegal-argumnet", ex.getMessage()));
    }

    @ExceptionHandler(ImageAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handelImageAlreadyExistsException(ImageAlreadyExistsException ex){
        logger.warn("Image already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("Upload-denied", ex.getMessage()));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorDTO> handleUnexpectedErrors(Exception ex) {
//        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ErrorDTO("server-error", "An unexpected error occurred. Please try again later."));
//    }
}

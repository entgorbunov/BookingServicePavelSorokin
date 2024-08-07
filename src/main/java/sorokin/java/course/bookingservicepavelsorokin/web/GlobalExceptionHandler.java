package sorokin.java.course.bookingservicepavelsorokin.web;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        LOGGER.error("Get common exception", ex);
        ErrorMessageResponse messageResponse = new ErrorMessageResponse(
                "Internal error",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(500).body(messageResponse);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(Exception ex) {
        LOGGER.error("Get entity exception", ex);
        ErrorMessageResponse messageResponse = new ErrorMessageResponse(
                "Entity not found",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(404).body(messageResponse);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleIllegalArgument(Exception ex) {
        LOGGER.error("Get bad request", ex);
        ErrorMessageResponse messageResponse = new ErrorMessageResponse(
                "Bad request",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(400).body(messageResponse);
    }
}

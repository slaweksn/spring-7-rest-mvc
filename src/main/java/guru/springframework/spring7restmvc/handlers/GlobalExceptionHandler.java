package guru.springframework.spring7restmvc.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import guru.springframework.spring7restmvc.responses.ErrorResponse;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        
        // Sprawdzamy, czy pole, którego konwersja się nie powiodła, oczekiwało typu UUID
        if (Objects.equals(ex.getRequiredType(), java.util.UUID.class)) {
            String message = String.format("Parametr '%s' musi być poprawnym identyfikatorem UUID. Podana wartość: '%s'", 
                    ex.getName(), ex.getValue());
            
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid UUID Format",
                    message
            );
            
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        // Obsługa innych błędów dopasowania typów (opcjonalnie)
        ErrorResponse defaultError = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Niepoprawny typ parametru: " + ex.getName()
        );
        return new ResponseEntity<>(defaultError, HttpStatus.BAD_REQUEST);
    }
}


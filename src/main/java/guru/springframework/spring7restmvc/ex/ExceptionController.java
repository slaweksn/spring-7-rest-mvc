package guru.springframework.spring7restmvc.ex;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by jt, Spring Framework Guru.
 */
@ControllerAdvice
public class ExceptionController {
	
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
    	
    	//return ResponseEntity.notFound().build();
        return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
    	//return ResponseEntity.ok(Map.of("error", ex.getMessage()));
    }
}
package purihuaman.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleAllExceptions(Exception ex) {
		final HttpStatus INTERNAL_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
		return ResponseEntity
			.status(INTERNAL_ERROR)
			.body(new ApiRequestException(true, "An unexpected error occurred", ex.getMessage(), INTERNAL_ERROR));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> handleNotFoundException(Exception ex) {
		final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;

		return ResponseEntity
			.status(NOT_FOUND)
			.body(new ApiRequestException(true, "Entity not found", ex.getMessage(), NOT_FOUND));
	}
}

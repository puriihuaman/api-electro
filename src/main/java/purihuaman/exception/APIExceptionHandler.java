package purihuaman.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import purihuaman.dto.APIExceptionDTO;
import purihuaman.enums.APIError;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class APIExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<APIExceptionDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));
		return new ResponseEntity<>(
			new APIExceptionDTO(
				APIError.INVALID_REQUEST_DATA.getMessage(),
				APIError.INVALID_REQUEST_DATA.getDescription(),
				APIError.INVALID_REQUEST_DATA.getStatus().value(),
				errors,
				ZonedDateTime.now().toLocalDateTime()
			), HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<APIExceptionDTO> handleAllExceptions(Exception ex) {
		return new ResponseEntity<>(
			new APIExceptionDTO(
				APIError.INTERNAL_SERVER_ERROR.getMessage(),
				APIError.INTERNAL_SERVER_ERROR.getDescription(),
				APIError.INTERNAL_SERVER_ERROR.getStatus().value(),
				null,
				ZonedDateTime.now().toLocalDateTime()
			), APIError.INTERNAL_SERVER_ERROR.getStatus()
		);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<APIExceptionDTO> handleNotFoundException(NoHandlerFoundException ex) {
		return new ResponseEntity<>(
			new APIExceptionDTO(
				APIError.ENDPOINT_NOT_FOUND.getMessage(),
				APIError.ENDPOINT_NOT_FOUND.getDescription(),
				APIError.ENDPOINT_NOT_FOUND.getStatus().value(),
				null,
				ZonedDateTime.now().toLocalDateTime()
			), APIError.ENDPOINT_NOT_FOUND.getStatus()
		);
	}

	@ExceptionHandler(APIRequestException.class)
	public ResponseEntity<APIExceptionDTO> handleApiRequestException(final APIRequestException ex) {
		// 1. Create payload containing exception details
		APIExceptionDTO apiException = new APIExceptionDTO(
			ex.getMessage(),
			ex.getDescription(),
			ex.getStatusCode().value(),
			ex.getReasons(),
			ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime()
		);

		// 2. Return response entity
		return new ResponseEntity<>(apiException, ex.getStatusCode());
	}
}

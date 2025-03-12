package purihuaman.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import purihuaman.dto.APIExceptionDTO;
import purihuaman.enums.APIError;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class APIExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<APIExceptionDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach((error) -> errors.put(
			error.getField(),
			error.getDefaultMessage()
		));
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

	@ExceptionHandler(APIRequestException.class)
	public ResponseEntity<APIExceptionDTO> handleApiRequestException(final APIRequestException ex) {
		APIExceptionDTO apiException = new APIExceptionDTO(
			ex.getMessage(),
		                                                   ex.getDescription(),
		                                                   ex.getStatusCode().value(),
		                                                   ex.getReasons(),
		                                                   ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime()
		);

		return new ResponseEntity<>(apiException, ex.getStatusCode());
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

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<APIExceptionDTO> handleAuthenticationException(AuthenticationException ex) {
		return new ResponseEntity<>(
			new APIExceptionDTO(
				"Authentication failed",
			                    ex.getMessage(),
			                    HttpStatus.UNAUTHORIZED.value(),
			                    Map.of(),
			                    LocalDateTime.now(ZoneOffset.UTC)
			), HttpStatus.UNAUTHORIZED
		);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<APIExceptionDTO> handleAccessDeniedException(AccessDeniedException ex) {
		APIExceptionDTO error = new APIExceptionDTO(
			"Access denied",
		                                            "Insufficient privileges",
		                                            HttpStatus.FORBIDDEN.value(),
		                                            Map.of(),
		                                            LocalDateTime.now(ZoneOffset.UTC)
		);
		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
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
}

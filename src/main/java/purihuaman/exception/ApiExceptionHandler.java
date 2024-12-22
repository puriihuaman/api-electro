package purihuaman.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(value = {ApiRequestException.class})
	public ResponseEntity<Object> handleApiRequestException(ApiRequestException exception)
	{

		// 1. Create payload containing exception details
		ApiException apiException = new ApiException(
			exception.getMessage(),
			exception.getDescription(),
			exception.getStatusCode().value(),
			ZonedDateTime.now(ZoneId.of("Z"))
		);

		// 2. Return response entity
		return new ResponseEntity<>(apiException, exception.getStatusCode());
	}
}

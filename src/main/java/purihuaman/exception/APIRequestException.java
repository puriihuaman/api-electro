package purihuaman.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import purihuaman.enums.APIError;

import java.util.Map;

@Getter
public class APIRequestException extends RuntimeException {
	private final Boolean hasError;
	private final String message;
	private final String description;
	private final HttpStatus statusCode;
	private Map<String, String> reasons;
	private APIError apiError;

	public APIRequestException(APIError error) {
		this.hasError = true;
		this.message = error.getMessage();
		this.description = error.getDescription();
		this.statusCode = error.getStatus();
		this.apiError = error;
	}

	public APIRequestException(
		final String message,
		final String description,
		final HttpStatus statusCode,
		final Map<String, String> reasons
	)
	{
		this.hasError = true;
		this.message = message;
		this.description = description;
		this.statusCode = statusCode;
		this.reasons = reasons;
	}

	public APIRequestException(final APIError error, final String message, String description) {
		this.hasError = true;
		this.message = message;
		this.description = description;
		this.statusCode = error.getStatus();
		this.apiError = error;
	}
}

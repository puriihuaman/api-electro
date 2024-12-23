package purihuaman.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiRequestException extends RuntimeException {
	private final Boolean hasError;
	private final String message;
	private final String description;
	private final HttpStatus statusCode;
}

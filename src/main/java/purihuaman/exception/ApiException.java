package purihuaman.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiException {
	private final Boolean hasError = true;
	private final String message;
	private final String description;
	private final Integer statusCode;
	private final ZonedDateTime timestamp;
}

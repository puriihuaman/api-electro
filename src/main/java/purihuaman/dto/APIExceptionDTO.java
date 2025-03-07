package purihuaman.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class APIExceptionDTO {
	private final boolean hasError = true;
	private final String message;
	private final String description;
	private final int code;
	private final Map<String, String> reasons;
	private final LocalDateTime timestamp;
}

package purihuaman.util;

import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public record ApiResponse(Boolean hasError, String message, Object data, Integer statusCode, ZonedDateTime timestamp) {
	public static ApiResponse success(String message, Object data, HttpStatus status) {
		return new ApiResponse(false, message, data, status.value(), ZonedDateTime.now(ZoneId.of("Z")));
	}
}
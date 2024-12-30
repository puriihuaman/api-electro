package purihuaman.util;

import lombok.Getter;
import purihuaman.enums.APISuccess;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
public class APIResponse {
	private final Object data;
	private final Boolean hasError;
	private final String message;
	private final Integer statusCode;
	private final LocalDateTime timestamp;

	public APIResponse(final APISuccess apiSuccess, final Object data) {
		this.data = data;
		this.hasError = false;
		this.message = apiSuccess.getMessage();
		this.statusCode = apiSuccess.getStatus().value();
		this.timestamp = LocalDateTime.now(ZoneId.systemDefault());
	}
}
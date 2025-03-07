package purihuaman.util;

import lombok.Getter;
import purihuaman.enums.APISuccess;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
public class APIResponse {
	private final Object data;
	private final boolean hasError;
	private final String message;
	private final int statusCode;
	private final LocalDateTime timestamp;

	public APIResponse(APISuccess apiSuccess, Object data) {
		this.data = data;
		this.hasError = false;
		this.message = apiSuccess.getMessage();
		this.statusCode = apiSuccess.getStatus().value();
		this.timestamp = LocalDateTime.now(ZoneId.systemDefault());
	}
}
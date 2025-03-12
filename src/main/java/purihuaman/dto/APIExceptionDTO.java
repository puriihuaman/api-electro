package purihuaman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(
	description = "Standard format for error responses in the API",
	examples = """
		{
			"hasError": true,
			"message": "Invalid data",
			"description": "Request data contains invalid values or incorrect format.",
			"code": 400,
			"reasons": {
				"firstName": "The field must contain at least 2 characters.",
				"username": "The field must contain at least 3 characters."
			},
			"timestamp": "2025-03-07T10:12:35.9687337"
		}
		"""

)
@Builder
@Getter
@Setter
public class APIExceptionDTO {
	@Schema(description = "Indicator of error", example = "true", defaultValue = "true")
	private final boolean hasError = true;

	@Schema(description = "Short error message", example = "Bad request")
	private final String message;

	@Schema(description = "Detailed technical description of the error", example = "The request is invalid")
	private final String description;

	@Schema(
		description = "HTTP status code of the error",
		example = "400",
		allowableValues = {"400", "401", "403", "404", "409", "422", "500"}
	)
	private final int code;

	@Schema(
		description = "Detail of validation errors by field", example = """
		{
			"user.username": "The field can only contain letters and numbers",
			"user.username": "The field must contain at least 3 characters"
		}
		"""
	)
	private final Map<String, String> reasons;

	@Schema(
		description = "Response timestamp in ISO 8601 format",
		type = "string",
		format = "date-time",
		example = "2023-10-15T14:23:45Z"
	)
	private final LocalDateTime timestamp;

	public APIExceptionDTO(
		String message,
		String description,
		int code,
		Map<String, String> reasons,
		LocalDateTime timestamp
	)
	{
		super();
		this.message = message;
		this.description = description;
		this.code = code;
		this.reasons = reasons;
		this.timestamp = timestamp;
	}
}

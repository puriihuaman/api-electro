package purihuaman.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.MediaType;
import purihuaman.dto.CategoryDTO;
import purihuaman.dto.ProductDTO;
import purihuaman.dto.RoleDTO;
import purihuaman.dto.UserDTO;
import purihuaman.enums.APISuccess;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Schema(
	description = "Standard format for all API responses",
	subTypes = {APISuccess.class, CategoryDTO.class, ProductDTO.class, UserDTO.class, RoleDTO.class}
)
@Getter
public class APIResponseData {
	@Schema(
		description = "Primary data of the response. Can be an object, list, or empty value.",
		oneOf = {UserDTO.class, CategoryDTO.class, ProductDTO.class, RoleDTO.class},
		subTypes = {UserDTO.class, CategoryDTO.class, ProductDTO.class, RoleDTO.class},
		contentMediaType = MediaType.APPLICATION_JSON_VALUE,
		nullable = true
	)
	private final Object data;

	@Schema(
		description = "Indicator of whether the response contains an error", example = "false", defaultValue = "false"
	)
	private final boolean hasError;

	@Schema(
		description = "Descriptive message of the result of the operation", example = "Resource successfully obtained"
	)
	private final String message;

	@Schema(
		description = "HTTP status code corresponding to the response",
		example = "200",
		allowableValues = {"200", "201"}
	)
	private final int statusCode;

	@Schema(
		description = "Response timestamp in ISO 8601 format",
		type = "string",
		format = "date-time",
		example = "2023-10-15T14:23:45Z"
	)
	private final LocalDateTime timestamp;

	public APIResponseData(APISuccess apiSuccess, Object data) {
		this.data = data;
		this.hasError = false;
		this.message = apiSuccess.getMessage();
		this.statusCode = apiSuccess.getStatus().value();
		this.timestamp = LocalDateTime.now(ZoneId.systemDefault());
	}
}
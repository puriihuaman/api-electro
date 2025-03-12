package purihuaman.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import purihuaman.enums.RoleType;

@Schema(description = "Response with JWT token for successful authentication.")
public record TokenResponse(
	@Schema(
		description = "JWT token for access to protected endpoints",
		example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.xxxxx.xxxxx"
	)
	@JsonProperty("access_token") String accessToken,
	@Schema(description = "Username that is used for login", example = "perez")
	@JsonProperty("username") String username,
	@Schema(description = "Role of the user who logs in", example = "Admin")
	@JsonProperty("role") RoleType role
) {}

package purihuaman.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import purihuaman.enums.RoleType;

public record TokenResponse(
	@JsonProperty("access_token") String accessToken,
	@JsonProperty("username") String username,
	@JsonProperty("role") RoleType role
) {}

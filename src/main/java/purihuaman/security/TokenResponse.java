package purihuaman.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
	@JsonProperty("access_token") String accessToken
) {}

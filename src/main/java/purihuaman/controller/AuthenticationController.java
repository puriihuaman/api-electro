package purihuaman.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import purihuaman.dto.APIExceptionDTO;
import purihuaman.dto.LoginRequestDTO;
import purihuaman.security.SecurityService;
import purihuaman.security.TokenResponse;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Tag(name = "Auth", description = "Operations for authentication management")
public class AuthenticationController {
	private final SecurityService securityService;

	public AuthenticationController(SecurityService securityService) {
		this.securityService = securityService;
	}

	@Operation(
		summary = "Authenticate user",
		description = "Verify credentials and generate a JWT access token",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "User data for login",
			required = true,
			content = @Content(
				schema = @Schema(implementation = LoginRequestDTO.class),
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				examples = @ExampleObject(
					value = """
						{
							"username": "admin",
							"password": "AdM¡N&20_25"
						}
						"""
				)
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Authentication successful",
				content = @Content(
					schema = @Schema(implementation = TokenResponse.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
						value = """
							{
							  "access_token": "eyJhbGciOiJIUzI1NiJ9.xxx.xxx.xxx.xxx.xxx",
							  "username": "perez",
							  "role": "Admin"
							}
							"""
					)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Validation error in request body",
				content = @Content(
					schema = @Schema(implementation = APIExceptionDTO.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
						value = """
							{
								"hasError": true,
								"message": "Invalid data",
								"description": "Request data contains invalid values or incorrect format.",
								"code": 400,
								"reasons": {
									"username": "The field is empty."
								},
								"timestamp": "2025-03-07T22:08:21.178014"
							}
							"""
					)
				)
			),
			@ApiResponse(
				responseCode = "401",
				description = "Invalid credentials",
				content = @Content(
					schema = @Schema(implementation = APIExceptionDTO.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
						value = """
							{
								"message": "Invalid Credentials",
								"description": "Incorrect username or password. Please try again with the correct credentials.",
								"code": 401,
								"reasons": null,
								"timestamp": "2025-03-10T22:58:53.8336704",
								"hasError": true
							}
							"""
					)
				)
			)
		}
	)
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> authenticate(@Valid @RequestBody LoginRequestDTO user) {
		TokenResponse tokenResponse = securityService.login(user);

		return ResponseEntity.ok(tokenResponse);
	}
}

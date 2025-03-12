package purihuaman.annotations;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import purihuaman.dto.APIExceptionDTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(
	value = {
		@ApiResponse(
			responseCode = "400", description = "Bad Request", content = @Content(
			mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIExceptionDTO.class)
		)
		), @ApiResponse(
		responseCode = "401", description = "Unauthorized", content = @Content(
		mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIExceptionDTO.class)
	)
	), @ApiResponse(
		responseCode = "403", description = "Forbidden", content = @Content(
		mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIExceptionDTO.class)
	)
	), @ApiResponse(
		responseCode = "404", description = "Resource not found", content = @Content(
		mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIExceptionDTO.class)
	)
	), @ApiResponse(
		responseCode = "409", description = "Conflict", content = @Content(
		mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIExceptionDTO.class)
	)
	), @ApiResponse(
		responseCode = "500", description = "Internal Server Error", content = @Content(
		mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = APIExceptionDTO.class)
	)
	)
	}
)
public @interface SwaggerApiResponses {}

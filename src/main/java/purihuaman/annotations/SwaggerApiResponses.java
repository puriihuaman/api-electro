package purihuaman.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import purihuaman.dto.APIExceptionDTO;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(
	value = {
		@ApiResponse(
			responseCode = "400",
			description = "Solicitud incorrecta",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionDTO.class))
		), @ApiResponse(
		responseCode = "401",
		description = "No autorizado",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionDTO.class))
	), @ApiResponse(
		responseCode = "403",
		description = "Prohibido",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionDTO.class))
	), @ApiResponse(
		responseCode = "404",
		description = "Recurso no encontrado",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionDTO.class))
	), @ApiResponse(
		responseCode = "409",
		description = "Conflicto",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionDTO.class))
	), @ApiResponse(
		responseCode = "500",
		description = "Error interno del servidor",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionDTO.class))
	)
	}
)
public @interface SwaggerApiResponses {}

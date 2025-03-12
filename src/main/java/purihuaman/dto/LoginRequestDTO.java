package purihuaman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(description = "DTO for user authentication. Contains access credentials.")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequestDTO {
	@Schema(
		description = "Unique username for identification.",
		example = "perez",
		requiredMode = Schema.RequiredMode.REQUIRED,
		minLength = 2,
		maxLength = 15
	)
	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 2, max = 15, message = "{user.size}")
	private String username;

	@Schema(
		description = "Secure Access Password.",
		example = "Jv4N&_&P3r35",
		requiredMode = Schema.RequiredMode.REQUIRED,
		format = "password"
	)
	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	private String password;
}

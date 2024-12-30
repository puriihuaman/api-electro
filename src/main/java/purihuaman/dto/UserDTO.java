package purihuaman.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {
	private String userId;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Size(min = 2, message = "${user.size}")
	@Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "${user.pattern}")
	private String firstName;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Size(min = 2, message = "${user.size}")
	@Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "${user.pattern}")
	private String lastName;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Email(message = "${user.email}")
	private String email;

	private String username;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
		message = "${user.pattern.password}"
	)
	private String password;
}

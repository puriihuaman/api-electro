package purihuaman.dto;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import purihuaman.entity.RoleEntity;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
	private String id;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 2, message = "{user.size}")
	@Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*", message = "{user.pattern}")
	private String firstName;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 2, message = "{user.size}")
	@Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "{user.pattern}")
	private String lastName;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Email(message = "{user.email}")
	private String email;

	private String username;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{6,}$", message = "{user.password}")
	private String password;

	@Valid
	@ManyToOne
	private RoleEntity role;
}

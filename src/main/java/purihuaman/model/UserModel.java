package purihuaman.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
public class UserModel {
	@Id
	@Column(name = "user_id", length = 40)
	private String userId;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Size(min = 2, message = "${user.size}")
	@Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "${user.pattern}")
	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Size(min = 2, message = "${user.size}")
	@Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "${user.pattern}")
	@Column(name = "last_name", length = 60, nullable = false)
	private String lastName;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Email(message = "${user.email}")
	@Column(name = "email", length = 50, nullable = false, unique = true)
	private String email;

	@Column(name = "username", length = 15, nullable = false, unique = true)
	private String username;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Column(name = "password", length = 100, nullable = false)
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
		message = "${user.pattern.password}"
	)
	private String password;
}

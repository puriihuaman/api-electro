package purihuaman.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Schema(description = "Represents a user in the system")
@Entity(name = "User")
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
@Data
public class UserEntity {
	@Schema(
		description = "Unique user ID",
		example = "02e6ffb2-4c00-48e3-a673-eaa614cb1c09",
		type = "String",
		accessMode = Schema.AccessMode.READ_ONLY,
		hidden = true
	)
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@Schema(
		description = "First name of the user",
		example = "Juan",
		type = "String",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 2, message = "{user.size}")
	@Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*", message = "{user.pattern}")
	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;

	@Schema(
		description = "Last name of the user",
		example = "Perez",
		type = "String",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 2, message = "{user.size}")
	@Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*", message = "{user.pattern}")
	@Column(name = "last_name", length = 60, nullable = false)
	private String lastName;

	@Schema(
		description = "Valid user email",
		example = "juan.perez@example.com",
		type = "String",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Email(message = "{user.email}")
	@Column(name = "email", length = 50, nullable = false, unique = true)
	private String email;

	@Schema(
		description = "Username of the user",
		example = "perez",
		type = "String",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 3, max = 15, message = "{user.username.size}")
	@Pattern(regexp = "^[a-z0-9]*$", message = "{user.username.pattern}")
	@Column(name = "username", length = 15, nullable = false, unique = true)
	private String username;

	@Schema(
		description = "Password of the user (must meet security constraints)",
		example = "Jv4N&_&P3r35",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Column(name = "password", length = 100, nullable = false)
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{6,}$", message = "{user.password}")
	private String password;

	@Schema(description = "Role assigned to the user", allowableValues = {"ADMIN", "USER", "INVITED"})
	@Valid
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
	private RoleEntity role;
	//	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
}

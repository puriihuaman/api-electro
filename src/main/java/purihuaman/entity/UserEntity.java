package purihuaman.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity(name = "User")
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
@Data
public class UserEntity {
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 2, message = "{user.size}")
	@Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*", message = "{user.pattern}")
	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 2, message = "{user.size}")
	@Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*", message = "{user.pattern}")
	@Column(name = "last_name", length = 60, nullable = false)
	private String lastName;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Email(message = "{user.email}")
	@Column(name = "email", length = 50, nullable = false, unique = true)
	private String email;

	@Column(name = "username", length = 15, nullable = false, unique = true)
	private String username;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Column(name = "password", length = 100, nullable = false)
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{6,}$", message = "{user.password}")
	private String password;

	//	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Valid
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
	private RoleEntity role;
}

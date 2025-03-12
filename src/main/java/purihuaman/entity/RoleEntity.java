package purihuaman.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import purihuaman.enums.RoleType;

@Schema(description = "Represents a role in the system")
@Entity(name = "Role")
@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"role_name"})})
@Data
public class RoleEntity {
	@Schema(
		description = "Unique role ID",
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
		example = "Admin",
		type = "String",
		requiredMode = Schema.RequiredMode.REQUIRED,
		allowableValues = {"ADMIN", "USER", "INVITED"}
	)
	@NotNull(message = "{field.null}")
	@Column(name = "role_name", length = 20, nullable = false, unique = true)
	@Enumerated(EnumType.STRING)
	private RoleType roleName;

	/*@OneToMany(mappedBy = "role")
	//@JsonIgnore
	//
	//private List<UserEntity> users;
	 */
}

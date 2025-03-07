package purihuaman.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import purihuaman.enums.RoleType;

import java.util.List;

@Entity(name = "Role")
@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"role_name"})})
@Data
public class RoleEntity {
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

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

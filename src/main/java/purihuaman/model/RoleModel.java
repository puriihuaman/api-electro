package purihuaman.model;

import jakarta.persistence.*;
import lombok.Data;
import purihuaman.enums.ERole;

@Data
@Entity
@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"role_name"})})
public class RoleModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private String roleId;

	@Enumerated(EnumType.STRING)
	@Column(name = "role_name", length = 20, nullable = false, unique = true)
	private ERole roleName;
}

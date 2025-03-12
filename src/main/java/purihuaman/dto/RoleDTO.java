package purihuaman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import purihuaman.entity.UserEntity;
import purihuaman.enums.RoleType;

import java.util.List;

@Schema(description = "Represents a role in the system")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RoleDTO {
	@Schema(
		description = "Unique role ID",
		example = "02e6ffb2-4c00-48e3-a673-eaa614cb1c09",
		type = "String",
		accessMode = Schema.AccessMode.READ_ONLY,
		hidden = true
	)
	private String id;

	@Schema(
		description = "First name of the user",
		example = "Admin",
		type = "String",
		requiredMode = Schema.RequiredMode.REQUIRED,
		allowableValues = {"ADMIN", "USER", "INVITED"}
	)
	@NotNull(message = "{field.null}")
	@Enumerated(EnumType.STRING)
	private RoleType roleName;

	@OneToMany(mappedBy = "role")
	private List<UserEntity> users;
}

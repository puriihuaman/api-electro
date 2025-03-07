package purihuaman.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import purihuaman.entity.UserEntity;
import purihuaman.enums.RoleType;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RoleDTO {
	private String id;

	@NotNull(message = "{field.null}")
	@Enumerated(EnumType.STRING)
	private RoleType roleName;

	@OneToMany(mappedBy = "role")
	private List<UserEntity> users;
}

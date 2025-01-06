package purihuaman.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import purihuaman.enums.ERole;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RoleDTO {
	private String roleId;

	@Enumerated(EnumType.STRING)
	private ERole roleName;
}

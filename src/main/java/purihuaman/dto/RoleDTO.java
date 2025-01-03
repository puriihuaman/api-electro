package purihuaman.dto;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import purihuaman.enums.ERole;
import purihuaman.model.UserModel;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoleDTO {
	private String roleId;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Enumerated(EnumType.STRING)
	private ERole roleName;

	@Valid
	private List<UserModel> users = new ArrayList<>();

}

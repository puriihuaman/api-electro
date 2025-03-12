package purihuaman.service;

import purihuaman.dto.RoleDTO;
import purihuaman.enums.RoleType;

public interface RoleService {
	RoleDTO createRole(RoleType roleName);

	RoleDTO findRoleByRoleName(RoleType roleName);
}

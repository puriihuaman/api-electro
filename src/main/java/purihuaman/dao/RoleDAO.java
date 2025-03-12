package purihuaman.dao;

import purihuaman.entity.RoleEntity;
import purihuaman.enums.RoleType;

import java.util.Optional;

public interface RoleDAO {
	Optional<RoleEntity> findRoleByRoleName(RoleType roleName);

	RoleEntity createRole(RoleEntity role);
}

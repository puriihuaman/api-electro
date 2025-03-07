package purihuaman.dao.impl;

import org.springframework.stereotype.Repository;
import purihuaman.dao.RoleDAO;
import purihuaman.dao.repository.RoleRepository;
import purihuaman.entity.RoleEntity;
import purihuaman.enums.RoleType;

import java.util.Optional;

@Repository
public class RoleDAOImpl implements RoleDAO {
	private final RoleRepository roleRepository;

	public RoleDAOImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Optional<RoleEntity> findRoleByRoleName(RoleType role) {
		return roleRepository.findRoleByRoleName(role);
	}

	@Override
	public Optional<RoleEntity> findRoleById(String roleId) {
		return roleRepository.findById(roleId);
	}

	@Override
	public RoleEntity createRole(RoleEntity role) {
		return roleRepository.save(role);
	}
}

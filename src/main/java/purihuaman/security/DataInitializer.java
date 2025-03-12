package purihuaman.security;

import jakarta.annotation.PostConstruct;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import purihuaman.dto.RoleDTO;
import purihuaman.dto.UserDTO;
import purihuaman.entity.RoleEntity;
import purihuaman.enums.APIError;
import purihuaman.enums.RoleType;
import purihuaman.exception.APIRequestException;
import purihuaman.mapper.RoleMapper;
import purihuaman.service.RoleService;
import purihuaman.service.UserService;

import java.util.Arrays;
import java.util.UUID;

@Component
public class DataInitializer {
	private final UserService userService;
	private final RoleService roleService;
	private final RoleMapper roleMapper;

	public DataInitializer(UserService userService, RoleService roleService, RoleMapper roleMapper) {
		this.userService = userService;
		this.roleService = roleService;
		this.roleMapper = roleMapper;
	}

	@PostConstruct
	@Transactional
	public void init() {
		this.createRoles();
		this.createAdminUser();
	}

	private void createRoles() {
		Arrays.stream(RoleType.values()).forEach(roleType -> {
			RoleEntity role = new RoleEntity();
			role.setRoleName(roleType);
			roleService.createRole(role.getRoleName());
		});
	}

	private void createAdminUser() {
		try {
			RoleDTO adminRole = roleService.createRole(RoleType.ADMIN);

			final String ENCODED_PASSWORD = "AdM¡N&20_25";

			UserDTO
				admin =
				UserDTO
					.builder()
					.firstName("Admin")
					.lastName("Admin")
					.email("anonymous@gmail.com")
					.username("admin")
					.password(ENCODED_PASSWORD)
					.role(roleMapper.toRoleModel(adminRole))
					.build();
			admin.setId(UUID.randomUUID().toString());

			userService.createUser(admin);
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}
}

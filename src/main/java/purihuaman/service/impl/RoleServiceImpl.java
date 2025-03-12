package purihuaman.service.impl;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import purihuaman.dao.RoleDAO;
import purihuaman.dto.RoleDTO;
import purihuaman.entity.RoleEntity;
import purihuaman.enums.APIError;
import purihuaman.enums.RoleType;
import purihuaman.exception.APIRequestException;
import purihuaman.mapper.RoleMapper;
import purihuaman.service.RoleService;

import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {
	private final RoleDAO roleDao;
	private final RoleMapper roleMapper;

	public RoleServiceImpl(RoleDAO roleDao, RoleMapper roleMapper) {
		this.roleDao = roleDao;
		this.roleMapper = roleMapper;
	}

	@Override
	public RoleDTO findRoleByRoleName(RoleType roleName) {
		try {
			Optional<RoleEntity> existingRole = roleDao.findRoleByRoleName(roleName);

			return roleMapper.toRoleDTO(existingRole.orElse(null));
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public RoleDTO createRole(RoleType roleName) {
		try {
			RoleDTO existingRole = this.findRoleByRoleName(roleName);

			if (existingRole == null) {
				String uuid = UUID.randomUUID().toString();
				RoleDTO newRole = RoleDTO.builder().id(uuid).roleName(roleName).build();
				RoleEntity savedRole = roleDao.createRole(roleMapper.toRoleModel(newRole));

				return roleMapper.toRoleDTO(savedRole);
			} else return existingRole;

		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataIntegrityViolationException ex) {
			Throwable cause = ex.getCause();
			Throwable rootCause = cause.getCause();
			if (cause instanceof ConstraintViolationException || rootCause instanceof ConstraintViolationException)
				throw new APIRequestException(APIError.UNIQUE_CONSTRAINT_VIOLATION);
			else throw new APIRequestException(APIError.RESOURCE_ASSOCIATED_EXCEPTION);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}
}

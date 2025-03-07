package purihuaman.service.impl;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import purihuaman.dao.RoleDAO;
import purihuaman.dao.UserDAO;
import purihuaman.dto.RoleDTO;
import purihuaman.dto.UserDTO;
import purihuaman.entity.RoleEntity;
import purihuaman.entity.UserEntity;
import purihuaman.enums.APIError;
import purihuaman.enums.RoleType;
import purihuaman.exception.APIRequestException;
import purihuaman.mapper.RoleMapper;
import purihuaman.mapper.UserMapper;
import purihuaman.service.UserService;
import purihuaman.util.UserSpecification;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
	private final UserDAO userDao;
	private final RoleDAO roleDao;
	private final UserMapper userMapper;
	private final RoleMapper roleMapper;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(
		UserDAO userDao,
		RoleDAO roleDao,
		UserMapper userMapper,
		RoleMapper roleMapper,
		PasswordEncoder passwordEncoder
	)
	{
		this.userDao = userDao;
		this.roleDao = roleDao;
		this.userMapper = userMapper;
		this.roleMapper = roleMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<UserDTO> findAllUsers(Pageable page) {
		try {
			return userMapper.toUserDTOList(userDao.findAllUsers(page).getContent());
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public UserDTO findUserById(String userId) {
		try {
			Optional<UserEntity> existingUser = userDao.findUserById(userId);

			if (existingUser.isEmpty()) {
				throw new APIRequestException(APIError.ENDPOINT_NOT_FOUND);
			}

			return userMapper.toUserDTO(existingUser.get());
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<UserDTO> filterUsers(Map<String, String> filters, Pageable page) {
		try {
			Specification<UserEntity> spec = UserSpecification.filterUsers(filters);

			return userMapper.toUserDTOList(userDao.filterUsers(spec, page).getContent());
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public UserDTO createUser(UserDTO user) {
		try {
			RoleType roleName = user.getRole().getRoleName();
			RoleEntity role = this.findRoleByRoleName(roleName);

			user.setId(UUID.randomUUID().toString());
			user.setUsername(user.getUsername().trim().toLowerCase());
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRole(role);

			UserEntity savedUser = userDao.createUser(userMapper.toUserModel(user));
			return userMapper.toUserDTO(savedUser);
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

	@Override
	public UserDTO updateUser(String userId, UserDTO user) {
		try {
			RoleType roleName = user.getRole().getRoleName();
			RoleEntity existingRole = this.findRoleByRoleName(roleName);
			UserEntity existingUser = userMapper.toUserModel(this.findUserById(userId));

			existingUser.setFirstName(user.getFirstName());
			existingUser.setLastName(user.getLastName());
			existingUser.setEmail(user.getEmail());
			existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
			existingUser.setPassword(user.getPassword());
			existingUser.setUsername(user.getUsername());
			existingUser.setRole(existingRole);

			return userMapper.toUserDTO(userDao.updateUser(existingUser));
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

	@Override
	public void deleteUser(String userId) {
		try {
			UserDTO existingUser = this.findUserById(userId);

			userDao.deleteUser(existingUser.getId());
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataIntegrityViolationException ex) {
			Throwable cause = ex.getCause();
			Throwable rootCause = cause.getCause();
			if (cause instanceof DataIntegrityViolationException || rootCause instanceof DataAccessException)
				throw new APIRequestException(APIError.UNIQUE_CONSTRAINT_VIOLATION);
			else throw new APIRequestException(APIError.RESOURCE_CONFLICT);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public UserDTO authentication(String username, String password) {
		System.out.println("user: " + username);
		try {
			Optional<UserEntity> optionalUser = userDao.authentication(username, password);

			if (optionalUser.isEmpty()) {
				throw new APIRequestException(APIError.ENDPOINT_NOT_FOUND);
			}

			return userMapper.toUserDTO(optionalUser.get());
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public UserDTO findUserByUsername(String username) {
		try {
			String usernameToSearch = username.trim().toLowerCase();
			Optional<UserEntity> existingUser = userDao.findUserByUsername(usernameToSearch);

			if (existingUser.isEmpty()) {
				APIError.RECORD_NOT_FOUND.setMessage("User not found");
				throw new APIRequestException(APIError.RECORD_NOT_FOUND);
			}

			return userMapper.toUserDTO(existingUser.get());
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}

	}

	public RoleEntity createRole(RoleType roleName) {
		RoleDTO newRole = RoleDTO.builder().id(UUID.randomUUID().toString()).roleName(roleName).build();

		return roleDao.createRole(roleMapper.toRoleModel(newRole));
	}

	public RoleEntity findRoleByRoleName(RoleType roleName) {
		Optional<RoleEntity> existingRole = roleDao.findRoleByRoleName(roleName);

		return existingRole.orElseGet(() -> this.createRole(roleName));
	}
}

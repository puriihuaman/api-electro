package purihuaman.service.impl;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import purihuaman.dao.UserDAO;
import purihuaman.dto.RoleDTO;
import purihuaman.dto.UserDTO;
import purihuaman.entity.UserEntity;
import purihuaman.enums.APIError;
import purihuaman.enums.RoleType;
import purihuaman.exception.APIRequestException;
import purihuaman.mapper.RoleMapper;
import purihuaman.mapper.UserMapper;
import purihuaman.service.RoleService;
import purihuaman.service.UserService;
import purihuaman.util.UserSpecification;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
	private final UserDAO userDao;
	private final RoleService roleService;
	private final UserMapper userMapper;
	private final RoleMapper roleMapper;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(
		UserDAO userDao,
		RoleService roleService,
		UserMapper userMapper,
		RoleMapper roleMapper,
		PasswordEncoder passwordEncoder
	)
	{
		this.userDao = userDao;
		this.roleService = roleService;
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
				APIError.ENDPOINT_NOT_FOUND.setMessage("User not found");
				APIError.ENDPOINT_NOT_FOUND.setDescription(
					"The user you are trying to access does not exist in our system. Please check the user ID provided and try again.");
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
			RoleDTO existingRole = roleService.findRoleByRoleName(roleName);

			user.setId(UUID.randomUUID().toString());
			user.setEmail(user.getEmail().trim().toLowerCase());
			user.setUsername(user.getUsername().trim().toLowerCase());
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRole(roleMapper.toRoleModel(existingRole));

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
			RoleDTO existingRole = roleService.findRoleByRoleName(roleName);

			UserEntity existingUser = userMapper.toUserModel(this.findUserById(userId));

			existingUser.setFirstName(user.getFirstName());
			existingUser.setLastName(user.getLastName());
			existingUser.setEmail(user.getEmail().trim().toLowerCase());
			existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
			existingUser.setPassword(user.getPassword());
			existingUser.setUsername(user.getUsername().trim().toLowerCase());
			existingUser.setRole(roleMapper.toRoleModel(existingRole));

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
}

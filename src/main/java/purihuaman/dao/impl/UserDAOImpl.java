package purihuaman.dao.impl;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import purihuaman.dao.UserDAO;
import purihuaman.dao.repository.UserRepository;
import purihuaman.dto.UserDTO;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;
import purihuaman.mapper.UserMapper;
import purihuaman.model.UserModel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {
	private final UserMapper userMapper;
	private final UserRepository userRepository;

	private final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

	@Override
	public List<UserDTO> getAllUsers(final Pageable page) {
		try {
			List<UserModel> users = userRepository.findAll(page).getContent();
			return userMapper.toUserDTOList(users);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public UserDTO getUserById(final String userId) {
		try {
			UserModel
				user =
				userRepository.findById(userId).orElseThrow(() -> new APIRequestException(APIError.ENDPOINT_NOT_FOUND));

			return userMapper.toUserDTO(user);
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public UserDTO authentication(final String username, final String password) {
		try {
			UserModel
				userFound =
				userRepository
					.findByUsernameAndPassword(username, password)
					.orElseThrow(() -> new APIRequestException(APIError.ENDPOINT_NOT_FOUND));

			return userMapper.toUserDTO(userFound);
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public UserDTO getUserByUsername(final String username) {
		try {
			UserModel
				userFound =
				userRepository.findByUsername(username).orElseThrow(() -> new APIRequestException(APIError.ENDPOINT_NOT_FOUND));

			return userMapper.toUserDTO(userFound);
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<UserDTO> filterUsers(final Map<String, String> filters, final Pageable page) {
		try {
			Short offset = (short) page.getOffset();
			Short limit = (short) page.getPageSize();

			List<UserModel> filteredUsers = userRepository.filterUsers(
				filters.containsKey("first_name") ? filters.get("first_name").trim() : null,
				filters.containsKey("last_price") ? filters.get("last_price").trim() : null,
				filters.containsKey("email") ? filters.get("email").trim() : null,
				filters.containsKey("username") ? filters.get("username").trim() : null,
				offset,
				limit
			);

			return userMapper.toUserDTOList(filteredUsers);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public UserDTO addUser(final @Valid UserDTO user) {
		try {
			UserModel userModel = userMapper.toUserModel(user);

			userModel.setUserId(UUID.randomUUID().toString());
			userModel.setPassword(ENCODER.encode(user.getPassword()));

			UserModel savedUser = userRepository.save(userModel);

			return userMapper.toUserDTO(savedUser);
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
	public UserDTO updateUser(final String userId, final @Valid UserDTO user) {
		try {
			UserDTO userExisting = getUserById(userId);

			userExisting.setFirstName(user.getFirstName());
			userExisting.setLastName(user.getLastName());
			userExisting.setEmail(user.getEmail());
			userExisting.setPassword(ENCODER.encode(user.getPassword()));
			userExisting.setUsername(user.getUsername());

			UserModel updatedUser = userRepository.save(userMapper.toUserModel(userExisting));

			return userMapper.toUserDTO(updatedUser);
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
	public Integer deleteUser(final String userId) {
		try {
			UserDTO user = getUserById(userId);

			UserModel userModel = userMapper.toUserModel(user);
			userRepository.deleteById(userModel.getUserId());

			return 1;
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
}

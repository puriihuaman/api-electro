package purihuaman.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import purihuaman.dao.UserDAO;
import purihuaman.dao.repository.UserRepository;
import purihuaman.dto.UserDTO;
import purihuaman.exception.ApiRequestException;
import purihuaman.mapper.UserMapper;
import purihuaman.model.UserModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserDAOImpl implements UserDAO {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRepository userRepository;

	private final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

	@Override
	public List<UserDTO> getAllUsers(Pageable page) {
		try {
			List<UserModel> users = userRepository.findAll(page).getContent();
			return userMapper.toUserDTOList(users);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"Database error",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public UserDTO getUserById(String userId) {
		try {
			Optional<UserModel> result = userRepository.findById(userId);
			if (result.isEmpty()) {
				throw new ApiRequestException(
					true,
					"User not found",
					String.format("User with id %s not found", userId),
					HttpStatus.NOT_FOUND
				);
			}

			return userMapper.toUserDTO(result.get());
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"An error occurred",
				"An error occurred internally. Please try again.",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public UserDTO authentication(String username, String password) {
		try {
			UserModel
				userFound =
				userRepository
					.findByUsernameAndPassword(username, password)
					.orElseThrow(() -> new ApiRequestException(
						true,
						"User not found",
						String.format("User with username '%s' not found", username),
						HttpStatus.NOT_FOUND
					));

			return userMapper.toUserDTO(userFound);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"An error occurred",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public UserDTO getUserByUsername(String username) {
		try {
			UserModel
				userFound =
				userRepository
					.findByUsername(username)
					.orElseThrow(() -> new ApiRequestException(
						true,
						"User not found",
						String.format("User with username '%s' not found", username),
						HttpStatus.NOT_FOUND
					));

			return userMapper.toUserDTO(userFound);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"An error occurred",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public List<UserDTO> filterUsers(Map<String, String> filters, Pageable page) {
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
			throw new ApiRequestException(
				true,
				"Database error",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public UserDTO addUser(UserDTO user) {
		try {
			UserModel userModel = userMapper.toUserModel(user);
			userModel.setUserId(UUID.randomUUID().toString());
			userModel.setPassword(ENCODER.encode(user.getPassword()));

			UserModel savedUser = userRepository.save(userModel);

			return userMapper.toUserDTO(savedUser);
		} catch (DataIntegrityViolationException ex) {
			throw new ApiRequestException(
				true,
				"Duplicate key",
				"The record username is already in use",
				HttpStatus.CONFLICT
			);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"Database error",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public UserDTO updateUser(String userId, UserDTO user) {
		UserDTO userExisting = getUserById(userId);

		try {
			userExisting.setFirstName(user.getFirstName());
			userExisting.setLastName(user.getLastName());
			userExisting.setEmail(user.getEmail());
			userExisting.setPassword(user.getPassword());
			userExisting.setUsername(user.getUsername());

			UserModel updatedUser = userRepository.save(userMapper.toUserModel(userExisting));

			return userMapper.toUserDTO(updatedUser);
		} catch (DataIntegrityViolationException ex) {
			throw new ApiRequestException(
				true,
				"Duplicate key",
				"The record username is already in use",
				HttpStatus.CONFLICT
			);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"Database error",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public Integer deleteUser(String userId) {
		try {
			UserDTO user = getUserById(userId);

			if (user == null) {
				throw new ApiRequestException(
					true,
					"User not found",
					String.format("User with id '%s' not found", userId),
					HttpStatus.NOT_FOUND
				);
			}

			UserModel userModel = userMapper.toUserModel(user);
			userRepository.deleteById(userModel.getUserId());

			return 1;
		} catch (DataIntegrityViolationException ex) {
			throw new ApiRequestException(
				true,
				"Cannot delete record",
				"The record is linked to the other record(s) and cannot be deleted",
				HttpStatus.CONFLICT
			);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"Delete error",
				"Database error occurred while deleting the user",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}
}

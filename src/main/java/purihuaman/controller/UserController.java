package purihuaman.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import purihuaman.dto.UserDTO;
import purihuaman.exception.ApiRequestException;
import purihuaman.service.UserService;
import purihuaman.util.ApiResponse;
import purihuaman.util.ApiResponseHandler;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private HttpStatus HTTP_STATUS = HttpStatus.OK;
	private ResponseEntity<ApiResponse> API_RESPONSE;

	private final UserService userService;

	@GetMapping
	public ResponseEntity<Object> getAllUsers(
		@RequestParam Map<String, String> keywords
	)
	{
		List<UserDTO> users;
		short offset = keywords.containsKey("offset") ? Short.parseShort(keywords.get("offset")) : 0;
		short limit = keywords.containsKey("limit") ? Short.parseShort(keywords.get("limit")) : 10;

		Pageable page = PageRequest.of(offset, limit);

		users = (keywords.isEmpty()) ? userService.getAllUsers(page) : userService.filterUsers(keywords, page);
		HTTP_STATUS = HttpStatus.OK;

		API_RESPONSE = ApiResponseHandler.handleApiResponse("Users successfully obtained", users, HTTP_STATUS);
		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@Valid @PathVariable("id") String userId) {
		validateId(userId);

		UserDTO user = userService.getUserById(userId);

		if (user == null) {
			throw new ApiRequestException(
				true,
				"User not found",
				String.format("User with id '%s' does not exist", userId),
				HttpStatus.NOT_FOUND
			);
		}

		API_RESPONSE = ApiResponseHandler.handleApiResponse("User successfully obtained", user, HTTP_STATUS);
		HTTP_STATUS = HttpStatus.OK;

		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	@PostMapping
	public ResponseEntity<Object> addUser(@Valid @RequestBody UserDTO user) {
		validateUser(user);

		UserDTO savedUser = userService.addUser(user);
		HTTP_STATUS = HttpStatus.CREATED;
		API_RESPONSE = ApiResponseHandler.handleApiResponse("User created successfully", savedUser, HTTP_STATUS);

		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable("id") String userId, @RequestBody UserDTO user)
	{
		validateId(userId);
		validateUser(user);

		UserDTO updatedUser = userService.updateUser(userId, user);

		HTTP_STATUS = HttpStatus.OK;
		API_RESPONSE = ApiResponseHandler.handleApiResponse("User updated successfully", updatedUser, HTTP_STATUS);

		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable("id") String userId) {
		validateId(userId);

		Integer isDeleted = userService.deleteUser(userId);
		if (isDeleted != 1) {
			throw new ApiRequestException(
				true,
				"Delete error",
				"Database error occurred while deleting the user",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		} else {
			HTTP_STATUS = HttpStatus.OK;
			API_RESPONSE = ApiResponseHandler.handleApiResponse("User successfully deleted", null, HTTP_STATUS);
		}
		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	private void validateId(String id) {
		if (id == null || id.length() != 36) {
			throw new ApiRequestException(true, "Invalid ID", "The ID must be a String", HttpStatus.BAD_REQUEST);
		}
	}

	private void validateUser(@RequestBody final UserDTO user) {
		if (user.getFirstName() == null ||
			user.getFirstName().isEmpty() ||
			user.getLastName() == null ||
			user.getLastName().isEmpty() ||
			user.getEmail() == null ||
			user.getEmail().isEmpty() ||
			user.getPassword() == null ||
			user.getPassword().isEmpty())
		{
			throw new ApiRequestException(true, "Required fields", "All fields required", HttpStatus.BAD_REQUEST);
		}
	}
}

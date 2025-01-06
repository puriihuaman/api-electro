package purihuaman.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import purihuaman.dto.UserDTO;
import purihuaman.enums.APIError;
import purihuaman.enums.APISuccess;
import purihuaman.exception.APIRequestException;
import purihuaman.service.UserService;
import purihuaman.util.APIResponse;
import purihuaman.util.APIResponseHandler;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private ResponseEntity<APIResponse> API_RESPONSE;

	private final UserService userService;

	@GetMapping(produces = "application/json")
	public ResponseEntity<APIResponse> getAllUsers(
		final @RequestParam Map<String, String> keywords
	)
	{
		List<UserDTO> users;
		short offset = keywords.containsKey("offset") ? Short.parseShort(keywords.get("offset")) : 0;
		short limit = keywords.containsKey("limit") ? Short.parseShort(keywords.get("limit")) : 10;

		Pageable page = PageRequest.of(offset, limit);

		users = (keywords.isEmpty()) ? userService.getAllUsers(page) : userService.filterUsers(keywords, page);

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_FETCHED_SUCCESSFULLY, users);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_FETCHED_SUCCESSFULLY.getStatus());
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<APIResponse> getUserById(final @PathVariable("id") String userId) {
		validateId(userId);

		UserDTO user = userService.getUserById(userId);

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_FETCHED_SUCCESSFULLY, user);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_FETCHED_SUCCESSFULLY.getStatus());
	}

	@PostMapping(produces = "application/json")
	public ResponseEntity<APIResponse> addUser(final @Valid @RequestBody UserDTO user) {
		UserDTO savedUser = userService.addUser(user);

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_CREATED_SUCCESSFULLY, savedUser);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_CREATED_SUCCESSFULLY.getStatus());
	}

	@PutMapping(value = "/{id}", produces = "application/json")
	@Transactional
	public ResponseEntity<APIResponse> updateUser(
		final @PathVariable("id") String userId,
		final @Valid @RequestBody UserDTO user
	)
	{
		validateId(userId);

		UserDTO updatedUser = userService.updateUser(userId, user);

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_UPDATED_SUCCESSFULLY, updatedUser);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_UPDATED_SUCCESSFULLY.getStatus());
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	@Transactional
	public ResponseEntity<APIResponse> deleteUser(final @PathVariable("id") String userId) {
		validateId(userId);

		userService.deleteUser(userId);

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_DELETED_SUCCESSFULLY, null);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_DELETED_SUCCESSFULLY.getStatus());
	}

	private void validateId(final String id) {
		if (id == null || id.length() != 36) {
			throw new APIRequestException(APIError.INVALID_REQUEST_DATA);
		}
	}
}

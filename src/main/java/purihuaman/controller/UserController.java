package purihuaman.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import purihuaman.dto.LoginRequestDTO;
import purihuaman.dto.UserDTO;
import purihuaman.enums.APIError;
import purihuaman.enums.APISuccess;
import purihuaman.exception.APIRequestException;
import purihuaman.security.SecurityService;
import purihuaman.security.TokenResponse;
import purihuaman.service.UserService;
import purihuaman.util.APIResponse;
import purihuaman.util.APIResponseHandler;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
	private final UserService userService;
	private final SecurityService securityService;

	public UserController(UserService userService, SecurityService securityService) {
		this.userService = userService;
		this.securityService = securityService;
	}

	@GetMapping
	public ResponseEntity<APIResponse> findAllUsers(@RequestParam Map<String, String> keywords) {
		List<UserDTO> users;
		short offset = keywords.containsKey("offset") ? Short.parseShort(keywords.get("offset")) : 0;
		short limit = keywords.containsKey("limit") ? Short.parseShort(keywords.get("limit")) : 10;

		Pageable page = PageRequest.of(offset, limit);

		users = (keywords.isEmpty()) ? userService.findAllUsers(page) : userService.filterUsers(keywords, page);

		APISuccess.RESOURCE_RETRIEVED.setMessage("Recovered users");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_RETRIEVED, users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<APIResponse> findUserById(@PathVariable("id") String userId) {
		this.validateId(userId);

		UserDTO user = userService.findUserById(userId);

		APISuccess.RESOURCE_RETRIEVED.setMessage("Retrieved user");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_RETRIEVED, user);
	}

	@PostMapping
	public ResponseEntity<APIResponse> createUser(@Valid @RequestBody UserDTO user) {
		UserDTO savedUser = userService.createUser(user);

		APISuccess.RESOURCE_CREATED.setMessage("Created user");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_CREATED, savedUser);
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<APIResponse> updateUser(@PathVariable("id") String userId, @Valid @RequestBody UserDTO user) {
		this.validateId(userId);

		UserDTO updatedUser = userService.updateUser(userId, user);

		APISuccess.RESOURCE_UPDATED.setMessage("Updated user");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_UPDATED, updatedUser);
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<APIResponse> deleteUser(@PathVariable("id") String userId) {
		this.validateId(userId);

		userService.deleteUser(userId);

		APISuccess.RESOURCE_REMOVED.setMessage("Deleted user");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_REMOVED, null);
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> authenticate(@Valid @RequestBody LoginRequestDTO user) {
		TokenResponse tokenResponse = securityService.login(user);

		return ResponseEntity.ok(tokenResponse);
	}

	private void validateId(String id) {
		if (id == null || id.length() != 36) {
			throw new APIRequestException(APIError.INVALID_REQUEST_DATA);
		}
	}
}

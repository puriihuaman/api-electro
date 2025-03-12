package purihuaman.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import purihuaman.dto.APIExceptionDTO;
import purihuaman.dto.UserDTO;
import purihuaman.enums.APIError;
import purihuaman.enums.APISuccess;
import purihuaman.exception.APIRequestException;
import purihuaman.service.UserService;
import purihuaman.util.APIResponseData;
import purihuaman.util.APIResponseHandler;

import java.util.List;
import java.util.Map;

@Tag(name = "User", description = "Operations for user management")
@Validated
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Operation(
		summary = "Get all users with pagination and filters", description = """
		Retrieves a paginated list of users.
		**Requires ADMINISTRATOR role**.
		Available filters (use as query params):
		- firstName: Filter by first name (contains).
		- lastName: Filter by last name (contains).
		- email: Filter by email (exact)
		- username: Filter by username (exact)
		""", parameters = {
		@Parameter(
			name = "firstName", description = "First name to filter", example = "Juan"
		), @Parameter(
		name = "lastName", description = "Last name to filter", example = "Perez"
	), @Parameter(
		name = "email", description = "Email to filter", example = "juan.perez@example.com"
	), @Parameter(
		name = "username", description = "Username to filter", example = "Perez"
	), @Parameter(
		name = "offset", description = "Indicates from which position (page) the query should start", example = "0"
	), @Parameter(
		name = "limit", description = "Indicates the maximum number of elements on a page", example = "10"
	)
	}, responses = {
		@ApiResponse(
			responseCode = "200", description = "User list successfully retrieved", content = @Content(
			schema = @Schema(implementation = Object.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"data": [
							{
								"id": "3f040b4c-4fd4-4ff6-b835-c4dc0ac36da3",
								"firstName": "Juan",
								"lastName": "Perez",
								"email": "juan.perez@example.com",
								"username": "perez",
								"password": "$2a$10$qha37TqZpQ45J7IOAgkH2ejb8XlSVXaClqJGLnwyv.JeDK9sozEY2",
								"role": {
									"id": "51a02ccf-3638-4436-8674-6c710f0c8963",
									"roleName": "Admin"
								}
							}
						],
						"hasError": false,
						"message": "Recovered users",
						"statusCode": 200,
						"timestamp": "2025-03-10T13:36:30.2974404"
					}
					"""
			)
		)
		), @ApiResponse(
		responseCode = "403", description = "Access denied (requires ADMIN role)", content = @Content(
		schema = @Schema(implementation = APIExceptionDTO.class),
		mediaType = MediaType.APPLICATION_JSON_VALUE,
		examples = @ExampleObject(
			value = """
				{
					"hasErrors": true,
					"message": "Forbidden",
					"description": "You do not have the necessary permissions to perform this action.",
					"code": 403,
					"reasons": null,
					"timestamp": "2025-03-07T10:12:35.9687337"
				}
				"""
		)
	)
	), @ApiResponse(
		responseCode = "400", description = "Invalid parameters", content = @Content(
		schema = @Schema(implementation = APIExceptionDTO.class)
	)
	)
	}
	)
	@GetMapping
	public ResponseEntity<APIResponseData> findAllUsers(@RequestParam Map<String, String> keywords) {
		List<UserDTO> users;
		short offset = keywords.containsKey("offset") ? Short.parseShort(keywords.get("offset")) : 0;
		short limit = keywords.containsKey("limit") ? Short.parseShort(keywords.get("limit")) : 10;

		Pageable page = PageRequest.of(offset, limit);

		users = (keywords.isEmpty()) ? userService.findAllUsers(page) : userService.filterUsers(keywords, page);

		APISuccess.RESOURCE_RETRIEVED.setMessage("Recovered users");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_RETRIEVED, users);
	}

	@Operation(
		summary = "Get a user by ID",
		description = "Retrieves the details of an existing user using their unique ID.",
		parameters = @Parameter(
			name = "User ID",
			description = "User ID to search for",
			example = "51a02ccf-3638-4436-8674-6c710f0c8963",
			required = true
		),
		responses = {
			@ApiResponse(
				responseCode = "200", description = "User found", content = @Content(
				schema = @Schema(implementation = Object.class),
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				examples = @ExampleObject(
					value = """
						{
							"data": {
								"id": "3f040b4c-4fd4-4ff6-b835-c4dc0ac36da3",
								"firstName": "Juan",
								"lastName": "Perez",
								"email": "juan.perez@example.com",
								"username": "perez",
								"password": "$2a$10$qha37TqZpQ45J7IOAgkH2ejb8XlSVXaClqJGLnwyv.JeDK9sozEY2",
								"role": {
									"id": "51a02ccf-3638-4436-8674-6c710f0c8963",
									"roleName": "Admin"
								}
							},
							"hasError": false,
							"message": "Retrieved user",
							"statusCode": 200,
							"timestamp": "2025-03-10T13:17:52.2671105"
						}
						"""
				)
			)
			), @ApiResponse(
			responseCode = "400", description = "Invalid parameters", content = @Content(
			schema = @Schema(implementation = APIExceptionDTO.class)
		)
		), @ApiResponse(
			responseCode = "404", description = "User not found", content = @Content(
			schema = @Schema(implementation = APIExceptionDTO.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"message": "User Not Found",
						"description": "The user you are trying to access does not exist in our system. Please check the user ID provided and try again.",
						"code": 404,
						"reasons": null,
						"timestamp": "2025-03-10T18:20:12.9741577",
						"hasError": true
					}
					"""
			)
		)
		)
		}
	)
	@GetMapping("/{id}")
	public ResponseEntity<APIResponseData> findUserById(@PathVariable("id") String userId) {
		this.validateId(userId);

		UserDTO user = userService.findUserById(userId);

		APISuccess.RESOURCE_RETRIEVED.setMessage("Retrieved user");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_RETRIEVED, user);
	}

	@Operation(
		summary = "Create a new user",
		description = "Register a new user in the system.",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "New user data", required = true, content = @Content(
			schema = @Schema(implementation = UserDTO.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"firstName": "Juan",
						"lastName": "Perez",
						"email": "juan.perez@example.com",
						"username": "perez",
						"password": "Jv4N&_&P3r35",
						"role": {
							"roleName": "Admin"
						}
					}
					"""
			)
		)
		),
		responses = {
			@ApiResponse(
				responseCode = "201", description = "User created successfully", content = @Content(
				schema = @Schema(implementation = Object.class),
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				examples = @ExampleObject(
					value = """
						{
							"data": {
								"id": "a5cbe765-f79d-4a55-a262-40bde5e499f8",
								"firstName": "Juan",
								"lastName": "Perez",
								"email": "juan.perez@example.com",
								"username": "perez",
								"password": "$2a$10$Tv58oOnSxD2l9ZRCjJn6K.Qtm5ZJmoHv.yFGdkioAht/AjDgGqNbS",
								"role": {
									"id": "e144cecb-29d5-439b-a739-b4e4eafb81b9",
									"roleName": "Admin"
								}
						    },
						    "hasError": false,
						    "message": "Created user",
						    "statusCode": 201,
						    "timestamp": "2025-03-10T13:03:59.2791386"
						}
						"""
				)
			)
			), @ApiResponse(
			responseCode = "400", description = "Validation error in request body", content = @Content(
			schema = @Schema(implementation = APIExceptionDTO.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"hasErrors": true,
						"message": "Invalid data",
						"description": "Request data contains invalid values or incorrect format.",
						"code": "400",
						"reasons": {
							"firstName": "The field must contain at least 2 characters.",
							"username": "The field must contain at least 3 characters."
						},
						"timestamp": "2025-03-07T10:12:35.9687337"
					}
					"""
			)
		)
		), @ApiResponse(
			responseCode = "403", description = "Access denied: ADMIN or USER roles are required", content = @Content(
			schema = @Schema(implementation = APIExceptionDTO.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"hasErrors": true,
						"message": "Forbidden",
						"description": "You do not have the necessary permissions to perform this action.",
						"code": 403,
						"reasons": null,
						"timestamp": "2025-03-07T10:12:35.9687337"
					}
					"""
			)
		)
		)
		}
	)
	@PostMapping
	public ResponseEntity<APIResponseData> createUser(@Valid @RequestBody UserDTO user) {
		UserDTO savedUser = userService.createUser(user);

		APISuccess.RESOURCE_CREATED.setMessage("Created user");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_CREATED, savedUser);
	}

	@Operation(
		summary = "Update user",
		description = "Updates an existing user. **Allowed roles:** ADMIN",
		parameters = @Parameter(
			name = "User ID",
			description = "ID of the user to update",
			example = "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
			required = true
		),
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Data for the user to be updated", required = true, content = @Content(
			schema = @Schema(implementation = Object.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"firstName": "John",
						"lastName": "Doe",
						"email": "john.doe@example.com",
						"username": "john",
						"password": "J0hN&1234",
						"role" : {
							"roleName": "Admin"
						}
					}
					"""
			)
		)
		),
		responses = {
			@ApiResponse(
				responseCode = "200", description = "User updated successfully", content = @Content(
				schema = @Schema(implementation = Object.class),
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				examples = @ExampleObject(
					value = """
						{
							"data": {
								"id": "a3a131f3-2991-4ffb-aeb2-e4df775ad8a6",
								"firstName": "John",
								"lastName": "Doe",
								"email": "john.doe@example.com",
								"username": "john",
								"password": "J0hN&1234",
								"role": {
									"id": "e144cecb-29d5-439b-a739-b4e4eafb81b9",
									"roleName": "Invited"
								}
							},
							"hasError": false,
							"message": "Updated user",
							"statusCode": 200,
							"timestamp": "2025-03-10T13:45:39.0155997"
						}
						"""
				)
			)
			), @ApiResponse(
			responseCode = "400", description = "Validation error in request body", content = @Content(
			schema = @Schema(implementation = APIExceptionDTO.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"hasError": true,
						"message": "Invalid data",
						"description": "Request data contains invalid values or incorrect format.",
						"code": 400,
						"reasons": {
							"firstName": "The field must contain at least 2 characters.",
					        "username": "The field must contain at least 3 characters."
						},
						"timestamp": "2025-03-07T10:12:35.9687337"
					}
					"""
			)
		)
		), @ApiResponse(
			responseCode = "403", description = "Access denied: ADMIN or USER roles are required", content = @Content(
			schema = @Schema(implementation = APIExceptionDTO.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"hasErrors": true,
						"message": "Forbidden",
						"description": "You do not have the necessary permissions to perform this action.",
						"code": 403,
						"reasons": null,
						"timestamp": "2025-03-07T10:12:35.9687337"
					}
					"""
			)
		)
		), @ApiResponse(
			responseCode = "404", description = "User not found", content = @Content(
			schema = @Schema(implementation = APIExceptionDTO.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"hasError": true,
						"message": "User not found",
					    "description": "The requested resource does not exist.",
					    "code": 404,
					    "reasons": null,
					    "timestamp": "2025-03-07T17:23:42.3706153"
					}
					"""
			)
		)
		)
		}
	)
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<APIResponseData> updateUser(
		@PathVariable("id") String userId,
		@Valid @RequestBody UserDTO user
	)
	{
		this.validateId(userId);

		UserDTO updatedUser = userService.updateUser(userId, user);

		APISuccess.RESOURCE_UPDATED.setMessage("Updated user");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_UPDATED, updatedUser);
	}

	@Operation(
		summary = "Delete user",
		description = "Delete an existing user. **Allowed roles:** ADMIN",
		parameters = @Parameter(
			name = "User ID",
			description = "ID of the user to delete",
			example = "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
			required = true
		),
		responses = {
			@ApiResponse(
				responseCode = "204", description = "User deleted successfully", content = @Content(
				schema = @Schema(implementation = Object.class), mediaType = MediaType.APPLICATION_JSON_VALUE
			)
			), @ApiResponse(
			responseCode = "403", description = "Access denied: ADMIN role required", content = @Content(
			schema = @Schema(implementation = APIExceptionDTO.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"hasError": true,
						"message": "Forbidden",
						"description": "You do not have the necessary permissions to perform this action.",
						"code": 403,
						"reasons": null,
						"timestamp": "2025-03-07T10:12:35.9687337"
					}
					"""
			)
		)
		), @ApiResponse(
			responseCode = "404", description = "User not found", content = @Content(
			schema = @Schema(implementation = APIExceptionDTO.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"hasError": true,
						"message": "User not found",
						"description": "The requested resource does not exist.",
						"code": 404,
						"reasons": null,
						"timestamp": "2025-03-07T17:23:42.3706153"
					}
					"""
			)
		)
		)
		}
	)
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<APIResponseData> deleteUser(@PathVariable("id") String userId) {
		this.validateId(userId);

		userService.deleteUser(userId);

		APISuccess.RESOURCE_REMOVED.setMessage("Deleted user");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_REMOVED, null);
	}

	private void validateId(String id) {
		if (id == null || id.length() != 36) {
			throw new APIRequestException(APIError.INVALID_REQUEST_DATA);
		}
	}
}

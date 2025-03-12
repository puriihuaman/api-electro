package purihuaman.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import purihuaman.dto.APIExceptionDTO;
import purihuaman.dto.CategoryDTO;
import purihuaman.enums.APIError;
import purihuaman.enums.APISuccess;
import purihuaman.exception.APIRequestException;
import purihuaman.service.CategoryService;
import purihuaman.util.APIResponseData;
import purihuaman.util.APIResponseHandler;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Category", description = "Operations for category management")
public class CategoryController {
	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Operation(
		summary = "Get all categories",
		description = "Retrieves a list of categories. **Allowed roles:** ADMIN, USER, INVITED",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Category list successfully retrieved",
				content = @Content(
					schema = @Schema(implementation = Object.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
						value = """
								{
									"data": [
										{
											"id": "51a02ccf-3638-4436-8674-6c710f0c8963",
											"categoryName": "Electronics"
										},
										{
											"id": "06b94ddf-dd4a-44d6-bf8b-9973a9711ae8",
											"categoryName": "Books"
										}
									],
									"hasError": false,
									"message": "Recovered Categories",
									"statusCode": 200,
									"timestamp": "2025-03-07T10:32:34.1019831"
								}
							"""
					)
				)
			), @ApiResponse(
				responseCode = "403",
				description = "Access denied: Roles required ADMIN, USER or INVITED",
				content = @Content(
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
	@GetMapping
	public ResponseEntity<APIResponseData> getAllCategories() {
		List<CategoryDTO> categories = categoryService.findAllCategories();

		for (CategoryDTO category : categories) {
			category.add(linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel());
			category.add(linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));
		}

		APISuccess.RESOURCE_RETRIEVED.setMessage("Recovered Categories");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_RETRIEVED, categories);
	}

	@Operation(
		summary = "Get a category by ID",
		description = "Retrieves the details of an existing category. **Allowed roles:** ADMIN, USER",
		parameters = @Parameter(
			name = "Category ID",
			description = "ID of the category to search for",
			example = "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
			required = true,
			hidden = true,
			schema = @Schema(implementation = UUID.class)
		),
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Category found", content = @Content(
				schema = @Schema(implementation = Object.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
				value = """
					{
						"data": {
							"id": "06b94ddf-dd4a-44d6-bf8b-9973a9711ae8",
							"categoryName": "Electronics"
						},
						"hasError": false,
						"message": "Recovered Category",
						"statusCode": 200,
						"timestamp": "2025-03-07T10:32:57.524631"
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
			responseCode = "404", description = "Category not found", content = @Content(
			schema = @Schema(implementation = APIExceptionDTO.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"hasError": true,
					    "message": "Category not found",
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
	@GetMapping("/{id}")
	public ResponseEntity<APIResponseData> getCategoryById(@PathVariable("id") String categoryId) {
		this.validateId(categoryId);

		CategoryDTO category = categoryService.findCategoryById(categoryId);

		category.add(linkTo(methodOn(CategoryController.class).getCategoryById(categoryId)).withSelfRel());
		category.add(linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));
		category.add(linkTo(methodOn(ProductController.class).getAllProducts(Map.of(
			"category_name",
			category.getCategoryName()
		))).withRel("products"));

		APISuccess.RESOURCE_RETRIEVED.setMessage("Recovered Category");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_RETRIEVED, category);
	}

	@Operation(
		summary = "Create new category",
		description = "Creates a new category. **Allowed roles:** ADMIN, USER",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Data for the category to be created",
			required = true,
			content = @Content(
				schema = @Schema(implementation = Object.class),
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				examples = @ExampleObject(
					value = """
						{
							"categoryName": "Electronics"
						}
						"""
				)
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "201", description = "Category created successfully",
				content = @Content(
					schema = @Schema(implementation = Object.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
						value = """
							{
								"data": {
									"id": "5c1b8dfe-7409-4f0a-84ae-fb8dd746c5dc",
									"categoryName": "Electronics"
								},
							    "hasError": false,
							    "message": "Category created",
							    "statusCode": 201,
							    "timestamp": "2025-03-07T10:33:19.8482091"
							}
							"""
					)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Validation error in request body",
				content = @Content(
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
									"categoryName": "The field is empty."
								},
								"timestamp": "2025-03-07T22:08:21.178014"
							}
							"""
					)
				)
			),
			@ApiResponse(
				responseCode = "403",
				description = "Access denied: ADMIN or USER roles are required",
				content = @Content(
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
	public ResponseEntity<APIResponseData> createCategory(@Valid @RequestBody CategoryDTO category) {
		CategoryDTO createdCategory = categoryService.createCategory(category);

		createdCategory.add(linkTo(methodOn(CategoryController.class).getCategoryById(createdCategory.getId())).withSelfRel());
		createdCategory.add(linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));

		APISuccess.RESOURCE_CREATED.setMessage("Category created");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_CREATED, createdCategory);
	}

	@Operation(
		summary = "Update category",
		description = "Updates an existing category. **Allowed roles:** ADMIN, USER",
		parameters = @Parameter(
			name = "Category ID",
			description = "ID of the category to update",
			example = "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
			required = true
		),
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Data for the category to be updated",
			required = true,
			content = @Content(
				schema = @Schema(implementation = Object.class),
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				examples = @ExampleObject(
					value = """
						{
							"categoryName": "Electronics"
						}
						"""
				)
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Category updated successfully",
				content = @Content(
					schema = @Schema(implementation = Object.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
						value = """
							{
								"data": {
									"id": "571b3387-0625-4aaa-b078-888fec5e334e",
									"categoryName": "Electronics"
								},
								"hasError": false,
								"message": "Category updated",
								"statusCode": 200,
								"timestamp": "2025-04-07T10:33:19.8482091"
							}
							"""
					)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Validation error in request body",
				content = @Content(
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
									"categoryName": "The field is empty."
								},
								"timestamp": "2025-03-07T22:08:21.178014"
							}
						"""
					)
				)
			),
			@ApiResponse(
				responseCode = "403",
				description = "Access denied: ADMIN or USER roles are required",
				content = @Content(
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
			),
			@ApiResponse(
				responseCode = "404",
				description = "Category not found",
				content = @Content(
					schema = @Schema(implementation = APIExceptionDTO.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
						value = """
							{
								"hasError": true,
								"message": "Category not found",
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
	public ResponseEntity<APIResponseData> updateCategory(
		 @PathVariable("id") String categoryId, @Valid @RequestBody CategoryDTO category
	)
	{
		this.validateId(categoryId);
		CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, category);

		updatedCategory.add(linkTo(methodOn(CategoryController.class).getCategoryById(updatedCategory.getId())).withSelfRel());
		updatedCategory.add(linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));

		APISuccess.RESOURCE_UPDATED.setMessage("Category updated");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_UPDATED, updatedCategory);
	}

	@Operation(
		summary = "Delete category",
		description = "Deletes an existing category. **Allowed roles:** ADMIN",
		parameters = @Parameter(
			name = "Category ID",
			description = "ID of the category to delete",
			example = "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
			required = true
		),
		responses = {
			@ApiResponse(
				responseCode = "204",
				description = "Category deleted successfully",
				content = @Content(
					schema = @Schema(implementation = Object.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE
				)
			),
			@ApiResponse(
				responseCode = "403",
				description = "Access denied: ADMIN role required",
				content = @Content(
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
			),
			@ApiResponse(
				responseCode = "404",
				description = "Category not found",
				content = @Content(
					schema = @Schema(implementation = APIExceptionDTO.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
						value = """
							{
								"hasError": true,
								"message": "Category not found",
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
	public ResponseEntity<APIResponseData> deleteCategory(@PathVariable("id") String categoryId) {
		this.validateId(categoryId);

		categoryService.deleteCategory(categoryId);

		APISuccess.RESOURCE_REMOVED.setMessage("Category deleted");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_REMOVED, null);
	}

	private void validateId(String id) {
		if (id == null || id.length() != 36) {
			throw new APIRequestException(APIError.INVALID_REQUEST_DATA);
		}
	}
}

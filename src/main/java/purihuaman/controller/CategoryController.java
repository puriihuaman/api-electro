package purihuaman.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import purihuaman.dto.CategoryDTO;
import purihuaman.enums.APIError;
import purihuaman.enums.APISuccess;
import purihuaman.exception.APIRequestException;
import purihuaman.service.CategoryService;
import purihuaman.util.APIResponse;
import purihuaman.util.APIResponseHandler;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;

	private ResponseEntity<APIResponse> API_RESPONSE;

	@GetMapping(produces = "application/json")
	public ResponseEntity<Object> getAllCategories() {
		List<CategoryDTO> categories = categoryService.getAllCategories();

		for (CategoryDTO category : categories) {
			category.add(linkTo(methodOn(CategoryController.class).getCategoryById(category.getCategoryId())).withSelfRel());
			category.add(linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));
		}

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_FETCHED_SUCCESSFULLY, categories);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_FETCHED_SUCCESSFULLY.getStatus());
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER', 'INVITED')")
	public ResponseEntity<Object> getCategoryById(final @PathVariable("id") String categoryId) {
		validateId(categoryId);

		CategoryDTO category = categoryService.getCategoryById(categoryId);

		category.add(linkTo(methodOn(CategoryController.class).getCategoryById(categoryId)).withSelfRel());
		category.add(linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));
		category.add(linkTo(methodOn(ProductController.class).getAllProducts(Map.of(
			"category_name",
			category.getCategoryName()
		))).withRel("products"));

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_FETCHED_SUCCESSFULLY, category);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_FETCHED_SUCCESSFULLY.getStatus());
	}

	@PostMapping(produces = "application/json")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Object> addCategory(final @Valid @RequestBody CategoryDTO category) {
		CategoryDTO createdCategory = categoryService.addCategory(category);

		createdCategory.add(linkTo(methodOn(CategoryController.class).getCategoryById(createdCategory.getCategoryId())).withSelfRel());
		createdCategory.add(linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_CREATED_SUCCESSFULLY, createdCategory);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_CREATED_SUCCESSFULLY.getStatus());
	}

	@PutMapping(value = "/{id}", produces = "application/json")
	@Transactional
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Object> updateCategory(
		final @PathVariable("id") String categoryId,
		final @Valid @RequestBody CategoryDTO category
	)
	{
		validateId(categoryId);
		CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, category);

		updatedCategory.add(linkTo(methodOn(CategoryController.class).getCategoryById(updatedCategory.getCategoryId())).withSelfRel());
		updatedCategory.add(linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_UPDATED_SUCCESSFULLY, updatedCategory);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_UPDATED_SUCCESSFULLY.getStatus());
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	@Transactional
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<APIResponse> deleteCategory(final @PathVariable("id") String categoryId) {
		validateId(categoryId);

		categoryService.deleteCategory(categoryId);

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_DELETED_SUCCESSFULLY, null);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_DELETED_SUCCESSFULLY.getStatus());
	}

	private void validateId(final String id) {
		if (id == null || id.length() != 36) {
			throw new APIRequestException(APIError.INVALID_REQUEST_DATA);
		}
	}
}

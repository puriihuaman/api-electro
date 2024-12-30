package purihuaman.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

@Validated
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;

	private ResponseEntity<APIResponse> API_RESPONSE;

	@GetMapping
	public ResponseEntity<Object> getAllCategories() {
		List<CategoryDTO> categories = categoryService.getAllCategories();

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_FETCHED_SUCCESSFULLY, categories);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_FETCHED_SUCCESSFULLY.getStatus());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getCategoryById(final @PathVariable("id") String categoryId) {
		validateId(categoryId);

		CategoryDTO category = categoryService.getCategoryById(categoryId);

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_FETCHED_SUCCESSFULLY, category);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_FETCHED_SUCCESSFULLY.getStatus());
	}

	@PostMapping
	public ResponseEntity<Object> addCategory(final @Valid @RequestBody CategoryDTO category) {
		CategoryDTO createdCategory = categoryService.addCategory(category);

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_CREATED_SUCCESSFULLY, createdCategory);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_CREATED_SUCCESSFULLY.getStatus());

	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateCategory(
		final @PathVariable("id") String categoryId,
		final @Valid @RequestBody CategoryDTO category
	)
	{
		validateId(categoryId);
		CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, category);

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_UPDATED_SUCCESSFULLY, updatedCategory);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_UPDATED_SUCCESSFULLY.getStatus());
	}

	@DeleteMapping(value = "/{id}")
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

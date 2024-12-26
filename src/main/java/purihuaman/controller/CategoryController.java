package purihuaman.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import purihuaman.dto.CategoryDTO;
import purihuaman.exception.ApiRequestException;
import purihuaman.service.CategoryService;
import purihuaman.util.ApiResponse;
import purihuaman.util.ApiResponseHandler;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	private HttpStatus HTTP_STATUS = HttpStatus.OK;
	private ResponseEntity<ApiResponse> API_RESPONSE;

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public ResponseEntity<Object> getAllCategories() {
		List<CategoryDTO> categories = categoryService.getAllCategories();

		HTTP_STATUS = HttpStatus.OK;

		API_RESPONSE = ApiResponseHandler.handleApiResponse("Categories successfully obtained", categories, HTTP_STATUS);
		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getCategoryById(@PathVariable("id") String categoryId) {
		validateId(categoryId);

		CategoryDTO category = categoryService.getCategoryById(categoryId);
		if (category == null) {
			throw new ApiRequestException(
				true,
				"Category not found",
				String.format("Category with id '%s' does not exist", categoryId),
				HttpStatus.NOT_FOUND
			);
		}

		HTTP_STATUS = HttpStatus.OK;
		API_RESPONSE = ApiResponseHandler.handleApiResponse("Category successfully obtained", category, HTTP_STATUS);

		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	@PostMapping
	public ResponseEntity<Object> addCategory(@Valid @RequestBody CategoryDTO category) {
		if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
			throw new ApiRequestException(true, "Required fields", "All fields required", HttpStatus.BAD_REQUEST);
		}

		CategoryDTO createdCategory = categoryService.addCategory(category);
		HTTP_STATUS = HttpStatus.CREATED;

		API_RESPONSE = ApiResponseHandler.handleApiResponse("Category created successfully", createdCategory, HTTP_STATUS);

		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateCategory(@PathVariable("id") String categoryId, @RequestBody CategoryDTO category)
	{
		validateId(categoryId);

		if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
			throw new ApiRequestException(true, "Required fields", "All fields required", HttpStatus.BAD_REQUEST);
		}

		CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, category);
		HTTP_STATUS = HttpStatus.OK;
		API_RESPONSE = ApiResponseHandler.handleApiResponse("Category updated successfully", updatedCategory, HTTP_STATUS);
		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable("id") String categoryId) {
		validateId(categoryId);

		Integer isDeleted = categoryService.deleteCategory(categoryId);

		if (isDeleted != 1) {
			throw new ApiRequestException(
				true,
				"Delete error",
				"Database error occurred while deleting the category",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		} else {
			HTTP_STATUS = HttpStatus.OK;
			API_RESPONSE = ApiResponseHandler.handleApiResponse("Category successfully deleted", null, HTTP_STATUS);
		}
		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	private void validateId(String id) {
		if (id == null || id.length() != 36) {
			throw new ApiRequestException(true, "Invalid ID", "The ID must be a String", HttpStatus.BAD_REQUEST);
		}
	}
}

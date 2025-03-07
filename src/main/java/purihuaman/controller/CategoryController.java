package purihuaman.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import purihuaman.dto.CategoryDTO;
import purihuaman.enums.APIError;
import purihuaman.enums.APISuccess;
import purihuaman.exception.APIRequestException;
import purihuaman.service.CategoryService;
import purihuaman.util.APIResponse;
import purihuaman.util.APIResponseHandler;

@Validated
@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-key")
@Slf4j
public class CategoryController {
	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public ResponseEntity<APIResponse> getAllCategories() {
		List<CategoryDTO> categories = categoryService.findAllCategories();

		for (CategoryDTO category : categories) {
			category.add(linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel());
			category.add(linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));
		}

		APISuccess.RESOURCE_RETRIEVED.setMessage("Recovered Categories");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_RETRIEVED, categories);
	}

	@GetMapping("/{id}")
	public ResponseEntity<APIResponse> getCategoryById(@PathVariable("id") String categoryId) {
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

	@PostMapping
	public ResponseEntity<APIResponse> addCategory(@Valid @RequestBody CategoryDTO category) {
		CategoryDTO createdCategory = categoryService.createCategory(category);

		createdCategory.add(linkTo(methodOn(CategoryController.class).getCategoryById(createdCategory.getId())).withSelfRel());
		createdCategory.add(linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));

		APISuccess.RESOURCE_CREATED.setMessage("Category created");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_CREATED, createdCategory);
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<APIResponse> updateCategory(
		@PathVariable("id") String categoryId,
		@Valid @RequestBody CategoryDTO category
	)
	{
		this.validateId(categoryId);
		CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, category);

		updatedCategory.add(linkTo(methodOn(CategoryController.class).getCategoryById(updatedCategory.getId())).withSelfRel());
		updatedCategory.add(linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));

		APISuccess.RESOURCE_UPDATED.setMessage("Category updated");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_UPDATED, updatedCategory);
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<APIResponse> deleteCategory(@PathVariable("id") String categoryId) {
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

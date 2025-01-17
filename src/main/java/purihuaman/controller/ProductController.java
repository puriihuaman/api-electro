package purihuaman.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import purihuaman.dto.ProductDTO;
import purihuaman.enums.APIError;
import purihuaman.enums.APISuccess;
import purihuaman.exception.APIRequestException;
import purihuaman.service.ProductService;
import purihuaman.util.APIResponse;
import purihuaman.util.APIResponseHandler;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	private ResponseEntity<APIResponse> API_RESPONSE;

	private final ProductService productService;

	@GetMapping(produces = "application/json")
	public ResponseEntity<APIResponse> getAllProducts(
		final @RequestParam Map<String, String> keywords
	)
	{
		List<ProductDTO> products;
		short offset = keywords.containsKey("offset") ? Short.parseShort(keywords.get("offset")) : 0;
		short limit = keywords.containsKey("limit") ? Short.parseShort(keywords.get("limit")) : 10;

		Pageable page = PageRequest.of(offset, limit);

		products =
			(keywords.isEmpty()) ? productService.getAllProducts(page) : productService.getProductsByFilters(keywords, page);

		products.forEach(product -> {
			product.add(linkTo(methodOn(ProductController.class).getProductById(product.getProductId())).withSelfRel());
			product.add(linkTo(methodOn(ProductController.class).getAllProducts(Map.of())).withRel("products"));
			product.add(linkTo(methodOn(CategoryController.class).getCategoryById(product
				.getCategory()
				.getCategoryId())).withRel("category"));
		});

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_FETCHED_SUCCESSFULLY, products);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_FETCHED_SUCCESSFULLY.getStatus());
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<APIResponse> getProductById(final @Valid @PathVariable("id") String productId) {
		validateId(productId);

		ProductDTO product = productService.getProductById(productId);

		product.add(linkTo(methodOn(ProductController.class).getProductById(productId)).withSelfRel());
		product.add(linkTo(methodOn(ProductController.class).getAllProducts(Map.of())).withRel("products"));
		product.add(linkTo(methodOn(CategoryController.class).getCategoryById(product
			.getCategory()
			.getCategoryId())).withRel("category"));

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_FETCHED_SUCCESSFULLY, product);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_FETCHED_SUCCESSFULLY.getStatus());
	}

	@PostMapping
	public ResponseEntity<APIResponse> addProduct(final @Valid @RequestBody ProductDTO product) {
		ProductDTO savedProduct = productService.addProduct(product);

		savedProduct.add(linkTo(methodOn(ProductController.class).getProductById(savedProduct.getProductId())).withSelfRel());
		savedProduct.add(linkTo(methodOn(ProductController.class).getAllProducts(Map.of())).withRel("products"));
		savedProduct.add(linkTo(methodOn(CategoryController.class).getCategoryById(savedProduct
			.getCategory()
			.getCategoryId())).withRel("category"));

		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_CREATED_SUCCESSFULLY, savedProduct);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_CREATED_SUCCESSFULLY.getStatus());
	}

	@PutMapping(value = "/{id}", produces = "application/json")
	@Transactional
	public ResponseEntity<APIResponse> updateProduct(
		final @PathVariable("id") String productId,
		final @Valid @RequestBody ProductDTO product
	)
	{
		validateId(productId);

		ProductDTO updatedProduct = productService.updateProduct(productId, product);
		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_UPDATED_SUCCESSFULLY, updatedProduct);

		updatedProduct.add(linkTo(methodOn(ProductController.class).getProductById(updatedProduct.getProductId())).withSelfRel());
		updatedProduct.add(linkTo(methodOn(ProductController.class).getAllProducts(Map.of())).withRel("products"));
		updatedProduct.add(linkTo(methodOn(CategoryController.class).getCategoryById(updatedProduct
			.getCategory()
			.getCategoryId())).withRel("category"));

		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_UPDATED_SUCCESSFULLY.getStatus());
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	@Transactional
	public ResponseEntity<APIResponse> deleteProduct(final @PathVariable("id") String productId) {
		validateId(productId);

		productService.deleteProduct(productId);
		API_RESPONSE = APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_DELETED_SUCCESSFULLY, null);
		return new ResponseEntity<>(API_RESPONSE.getBody(), APISuccess.RESOURCE_DELETED_SUCCESSFULLY.getStatus());
	}

	private void validateId(final String id) {
		if (id == null || id.length() != 36) {
			throw new APIRequestException(APIError.INVALID_REQUEST_DATA);
		}
	}
}

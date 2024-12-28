package purihuaman.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import purihuaman.dto.ProductDTO;
import purihuaman.exception.ApiRequestException;
import purihuaman.service.ProductService;
import purihuaman.util.ApiResponse;
import purihuaman.util.ApiResponseHandler;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	private HttpStatus HTTP_STATUS = HttpStatus.OK;
	private ResponseEntity<ApiResponse> API_RESPONSE;

	@Autowired
	private ProductService productService;

	@GetMapping
	public ResponseEntity<Object> getAllProducts(
		@RequestParam Map<String, String> keywords
	)
	{
		List<ProductDTO> products;
		short offset = keywords.containsKey("offset") ? Short.parseShort(keywords.get("offset")) : 0;
		short limit = keywords.containsKey("limit") ? Short.parseShort(keywords.get("limit")) : 10;

		Pageable page = PageRequest.of(offset, limit);

		products =
			(keywords.isEmpty()) ? productService.getAllProducts(page) : productService.getProductsByFilters(keywords, page);
		HTTP_STATUS = HttpStatus.OK;

		API_RESPONSE = ApiResponseHandler.handleApiResponse("Products successfully obtained", products, HTTP_STATUS);
		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getProductById(@Valid @PathVariable("id") String productId) {
		validateId(productId);

		ProductDTO product = productService.getProductById(productId);

		if (product == null) {
			throw new ApiRequestException(
				true,
				"Product not found",
				String.format("Product with id '%s' does not exist", productId),
				HttpStatus.NOT_FOUND
			);
		}

		API_RESPONSE = ApiResponseHandler.handleApiResponse("Product successfully obtained", product, HTTP_STATUS);
		HTTP_STATUS = HttpStatus.OK;

		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	@PostMapping
	public ResponseEntity<Object> addProduct(@Valid @RequestBody ProductDTO product) {
		validateProduct(product);

		ProductDTO savedProduct = productService.addProduct(product);
		HTTP_STATUS = HttpStatus.CREATED;
		API_RESPONSE = ApiResponseHandler.handleApiResponse("Product created successfully", savedProduct, HTTP_STATUS);

		return new ResponseEntity<>(API_RESPONSE, HTTP_STATUS);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable("id") String productId, @RequestBody ProductDTO product)
	{
		validateId(productId);

		validateProduct(product);

		ProductDTO updatedProduct = productService.updateProduct(productId, product);

		HTTP_STATUS = HttpStatus.OK;
		API_RESPONSE = ApiResponseHandler.handleApiResponse("Product updated successfully", updatedProduct, HTTP_STATUS);

		return new ResponseEntity<>(API_RESPONSE.getBody(), HTTP_STATUS);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteProduct(@PathVariable("id") String productId) {
		validateId(productId);

		Integer isDeleted = productService.deleteProduct(productId);
		if (isDeleted != 1) {
			throw new ApiRequestException(
				true,
				"Delete error",
				"Database error occurred while deleting the product",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		} else {
			HTTP_STATUS = HttpStatus.OK;
			API_RESPONSE = ApiResponseHandler.handleApiResponse("Product successfully deleted", null, HTTP_STATUS);
		}
		return new ResponseEntity<>(API_RESPONSE, HTTP_STATUS);
	}

	private void validateProduct(@RequestBody ProductDTO product) {
		if (product.getProductName() == null ||
			product.getProductName().isEmpty() ||
			product.getPrice() == null ||
			product.getPrice() < 0 ||
			product.getOldPrice() == null ||
			product.getOldPrice() < 0 ||
			product.getNewProduct() == null ||
			product.getNewProduct() < 0)
		{
			throw new ApiRequestException(true, "Required fields", "All fields required", HttpStatus.BAD_REQUEST);
		}
	}

	private void validateId(String id) {
		if (id == null || id.length() != 36) {
			throw new ApiRequestException(true, "Invalid ID", "The ID must be a String", HttpStatus.BAD_REQUEST);
		}
	}
}

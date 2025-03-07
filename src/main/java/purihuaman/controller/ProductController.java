package purihuaman.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<APIResponse> getAllProducts(@RequestParam Map<String, String> keywords) {
		List<ProductDTO> products;
		short offset = keywords.containsKey("offset") ? Short.parseShort(keywords.get("offset")) : 0;
		short limit = keywords.containsKey("limit") ? Short.parseShort(keywords.get("limit")) : 10;

		Pageable page = PageRequest.of(offset, limit);

		products = (keywords.isEmpty()) ? productService.findAllProducts(page) : productService.filterProducts(keywords,
		                                                                                                       page
		);

		products.forEach(product -> {
			product.add(linkTo(methodOn(ProductController.class).getProductById(product.getId())).withSelfRel());
			product.add(linkTo(methodOn(ProductController.class).getAllProducts(Map.of())).withRel("products"));
			product.add(linkTo(methodOn(CategoryController.class).getCategoryById(product
				                                                                      .getCategory()
				                                                                      .getId())).withRel("category"));
		});

		APISuccess.RESOURCE_RETRIEVED.setMessage("Recovered products");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_RETRIEVED, products);
	}

	@GetMapping("/{id}")
	public ResponseEntity<APIResponse> getProductById(@Valid @PathVariable("id") String productId) {
		this.validateId(productId);

		ProductDTO product = productService.findProductById(productId);

		product.add(linkTo(methodOn(ProductController.class).getProductById(productId)).withSelfRel());
		product.add(linkTo(methodOn(ProductController.class).getAllProducts(Map.of())).withRel("products"));
		product.add(linkTo(methodOn(CategoryController.class).getCategoryById(product.getCategory().getId())).withRel(
			"category"));

		APISuccess.RESOURCE_RETRIEVED.setMessage("Recovered product");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_RETRIEVED, product);
	}

	@PostMapping
	public ResponseEntity<APIResponse> addProduct(@Valid @RequestBody ProductDTO product) {
		ProductDTO savedProduct = productService.createProduct(product);

		savedProduct.add(linkTo(methodOn(ProductController.class).getProductById(savedProduct.getId())).withSelfRel());
		savedProduct.add(linkTo(methodOn(ProductController.class).getAllProducts(Map.of())).withRel("products"));
		savedProduct.add(linkTo(methodOn(CategoryController.class).getCategoryById(savedProduct
			                                                                           .getCategory()
			                                                                           .getId())).withRel("category"));

		APISuccess.RESOURCE_CREATED.setMessage("Created product");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_CREATED, savedProduct);
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<APIResponse> updateProduct(
		@PathVariable("id") String productId,
		@Valid @RequestBody ProductDTO product
	)
	{
		this.validateId(productId);

		ProductDTO updatedProduct = productService.updateProduct(productId, product);

		updatedProduct.add(linkTo(methodOn(ProductController.class).getProductById(updatedProduct.getId())).withSelfRel());
		updatedProduct.add(linkTo(methodOn(ProductController.class).getAllProducts(Map.of())).withRel("products"));
		updatedProduct.add(linkTo(methodOn(CategoryController.class).getCategoryById(updatedProduct
			                                                                             .getCategory()
			                                                                             .getId())).withRel("category"));

		APISuccess.RESOURCE_UPDATED.setMessage("Updated product");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_UPDATED, updatedProduct);

	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<APIResponse> deleteProduct(@PathVariable("id") String productId) {
		this.validateId(productId);

		productService.deleteProduct(productId);

		APISuccess.RESOURCE_REMOVED.setMessage("Deleted product");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_REMOVED, null);
	}

	private void validateId(String id) {
		if (id == null || id.length() != 36) {
			throw new APIRequestException(APIError.INVALID_REQUEST_DATA);
		}
	}
}

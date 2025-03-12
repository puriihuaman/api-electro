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
import purihuaman.dto.ProductDTO;
import purihuaman.enums.APIError;
import purihuaman.enums.APISuccess;
import purihuaman.exception.APIRequestException;
import purihuaman.service.ProductService;
import purihuaman.util.APIResponseData;
import purihuaman.util.APIResponseHandler;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Product", description = "Operations for product management")
@Validated
@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@Operation(
		summary = "Get all users with pagination and filters",
		description = """
			Retrieves a paginated list of products.
			**Allowed roles:** ADMIN, USER, INVITED
			Available filters (use as query params):
			- productName: Filter by product name (contains).
			- price: Filter by price (equal, lower or higher).
			- oldPrice: Filter by old price (equal, lower or higher).
			- newProduct: Filter by new or old product (1 or 0).
			- categoryName: Filter by category name (contains).
			""",
		parameters = {
			@Parameter(
				name = "productName",
				description = "Product name to filter",
				example = "Laptop"
			),
			@Parameter(
				name = "price",
				description = "Price to filter",
				example = "5000.00"
			),
			@Parameter(
				name = "oldPrice",
				description = "Old price to filter",
				example = "6500.00"
			),
			@Parameter(
				name = "newProduct",
				description = "Value of new product to filter",
				example = "1"
			),
			@Parameter(
				name = "categoryName",
				description = "Category name to filter",
				example = "Tech"
			),
			@Parameter(
				name = "offset",
				description = "Indicates from which position (page) the query should start",
				example = "0"
			),
			@Parameter(
				name = "limit",
				description = "Indicates the maximum number of elements on a page",
				example = "10"
			)
		},
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Product list successfully retrieved",
				content = @Content(
					schema = @Schema(implementation = Object.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
						value = """
								{
									"data": [
										{
											"id": "06b94ddf-dd4a-44d6-bf8b-9973a9711ae8",
											"productName": "Laptop Samsung",
											"price": 5000.00,
											"oldPrice": 7000.00,
											"newProduct": 1,
											"photo": "/images/laptop.jpg",
											"category" : {
												"id": "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
												"categoryName": "Technology"
											}
										},
										{
											"id": "06b94ddf-dd4a-44d6-bf8b-9973a9711ae8",
											"productName": "Laptop MSI",
											"price": 5000.00,
											"oldPrice": 7000.00,
											"newProduct": 1,
											"photo": "/images/laptop.jpg",
											"category" : {
												"id": "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
												"categoryName": "Technology"
											}
										}
									],
									"hasError": false,
									"message": "Recovered products",
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
	public ResponseEntity<APIResponseData> getAllProducts(@RequestParam Map<String, String> keywords) {
		List<ProductDTO> products;
		short offset = keywords.containsKey("offset") ? Short.parseShort(keywords.get("offset")) : 0;
		short limit = keywords.containsKey("limit") ? Short.parseShort(keywords.get("limit")) : 10;

		Pageable page = PageRequest.of(offset, limit);

		products = (keywords.isEmpty()) ? productService.findAllProducts(page) : productService.filterProducts(
			keywords,
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

	@Operation(
		summary = "Get a product by ID",
		description = "Retrieves the details of an existing product. **Allowed roles:** ADMIN, USER",
		parameters = @Parameter(
			name = "Product ID",
			description = "ID of the product to search for",
			example = "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
			required = true,
			hidden = true,
			schema = @Schema(implementation = UUID.class)
		),
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Product found",
				content = @Content(
					schema = @Schema(implementation = Object.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
						value = """
							{
								"data": {
									"id": "06b94ddf-dd4a-44d6-bf8b-9973a9711ae8",
									"productName": "Laptop Samsung",
									"price": 5000.00,
									"oldPrice": 7000.00,
									"newProduct": 1,
									"photo": "/images/laptop.jpg",
									"category" : {
										"id": "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
										"categoryName": "Technology"
									}
								},
								"hasError": false,
								"message": "Recovered product",
								"statusCode": 200,
								"timestamp": "2025-03-07T10:32:57.524631"
							}
							"""
			)
			)
			), @ApiResponse(
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
		), @ApiResponse(
			responseCode = "404",
			description = "Product not found",
			content = @Content(
			schema = @Schema(implementation = APIExceptionDTO.class),
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = @ExampleObject(
				value = """
					{
						"hasError": true,
					    "message": "Product not found",
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
	public ResponseEntity<APIResponseData> getProductById(@Valid @PathVariable("id") String productId) {
		this.validateId(productId);

		ProductDTO product = productService.findProductById(productId);

		product.add(linkTo(methodOn(ProductController.class).getProductById(productId)).withSelfRel());
		product.add(linkTo(methodOn(ProductController.class).getAllProducts(Map.of())).withRel("products"));
		product.add(linkTo(methodOn(CategoryController.class).getCategoryById(product.getCategory().getId())).withRel(
			"category"));

		APISuccess.RESOURCE_RETRIEVED.setMessage("Recovered product");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_RETRIEVED, product);
	}

	@Operation(
		summary = "Create new product",
		description = "Creates a new product. **Allowed roles:** ADMIN, USER",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Data for the product to be created",
			required = true,
			content = @Content(
				schema = @Schema(implementation = Object.class),
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				examples = @ExampleObject(
					value = """
						{
							"productName": "Laptop Samsung",
							"price": 5000.00,
							"oldPrice": 7000.00,
							"newProduct": 1,
							"photo": "/images/laptop.jpg",
							"category" : {
								"id": "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
								"categoryName": "Technology"
							}
						}
						"""
				)
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Product created successfully",
				content = @Content(
					schema = @Schema(implementation = Object.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
						value = """
							{
								"data": {
									"id": "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
									"productName": "Laptop Samsung",
									"price": 5000.00,
									"oldPrice": 7000.00,
									"newProduct": 1,
									"photo": "/images/laptop.jpg",
									"category" : {
										"id": "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
										"categoryName": "Technology"
									}
								},
							    "hasError": false,
							    "message": "Product created",
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
									"productName": "The field is empty."
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
	public ResponseEntity<APIResponseData> addProduct(@Valid @RequestBody ProductDTO product) {
		ProductDTO savedProduct = productService.createProduct(product);

		savedProduct.add(linkTo(methodOn(ProductController.class).getProductById(savedProduct.getId())).withSelfRel());
		savedProduct.add(linkTo(methodOn(ProductController.class).getAllProducts(Map.of())).withRel("products"));
		savedProduct.add(linkTo(methodOn(CategoryController.class).getCategoryById(savedProduct
			                                                                           .getCategory()
			                                                                           .getId())).withRel("category"));

		APISuccess.RESOURCE_CREATED.setMessage("Created product");
		return APIResponseHandler.handleApiResponse(APISuccess.RESOURCE_CREATED, savedProduct);
	}

	@Operation(
		summary = "Update product",
		description = "Updates an existing product. **Allowed roles:** ADMIN",
		parameters = @Parameter(
			name = "Product ID",
			description = "ID of the product to update",
			example = "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
			required = true
		),
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Data for the product to be updated",
			required = true,
			content = @Content(
				schema = @Schema(implementation = Object.class),
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				examples = @ExampleObject(
					value = """
					{
						"productName": "Laptop Samsung",
						"price": 5000.00,
						"oldPrice": 7000.00,
						"newProduct": 1,
						"photo": "/images/laptop.jpg",
						"category" : {
							"id": "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
							"categoryName": "Technology"
						}
					}
					"""
			)
		)
		),
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Product updated successfully",
				content = @Content(
					schema = @Schema(implementation = Object.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
					value = """
						{
							"data": {
								"id": "a3a131f3-2991-4ffb-aeb2-e4df775ad8a6",
								"productName": "Laptop Samsung",
								"price": 5000.00,
								"oldPrice": 6500.00,
								"newProduct": 1,
								"photo": "/images/laptop.png",
								"category": {
									"id": "e144cecb-29d5-439b-a739-b4e4eafb81b9",
									"categoryName": "Technology"
								}
							},
							"hasError": false,
							"message": "Updated product",
							"statusCode": 200,
							"timestamp": "2025-03-10T13:45:39.0155997"
						}
						"""
				)
			)
			), @ApiResponse(
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
							"productName": "The field must contain at least 6 characters."
						},
						"timestamp": "2025-03-07T10:12:35.9687337"
					}
					"""
			)
		)
		), @ApiResponse(
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
		), @ApiResponse(
			responseCode = "404",
			description = "Product not found",
			content = @Content(
				schema = @Schema(implementation = APIExceptionDTO.class),
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				examples = @ExampleObject(
					value = """
					{
						"hasError": true,
						"message": "Product not found",
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
	public ResponseEntity<APIResponseData> updateProduct(
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

	@Operation(
		summary = "Delete product",
		description = "Delete an existing product. **Allowed roles:** ADMIN",
		parameters = @Parameter(
			name = "Product ID",
			description = "ID of the product to delete",
			example = "cf29e4ab-b7b2-4af1-a679-68e926514dcb",
			required = true
		),
		responses = {
			@ApiResponse(
				responseCode = "204",
				description = "Product deleted successfully",
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
			),
			@ApiResponse(
				responseCode = "404",
				description = "Product not found",
				content = @Content(
					schema = @Schema(implementation = APIExceptionDTO.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = @ExampleObject(
						value = """
							{
								"hasError": true,
								"message": "Product not found",
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
	public ResponseEntity<APIResponseData> deleteProduct(@PathVariable("id") String productId) {
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

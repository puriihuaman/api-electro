package purihuaman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import purihuaman.dto.ProductDTO;
import purihuaman.service.ProductService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	//	@RequestParam(value = "product_name", required = false) String productName
	@GetMapping
	public ResponseEntity<List<ProductDTO>> getAllProducts(
		@RequestParam Map<String, String> keywords
	)
	{
		List<ProductDTO> products;
		short offset = keywords.containsKey("offset") ? Short.parseShort(keywords.get("offset")) : 0;
		short limit = keywords.containsKey("limit") ? Short.parseShort(keywords.get("limit")) : 10;

		Pageable page = PageRequest.of(offset, limit);

		products =
			(keywords.isEmpty()) ? productService.getAllProducts() : productService.getProductsByFilters(keywords, page);

		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") String productId) {
		ProductDTO product = productService.getProductById(productId);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO product) {
		ProductDTO savedProduct = productService.addProduct(product);
		return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") String productId, @RequestBody ProductDTO product)
	{
		ProductDTO updatedProduct = productService.updateProduct(productId, product);
		return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("id") String productId) {
		Integer isDeleted = productService.deleteProduct(productId);
		if (isDeleted == 1) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

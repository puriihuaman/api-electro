package purihuaman.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import purihuaman.dto.ProductDTO;

public interface ProductService {
	List<ProductDTO> findAllProducts(Pageable page);

	ProductDTO findProductById(String productId);

	List<ProductDTO> filterProducts(Map<String, String> filters, Pageable page);

	ProductDTO createProduct(ProductDTO product);

	ProductDTO updateProduct(String productId, ProductDTO product);

	void deleteProduct(String productId);
}

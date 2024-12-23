package purihuaman.service;

import org.springframework.data.domain.Pageable;
import purihuaman.dto.ProductDTO;

import java.util.List;
import java.util.Map;

public interface ProductService {
	List<ProductDTO> getAllProducts(Pageable page);

	ProductDTO getProductById(String productId);

	List<ProductDTO> getProductsByFilters(Map<String, String> filters, Pageable page);

	ProductDTO addProduct(ProductDTO product);

	ProductDTO updateProduct(String productId, ProductDTO product);

	Integer deleteProduct(String productId);
}

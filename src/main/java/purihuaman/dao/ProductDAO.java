package purihuaman.dao;

import org.springframework.data.domain.Pageable;
import purihuaman.dto.ProductDTO;

import java.util.List;
import java.util.Map;

public interface ProductDAO {
	List<ProductDTO> getAllProducts();

	ProductDTO getProductById(String productId);

	List<ProductDTO> searchProductByName(String productName);

	List<ProductDTO> filterProducts(Map<String, String> filters, Pageable page);

	ProductDTO addProduct(ProductDTO product);

	ProductDTO updateProduct(String productId, ProductDTO product);

	Integer deleteProduct(String productId);
}

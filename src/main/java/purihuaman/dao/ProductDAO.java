package purihuaman.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import purihuaman.entity.ProductEntity;

public interface ProductDAO {
	Page<ProductEntity> findAllProducts(Pageable page);

	Optional<ProductEntity> findProductById(String productId);

	List<ProductEntity> searchProductByName(String productName);

	Page<ProductEntity> filterProducts(Specification<ProductEntity> spec, Pageable page);

	ProductEntity createProduct(ProductEntity productEntity);

	ProductEntity updateProduct(ProductEntity productEntity);

	void deleteProduct(String productId);
}

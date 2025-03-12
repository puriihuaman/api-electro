package purihuaman.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import purihuaman.entity.ProductEntity;

import java.util.Optional;

public interface ProductDAO {
	Page<ProductEntity> findAllProducts(Pageable page);

	Optional<ProductEntity> findProductById(String productId);

	Page<ProductEntity> filterProducts(Specification<ProductEntity> spec, Pageable page);

	ProductEntity createProduct(ProductEntity productEntity);

	ProductEntity updateProduct(ProductEntity productEntity);

	void deleteProduct(String productId);
}

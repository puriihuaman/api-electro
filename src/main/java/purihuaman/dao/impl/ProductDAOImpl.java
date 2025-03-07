package purihuaman.dao.impl;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import purihuaman.dao.ProductDAO;
import purihuaman.dao.repository.ProductRepository;
import purihuaman.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductDAOImpl implements ProductDAO {
	private final ProductRepository productRepository;

	public ProductDAOImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public Page<ProductEntity> findAllProducts(Pageable page) {
		return productRepository.findAll(page);
	}

	@Override
	public Optional<ProductEntity> findProductById(String productId) {
		return productRepository.findById(productId);
	}

	@Override
	public List<ProductEntity> searchProductByName(String productName) {
		return productRepository.searchProductByName(productName);
	}

	@Override
	public Page<ProductEntity> filterProducts(Specification<ProductEntity> spec, Pageable page) {
		return productRepository.findAll(spec, page);
	}

	@Override
	public ProductEntity createProduct(@Valid ProductEntity productEntity) {
		return productRepository.save(productEntity);
	}

	@Override
	public ProductEntity updateProduct(@Valid ProductEntity productEntity) {
		return productRepository.save(productEntity);
	}

	@Override
	public void deleteProduct(String productId) {
		productRepository.deleteById(productId);
	}
}

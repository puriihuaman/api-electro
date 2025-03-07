package purihuaman.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import purihuaman.dao.ProductDAO;
import purihuaman.dto.CategoryDTO;
import purihuaman.dto.ProductDTO;
import purihuaman.entity.CategoryEntity;
import purihuaman.entity.ProductEntity;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;
import purihuaman.mapper.CategoryMapper;
import purihuaman.mapper.ProductMapper;
import purihuaman.service.CategoryService;
import purihuaman.service.ProductService;
import purihuaman.util.ProductSpecification;

@Service
public class ProductServiceImpl implements ProductService {
	private final ProductDAO productDao;
	private final ProductMapper productMapper;
	private final CategoryService categoryService;
	private final CategoryMapper categoryMapper;

	public ProductServiceImpl(
		ProductDAO productDao,
		ProductMapper productMapper,
		CategoryService categoryService,
		CategoryMapper categoryMapper
	)
	{
		this.productDao = productDao;
		this.productMapper = productMapper;
		this.categoryService = categoryService;
		this.categoryMapper = categoryMapper;
	}

	@Override
	public List<ProductDTO> findAllProducts(Pageable page) {
		try {
			List<ProductEntity> productEntities = productDao.findAllProducts(page).getContent();
			return productMapper.toProductDTOList(productEntities);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ProductDTO findProductById(String productId) {
		try {
			Optional<ProductEntity> optionalProduct = productDao.findProductById(productId);

			if (optionalProduct.isEmpty()) {
				APIError.RECORD_NOT_FOUND.setMessage("Product not found");
				throw new APIRequestException(APIError.RECORD_NOT_FOUND);
			}

			return productMapper.toProductDTO(optionalProduct.get());
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<ProductDTO> filterProducts(Map<String, String> filters, Pageable page) {
		try {
			Specification<ProductEntity> spec = ProductSpecification.filterProducts(filters);
			List<ProductEntity> productEntities = productDao.filterProducts(spec, page).getContent();
			return productMapper.toProductDTOList(productEntities);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ProductDTO createProduct(ProductDTO product) {
		try {
			if (product.getCategory() == null) {
				throw new APIRequestException(APIError.BAD_REQUEST);
			}
			CategoryDTO found = categoryService.findCategoryById(product.getCategory().getId());

			CategoryEntity category = categoryMapper.toCategoryModel(found);

			product.setId(UUID.randomUUID().toString());
			product.setCategory(category);

			ProductEntity savedProductEntity = productMapper.toProductModel(product);

			return productMapper.toProductDTO(productDao.createProduct(savedProductEntity));
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataIntegrityViolationException ex) {
			Throwable cause = ex.getCause();
			Throwable rootCause = cause.getCause();
			if (rootCause instanceof ConstraintViolationException ||
				rootCause instanceof DataIntegrityViolationException)
				throw new APIRequestException(APIError.UNIQUE_CONSTRAINT_VIOLATION);
			else throw new APIRequestException(APIError.RESOURCE_ASSOCIATED_EXCEPTION);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ProductDTO updateProduct(String productId, ProductDTO product) {
		try {
			ProductEntity productEntityExisting = productMapper.toProductModel(this.findProductById(productId));

			productEntityExisting.setProductName(product.getProductName());
			productEntityExisting.setPrice(product.getPrice());
			productEntityExisting.setOldPrice(product.getOldPrice());
			productEntityExisting.setNewProduct(product.getNewProduct());
			productEntityExisting.setPhoto(product.getPhoto());
			productEntityExisting.setCategory(product.getCategory());

			return productMapper.toProductDTO(productDao.updateProduct(productEntityExisting));
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataIntegrityViolationException ex) {
			Throwable cause = ex.getCause();
			Throwable rootCause = cause.getCause();
			if (cause instanceof ConstraintViolationException || rootCause instanceof DataIntegrityViolationException)
				throw new APIRequestException(APIError.UNIQUE_CONSTRAINT_VIOLATION);
			else throw new APIRequestException(APIError.RESOURCE_ASSOCIATED_EXCEPTION);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void deleteProduct(String productId) {
		try {
			ProductEntity productEntity = productMapper.toProductModel(this.findProductById(productId));

			productDao.deleteProduct(productEntity.getId());
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataIntegrityViolationException ex) {
			Throwable cause = ex.getCause();
			Throwable rootCause = cause.getCause();
			if (cause instanceof ConstraintViolationException || rootCause instanceof DataIntegrityViolationException)
				throw new APIRequestException(APIError.UNIQUE_CONSTRAINT_VIOLATION);
			else throw new APIRequestException(APIError.RESOURCE_ASSOCIATED_EXCEPTION);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}
}

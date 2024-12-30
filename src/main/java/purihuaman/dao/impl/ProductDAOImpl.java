package purihuaman.dao.impl;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import purihuaman.dao.ProductDAO;
import purihuaman.dao.repository.ProductRepository;
import purihuaman.dto.ProductDTO;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;
import purihuaman.mapper.ProductMapper;
import purihuaman.model.ProductModel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductDAOImpl implements ProductDAO {
	private final ProductMapper productMapper;
	private final ProductRepository productRepository;

	@Override
	public List<ProductDTO> getAllProducts(final Pageable page) {
		try {
			List<ProductModel> products = productRepository.findAll(page).getContent();
			return productMapper.toProductDTOList(products);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ProductDTO getProductById(final String productId) {
		try {
			ProductModel
				product =
				productRepository
					.getProductById(productId)
					.orElseThrow(() -> new APIRequestException(APIError.RECORD_NOT_FOUND));

			return productMapper.toProductDTO(product);
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<ProductDTO> searchProductByName(final String productName) {
		List<ProductModel> products = productRepository.searchProductByName(productName);
		return productMapper.toProductDTOList(products);
	}

	@Override
	public List<ProductDTO> filterProducts(final Map<String, String> filters, final Pageable page) {
		try {
			Short offset = (short) page.getOffset();
			Short limit = (short) page.getPageSize();

			List<ProductModel> filteredProducts = productRepository.filterProducts(
				filters.containsKey("product_name") ? filters.get("product_name").trim() : null,
				filters.containsKey("min_price") ? Double.parseDouble(filters.get("min_price").trim()) : null,
				filters.containsKey("max_price") ? Double.parseDouble(filters.get("max_price").trim()) : null,
				filters.containsKey("new_product") ? Integer.parseInt(filters.get("new_product").trim()) : null,
				filters.containsKey("category_name") ? filters.get("category_name").trim() : null,
				offset,
				limit
			);

			return productMapper.toProductDTOList(filteredProducts);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ProductDTO addProduct(final @Valid ProductDTO product) {
		try {
			ProductModel productModel = productMapper.toProductModel(product);
			productModel.setProductId(UUID.randomUUID().toString());

			ProductModel savedProduct = productRepository.save(productModel);

			return productMapper.toProductDTO(savedProduct);
		} catch (DataIntegrityViolationException ex) {
			Throwable cause = ex.getCause();
			Throwable rootCause = cause.getCause();
			if (rootCause instanceof ConstraintViolationException || rootCause instanceof DataIntegrityViolationException)
				throw new APIRequestException(APIError.UNIQUE_CONSTRAINT_VIOLATION);
			else throw new APIRequestException(APIError.RESOURCE_ASSOCIATED_EXCEPTION);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ProductDTO updateProduct(final String productId, final @Valid ProductDTO product) {

		try {
			ProductDTO productExisting = getProductById(productId);

			productExisting.setProductName(product.getProductName());
			productExisting.setPrice(product.getPrice());
			productExisting.setOldPrice(product.getOldPrice());
			productExisting.setNewProduct(product.getNewProduct());
			productExisting.setPhoto(product.getPhoto());
			productExisting.setCategory(product.getCategory());

			ProductModel updatedProduct = productRepository.save(productMapper.toProductModel(productExisting));

			return productMapper.toProductDTO(updatedProduct);
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
	public Integer deleteProduct(final String productId) {
		try {
			ProductDTO product = getProductById(productId);

			ProductModel productModel = productMapper.toProductModel(product);
			productRepository.deleteProductById(productModel.getProductId());

			return 1;
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

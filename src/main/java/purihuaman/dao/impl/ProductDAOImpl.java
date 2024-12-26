package purihuaman.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import purihuaman.dao.ProductDAO;
import purihuaman.dao.repository.ProductRepository;
import purihuaman.dto.ProductDTO;
import purihuaman.exception.ApiRequestException;
import purihuaman.mapper.ProductMapper;
import purihuaman.model.ProductModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductDAOImpl implements ProductDAO {
	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<ProductDTO> getAllProducts(Pageable page) {
		try {
			List<ProductModel> products = productRepository.findAll(page).getContent();
			return productMapper.toProductDTOList(products);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"Database error",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public ProductDTO getProductById(String productId) {
		try {
			Optional<ProductModel> result = Optional.ofNullable(productRepository.getProductById(productId));
			if (result.isEmpty()) {
				throw new ApiRequestException(
					true,
					"Product not found",
					String.format("Product with id %s not found", productId),
					HttpStatus.NOT_FOUND
				);
			}
			return result.map((productModel) -> productMapper.toProductDTO(productModel)).orElse(null);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"An error occurred",
				"An error occurred internally. Please try again.",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public List<ProductDTO> searchProductByName(String productName) {
		List<ProductModel> products = productRepository.searchProductByName(productName);
		return productMapper.toProductDTOList(products);
	}

	@Override
	public List<ProductDTO> filterProducts(Map<String, String> filters, Pageable page) {
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
			throw new ApiRequestException(
				true,
				"Database error",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public ProductDTO addProduct(ProductDTO product) {
		try {
			ProductModel productModel = productMapper.toProductModel(product);
			productModel.setProductId(UUID.randomUUID().toString());

			ProductModel savedProduct = productRepository.save(productModel);

			return productMapper.toProductDTO(savedProduct);
		} catch (DataIntegrityViolationException ex) {
			throw new ApiRequestException(true, "Duplicate key", "The record name is already in use", HttpStatus.CONFLICT);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"Database error",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public ProductDTO updateProduct(String productId, ProductDTO product) {
		ProductDTO productExisting = getProductById(productId);

		try {
			productExisting.setProductName(product.getProductName());
			productExisting.setPrice(product.getPrice());
			productExisting.setOldPrice(product.getOldPrice());
			productExisting.setNewProduct(product.getNewProduct());
			productExisting.setPhoto(product.getPhoto());
			productExisting.setCategory(product.getCategory());

			ProductModel updatedProduct = productRepository.save(productMapper.toProductModel(productExisting));

			return productMapper.toProductDTO(updatedProduct);
		} catch (DataIntegrityViolationException ex) {
			throw new ApiRequestException(true, "Duplicate key", "The record name is already in use", HttpStatus.CONFLICT);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"Database error",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public Integer deleteProduct(String productId) {
		try {
			ProductDTO product = getProductById(productId);

			if (product == null) {
				throw new ApiRequestException(
					true,
					"Product not found",
					String.format("Product with id %s not found", productId),
					HttpStatus.NOT_FOUND
				);
			}

			ProductModel productModel = productMapper.toProductModel(product);
			productRepository.deleteProductById(productModel.getProductId());

			return 1;
		} catch (DataIntegrityViolationException ex) {
			throw new ApiRequestException(
				true,
				"Cannot delete record",
				"The record is linked to the other record(s) and cannot be deleted",
				HttpStatus.CONFLICT
			);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"Delete error",
				"Database error occurred while deleting the product",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}
}

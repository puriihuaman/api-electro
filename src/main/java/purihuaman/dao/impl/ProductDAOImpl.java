package purihuaman.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import purihuaman.dao.ProductDAO;
import purihuaman.dao.repository.ProductRepository;
import purihuaman.dto.ProductDTO;
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
	public List<ProductDTO> getAllProducts() {
		return productMapper.toProductDTOList(productRepository.getAllProducts());
	}

	@Override
	public ProductDTO getProductById(String productId) {
		Optional<ProductModel> result = Optional.ofNullable(productRepository.getProductById(productId));
		return result.map((productModel) -> productMapper.toProductDTO(productModel)).orElse(null);
	}

	@Override
	public List<ProductDTO> searchProductByName(String productName) {
		List<ProductModel> products = productRepository.searchProductByName(productName);
		return productMapper.toProductDTOList(products);
	}

	@Override
	public List<ProductDTO> filterProducts(Map<String, String> filters, Pageable page) {
		Short offset = (short) page.getOffset();
		Short limit = (short) page.getPageSize();

		List<ProductModel> filteredProducts = productRepository.filterProducts(
			filters.containsKey("product_name") ? filters.get("product_name").trim() : null,
			filters.containsKey("min_price") ? Double.parseDouble(filters.get("min_price").trim()) : null,
			filters.containsKey("max_price") ? Double.parseDouble(filters.get("max_price").trim()) : null,
			filters.containsKey("new_product") ? Integer.parseInt(filters.get("new_product").trim()) : null,
			offset,
			limit
		);

		return productMapper.toProductDTOList(filteredProducts);
	}

	@Override
	public ProductDTO addProduct(ProductDTO product) {
		ProductModel productModel = productMapper.toProductModel(product);
		productModel.setProductId(UUID.randomUUID().toString());

		ProductModel savedProduct = productRepository.save(productModel);

		return productMapper.toProductDTO(savedProduct);
	}

	@Override
	public ProductDTO updateProduct(String productId, ProductDTO product) {
		ProductDTO productExisting = getProductById(productId);

		if (productExisting != null) {
			productExisting.setProductName(product.getProductName());
			productExisting.setPrice(product.getPrice());
			productExisting.setOldPrice(product.getOldPrice());
			productExisting.setNewProduct(product.getNewProduct());
			productExisting.setPhoto(product.getPhoto());
			productExisting.setCategory(product.getCategory());

			ProductModel updatedProduct = productRepository.save(productMapper.toProductModel(productExisting));

			return productMapper.toProductDTO(updatedProduct);
		}
		return null;
	}

	@Override
	public Integer deleteProduct(String productId) {
		ProductDTO product = getProductById(productId);
		if (product != null) {
			ProductModel productModel = productMapper.toProductModel(product);
			productRepository.deleteProductById(productModel.getProductId());

			return 1;
		}
		return 0;
	}
}

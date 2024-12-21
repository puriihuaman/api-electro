package purihuaman.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import purihuaman.dao.ProductDAO;
import purihuaman.dto.ProductDTO;
import purihuaman.service.ProductService;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDAO productDAO;

	@Override
	public List<ProductDTO> getAllProducts() {
		return productDAO.getAllProducts();
	}

	@Override
	public ProductDTO getProductById(String productId) {
		return productDAO.getProductById(productId);
	}

	@Override
	public List<ProductDTO> getProductsByFilters(Map<String, String> filters, Pageable page) {
		return productDAO.filterProducts(filters, page);
	}

	@Override
	public ProductDTO addProduct(ProductDTO product) {
		return productDAO.addProduct(product);
	}

	@Override
	public ProductDTO updateProduct(String productId, ProductDTO product) {
		return productDAO.updateProduct(productId, product);
	}

	@Override
	public Integer deleteProduct(String productId) {
		return productDAO.deleteProduct(productId);
	}
}

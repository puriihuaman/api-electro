package purihuaman.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import purihuaman.dao.ProductDAO;
import purihuaman.dto.ProductDTO;
import purihuaman.service.ProductService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductDAO productDAO;

	@Override
	public List<ProductDTO> getAllProducts(Pageable page) {
		return productDAO.getAllProducts(page);
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

package purihuaman.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import purihuaman.model.ProductModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, String> {

	@Query(nativeQuery = true, value = "CALL read_products()")
	List<ProductModel> getAllProducts();

	@Query(nativeQuery = true, value = "CALL get_product_by_id(:id);")
	Optional<ProductModel> getProductById(@Param("id") String productId);

	@Query(nativeQuery = true, value = "CALL delete_product(:id)")
	void deleteProductById(@Param("id") String productId);

	@Query(nativeQuery = true, value = "CALL search_product_by_name(:productName)")
	List<ProductModel> searchProductByName(@Param("productName") String productName);

	@Query(
		nativeQuery = true,
		value = "CALL get_products_by_filters(:name, :minPrice, :maxPrice, :newProduct, :categoryName, :offset, :limit)"
	)
	List<ProductModel> filterProducts(
		@Param("name") String productName,
		@Param("minPrice") Double minPrice,
		@Param("maxPrice") Double maxPrice,
		@Param("newProduct") Integer newProduct,
		@Param("categoryName") String categoryName,
		@Param("offset") Short offset,
		@Param("limit") Short limit
	);
}

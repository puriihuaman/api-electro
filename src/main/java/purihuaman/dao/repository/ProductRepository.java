package purihuaman.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import purihuaman.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String>, JpaSpecificationExecutor<ProductEntity> {

	@Query(value = "CALL read_products()", nativeQuery = true)
	List<ProductEntity> getAllProducts();

	@Query(value = "CALL get_product_by_id(:id);", nativeQuery = true)
	Optional<ProductEntity> getProductById(@Param("id") String productId);

	@Query(value = "CALL delete_product(:id)", nativeQuery = true)
	void deleteProductById(@Param("id") String productId);

	@Query(value = "CALL search_product_by_name(:productName)", nativeQuery = true)
	List<ProductEntity> searchProductByName(@Param("productName") String productName);

	@Query(value = "CALL get_products_by_filters(:name, :minPrice, :maxPrice, :newProduct, :categoryName, :offset, :limit)", nativeQuery = true)
	List<ProductEntity> filterProducts(@Param("name") String productName, @Param("minPrice") double minPrice,
	                                   @Param("maxPrice") double maxPrice, @Param("newProduct") Integer newProduct,
	                                   @Param("categoryName") String categoryName, @Param("offset") short offset, @Param("limit") short limit);
}

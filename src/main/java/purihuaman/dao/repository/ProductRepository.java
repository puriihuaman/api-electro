package purihuaman.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import purihuaman.entity.ProductEntity;

import java.util.List;

@Repository
public interface ProductRepository
	extends JpaRepository<ProductEntity, String>, JpaSpecificationExecutor<ProductEntity> {
	@Query(value = "CALL search_product_by_name(:productName)", nativeQuery = true)
	List<ProductEntity> searchProductByName(@Param("productName") String productName);
}

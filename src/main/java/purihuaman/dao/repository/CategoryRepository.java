package purihuaman.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import purihuaman.model.CategoryModel;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryModel, UUID> {
}

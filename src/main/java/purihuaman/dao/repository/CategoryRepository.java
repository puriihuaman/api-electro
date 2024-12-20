package purihuaman.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import purihuaman.model.CategoryModel;

public interface CategoryRepository extends JpaRepository<CategoryModel, String> {}

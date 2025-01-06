package purihuaman.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purihuaman.model.CategoryModel;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, String> {}

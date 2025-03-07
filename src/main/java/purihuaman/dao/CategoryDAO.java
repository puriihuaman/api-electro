package purihuaman.dao;

import java.util.List;
import java.util.Optional;

import purihuaman.entity.CategoryEntity;

public interface CategoryDAO {
	List<CategoryEntity> findAllCategories();

	Optional<CategoryEntity> findCategoryById(String categoryId);

	CategoryEntity createCategory(CategoryEntity categoryEntity);

	CategoryEntity updateCategory(CategoryEntity categoryEntity);

	void deleteCategory(CategoryEntity categoryEntity);
}

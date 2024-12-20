package purihuaman.dao;

import purihuaman.dto.CategoryDTO;

import java.util.List;

public interface CategoryDAO {
	List<CategoryDTO> getAllCategories();

	CategoryDTO getCategoryById(String categoryId);

	CategoryDTO addCategory(CategoryDTO category);

	CategoryDTO updateCategory(String categoryId, CategoryDTO category);

	Integer deleteCategory(String categoryId);
}

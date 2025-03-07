package purihuaman.service;

import purihuaman.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
	List<CategoryDTO> findAllCategories();

	CategoryDTO findCategoryById(String categoryId);

	CategoryDTO createCategory(CategoryDTO category);

	CategoryDTO updateCategory(String categoryId, CategoryDTO category);

	void deleteCategory(String categoryId);
}

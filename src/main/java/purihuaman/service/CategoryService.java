package purihuaman.service;

import purihuaman.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
  List<CategoryDTO> getAllCategories();

	CategoryDTO getCategoryById(String categoryId);

	CategoryDTO addCategory(CategoryDTO category);

	CategoryDTO updateCategory(String categoryId, CategoryDTO category);

	Integer deleteCategory(String categoryId);
}

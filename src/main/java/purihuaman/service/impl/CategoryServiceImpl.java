package purihuaman.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import purihuaman.dao.CategoryDAO;
import purihuaman.dto.CategoryDTO;
import purihuaman.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
  @Autowired
  private CategoryDAO categoryDAO;

  @Override
  public List<CategoryDTO> getAllCategories() {
    return categoryDAO.getAllCategories();
  }

	@Override
	public CategoryDTO getCategoryById(String categoryId) {
		return categoryDAO.getCategoryById(categoryId);
	}

	@Override
	public CategoryDTO addCategory(CategoryDTO category) {
		return categoryDAO.addCategory(category);
	}

	@Override
	public CategoryDTO updateCategory(String categoryId, CategoryDTO category) {
		return categoryDAO.updateCategory(categoryId, category);
	}

	@Override
	public Integer deleteCategory(String categoryId) {
		return categoryDAO.deleteCategory(categoryId);
	}
}

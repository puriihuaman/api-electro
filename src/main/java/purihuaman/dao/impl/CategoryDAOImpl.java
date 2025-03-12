package purihuaman.dao.impl;

import jakarta.validation.Valid;
import org.springframework.stereotype.Repository;
import purihuaman.dao.CategoryDAO;
import purihuaman.dao.repository.CategoryRepository;
import purihuaman.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryDAOImpl implements CategoryDAO {
	private final CategoryRepository categoryRepository;

	public CategoryDAOImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<CategoryEntity> findAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Optional<CategoryEntity> findCategoryById(String categoryId) {
		return categoryRepository.findById(categoryId);
	}

	@Override
	public CategoryEntity createCategory(@Valid CategoryEntity categoryEntity) {
		return categoryRepository.save(categoryEntity);
	}

	@Override
	public CategoryEntity updateCategory(@Valid CategoryEntity categoryEntity) {
		return categoryRepository.save(categoryEntity);
	}

	@Override
	public void deleteCategory(CategoryEntity categoryEntity) {
		categoryRepository.delete(categoryEntity);
	}
}

package purihuaman.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import purihuaman.dao.CategoryDAO;
import purihuaman.dao.repository.CategoryRepository;
import purihuaman.dto.CategoryDTO;
import purihuaman.mapper.CategoryMapper;
import purihuaman.model.CategoryModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoryDAOImpl implements CategoryDAO {
	// private CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);
	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<CategoryDTO> getAllCategories() {
		return categoryMapper.toCategoryDTOList(categoryRepository.findAll());
	}

	@Override
	public CategoryDTO getCategoryById(String categoryId) {
		Optional<CategoryModel> category = categoryRepository.findById(categoryId);

		return category.map(categoryModel -> categoryMapper.toCategoryDTO(categoryModel)).orElse(null);
	}

	@Override
	public CategoryDTO addCategory(CategoryDTO category) {
		CategoryModel categoryModel = categoryMapper.toCategoryModel(category);

		categoryModel.setCategoryId(UUID.randomUUID().toString());
		CategoryModel savedCategory = categoryRepository.save(categoryModel);

		return categoryMapper.toCategoryDTO(savedCategory);
	}

	@Override
	public CategoryDTO updateCategory(String categoryId, CategoryDTO category) {
		CategoryDTO categoryExisting = getCategoryById(categoryId);

		if (categoryExisting != null) {
			CategoryModel categoryModel = categoryMapper.toCategoryModel(category);

			categoryExisting.setCategoryName(categoryModel.getCategoryName());

			CategoryModel savedCategory = categoryRepository.save(categoryMapper.toCategoryModel(categoryExisting));
			return categoryMapper.toCategoryDTO(savedCategory);
		}

		return null;
	}

	@Override
	public Integer deleteCategory(String categoryId) {
		CategoryDTO category = getCategoryById(categoryId);
		if (category != null) {
			CategoryModel categoryModel = categoryMapper.toCategoryModel(category);
			categoryRepository.delete(categoryModel);
			return 1;
		}

		return 0;
	}
}

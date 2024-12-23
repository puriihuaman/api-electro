package purihuaman.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import purihuaman.dao.CategoryDAO;
import purihuaman.dao.repository.CategoryRepository;
import purihuaman.dto.CategoryDTO;
import purihuaman.exception.ApiRequestException;
import purihuaman.mapper.CategoryMapper;
import purihuaman.model.CategoryModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoryDAOImpl implements CategoryDAO {
	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<CategoryDTO> getAllCategories() {
		try {
			return categoryMapper.toCategoryDTOList(categoryRepository.findAll());
		} catch (DataAccessException e) {
			throw new ApiRequestException(
				true,
				"Database error",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public CategoryDTO getCategoryById(String categoryId) {
		Optional<CategoryModel> category = categoryRepository.findById(categoryId);
		try {
			if (category.isEmpty()) {
				throw new ApiRequestException(
					true,
					"Category not found",
					String.format("Category with id '%s' does not exist", categoryId),
					HttpStatus.NOT_FOUND
				);
			}

			return categoryMapper.toCategoryDTO(category.get());
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"An error occurred",
				"An error occurred internally. Please try again.",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public CategoryDTO addCategory(CategoryDTO category) {
		try {
			CategoryModel categoryModel = categoryMapper.toCategoryModel(category);

			categoryModel.setCategoryId(UUID.randomUUID().toString());
			CategoryModel savedCategory = categoryRepository.save(categoryModel);

			return categoryMapper.toCategoryDTO(savedCategory);
		} catch (DataIntegrityViolationException ex) {
			throw new ApiRequestException(true, "Duplicate key", "The record name is already in use", HttpStatus.CONFLICT);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"Database error",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public CategoryDTO updateCategory(String categoryId, CategoryDTO category) {
		CategoryDTO categoryExisting = getCategoryById(categoryId);

		try {
			CategoryModel categoryModel = categoryMapper.toCategoryModel(category);

			categoryExisting.setCategoryName(categoryModel.getCategoryName());

			CategoryModel savedCategory = categoryRepository.save(categoryMapper.toCategoryModel(categoryExisting));
			return categoryMapper.toCategoryDTO(savedCategory);
		} catch (DataIntegrityViolationException ex) {
			throw new ApiRequestException(true, "Duplicate key", "The record name is already in use", HttpStatus.CONFLICT);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"Database error",
				"An error occurred while accessing the database",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public Integer deleteCategory(String categoryId) {
		try {
			CategoryDTO category = getCategoryById(categoryId);

			if (category == null) {
				throw new ApiRequestException(
					true,
					"Category not found",
					String.format("Category with id '%s' does not exist", categoryId),
					HttpStatus.NOT_FOUND
				);
			}

			CategoryModel categoryModel = categoryMapper.toCategoryModel(category);
			categoryRepository.delete(categoryModel);
			return 1;
		} catch (DataIntegrityViolationException ex) {
			throw new ApiRequestException(
				true,
				"Cannot delete record",
				"The record is linked to the other record(s) and cannot be deleted",
				HttpStatus.CONFLICT
			);
		} catch (DataAccessException ex) {
			throw new ApiRequestException(
				true,
				"Delete error",
				"Database error occurred while deleting the category",
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}
}

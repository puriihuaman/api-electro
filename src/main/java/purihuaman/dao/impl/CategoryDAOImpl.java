package purihuaman.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.validation.Valid;
import purihuaman.dao.CategoryDAO;
import purihuaman.dao.repository.CategoryRepository;
import purihuaman.entity.CategoryEntity;

@Repository
public class CategoryDAOImpl implements CategoryDAO {
//	private final CategoryMapper categoryMapper;
	private final CategoryRepository categoryRepository;

	public CategoryDAOImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<CategoryEntity> findAllCategories() {
		return categoryRepository.findAll();
//		try {
//			return categoryMapper.toCategoryDTOList(categoryRepository.findAll());
//		} catch (DataAccessException ex) {
//			throw new APIRequestException(APIError.DATABASE_ERROR);
//		} catch (Exception ex) {
//			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
//		}
	}

	@Override
	public Optional<CategoryEntity> findCategoryById(String categoryId) {
		return categoryRepository.findById(categoryId);
//		try {
//			Optional<CategoryModel> optionalCategory = categoryRepository.findById(categoryId);
//
//			if (optionalCategory.isEmpty()) {
//				throw new APIRequestException(APIError.RECORD_NOT_FOUND);
//			}
//
//			return categoryMapper.toCategoryDTO(optionalCategory.get());
//		} catch (APIRequestException ex) {
//			throw ex;
//		} catch (DataAccessException ex) {
//			throw new APIRequestException(APIError.DATABASE_ERROR);
//		} catch (Exception ex) {
//			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
//		}
	}

	@Override
	public CategoryEntity createCategory(@Valid CategoryEntity categoryEntity) {
		return categoryRepository.save(categoryEntity);
//		try {
//			CategoryModel categoryModel = categoryMapper.toCategoryModel(category);
//
//			categoryModel.setCategoryId(UUID.randomUUID().toString());
//			CategoryModel savedCategory = categoryRepository.save(categoryModel);
//
//			return categoryMapper.toCategoryDTO(savedCategory);
//		} catch (DataIntegrityViolationException ex) {
//			Throwable cause = ex.getCause();
//			Throwable rootCause = ex.getRootCause();
//			if (cause instanceof ConstraintViolationException || rootCause instanceof ConstraintViolationException)
//				throw new APIRequestException(APIError.UNIQUE_CONSTRAINT_VIOLATION);
//			else
//				throw new APIRequestException(APIError.RESOURCE_ASSOCIATED_EXCEPTION);
//		} catch (DataAccessException ex) {
//			throw new APIRequestException(APIError.DATABASE_ERROR);
//		} catch (Exception ex) {
//			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
//		}
	}

	@Override
	public CategoryEntity updateCategory(@Valid CategoryEntity categoryEntity) {
		return categoryRepository.save(categoryEntity);

//		try {
//			CategoryDTO categoryExisting = getCategoryById(categoryId);
//
//			CategoryModel categoryModel = categoryMapper.toCategoryModel(category);
//
//			categoryExisting.setCategoryName(categoryModel.getCategoryName());
//			CategoryModel savedCategory = categoryRepository.save(categoryMapper.toCategoryModel(categoryExisting));
//			return categoryMapper.toCategoryDTO(savedCategory);
//		} catch (APIRequestException ex) {
//			throw ex;
//		} catch (DataIntegrityViolationException ex) {
//			Throwable cause = ex.getCause();
//			Throwable rootCause = ex.getRootCause();
//			if (cause instanceof ConstraintViolationException || rootCause instanceof ConstraintViolationException)
//				throw new APIRequestException(APIError.UNIQUE_CONSTRAINT_VIOLATION);
//			else
//				throw new APIRequestException(APIError.RESOURCE_ASSOCIATED_EXCEPTION);
//		} catch (DataAccessException ex) {
//			throw new APIRequestException(APIError.DATABASE_ERROR);
//		} catch (Exception ex) {
//			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
//		}
	}

	@Override
	public void deleteCategory(CategoryEntity categoryEntity) {
		categoryRepository.delete(categoryEntity);
//		try {
//			CategoryDTO category = getCategoryById(categoryId);
//
//			CategoryModel categoryModel = categoryMapper.toCategoryModel(category);
//			categoryRepository.delete(categoryModel);
//			return 1;
//		} catch (APIRequestException ex) {
//			throw ex;
//		} catch (DataIntegrityViolationException ex) {
//			Throwable cause = ex.getCause();
//			Throwable rootCause = ex.getRootCause();
//			if (cause instanceof ConstraintViolationException || rootCause instanceof ConstraintViolationException)
//				throw new APIRequestException(APIError.UNIQUE_CONSTRAINT_VIOLATION);
//			else
//				throw new APIRequestException(APIError.RESOURCE_ASSOCIATED_EXCEPTION);
//		} catch (DataAccessException ex) {
//			throw new APIRequestException(APIError.DATABASE_ERROR);
//		} catch (Exception ex) {
//			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
//		}
	}
}

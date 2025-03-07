package purihuaman.service.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import purihuaman.dao.CategoryDAO;
import purihuaman.dto.CategoryDTO;
import purihuaman.entity.CategoryEntity;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;
import purihuaman.mapper.CategoryMapper;
import purihuaman.service.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
	private final CategoryDAO categoryDao;
	private final CategoryMapper categoryMapper;

	public CategoryServiceImpl(CategoryDAO categoryDao, CategoryMapper categoryMapper) {
		this.categoryDao = categoryDao;
		this.categoryMapper = categoryMapper;
	}

	@Override
	public List<CategoryDTO> findAllCategories() {
		try {
			return categoryMapper.toCategoryDTOList(categoryDao.findAllCategories());
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public CategoryDTO findCategoryById(String categoryId) {
		try {
			Optional<CategoryEntity> optionalCategory = categoryDao.findCategoryById(categoryId);
			if (optionalCategory.isEmpty()) {
				APIError.RECORD_NOT_FOUND.setMessage("Category not found");
				throw new APIRequestException(APIError.RECORD_NOT_FOUND);
			}

			return categoryMapper.toCategoryDTO(optionalCategory.get());
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public CategoryDTO createCategory(CategoryDTO category) {
		try {
			category.setId(UUID.randomUUID().toString());
			CategoryEntity savedCategoryEntity = categoryMapper.toCategoryModel(category);

			return categoryMapper.toCategoryDTO(categoryDao.createCategory(savedCategoryEntity));
		} catch (DataIntegrityViolationException ex) {
			Throwable cause = ex.getCause();
			Throwable rootCause = ex.getRootCause();
			if (cause instanceof ConstraintViolationException || rootCause instanceof ConstraintViolationException)
				throw new APIRequestException(APIError.UNIQUE_CONSTRAINT_VIOLATION);
			else throw new APIRequestException(APIError.RESOURCE_ASSOCIATED_EXCEPTION);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public CategoryDTO updateCategory(String categoryId, CategoryDTO category) {
		try {
			CategoryEntity categoryEntityExisting = categoryMapper.toCategoryModel(this.findCategoryById(categoryId));

			categoryEntityExisting.setCategoryName(category.getCategoryName());

			return categoryMapper.toCategoryDTO(categoryDao.updateCategory(categoryEntityExisting));
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataIntegrityViolationException ex) {
			Throwable cause = ex.getCause();
			Throwable rootCause = ex.getRootCause();
			if (cause instanceof ConstraintViolationException || rootCause instanceof ConstraintViolationException)
				throw new APIRequestException(APIError.UNIQUE_CONSTRAINT_VIOLATION);
			else throw new APIRequestException(APIError.RESOURCE_ASSOCIATED_EXCEPTION);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void deleteCategory(String categoryId) {
		try {
			CategoryEntity categoryEntityExisting = categoryMapper.toCategoryModel(this.findCategoryById(categoryId));

			categoryDao.deleteCategory(categoryEntityExisting);
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataIntegrityViolationException ex) {
			Throwable cause = ex.getCause();
			Throwable rootCause = ex.getRootCause();
			if (cause instanceof ConstraintViolationException || rootCause instanceof ConstraintViolationException)
				throw new APIRequestException(APIError.UNIQUE_CONSTRAINT_VIOLATION);
			else throw new APIRequestException(APIError.RESOURCE_ASSOCIATED_EXCEPTION);
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}
}

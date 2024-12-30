package purihuaman.dao.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import purihuaman.dao.CategoryDAO;
import purihuaman.dao.repository.CategoryRepository;
import purihuaman.dto.CategoryDTO;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;
import purihuaman.mapper.CategoryMapper;
import purihuaman.model.CategoryModel;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CategoryDAOImpl implements CategoryDAO {
	private final CategoryMapper categoryMapper;
	private final CategoryRepository categoryRepository;

	@Override
	public List<CategoryDTO> getAllCategories() {
		try {
			return categoryMapper.toCategoryDTOList(categoryRepository.findAll());
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public CategoryDTO getCategoryById(final String categoryId) {
		try {
			CategoryModel
				category =
				categoryRepository.findById(categoryId).orElseThrow(() -> new APIRequestException(APIError.RECORD_NOT_FOUND));

			return categoryMapper.toCategoryDTO(category);
		} catch (APIRequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new APIRequestException(APIError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public CategoryDTO addCategory(final @Valid CategoryDTO category) {
		try {
			CategoryModel categoryModel = categoryMapper.toCategoryModel(category);

			categoryModel.setCategoryId(UUID.randomUUID().toString());
			CategoryModel savedCategory = categoryRepository.save(categoryModel);

			return categoryMapper.toCategoryDTO(savedCategory);
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
	public CategoryDTO updateCategory(final String categoryId, final @Valid CategoryDTO category) {

		try {
			CategoryDTO categoryExisting = getCategoryById(categoryId);

			CategoryModel categoryModel = categoryMapper.toCategoryModel(category);

			categoryExisting.setCategoryName(categoryModel.getCategoryName());
			CategoryModel savedCategory = categoryRepository.save(categoryMapper.toCategoryModel(categoryExisting));
			return categoryMapper.toCategoryDTO(savedCategory);
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
	public Integer deleteCategory(final String categoryId) {
		try {
			CategoryDTO category = getCategoryById(categoryId);

			CategoryModel categoryModel = categoryMapper.toCategoryModel(category);
			categoryRepository.delete(categoryModel);
			return 1;
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

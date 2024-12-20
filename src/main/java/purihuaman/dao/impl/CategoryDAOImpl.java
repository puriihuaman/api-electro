package purihuaman.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import purihuaman.dao.CategoryDAO;
import purihuaman.dao.repository.CategoryRepository;
import purihuaman.dto.CategoryDTO;
import purihuaman.mapper.CategoryMapper;

import java.util.List;

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
}

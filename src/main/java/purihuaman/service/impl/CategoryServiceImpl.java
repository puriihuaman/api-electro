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
}

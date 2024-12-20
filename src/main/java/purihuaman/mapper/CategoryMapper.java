package purihuaman.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import purihuaman.dto.CategoryDTO;
import purihuaman.model.CategoryModel;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  /**
   * Convert a DTO model to a MODEL
   *
   * @param categoryDTO CategoryDTO
   * @return MODEL
   */
  CategoryModel toCategoryModel(CategoryDTO categoryDTO);

  /**
   * Convert a MODEL to a DTO model
   *
   * @param categoryModel Category Model
   * @return CategoryDTO
   */
  CategoryDTO toCategoryDTO(CategoryModel categoryModel);

  /**
   * Convert a dto model list to a model list
   *
   * @param categoryList List<CategoryDTO>
   * @return List<CategoryModel>
   */
  List<CategoryModel> toCategoryModelList(List<CategoryDTO> categoryList);

  /**
   * Convert a MODEL list to a DTO model list
   *
   * @param categoryList List<CategoryModel>
   * @return List<CategoryDTO>
   */
  List<CategoryDTO> toCategoryDTOList(List<CategoryModel> categoryList);
}

package purihuaman.mapper;

import org.mapstruct.Mapper;
import purihuaman.dto.CategoryDTO;
import purihuaman.model.CategoryModel;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	CategoryModel toCategoryModel(CategoryDTO categoryDTO);

	CategoryDTO toCategoryDTO(CategoryModel categoryModel);

	List<CategoryModel> toCategoryModelList(List<CategoryDTO> categoryList);

	List<CategoryDTO> toCategoryDTOList(List<CategoryModel> categoryList);
}

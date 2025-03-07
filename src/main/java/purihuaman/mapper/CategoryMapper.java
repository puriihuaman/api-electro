package purihuaman.mapper;

import org.mapstruct.Mapper;
import purihuaman.dto.CategoryDTO;
import purihuaman.entity.CategoryEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	CategoryEntity toCategoryModel(CategoryDTO categoryDTO);

	CategoryDTO toCategoryDTO(CategoryEntity categoryEntityModel);

	List<CategoryEntity> toCategoryModelList(List<CategoryDTO> categoryList);

	List<CategoryDTO> toCategoryDTOList(List<CategoryEntity> categoryEntityList);
}

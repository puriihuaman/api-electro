package purihuaman.mapper;

import org.mapstruct.Mapper;
import purihuaman.dto.ProductDTO;
import purihuaman.entity.ProductEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	ProductEntity toProductModel(ProductDTO product);

	ProductDTO toProductDTO(ProductEntity productEntity);

	List<ProductEntity> toProductModelList(List<ProductDTO> products);

	List<ProductDTO> toProductDTOList(List<ProductEntity> productEntities);
}

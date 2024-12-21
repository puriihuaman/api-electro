package purihuaman.mapper;

import org.mapstruct.Mapper;
import purihuaman.dto.ProductDTO;
import purihuaman.model.ProductModel;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	ProductModel toProductModel(ProductDTO product);

	ProductDTO toProductDTO(ProductModel product);

	List<ProductModel> toProductModelList(List<ProductDTO> products);

	List<ProductDTO> toProductDTOList(List<ProductModel> products);
}

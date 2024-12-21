package purihuaman.dto;

import lombok.Getter;
import lombok.Setter;
import purihuaman.model.CategoryModel;

@Getter
@Setter
public class ProductDTO {
	private String productId;
	private String productName;
	private Double price;
	private Double oldPrice;
	private Integer newProduct;
	private String photo;
	private CategoryModel category;
}

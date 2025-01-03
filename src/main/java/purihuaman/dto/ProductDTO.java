package purihuaman.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import purihuaman.model.CategoryModel;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO extends RepresentationModel<ProductDTO> {
	private String productId;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Size(min = 6, max = 40, message = "${product.size}")
	@Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]*$", message = "${product.pattern}")
	private String productName;

	@NotNull(message = "${field.null}")
	@PositiveOrZero(message = "${product.price}")
	@Min(value = 0, message = "${product.min}")
	private Double price;

	@NotNull(message = "${field.null}")
	@PositiveOrZero(message = "${product.price}")
	@Min(value = 0, message = "${product.min}")
	private Double oldPrice;

	@NotNull(message = "${field.null}")
	@Min(value = 0, message = "${product.min}")
	private Integer newProduct;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	private String photo;

	@Valid
	private CategoryModel category;
}

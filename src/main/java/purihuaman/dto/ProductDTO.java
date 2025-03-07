package purihuaman.dto;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import purihuaman.entity.CategoryEntity;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO extends RepresentationModel<ProductDTO> {
	private String id;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Size(min = 6, max = 40, message = "${product.size}")
	@Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]*$", message = "${product.pattern}")
	private String productName;

	@NotNull(message = "${field.null}")
	@PositiveOrZero(message = "${product.price}")
	@Min(value = 0, message = "${product.min}")
	private double price;

	@NotNull(message = "${field.null}")
	@PositiveOrZero(message = "${product.price}")
	@Min(value = 0, message = "${product.min}")
	private double oldPrice;

	@NotNull(message = "${field.null}")
	@Min(value = 0, message = "${product.min}")
	private int newProduct;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	private String photo;

	@Valid
	@ManyToOne
	private CategoryEntity category;
}

package purihuaman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import purihuaman.entity.CategoryEntity;

@Schema(description = "Represents a product in the system")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO extends RepresentationModel<ProductDTO> {
	@Schema(
		description = "Unique product ID",
		example = "02e6ffb2-4c00-48e3-a673-eaa614cb1c09",
		type = "String",
		accessMode = Schema.AccessMode.READ_ONLY,
		hidden = true
	)
	private String id;

	@Schema(
		description = "Name of the product",
		example = "Laptop Samsung",
		type = "String",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Size(min = 6, max = 40, message = "${product.size}")
	@Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]*$", message = "${product.pattern}")
	private String productName;

	@Schema(
		description = "Current product price",
		example = "5000.00",
		type = "Decimal",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "${field.null}")
	@PositiveOrZero(message = "${product.price}")
	@Min(value = 0, message = "${product.min}")
	private double price;

	@Schema(
		description = "Old product price",
		example = "5500.00",
		type = "Decimal",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "${field.null}")
	@PositiveOrZero(message = "${product.price}")
	@Min(value = 0, message = "${product.min}")
	private double oldPrice;

	@Schema(
		description = "New product", example = "1", type = "Integer", requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@NotNull(message = "${field.null}")
	@Min(value = 0, message = "${product.min}")
	private int newProduct;

	@Schema(
		description = "Product image",
		example = "/image/laptop.jpg",
		type = "Integer",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	private String photo;

	@Schema(
		description = "Category assigned to the product",
		example = "Laptop",
		type = "String",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@Valid
	@ManyToOne
	private CategoryEntity category;
}

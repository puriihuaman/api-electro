package purihuaman.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Schema(description = "Represents a product in the system")
@Entity(name = "Product")
@Table(name = "products")
@Data
public class ProductEntity {
	@Schema(
		description = "Unique product ID",
		example = "02e6ffb2-4c00-48e3-a673-eaa614cb1c09",
		type = "String",
		accessMode = Schema.AccessMode.READ_ONLY,
		hidden = true
	)
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@Schema(
		description = "Name of the product",
		example = "Laptop Samsung",
		type = "String",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 6, max = 40, message = "{product.size}")
	@Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]*$", message = "{product.pattern}")
	@Column(name = "product_name", length = 60)
	private String productName;

	@Schema(
		description = "Current product price",
		example = "5000.00",
		type = "Decimal",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "{field.null}")
	@PositiveOrZero(message = "{product.price}")
	@Min(value = 0, message = "{product.min}")
	@Column(name = "price", precision = 2)
	private double price;

	@Schema(
		description = "Old product price",
		example = "5500.00",
		type = "Decimal",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "{field.null}")
	@PositiveOrZero(message = "{product.price}")
	@Min(value = 0, message = "{product.min}")
	@Column(name = "old_price", precision = 2)
	private double oldPrice;

	@Schema(
		description = "New product", example = "1", type = "Integer", requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@NotNull(message = "{field.null}")
	@Min(value = 0, message = "{product.min}")
	@Column(name = "new_product")
	private int newProduct;

	@Schema(
		description = "Product image",
		example = "/image/laptop.jpg",
		type = "Integer",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Column(name = "photo", length = 50)
	private String photo;

	@Schema(
		description = "Category assigned to the product",
		example = "Laptop",
		type = "String",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@Valid
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private CategoryEntity category;
}

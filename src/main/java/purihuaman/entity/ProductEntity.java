package purihuaman.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity(name = "Product")
@Table(name = "products")
@Data
public class ProductEntity {
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 6, max = 40, message = "{product.size}")
	@Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]*$", message = "{product.pattern}")
	@Column(name = "product_name", length = 60)
	private String productName;

	@NotNull(message = "{field.null}")
	@PositiveOrZero(message = "{product.price}")
	@Min(value = 0, message = "{product.min}")
	@Column(name = "price", precision = 2)
	private double price;

	@NotNull(message = "{field.null}")
	@PositiveOrZero(message = "{product.price}")
	@Min(value = 0, message = "{product.min}")
	@Column(name = "old_price", precision = 2)
	private double oldPrice;

	@NotNull(message = "{field.null}")
	@Min(value = 0, message = "{product.min}")
	@Column(name = "new_product")
	private int newProduct;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Column(name = "photo", length = 50)
	private String photo;

	@Valid
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private CategoryEntity category;
}

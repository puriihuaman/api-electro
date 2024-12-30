package purihuaman.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class ProductModel {
	@Id
	@Column(name = "product_id", unique = true, nullable = false, length = 40)
	private String productId;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Size(min = 6, max = 40, message = "${product.size}")
	@Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]*$", message = "${product.pattern}")
	@Column(name = "product_name", length = 60)
	private String productName;

	@NotNull(message = "${field.null}")
	@PositiveOrZero(message = "${product.price}")
	@Min(value = 0, message = "${product.min}")
	@Column(name = "price", precision = 2)
	private Double price;

	@NotNull(message = "${field.null}")
	@PositiveOrZero(message = "${product.price}")
	@Min(value = 0, message = "${product.min}")
	@Column(name = "old_price", precision = 2)
	private Double oldPrice;

	@NotNull(message = "${field.null}")
	@Min(value = 0, message = "${product.min}")
	@Column(name = "new_product")
	private Integer newProduct;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Column(name = "photo", length = 50)
	private String photo;

	@Valid
	@OneToOne
	@JoinColumn(name = "category_id", referencedColumnName = "category_id")
	private CategoryModel category;
}

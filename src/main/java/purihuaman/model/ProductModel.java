package purihuaman.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class ProductModel {
  @Id
  @Column(name = "product_id", unique = true, nullable = false, length = 40)
  private String productId;

  @NotNull(message = "The field is null")
  @NotEmpty(message = "The field is empty")
  @Size(min = 6, max = 40, message = "The field must contain at least 6 characters")
  @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]*$", message = "The field can only contain letters, numbers and spaces")
  @Column(name = "product_name", length = 60)
  private String productName;

  @NotNull(message = "The field is null")
  @NotEmpty(message = "The field is empty")
  @Column(name = "price", precision = 2)
  private Double price;

  @NotNull(message = "The field is null")
  @NotEmpty(message = "The field is empty")
  @Column(name = "old_price", precision = 2)
  private Double oldPrice;

  @NotNull(message = "The field is null")
  @NotEmpty(message = "The field is empty")
  @Min(value = 0, message = "The field accepts 0 as the minimum number")
  @Column(name = "new_product")
  private Integer newProduct;

  @NotNull(message = "The field is null")
  @NotEmpty(message = "The field is empty")
  @Column(name = "photo", length = 50)
  private String photo;

  @OneToOne
  @JoinColumn(name = "category_id", referencedColumnName = "category_id")
  private CategoryModel category;
}

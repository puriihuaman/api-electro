package purihuaman.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@Table(name = "categories")
public class CategoryModel {
  @Id
  @Column(name = "category_id", length = 40)
  private String categoryId;

  @NotNull(message = "The field is null")
  @NotEmpty(message = "The field is empty")
  @Pattern(regexp = "^[a-zA-Z-áéíóúÁÉÍÓÚñÑ ]*$", message = "The field can only contain letters and spaces")
  @Column(name = "category_name", nullable = false, length = 30)
  private String categoryName;
}

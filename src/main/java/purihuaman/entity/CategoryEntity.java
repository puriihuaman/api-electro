package purihuaman.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity(name = "Category")
@Table(name = "categories")
@Data
public class CategoryEntity {
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Pattern(regexp = "^[a-zA-Z-áéíóúÁÉÍÓÚñÑ ]*$", message = "{field.category.name}")
	@Column(name = "category_name", nullable = false, length = 30, unique = true)
	private String categoryName;
}

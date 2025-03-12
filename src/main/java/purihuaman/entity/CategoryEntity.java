package purihuaman.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(description = "Represents a category in the system")
@Entity(name = "Category")
@Table(name = "categories")
@Data
public class CategoryEntity {
	@Schema(
		description = "Unique category ID",
		example = "02e6ffb2-4c00-48e3-a673-eaa614cb1c09",
		type = "String",
		accessMode = Schema.AccessMode.READ_ONLY,
		hidden = true
	)
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@Schema(
		description = "Name of the category",
		example = "Laptop",
		type = "String",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Pattern(regexp = "^[a-zA-Z-áéíóúÁÉÍÓÚñÑ ]*$", message = "{field.category.name}")
	@Column(name = "category_name", nullable = false, length = 30, unique = true)
	private String categoryName;
}

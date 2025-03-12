package purihuaman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Schema(description = "Represents a category in the system")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO extends RepresentationModel<CategoryDTO> {
	@Schema(
		description = "Unique category ID",
		example = "02e6ffb2-4c00-48e3-a673-eaa614cb1c09",
		type = "String",
		accessMode = Schema.AccessMode.READ_ONLY,
		hidden = true
	)
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
	private String categoryName;
}

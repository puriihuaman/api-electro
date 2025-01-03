package purihuaman.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO extends RepresentationModel<CategoryDTO> {
	private String categoryId;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Pattern(regexp = "^[a-zA-Z-áéíóúÁÉÍÓÚñÑ ]*$", message = "${field.category.name}")
	private String categoryName;
}

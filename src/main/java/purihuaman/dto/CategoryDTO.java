package purihuaman.dto;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO extends RepresentationModel<CategoryDTO> {
	private String id;

	@NotNull(message = "${field.null}")
	@NotEmpty(message = "${field.empty}")
	@Pattern(regexp = "^[a-zA-Z-áéíóúÁÉÍÓÚñÑ ]*$", message = "${field.category.name}")
	private String categoryName;
}

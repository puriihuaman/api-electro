package purihuaman.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequestDTO {
	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 2, message = "{user.size}")
	private String username;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	private String password;
}

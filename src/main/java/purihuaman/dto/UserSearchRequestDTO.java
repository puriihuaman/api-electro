package purihuaman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequestDTO {
	@Schema(description = "Filter by first name", example = "juan")
	private String first_name;

	@Schema(description = "Filter by last name", example = "Perez")
	private String last_name;

	@Schema(description = "Filter by email", example = "juan.perez@example.com")
	private String email;

	@Schema(description = "Filter by username", example = "perez")
	private String username;

	@Schema(description = "Indicates from which position (page) the query should start", example = "2")
	private int offset;

	@Schema(description = "Number of items per page", example = "10")
	private int limit;
}

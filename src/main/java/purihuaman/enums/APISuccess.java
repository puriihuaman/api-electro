package purihuaman.enums;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum APISuccess {
	RESOURCE_RETRIEVED("Resource Retrieved", HttpStatus.OK),
	RESOURCE_CREATED("Resource Created", HttpStatus.CREATED),
	RESOURCE_UPDATED("Resource Updated", HttpStatus.OK),
	RESOURCE_REMOVED("Resource Removed", HttpStatus.NO_CONTENT);

	private String message;
	private final HttpStatus status;

	@JsonSetter
	public void setMessage(String message) {
		this.message = message;
	}
}

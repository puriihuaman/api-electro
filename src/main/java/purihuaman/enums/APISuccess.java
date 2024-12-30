package purihuaman.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum APISuccess {
	RESOURCE_FETCHED_SUCCESSFULLY("resource(s) obtained successfully", HttpStatus.OK),
	RESOURCE_CREATED_SUCCESSFULLY("resource created successfully", HttpStatus.CREATED),
	RESOURCE_UPDATED_SUCCESSFULLY("resource updated successfully", HttpStatus.OK),
	RESOURCE_DELETED_SUCCESSFULLY("resource deleted successfully", HttpStatus.NO_CONTENT);

	private final String message;
	private final HttpStatus status;
}

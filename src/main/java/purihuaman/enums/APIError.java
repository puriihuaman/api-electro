package purihuaman.enums;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum APIError {
	INVALID_REQUEST_DATA(
		HttpStatus.BAD_REQUEST,
	                     "Invalid data",
	                     "Request data contains invalid values or incorrect format."
	),
	INVALID_CREDENTIALS(
		HttpStatus.BAD_REQUEST,
		"Invalid Credentials",
		"Incorrect username or password. Please try again with the correct credentials."
	),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request", "The request is invalid."),
	BAD_FORMAT(HttpStatus.BAD_REQUEST, "Invalid format", "The message does not have a valid format."),
	RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "Record not found", "The requested resource does not exist."),
	ENDPOINT_NOT_FOUND(HttpStatus.NOT_FOUND, "Endpoint Not Found", "Requested API endpoint does not exist."),
	UNAUTHORIZED_ACCESS(
		HttpStatus.UNAUTHORIZED,
		"Unauthorized Access",
		"Authentication is required or has failed or has not yet been provided"
	),
	FORBIDDEN_ACTION(
		HttpStatus.FORBIDDEN,
	                 "Forbidden Action",
	                 "You do not have the necessary permissions to perform this action."
	),
	INTERNAL_SERVER_ERROR(
		HttpStatus.INTERNAL_SERVER_ERROR,
		"Server Error",
		"Unexpected internal server error occurred. Please try again later."
	),
	METHOD_NOT_ALLOWED(
		HttpStatus.METHOD_NOT_ALLOWED,
		"Method Not Allowed",
		"HTTP method not supported for this endpoint."
	),
	UNPROCESSABLE_ENTITY(
		HttpStatus.UNPROCESSABLE_ENTITY,
		"Unprocessable Entity",
		"The request was well-formed but cannot be processed due to semantic errors."
	),
	RESOURCE_CONFLICT(
		HttpStatus.CONFLICT,
		"Resource Conflict",
		"The requested action conflicts with the current state of the resource."
	),
	DUPLICATE_RESOURCE(
		HttpStatus.CONFLICT,
	                   "Duplicate Resource",
	                   "Resource with unique identifier already exists."
	),
	UNIQUE_CONSTRAINT_VIOLATION(
		HttpStatus.CONFLICT,
	                            "Unique Constraint Violation",
	                            "A unique constraint violation occurred."
	),
	RESOURCE_ASSOCIATED_EXCEPTION(
		HttpStatus.CONFLICT,
		"Associated Resource Violation",
		"Resource is associated with other resources and cannot be deleted."
	),
	DATABASE_ERROR(
		HttpStatus.INTERNAL_SERVER_ERROR,
	               "Database Error",
	               "Failed to complete database operation."
	),
	TIMEOUT_ERROR(HttpStatus.REQUEST_TIMEOUT, "Request Timeout", "The request timed out before completion."),
	EXTERNAL_API_ERROR(
		HttpStatus.SERVICE_UNAVAILABLE,
		"External API Error",
		"An error occurred while communicating with an external API."
	);

	private HttpStatus status;
	private String message;
	private String description;

	@JsonSetter
	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	@JsonSetter
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonSetter
	public void setDescription(String description) {
		this.description = description;
	}
}

package purihuaman.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum APIError {
	INVALID_REQUEST_DATA(
		HttpStatus.BAD_REQUEST,
		"Invalid request data",
		"The provided data contains invalid values or formats."
	),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request", "The request is invalid."),
	BAD_FORMAT(HttpStatus.BAD_REQUEST, "Invalid format", "The message does not have a valid format."),
	RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "Record not found", "The requested resource could not be found."),
	ENDPOINT_NOT_FOUND(HttpStatus.NOT_FOUND, "Endpoint Not Found", "The requested endpoint does not exist."),
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
		"Internal Server Error",
		"An unexpected error occurred on the server. Please try again later."
	),
	METHOD_NOT_ALLOWED(
		HttpStatus.METHOD_NOT_ALLOWED,
		"Method Not Allowed",
		"The requested HTTP method is not allowed for this resource."
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
	DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "Duplicate Resource", "A resource with the same identifier already exists."),
	UNIQUE_CONSTRAINT_VIOLATION(
		HttpStatus.CONFLICT,
		"Unique Constraint Violation",
		"A unique constraint violation occurred."
	),
	RESOURCE_ASSOCIATED_EXCEPTION(HttpStatus.CONFLICT, "Associated Resource Violation","Resource is associated with other resources and cannot be deleted."),
	DATABASE_ERROR(
		HttpStatus.INTERNAL_SERVER_ERROR,
		"Database Error",
		"An error occurred while interacting with the database."
	),
	TIMEOUT_ERROR(HttpStatus.REQUEST_TIMEOUT, "Request Timeout", "The request timed out before completion."),
	EXTERNAL_API_ERROR(
		HttpStatus.SERVICE_UNAVAILABLE,
		"External API Error",
		"An error occurred while communicating with an external API."
	);

	private final HttpStatus status;
	private final String message;
	private final String description;
}

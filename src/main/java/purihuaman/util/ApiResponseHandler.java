package purihuaman.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseHandler {

	public static ResponseEntity<ApiResponse> handleApiResponse(String message, Object data, HttpStatus status) {
		ApiResponse apiResponse = ApiResponse.success(message, data, status);
		return new ResponseEntity<>(apiResponse, status);
	}
}

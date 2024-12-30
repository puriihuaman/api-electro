package purihuaman.util;

import org.springframework.http.ResponseEntity;
import purihuaman.enums.APISuccess;

public class APIResponseHandler {
	public static ResponseEntity<APIResponse> handleApiResponse(final APISuccess apiSuccess, Object data) {
		APIResponse apiResponse = new APIResponse(apiSuccess, data);
		return new ResponseEntity<>(apiResponse, apiSuccess.getStatus());
	}
}

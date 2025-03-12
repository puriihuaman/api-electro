package purihuaman.util;

import org.springframework.http.ResponseEntity;
import purihuaman.enums.APISuccess;

public class APIResponseHandler {
	public static ResponseEntity<APIResponseData> handleApiResponse(APISuccess apiSuccess, Object data) {
		APIResponseData apiResponseData = new APIResponseData(apiSuccess, data);
		return new ResponseEntity<>(apiResponseData, apiSuccess.getStatus());
	}
}

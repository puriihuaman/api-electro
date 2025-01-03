package purihuaman.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
	private Boolean hasError;
	private String message;
	private String description;
	private Integer statusCode;
}

package mfasample.model;

import org.springframework.http.HttpStatus;

public class APIErrorException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private HttpStatus errorStatus;
	private APIError apiError;

	public APIErrorException(HttpStatus errorStatus, APIError apiError) {
		super(errorStatus.toString() + " " + apiError.errorCode + " " + apiError.message);
		this.errorStatus = errorStatus;
		this.apiError = apiError;
	}

	public APIError getApiError() {
		return apiError;
	}

	public HttpStatus getErrorStatus() {
		return errorStatus;
	}
}

package mfasample.controller.interceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.fasterxml.jackson.databind.ObjectMapper;

import mfasample.model.APIError;
import mfasample.model.APIErrorException;

/**
 * Controllerで発生したエラーを補足して、クライアントにエラーを返すためのExceptionHandler
 * 
 * @author c9katayama
 *
 */
@ControllerAdvice
public class APIErrorExceptionHandler {

	/**
	 * APIErrorException発生時のハンドリング
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(APIErrorException.class)
	public ModelAndView handleAPIErrorException(final APIErrorException e) {
		return new ModelAndView(new APIErrorView(e));
	}

	protected static class APIErrorView implements View {

		private static final ObjectMapper objectMapper = new ObjectMapper();
		private static String ENCODING = "UTF-8";

		private APIErrorException apiError;

		public APIErrorView(APIErrorException error) {
			this.apiError = error;
		}

		@Override
		public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			HttpStatus statusCode = apiError.getErrorStatus();
			response.setStatus(statusCode.value());
			APIError error = apiError.getApiError();
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("errorCode", error.getErrorCode());
			responseBody.put("errorMessage", error.getMessage());

			final String body = objectMapper.writeValueAsString(responseBody);
			response.setContentType(getContentType());
			response.setContentLength(body.getBytes(ENCODING).length);
			response.setCharacterEncoding(ENCODING);

			try (PrintWriter writer = response.getWriter()) {
				writer.write(body);
			} catch (IllegalStateException e) {
			}
		}

		@Override
		public String getContentType() {
			return MediaType.APPLICATION_JSON_VALUE;
		}
	}
}

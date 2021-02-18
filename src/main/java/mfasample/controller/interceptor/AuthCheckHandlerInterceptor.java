package mfasample.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import mfasample.model.APIError;
import mfasample.model.APIErrorException;
import mfasample.model.User;

/**
 * セッション内に認証済みデータがあるかどうかを確認するInterceptor
 * 
 * <pre>
 * Controllerクラスが呼ばれる前に呼び出されます
 * </pre>
 * 
 * @author c9katayama
 *
 */
@Component
public class AuthCheckHandlerInterceptor implements HandlerInterceptor {

	private static final Log log = LogFactory.getLog(AuthCheckHandlerInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("request path=" + request.getRequestURI() + " method=" + request.getMethod());
		if (request.getMethod().equals("OPTIONS") == false) {
			// セッション内にUserデータがない場合は未認証なのでエラー
			HttpSession session = request.getSession(false);
			if (session == null) {
				throw new APIErrorException(HttpStatus.FORBIDDEN, APIError.AUTHENTICATION_ERROR);
			}
			if (session.getAttribute(User.class.getName()) == null) {
				throw new APIErrorException(HttpStatus.FORBIDDEN, APIError.AUTHENTICATION_ERROR);
			}
		}
		return true;
	}
}

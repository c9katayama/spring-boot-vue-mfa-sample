package mfasample.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mfasample.controller.io.AuthControllerIO.ActivateMFARequest;
import mfasample.controller.io.AuthControllerIO.InitMFAResponse;
import mfasample.controller.io.AuthControllerIO.LoginRequest;
import mfasample.model.APIError;
import mfasample.model.APIErrorException;
import mfasample.model.AuthenticatorKey;
import mfasample.model.TOTPAuthUri;
import mfasample.model.User;
import mfasample.service.AuthService;
import mfasample.service.AuthService.AuthenticationFailException;
import mfasample.service.AuthService.AuthenticatorKeyHasBeenActivatedException;
import mfasample.service.AuthService.AuthenticatorKeyNotFoundException;
import mfasample.service.AuthService.InvalidMFAOTPCodeFormatException;
import mfasample.service.AuthService.MFAOTPCodeRequiredException;

/**
 * 認証実行のコントローラークラス
 * 
 * @author c9katayama
 *
 */
@RestController
public class AuthController {

	private static final Log log = LogFactory.getLog(AuthController.class);

	@Autowired
	private AuthService authService;

	/**
	 * ログイン処理
	 * 
	 * @param loginRequest
	 * @param req
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public void login(@RequestBody LoginRequest loginRequest, HttpServletRequest req) {
		log.info("login " + loginRequest.getUserName());
		try {
			User user = authService.doAuth(loginRequest.getUserName(), loginRequest.getPassword(),
					loginRequest.getMfaOtpCode());
			// 認証のためユーザー情報をセッションに格納
			req.getSession(true).setAttribute(User.class.getName(), user);
		} catch (AuthenticationFailException e) {
			throw new APIErrorException(HttpStatus.UNAUTHORIZED, APIError.AUTHENTICATION_ERROR);
		} catch (MFAOTPCodeRequiredException e) {
			throw new APIErrorException(HttpStatus.UNAUTHORIZED, APIError.REQUIRED_MFA_OTP_CODE);
		} catch (InvalidMFAOTPCodeFormatException e) {
			throw new APIErrorException(HttpStatus.UNAUTHORIZED, APIError.AUTHENTICATION_ERROR);
		}
	}

	/**
	 * MFAの初期化処理
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/auth/mfa/init", produces = MediaType.APPLICATION_JSON_VALUE)
	public InitMFAResponse initMFA(HttpSession session) {
		log.info("MFA init");
		User user = (User) session.getAttribute(User.class.getName());
		String userId = user.getId();
		AuthenticatorKey authenticatorKey;

		try {
			authenticatorKey = authService.generateAuthenticatorKey(userId);
		} catch (AuthenticatorKeyHasBeenActivatedException e) {
			throw new APIErrorException(HttpStatus.BAD_REQUEST, APIError.MFA_ACTIVATED);
		}
		// 任意のアプリケーション名を設定します。
		String applicationName = "MY_APPLICATION_NAME";
		TOTPAuthUri totpAuthUri = new TOTPAuthUri(applicationName, userId, authenticatorKey.getSecretKey());
		InitMFAResponse res = new InitMFAResponse();
		res.setTotpAuthUri(totpAuthUri.toUri());
		return res;
	}

	/**
	 * MFAの有効化処理。この処理を前に、initMFA()を実行しておく必要があります。
	 * 
	 * @param activateMFARequest
	 * @param session
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/auth/mfa/activate", produces = MediaType.APPLICATION_JSON_VALUE)
	public void activateMFA(@RequestBody ActivateMFARequest activateMFARequest, HttpSession session) {
		log.info("MFA activate");
		User user = (User) session.getAttribute(User.class.getName());
		String userId = user.getId();
		try {
			authService.activateAuthenticatorKey(userId, activateMFARequest.getMfaOtpCode());
		} catch (AuthenticatorKeyNotFoundException e) {
			throw new APIErrorException(HttpStatus.BAD_REQUEST, APIError.MFA_NOT_INITIALIZED);
		} catch (AuthenticatorKeyHasBeenActivatedException e) {
			throw new APIErrorException(HttpStatus.BAD_REQUEST, APIError.MFA_ACTIVATED);
		} catch (MFAOTPCodeRequiredException | InvalidMFAOTPCodeFormatException | AuthenticationFailException e) {
			throw new APIErrorException(HttpStatus.BAD_REQUEST, APIError.INVALID_MFA_OTP_CODE);
		}
	}

	/**
	 * ログアウト
	 * 
	 * @param session
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/auth/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	public void login(HttpSession session) {
		log.info("logout");
		if (session != null) {
			session.invalidate();
		}
	}
}

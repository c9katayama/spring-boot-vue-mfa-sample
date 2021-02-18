package mfasample.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import mfasample.model.AuthenticatorKey;
import mfasample.model.User;
import mfasample.repository.AuthenticatorKeyRepository;
import mfasample.repository.UserRepository;

/**
 * 認証サービス
 * 
 * <pre>
 * このクラスで、ユーザーの認証とMFAの認証、MFAの初期などを行います。
 * 
 * MFAを利用する場合、最初にgenerateAuthenticatorKey()でMFA用のAuthenticatorKeyを生成します。
 * ユーザー側でMFAを設定し、最初のコードが正しかった時点で有効化します。
 * </pre>
 * 
 * @author c9katayama
 *
 */
@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthenticatorKeyRepository credentialRepository;

	private GoogleAuthenticator googleAuthenticator;

	@PostConstruct
	public void init() {
		googleAuthenticator = new GoogleAuthenticator();
	}

	/**
	 * ユーザー認証を行います。認証が成功した場合、Userが返されます
	 * 
	 * @param userId
	 * @param password
	 * @param mfaCode
	 * @return
	 * @throws AuthenticationFailException      認証失敗
	 * @throws MFAOTPCodeRequiredException      MFAが有効になっており、OTPコードが必要な場合
	 * @throws InvalidMFAOTPCodeFormatException MFAのOTPコードのフォーマットが正しくない場合
	 */
	public User doAuth(String userId, String password, String mfaCode)
			throws AuthenticationFailException, MFAOTPCodeRequiredException, InvalidMFAOTPCodeFormatException {
		User user = userRepository.find(userId);
		if (user == null) {
			throw new AuthenticationFailException();
		}
		if (user.validPassword(password) == false) {
			throw new AuthenticationFailException();
		}
		if (hasActivatedAuthenticatorKey(userId)) {
			confirmMFAOTPCode(userId, mfaCode);
		}
		return user;
	}

	private void confirmMFAOTPCode(String userId, String mfaCode)
			throws MFAOTPCodeRequiredException, InvalidMFAOTPCodeFormatException, AuthenticationFailException {
		if (StringUtils.hasLength(mfaCode) == false) {
			throw new MFAOTPCodeRequiredException();
		}
		Integer mfaCodeForGoogleAuth;
		try {
			mfaCodeForGoogleAuth = Integer.parseInt(mfaCode);
		} catch (Exception e) {
			throw new InvalidMFAOTPCodeFormatException();
		}
		AuthenticatorKey authenticatorKey = credentialRepository.find(userId);
		boolean authResult = googleAuthenticator.authorize(authenticatorKey.getSecretKey(), mfaCodeForGoogleAuth);
		if (authResult == false) {
			throw new AuthenticationFailException();
		}
	}

	/**
	 * アクティブ化された認証キーがあるかどうか
	 * 
	 * @param userId
	 * @return
	 */
	public boolean hasActivatedAuthenticatorKey(String userId) {
		AuthenticatorKey authenticatorKey = credentialRepository.find(userId);
		if (authenticatorKey != null && authenticatorKey.isActivated()) {
			return true;
		}
		return false;
	}

	/**
	 * MFA用のAuthenticatorKeyを生成します。
	 * 
	 * @param userId
	 * @return
	 * @throws AuthenticatorKeyHasBeenActivatedException
	 */
	public AuthenticatorKey generateAuthenticatorKey(String userId) throws AuthenticatorKeyHasBeenActivatedException {
		if (hasActivatedAuthenticatorKey(userId)) {
			throw new AuthenticatorKeyHasBeenActivatedException();
		}
		GoogleAuthenticatorKey googleAuthKey = googleAuthenticator.createCredentials();
		AuthenticatorKey generatedKey = new AuthenticatorKey(googleAuthKey.getKey(), googleAuthKey.getScratchCodes());
		credentialRepository.save(userId, generatedKey);
		return generatedKey;
	}
	/**
	 * AuthenticatorKeyを有効化します
	 * @param userId
	 * @param mfaCode
	 * @throws AuthenticatorKeyNotFoundException AuthenticatorKeyが未生成
	 * @throws AuthenticatorKeyHasBeenActivatedException　AuthenticatorKeyがすでに有効化されている
	 * @throws MFAOTPCodeRequiredException　MTAOTPコードがない
	 * @throws InvalidMFAOTPCodeFormatException MTAOTPコードのフォーマットが不正
	 * @throws AuthenticationFailException 認証失敗
	 */
	public void activateAuthenticatorKey(String userId, String mfaCode)
			throws AuthenticatorKeyNotFoundException, AuthenticatorKeyHasBeenActivatedException,
			MFAOTPCodeRequiredException, InvalidMFAOTPCodeFormatException, AuthenticationFailException {
		AuthenticatorKey authenticatorKey = credentialRepository.find(userId);
		if (authenticatorKey == null) {
			throw new AuthenticatorKeyNotFoundException();
		}
		if (authenticatorKey.isActivated()) {
			throw new AuthenticatorKeyHasBeenActivatedException();
		}
		confirmMFAOTPCode(userId, mfaCode);
		credentialRepository.activate(userId);
	}

	public static class AuthenticationFailException extends Exception {
		private static final long serialVersionUID = 1L;
	};

	public static class InvalidMFAOTPCodeFormatException extends Exception {
		private static final long serialVersionUID = 1L;
	};

	public static class MFAOTPCodeRequiredException extends Exception {
		private static final long serialVersionUID = 1L;
	};

	public static class AuthenticatorKeyNotFoundException extends Exception {
		private static final long serialVersionUID = 1L;
	};

	public static class AuthenticatorKeyHasBeenActivatedException extends Exception {
		private static final long serialVersionUID = 1L;
	};
}

package mfasample.model;

public enum APIError {

	REQUIRED_MFA_OTP_CODE("E001", "MFA OTPコードを入力してください。"), //
	AUTHENTICATION_ERROR("E002", "認証に失敗しました。"), //
	MFA_ACTIVATED("E003", "多要素認証はすでに有効化済みです。"), //
	MFA_NOT_INITIALIZED("E004", "多要素認証が初期化されていません。"), //
	INVALID_MFA_OTP_CODE("E005", "MFA OTPコードが正しくありません。"); //

	String errorCode;
	String message;

	private APIError(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}
}

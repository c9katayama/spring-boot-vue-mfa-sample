package mfasample.controller.io;

public class AuthControllerIO {

	public static class LoginRequest {
		private String userName;
		private String password;
		private String mfaOtpCode;

		public String getMfaOtpCode() {
			return mfaOtpCode;
		}

		public String getPassword() {
			return password;
		}

		public String getUserName() {
			return userName;
		}

		public void setMfaOtpCode(String mfaOtpCode) {
			this.mfaOtpCode = mfaOtpCode;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	public static class InitMFAResponse {
		private String totpAuthUri;

		public String getTotpAuthUri() {
			return totpAuthUri;
		}

		public void setTotpAuthUri(String totpAuthUri) {
			this.totpAuthUri = totpAuthUri;
		}
	}

	public static class ActivateMFARequest {
		private String mfaOtpCode;

		public String getMfaOtpCode() {
			return mfaOtpCode;
		}

	}
}

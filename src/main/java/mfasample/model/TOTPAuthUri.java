package mfasample.model;

public class TOTPAuthUri {
	private String issuer;
	private String userName;
	private String secretKey;

	public TOTPAuthUri(String issuer, String userName, String secretKey) {
		this.issuer = issuer;
		this.userName = userName;
		this.secretKey = secretKey;
	}

	public String toUri() {
		return "otpauth://totp/" + issuer + ":" + userName + "?secret=" + secretKey + "&issuer=" + issuer;
	}
}

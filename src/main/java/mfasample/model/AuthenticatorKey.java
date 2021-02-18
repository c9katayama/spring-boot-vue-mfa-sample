package mfasample.model;

import java.util.List;

public class AuthenticatorKey {
	private String secretKey;
	private List<Integer> scratchCodes;
	private boolean activated;

	public AuthenticatorKey(String secretKey, List<Integer> scratchCodes) {
		this.secretKey = secretKey;
		this.scratchCodes = scratchCodes;
	}

	public List<Integer> getScratchCodes() {
		return scratchCodes;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public boolean isActivated() {
		return activated;
	}
}

package mfasample;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class GooleAuthenticatorTest {

	@Test
	public void testAuth() {
		GoogleAuthenticator authenticator = new GoogleAuthenticator();
		GoogleAuthenticatorKey createCredentials = authenticator.createCredentials();
		System.out.println(createCredentials.getKey());
		System.out.println(createCredentials.getVerificationCode());
		System.out.println(createCredentials.getScratchCodes().stream().map(s -> String.valueOf(s))
				.collect(Collectors.joining(",")));

		authenticator.authorize(createCredentials.getKey(), createCredentials.getVerificationCode());
	}

}

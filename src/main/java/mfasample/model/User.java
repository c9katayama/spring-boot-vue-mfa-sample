package mfasample.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

/**
 * ユーザー情報
 * 
 * @author c9katayama
 *
 */
public class User {

	private String id;
	private String passwordSalt;
	private String passwordHash;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public static String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[32];
		random.nextBytes(salt);
		return Base64.encodeBase64String(salt);
	}

	public void initPassword(String password) {
		this.passwordSalt = generateSalt();
		this.passwordHash = toPasswordHash(this.passwordHash, password);
	}

	public boolean validPassword(String password) {
		String testHash = toPasswordHash(this.passwordHash, password);
		return this.passwordHash.equals(testHash);
	}

	private String toPasswordHash(String salt, String password) {
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			byte[] digest = sha256.digest((passwordSalt + password).getBytes());
			return Base64.encodeBase64String(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}

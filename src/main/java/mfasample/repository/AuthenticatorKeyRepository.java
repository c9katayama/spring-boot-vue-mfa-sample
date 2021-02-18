package mfasample.repository;

import mfasample.model.AuthenticatorKey;

/**
 * MFA用のAuthenticatorKeyのリポジトリインターフェース
 * 
 * <pre>
 * このインターフェースを実装して、AuthenticatorKeyの保存/有効化を行います。
 * </pre>
 * 
 * @author c9katayama
 *
 */
public interface AuthenticatorKeyRepository {

	AuthenticatorKey find(String userName);

	void save(String userName, AuthenticatorKey authenticatorKey);

	void activate(String userName);
}

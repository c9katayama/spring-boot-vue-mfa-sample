package mfasample.repository.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import mfasample.model.AuthenticatorKey;
import mfasample.repository.AuthenticatorKeyRepository;

/**
 * テスト用にインメモリでAuthenticatorKeyを持つリポジトリです。
 * 
 * <pre>
 * 組み込む際は、DBを使ってデータを保存/読み込みするようにしてください。
 * </pre>
 * 
 * @author c9katayama
 *
 */
@Component
public class InMemoryAuthenticatorKeyRepository implements AuthenticatorKeyRepository {

	private Map<String, AuthenticatorKey> repository = new ConcurrentHashMap<>();

	@Override
	public void save(String userName, AuthenticatorKey authenticatorKey) {
		repository.put(userName, authenticatorKey);
	}

	@Override
	public AuthenticatorKey find(String userName) {
		return repository.get(userName);
	}

	@Override
	public void activate(String userName) {
		AuthenticatorKey authenticatorKey = repository.get(userName);
		if (authenticatorKey != null) {
			authenticatorKey.setActivated(true);
		}
	}
}

package mfasample.repository.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import mfasample.model.User;
import mfasample.repository.UserRepository;

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
public class InMemoryUserRepository implements UserRepository {

	private Map<String, User> repository = new ConcurrentHashMap<>();
	{
		// テスト用初期ユーザー
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setId("user" + i);
			user.initPassword("password");
			repository.put(user.getId(), user);
		}
	}

	@Override
	public User find(String userId) {
		return repository.get(userId);
	}

}

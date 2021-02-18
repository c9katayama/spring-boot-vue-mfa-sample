package mfasample.repository;

import mfasample.model.User;

/**
 * ユーザー情報のリポジトリインターフェース
 * 
 * <pre>
 * このインターフェースを実装して、ユーザーの取得を行います。
 * </pre>
 * @author c9katayama
 *
 */
public interface UserRepository {
	
	User find(String userId);
}

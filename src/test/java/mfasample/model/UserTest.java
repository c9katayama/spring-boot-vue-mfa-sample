package mfasample.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UserTest {

	@Test
	public void testPassword() {
		User user = new User();
		user.setId("test");
		user.initPassword("hoge");

		assertTrue(user.validPassword("hoge"));
		assertFalse(user.validPassword("hoge2"));
		assertFalse(user.validPassword("Hoge"));
	}
}

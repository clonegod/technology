package protectionproxy;

public class AccessValidator {
	public boolean validateUser(String userId) {
		return "Admin".equals(userId);
	}
}

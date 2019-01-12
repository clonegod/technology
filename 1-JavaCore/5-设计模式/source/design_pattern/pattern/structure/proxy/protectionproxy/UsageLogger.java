package protectionproxy;

import util.SimpleUtil;

public class UsageLogger {
	private String userId;
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public void save() {
		String sql = "INSERT INTO USAGE_TABLE (user_id, oper_time) "
				+ " VALUES ('"+userId+"', '"+SimpleUtil.parseCurrentDateTime()+"');";
		// execute this SQL statements
		System.out.println(userId+" -> Save sql : "+sql);
	}
	
	public void save(String userId) {
		this.userId = userId;
		save();
	}
}

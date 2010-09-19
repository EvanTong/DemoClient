package client.core;

public class Utils {
	/**
	 * Parse user name from user information 
	 * @param userInfo
	 * @return
	 */
	public static String parseUser(String userInfo) {
		if(userInfo != null) {
			int s = userInfo.indexOf(':');
			if(s > 0) {
				return userInfo.substring(0,s);
			}
		}
		return "";
	}
	
	/**
	 * Parse password from user information 
	 * @param userInfo
	 * @return
	 */
	public static String parsePass(String userInfo) {
		if(userInfo != null) {
			int s = userInfo.indexOf(':');
			if(s > 0) {
				return userInfo.substring(s+1,userInfo.length());
			}
		}
		return "";
	}
}

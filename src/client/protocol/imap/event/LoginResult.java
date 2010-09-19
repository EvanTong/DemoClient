package client.protocol.imap.event;

import client.core.model.Event;

public class LoginResult extends Event {
	public static final int LOGIN_FAILED_unkown = -1;
	public static final int LOGIN_OK = 0;
	
	int    mCode     = -1;
	String mDesc     = "";
	
	public LoginResult() {
		
	}
	
	public LoginResult setResultCode(int code) {
		mCode = code;
		return this;
	}
	
	public boolean isOK() {
		return mCode == LOGIN_OK;
	}
	
	public String toString() {
		return String.format("(%s CODE %d)", super.toString(), mCode);
	}
}

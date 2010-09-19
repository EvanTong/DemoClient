package client.protocol.imap.event;

import java.util.Timer;

import client.core.model.Event;

public class ImapConnectionClose extends Event {
	String mReason = "unknow";
	
	public ImapConnectionClose(String reason) {
		setReason(reason);
	}
	
	public void setReason(String reason) {
		mReason = reason;
	}
	
	public String getReason() {
		return mReason;
	}
	
	public String toString() {
		return String.format("(%s (:REASON %s))", super.toString(), getReason());
	}
}

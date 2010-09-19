package client.protocol.imap;

import client.core.model.Event;

public abstract class ImapEvent extends Event {
	public enum Result {
		OK,
		NO,
		BAD
	};
	
	protected String mData = null;
	
	public ImapEvent() {
		
	}
	
	public ImapEvent(String text) {
		mData = text;
	}
}

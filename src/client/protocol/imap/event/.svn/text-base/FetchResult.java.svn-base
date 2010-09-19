package client.protocol.imap.event;

import client.core.model.Event;

public class FetchResult extends Event {
	private int mNumber = -1;
	public FetchResult(int number) {
		mNumber = number;
	}
	public int getNumber() {
		return mNumber;
	}
	public String toString() {
		return String.format("(%s %d)", super.toString(), mNumber);
	}
}

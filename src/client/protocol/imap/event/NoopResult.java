package client.protocol.imap.event;

import java.net.URI;

import client.core.model.Event;

public class NoopResult extends Event{
	public static final int NOOP_ok    =  0;
	public static final int NOOP_error = -1;
	private int mResult     = -1;
	private URI mSessionURI = null;
	
	public NoopResult(URI sessionURI) {
		mSessionURI = sessionURI;
	}
	
	public int getResult() {
		return mResult;
	}
	
	public void setResult(int result) {
		mResult = result;
	}
	
	public boolean isOk() {
		return getResult() == NOOP_ok;
	}
	
	public URI getSessionURI() {
		return mSessionURI;
	}
	
	@Override
	public String toString() {
		return String.format("(%s SESSION %s RESULT %d)", super.toString(),getSessionURI().toString(),getResult());
	}
}

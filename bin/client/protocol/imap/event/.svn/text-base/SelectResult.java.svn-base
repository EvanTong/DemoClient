package client.protocol.imap.event;

import java.net.URI;

import client.core.model.Event;

public class SelectResult extends Event {
	private long mExists      = 0;
	private long mUidValidity = 0;
	private long mUidNext     = 0;
	private long mRecent      = 0;
	
	public long getExists() {
		return mExists;
	}
	public void setExists(long mExists) {
		this.mExists = mExists;
	}
	public long getUidValidity() {
		return mUidValidity;
	}
	public void setUidValidity(long mUidValidity) {
		this.mUidValidity = mUidValidity;
	}
	public long getUidNext() {
		return mUidNext;
	}
	public void setUidNext(long mUidNext) {
		this.mUidNext = mUidNext;
	}
	public long getRecent() {
		return mRecent;
	}
	public void setRecent(long mRecent) {
		this.mRecent = mRecent;
	}
	private URI mSessionUri = null;
	public SelectResult(URI sessionUri) {
		mSessionUri = sessionUri;
	}	
	public URI getSessionUri() {
		return mSessionUri;
	}
	
	public String toString() {
		return String.format("(%s EXSIT %d RECENT %d UIDVALIDITY %d UIDNEXT %d)", super.toString(),getExists(), getRecent(), getUidValidity(), getUidNext());
	}
}

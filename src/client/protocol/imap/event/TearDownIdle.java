package client.protocol.imap.event;

import client.core.model.Event;
import client.core.model.Notifiers;
import client.protocol.imap.ImapSession;

import java.net.URI;

public class TearDownIdle extends Event {
	URI mURI = null;
	public TearDownIdle(URI sessionURI) {
		mURI = sessionURI;
		setTo(new Notifiers(mURI.toString()));
	}
	
	public boolean isMatched(final ImapSession targetSession) {
		return mURI != null && mURI.equals(targetSession.getURI());
	}
}

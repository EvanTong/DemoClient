package client.protocol.imap.event;

import java.net.URI;

import client.core.model.Event;
import client.core.model.Notifiers;
import client.protocol.imap.ImapSession;

public class QuitIdle extends Event {
	URI mSessionURI = null;
	
	public QuitIdle(ImapSession session) {
		mSessionURI = session.getURI();
		setTo(new Notifiers(mSessionURI.toString()));
	}
	
	public URI getSessionURI() {
		return mSessionURI;
	}
	
	public String toString() {
		return String.format("(%s SESSION %s)", super.toString(), getSessionURI());
	}
}

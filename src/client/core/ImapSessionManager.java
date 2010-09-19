package client.core;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

import client.protocol.imap.ImapSession;

public class ImapSessionManager {
	private static ImapSessionManager sInstance = new ImapSessionManager(); 
	private ConcurrentHashMap<URI, ImapSession> mSessions = new ConcurrentHashMap<URI, ImapSession>();
	
	private ImapSessionManager() {};
	public static ImapSessionManager I() {
		return sInstance;
	}
	
	public ImapSession getSession(URI uri) {
		ImapSession session = mSessions.get(uri);
		if(session != null) {
			System.err.println("have");
			return session;
		} else {
			session = new ImapSession(uri);
			mSessions.put(uri, session);
			return session;
		}
	}
}

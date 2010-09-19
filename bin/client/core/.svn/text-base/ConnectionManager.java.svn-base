package client.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;


import client.core.model.net.Connection;
import client.protocol.imap.ImapConnection;


/**
 * Manager multi-connections, singleton pattern
 * @author amas
 */
public class ConnectionManager {
	public static ConnectionManager sInstance = new ConnectionManager();
	private ConcurrentHashMap<String, Connection> mConnections = new ConcurrentHashMap<String, Connection>(4);

	private ConnectionManager() {
		
	}
	
	public static ConnectionManager I() {
		return sInstance;
	}
	
	
	/**
	 * <p> Create a imap connection </p>
	 * @param host
	 * @param port
	 * @param security
	 * @param account
	 * @param passwd
	 * @return
	 */
	public ImapConnection createImapConnection(String host, 
										       int    port, 
										       ImapConnection.Security security,
										       String account,
										       String passwd) {
		
		try {
			URI uri = new URI("imap", account+":"+passwd, host, port, "", "", "");
			ImapConnection conn = new ImapConnection(uri);
			putConnection(conn.getURI().toString(), conn);
			return conn;	
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Connection createConnection(URI uri) {
		if(uri != null) {
			String scheme = uri.getScheme();
			if(scheme.contains("imap")) {
				return new ImapConnection(uri);
			}
		}
		return null;
	}
	
	/**
	 * <p>Get connection object by give uri</p>
	 * @param uri
	 * @return connection object
	 */
	public Connection getConnection(String uri) {
		return mConnections.get(uri);
	}
	
	/**
	 * <p>Get connection object by give uri</p>
	 * @param uri
	 * @return connection object
	 */
	public Connection getConnection(URI uri) {
		return mConnections.get(uri.toString());
	}
	
	public Connection putConnection(String uri, Connection conn) {
		return mConnections.put(uri, conn);
	}
	
	public boolean testConnection(String uri) {
		return false;
	}
	
	public void removeConnection(String uri) {
		Connection rm = mConnections.remove(uri);
	}
}

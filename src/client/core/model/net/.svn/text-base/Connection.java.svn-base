package client.core.model.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import client.core.Core;
import client.core.model.Event;
import client.core.model.EventListener;
import client.core.model.IEventHandler;
import client.core.model.Notifiers;
import client.core.model.project.ISubscribable;

/**
 * A Connection is a kind of resource.
 * <li>Socket for data transfer</li>
 * <li>Receiver for receive data from server</li>
 * <li>Sender for send request to server</li>
 * <hr>
 * A Connection object has an unique URI.
 * A Connection will register a listener group with it's URI
 * If you care about the event of specify Connection, you have two choices:
 * <li>1. Register your listener group to the Connection listener group.</p>
 * <li>2. Subscribe it's event to your listener group, That's means you should have a listener group first.</li>
 * @author amas
 */
public abstract class Connection implements EventListener{
	
	public static String parseUser(String userInfo) {
		if(userInfo != null) {
			int s = userInfo.indexOf(':');
			if(s > 0) {
				return userInfo.substring(0,s);
			}
		}
		return "";
	}
	
	public static String parsePass(String userInfo) {
		if(userInfo != null) {
			int s = userInfo.indexOf(':');
			if(s > 0) {
				return userInfo.substring(s+1,userInfo.length());
			}
		}
		return "";
	}
	
	public static enum Security {
		Plain,
		SSL,
		TLS
	}
	
	protected String    mUser        = "";
	protected String    mPass        = "";
	protected Socket    mSock        = null;
	protected String    mHost        = null;
	protected int       mPort        = -1;
	protected String    mSche        = "";
	protected Security  mSecurity    = Security.Plain;
	protected Notifiers mEventTo     = null;
//	protected String    mUid         = ""+hashCode();	
	protected URI       mURI         = null;
	protected HashMap<Object, IEventHandler<Void>>  mEventHandler = new HashMap<Object, IEventHandler<Void>>();
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Host = " + mHost).append('\n');
		sb.append("Port = " + mPort).append('\n');
		sb.append("User = " + mUser).append('\n');
		sb.append("Pass = " + mPass).append('\n');
		return sb.toString();
	}
	
	//TODO: need check encode and decode issue
	public Connection(URI uri) {
		mURI  = uri;
		mHost = uri.getHost();
		mPort = uri.getPort();
		mUser = parseUser(uri.getUserInfo());
		mPass = parsePass(uri.getUserInfo());
		mSche = uri.getScheme();
		if(mSche.contains("ssl")) {
			mSecurity = Security.SSL;
		} else if(mSche.contains("tls")) {
			mSecurity = Security.TLS;
		}
		
		Core.I().addListener(uri.toString(), this);
		mEventTo = new Notifiers(uri.toString());
	}
	
	protected String getUser() {
		return mUser;
	}
	
	protected int getPort() {
		return mPort;
	}
	
	protected String getScheme() {
		return mSche;
	}
	
	protected String getHost() {
		return mHost;
	}
	
	protected String getPassword() {
		return mPass;
	}
	
	protected Security getSecurity() {
		return mSecurity;
	}
	
	/**
	 * Get connection uri
	 * @return
	 */
	public URI getURI() {
		return mURI;
	}
		
	public void setNotifiers(Notifiers eventTo) {
		mEventTo = eventTo;
	}
	
	public Notifiers getNotifiers() {
		return mEventTo;
	}
	
	/**
	 * Open connection
	 * @throws IOException
	 */
	public void open() throws IOException {

	}
	
	/**
	 *  Close connection 
	 */
	public void close() {
		
	}
		
	public void onEvent(Event event) {
		IEventHandler<Void> h = mEventHandler.get(event.getClass());
		if(h != null) {
			h.handle(event);
		}
	}
	
	public void pushEvent(Event event) {
		event.setFrom(mURI.toString());
		event.setTo(mEventTo);
		Core.I().push(event);
	}
}

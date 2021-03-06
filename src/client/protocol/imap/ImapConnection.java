package client.protocol.imap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.util.HashMap;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


import client.core.Core;
import client.core.TaskManager;
import client.core.model.Event;
import client.core.model.EventListener;
import client.core.model.IEventHandler;
import client.core.model.Task;
import client.core.model.TimeStamp;
import client.core.model.TimeStamp.Tag;
import client.core.model.net.Connection;
import client.core.model.net.Receiver;
import client.core.model.net.Sender;
import client.protocol.imap.bean.CapabilityData;
import client.protocol.imap.bean.Greeting;
import client.protocol.imap.bean.ImapTaggedResponse;
import client.protocol.imap.bean.Response;
import client.protocol.imap.event.ImapConnectionClose;
import client.protocol.imap.event.SelectResult;


/**
 * An IMAP4rev1 Connection object.
 * An IMAP4rev1 connection consists of :
 *  1. establishment of a client/server network connection
 *  2. initial greeting from the server
 *  3. client/server interactions
 *  	3.1. client command
 *      3.2. server data
 *      3.3. server completion result response.
 * @author amas
 */
public class ImapConnection extends Connection {
	static final int 	SOCKET_CONNECT_TIMEOUT = 60000;
	static final int 	SOCKET_READ_TIMEOUT    = 0;//300000;
	
	public static enum State {
		UnConnected,       // stands for not connected
		NotAuthenticated,
		Authenticated,
		Selected,
		Logout,
		Idling
	};
	
	TimeStamp            mTimeStamp   = new TimeStamp();
	private State        mStatus      = State.UnConnected;
	private String       mLoginCmdTag = "X00";
	private String       mLoginCmd    = null;
	private ImapReceiver mRecv        = null;
	private Sender       mSend        = null;
		
	public class LoggedIn extends Event {
		public LoggedIn() {
			setFrom(getURI().toString());
		}
	}
	
	public ImapConnection(URI uri) {
		super(uri);
 		mLoginCmd = String.format("%s LOGIN %s %s", mLoginCmdTag, getUser(), getPassword());
 		
 		mEventHandler.put(ImapConnectionClose.class, new IEventHandler<Void>() {
			public Void handle(Event event) {
				close();
				return null;
			}
		});
 		
 		mEventHandler.put(ImapTaggedResponse.class, new IEventHandler<Void>() {
			public Void handle(Event event) {
				ImapTaggedResponse tr = (ImapTaggedResponse)event;
				if(tr.isOK() && tr.getTag().equals(mLoginCmdTag)) {
					setState(State.Authenticated);
					mTimeStamp.touch(Tag.END_TIME);
					pushEvent(new LoggedIn());
					System.err.println("IMAP.CONN.ON: " + mTimeStamp.getLifeTimeSec() + " sec");
				} else if (tr.isNo() || tr.isBad()) {
					setState(State.NotAuthenticated);
				}
				return null;
			}
		});
 		
 		mEventHandler.put(CapabilityData.class, new IEventHandler<Void>() {
			public Void handle(Event event) {
				return null;
			}
		});
	}
	
	/**
	 * Create socket object
	 * TODO: give a chance to config the created socket, such as timeout , interface name and etc.
	 * @param host
	 * @param port
	 * @return
	 * @throws IOException
	 */
	private Socket onCreateSocket(String host, int port) throws IOException {
		//TODO:
		// 1. support TLS
		// 2. SSL right???     
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(host, port), SOCKET_CONNECT_TIMEOUT);
		if(mSecurity == Security.SSL) { 
			System.err.println("UPGRADE SSL");
			SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket ss = (SSLSocket) factory.createSocket(socket, host,port, true);
			ss.setEnabledProtocols(new String[] { "TLSv1", "SSLv3" });
			ss.setUseClientMode(true);
			ss.startHandshake();
			socket = ss;
		}
		socket.setSoTimeout(SOCKET_READ_TIMEOUT);
		socket.setKeepAlive(true);
		return socket;
	}
		
	/**
	 * Get connection state
	 * @return
	 */
	public synchronized State getState() {
		return mStatus;
	}
	
	public synchronized void setState(State state) {
		mStatus = state;
	}
	
	/**
	 * Open connections and connect to the target.
	 * The Sender and Receiver will be created for comunication.
	 * @throws IOException
	 */
	public synchronized void open() throws IOException {
		if(getState() == State.UnConnected) {
			System.out.println("UnConnected...open");
			mTimeStamp.touch(Tag.START_TIME);
			try{
				//System.err.println("IMAP.CONN.OPEN         : " + getURI().toString());
				mSock = onCreateSocket(mHost, mPort);
				//System.err.println("IMAP.CONN.CREATE SOCKET: " + mSock);
				mRecv = new ImapReceiver(mSock.getInputStream(), mEventTo);
				// we need create sender before start login
				mSend = new Sender(mSock.getOutputStream());
				// handle greeting
				onImapEventGreeting(((ImapReceiver)mRecv).getGreeting());
				//System.err.println("IMAP.CONN.CREATE RECVER: " + mRecv);
				mRecv.start();			
				//System.err.println("IMAP.CONN.CREATE SENDER: " + mSend);
				//mIsOn = true;
			} catch (ConnectException e) {
				e.printStackTrace();
				// TODO connection open failed, push event 
				// TODO clear staff
				//pushEvent();
			} catch (SocketException e) { 
				System.err.println("SOCKET EXCEPTION: " + e);
			}
		}
	}
	
	/**
	 *  Close connection 
	 */
	public void close() {
		System.err.println(getURI().toString()+"    CLOSED");
		if(mSock != null) {
			try {
				mRecv.shutdown();
				mSock.close();
				mRecv = null;
				mSock = null;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//TODO: we need clear listeners on this session
			//Core.I().clearAllListener(getURI().toString());
			//Core.I().addListener(getURI().toString(), this);	
		}
		setState(State.UnConnected);
	}

	/**
	 * Hanle server greeting
	 * @param event
	 */
	private void onImapEventGreeting(Greeting event) {
		if(event.isOK()) {
			setState(State.NotAuthenticated);
			try {
				sendLine(mLoginCmd);
			} catch (IOException e) {
				close();
				e.printStackTrace();
			}
		} else if(event.isBYE()) {
			System.out.println("SERVER SAY GOODBYE");
		}
	}
	
	/**
	 * Send line(end with CRLF) to the server 
	 * @param text
	 * @throws IOException
	 */
	public synchronized void sendLine(String text) throws IOException {
		mSend.sendLine(text);
	}

	public void setReaderTimeout(int msec) {
		// TODO Auto-generated method stub
		try {
			mSock.setSoTimeout(msec);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}

package client.protocol.imap;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import client.core.ConnectionManager;
import client.core.Core;
import client.core.model.Event;
import client.core.model.EventListener;
import client.core.model.IEventHandler;
import client.core.model.Notifiers;
import client.core.model.Task;
import client.core.model.project.ISubscribable;
import client.protocol.imap.ImapConnection.LoggedIn;
import client.protocol.imap.ImapConnection.State;
import client.protocol.imap.event.ImapConnectionClose;
import client.protocol.imap.event.ImapIdling;
import client.protocol.imap.event.ImapSessionOn;
import client.protocol.imap.event.QuitIdle;
import client.protocol.imap.event.SelectResult;
import client.protocol.imap.event.TearDownIdle;
import client.protocol.imap.task.CmdIdle;
import client.protocol.imap.task.CmdSelect;

public class ImapSession implements ISubscribable{
	private static AtomicLong sIC   = new AtomicLong();
	
	
	private long           mId      = sIC.getAndIncrement();
	private String         mMailbox = "INBOX";
	private ImapConnection mConn    = null;
	private CmdSelect      mSelect  = null;
	private Notifiers      mEventTo = null;
	private SelectResult   mInfo    = null;
	
	protected HashMap<Object, IEventHandler<Void>>  mEH = new HashMap<Object, IEventHandler<Void>>();
	
	private EventListener  mEl      = new EventListener() {		
		public void onEvent(Event event) {
			_onEvent(event);
		}
	};
	
	public String getMailbox() {
		return mMailbox;
	}
	
	//TODO: bad name
	public SelectResult getSelectResult() {
		return mInfo;
	}
	
	private void setSelectResult(SelectResult result) {
		mInfo = result;
	}
	
	private void _onEvent(Event event) {
		IEventHandler<Void> eh = mEH.get(event.getClass());
		if(eh != null) {
			eh.handle(event);
		}
	}
	
	/**
	 * @param mailbox
	 */
	public ImapSession(URI uri) {
		mMailbox       = uri.getPath();
		mConn = (ImapConnection) ConnectionManager.I().createConnection(uri);
		Core.I().addListener(getURI().toString(), mEl);

		// install event handler
		mEH.put(LoggedIn.class, new IEventHandler<Void>() {
			public Void handle(Event event) {
				select();
				return null;
			}
		});
		
		mEH.put(SelectResult.class, new IEventHandler<Void>() {
			public Void handle(Event event) {
				setSelectResult((SelectResult) event);
				changeConnectionState(State.Selected);
				pushEvent(new ImapSessionOn(getURI()));
				return null;
			}
		});
		
		mEH.put(ImapIdling.class, new IEventHandler<Void>() {
			public Void handle(Event event) {
				System.err.println("---------------------------------");
				changeConnectionState(State.Idling);
				return null;
			}
		});		
		
		mEH.put(QuitIdle.class, new IEventHandler<Void>() {
			public Void handle(Event event) {
				changeConnectionState(State.Selected);
				pushEvent(new ImapSessionOn(getURI()));
				return null;
			}
		});	
		
		mEventTo = new Notifiers(getURI().toString());
		mSelect  = new CmdSelect(this, getMailbox());
	}
	
	//TODO: this need hide
	public void changeConnectionState(State state) {
		mConn.setState(state);
	}
	
	/**
	 * push a event
	 * @param event
	 */
	protected void pushEvent(Event event) {
		if(event != null) {
			event.setFrom(getURI().toString());
			event.setTo(mEventTo);
			Core.I().push(event);
		}
	}
	
	/**
	 * Change the imap connection state
	 */
	private void select() {
		submitTask(mSelect);
	}
	
	/**
	 * Start idle
	 */
	public void startIdle() {
		//TODO: ensure the server capa.
		if(mConn.getState() == State.Idling) {
			return;
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		submitTask(new CmdIdle(this));
	}
	
	/**
	 * Submit a task to Core and let task event push to the session listener group
	 * @param task
	 */
	private void submitTask(Task task) {
		task.setEventTo(mEventTo);
		Core.I().exec(task);
	}
	
	/**
	 * Close idle, if the connection not in {@link State#Idling} State, noting would happend.
	 */
	public void closeIdle() {
		if (mConn.getState() == State.Idling) {
			try {
				sendLine("DONE");
			} catch (IOException e) {
				e.printStackTrace();
			}
			changeConnectionState(State.Selected);
		}
	}
	
	/**
	 * Send text to server
	 * @param text
	 * @throws IOException 
	 */
	public void sendLine(String text) throws IOException {
		mConn.sendLine(text);
	}
	
	/**
	 * Start a Imap session, if session ready, the {@link SelectResult} event will be published
	 * @throws IOException
	 */
	public synchronized void start() throws IOException {
		if(mConn.getState() == State.Selected) {
			pushEvent(new ImapSessionOn(getURI()));
		} else if(mConn.getState() == State.Idling) {
			closeIdle();
		} else {
			mConn.open();
		}
	}
	
	/**
	 * Finish a Imap session and destory the connection
	 */
	public void finish() {
		mConn.close();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
	
	public URI getURI() {
		return mConn.getURI();
	}

	public void subscribeTo(String listenerGroupUri) {
		mEventTo.addNotifyUri(listenerGroupUri);
	}

	public boolean isAvaliable() {
		return mConn.getState() == State.Selected;
	}
}

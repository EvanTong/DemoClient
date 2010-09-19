package client.protocol.imap.task;

import java.util.Vector;

import client.core.model.Event;
import client.core.model.IEventHandler;
import client.protocol.imap.ImapConnection;
import client.protocol.imap.ImapSession;
import client.protocol.imap.bean.ImapTaggedResponse;
import client.protocol.imap.bean.MsgAtt;
import client.protocol.imap.event.FetchResult;
import client.protocol.imap.event.ImapConnectionClose;
/**
 * Example:    C: A654 FETCH 2:4 (FLAGS BODY[HEADER.FIELDS (DATE FROM)])
 *             S: * 2 FETCH ....
 *             S: * 3 FETCH ....
 *             S: * 4 FETCH ....
 *             S: A654 OK FETCH completed
 * TODO: very freq used command, need performance tuning
 */

public class CmdFetch extends ImapCommand {
	public interface MessageHandler {
		public int onReceiveMessage(MsgAtt msg);
	}
	
	public enum Macro {
		/**
		 * Defined by RFC3501
		 * equivalent to: (FLAGS INTERNALDATE RFC822.SIZE ENVELOPE)
		 */
		ALL,
		/**
		 * Defined by RFC3501
		 * equivalent to: (FLAGS INTERNALDATE RFC822.SIZE ENVELOPE BODY)
		 */
		FULL,
		/**
		 * Defined by RFC3501
		 * equivalent to: (FLAGS INTERNALDATE RFC822.SIZE)
		 */
		FAST,
		
		/**
		 * RFC822.HEADER+RFC822.SIZE
		 */
		RFC822_HEADER_SIZE,
	};
	
	/**
	 * Message sequence number / message uid set
	 */
	private Vector<Integer> mSN   = new Vector<Integer>();
		
	/**
	 * The attributes of message to be fetched
	 */
	private Vector<String> mAttrs = new Vector<String>();
	private String         mSNText="";
	
	private boolean        mUseMacro= false;
	private boolean        mUseUid  = false;
	private int            mNumberDone = 0;
	private MessageHandler mMH         = null;
	
	public void setMessageHandler(MessageHandler handler) {
		mMH = handler;
	}
	
	/**
	 * @param conn
	 * @param msgSn "1,2" "1:10" or "1,2,5:10"
	 */
	public CmdFetch(ImapSession session, String msgSn) {
		mSNText     = msgSn;
		mSession    = session;
		
		mEHs.put(MsgAtt.class, new IEventHandler<Void>() {
			public Void handle(Event event) {
				onMsgAtt((MsgAtt)event);
				return null;
			}
		});
	}
	
	public void onMsgAtt(MsgAtt msg) {
		mNumberDone++;
		if(mMH != null) {
			mMH.onReceiveMessage(msg);
		}
	}
	
	protected Event onPublishResult(ImapTaggedResponse event) {
		if(event.isOK()) {
			return new FetchResult(mNumberDone);
		}
		//TODO: how to handle failure ??? I don't see it now
		return null;
	}
	
	//---------------------------------------------------------------[ Command Builder ]
	public CmdFetch useUid() {
		mUseUid = true;
		return this;
	}
	
	public CmdFetch withEnvelope() {
		mAttrs.add("ENVELOPE");
		return this;
	}
	
	public CmdFetch withBodyStructure() {
		mAttrs.add("BODY");
		return this;
	}
	
	public CmdFetch withFlags() {
		mAttrs.add("FLAGS");
		return this;
	}
	
	public CmdFetch withInternalDate() {
		mAttrs.add("INTERNALDATE");
		return this;
	}
	
	/**
	 * Functionally equivalent to BODY[], differing in the syntax of Functionally equivalent to BODY[], differing in the syntax of
	 * e.g.:
	 * <pre>
	 * 
	 * </pre>
	 * @return
	 */
	public CmdFetch withRFC822() {
		mAttrs.add("RFC822");
		return this;
	}
	
	/**
	 * Fetch the RFC822 Header of the message
	 * @return
	 */
	public CmdFetch withRFC822Header() {
		mAttrs.add("RFC822.HEADER");
		return this;
	}
	
	/**
	 * Fetch the [RFC-2822] size of the message
	 * e.g:
	 * 
	 * @return
	 */
	public CmdFetch withRFC822Size() {
		mAttrs.add("RFC822.SIZE");
		return this;
	}
	
	/**
	 * @return
	 */
	public CmdFetch withRFC822Text() {
		mAttrs.add("RFC822.TEXT");
		return this;
	}
	
	/**
	 * Fetch message UID
	 * @return
	 */
	public CmdFetch withUid() {
		mAttrs.add("RFC822.UID");
		return this;
	}
	
	/**
	 * <p>
	 * Get the text of a particular body section.  
	 * 
	 * <h1>The section specification</h1>
	 * The section specification is a set of zero or more part specifiers delimited by periods.
	 * <h2>part specifiers include:</h2>
	 * <li>HEADER</li>
	 * <li>HEADER.FIELDS</li>
	 * <li>HEADER.FIELDS.NOT</li>
	 * <li>MIME</li>
	 * <li>TEXT</li>
	 * <code>
	 * A empty section specification stands for the entire message(include header).
	 * </code>
	 * 
	 * 
	 * </p>
	 * @param section section specification
	 * @param start start octects 
	 * @param end end octects
	 * Notice:  
	 *  1. If the starting octet is beyond the end of the text, an empty string is returned.
	 *  2. A substring fetch of a HEADER.FIELDS or HEADER.FIELDS.NOT part specifier is calculated after subsetting the header.
	 *  3. The \Seen flag is implicitly set; 
	 * @return
	 */
	public CmdFetch withBody(String section, long start, long end) {
		return null;
	}
	
	/**
	 * <p>
	 * An alternate form of BODY[<section>](see: withBody(...)) that does not implicitly set the \Seen flag.
	 * </p>
	 * @param section section number
	 * @param start start offset
	 * @param end end offset
	 * @return
	 * Notice:  
	 *  1. If the starting octet is beyond the end of the text, an empty string is returned.
	 */
	public CmdFetch withBodyPeek(long section, long start, long end) {	
		return this;
	}
	
	public CmdFetch withMacro(Macro macro) {
		return this;
	}
	
	@Override
	protected String getCommand() {
		mCmd.delete(0, mCmd.length());
		mCmd.append(mTag).append(mUseUid ? " UID" : "").append(" FETCH ");
		mCmd.append(mSNText).append(" ").append("(");
		for(int i=0; i<mAttrs.size()-1; ++i) {
			mCmd.append(mAttrs.get(i)).append(" ");
		}
		mCmd.append(mAttrs.get(mAttrs.size()-1));
		mCmd.append(')');
		return mCmd.toString();
	}
}

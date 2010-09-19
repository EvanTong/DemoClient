package client.protocol.imap.task;

import client.core.model.Event;
import client.core.model.IEventHandler;
import client.protocol.imap.ImapConnection;
import client.protocol.imap.ImapSession;
import client.protocol.imap.bean.ExistsData;
import client.protocol.imap.bean.ImapTaggedResponse;
import client.protocol.imap.bean.MailboxData;
import client.protocol.imap.bean.RCSUidNext;
import client.protocol.imap.bean.RCSUidValidity;
import client.protocol.imap.bean.RecentData;
import client.protocol.imap.bean.RespCondState;
import client.protocol.imap.bean.ResponseDone;
import client.protocol.imap.event.SelectResult;
/**
 * <h3>6.3.1.  SELECT Command</h3>
 * <pre>
 * Arguments:  mailbox name
 * Responses:  REQUIRED untagged responses: FLAGS, EXISTS, RECENT
 *             REQUIRED OK untagged responses:  UNSEEN,  PERMANENTFLAGS,
 *             UIDNEXT, UIDVALIDITY
 * Result:     OK  - select completed, now in selected state
 *             NO  - select failure, now in authenticated state: no
 *                   such mailbox, can't access mailbox
 *             BAD - command unknown or arguments invalid
 *             
 *             OK [UNSEEN <n>]    // Message SN of 1st unseen message, this may missing
 *             OK [PERMANENTFLAGS (<list of flags>)]
 *             OK [UIDNEXT <n>]   // X1 OK LOGIN completed
 *             OK [UIDVALIDITY <n>]  // 
 *  
 *  Only one mailbox can be selected at a time in a connection;
 *  simultaneous access to multiple mailboxes requires multiple connections. 
 *  The SELECT command automatically deselects any currently selected mailbox 
 *  before attempting the new selection. Consequently, 
 *  if a mailbox is selected and a SELECT command that fails is attempted, 
 *  no mailbox is selected.
 *  If the client is permitted to modify the mailbox, 
 *  the server SHOULD prefix the text of the tagged OK response with the "[READ-WRITE]" response code. 
 * </pre>
 */
public class CmdSelect extends ImapCommand {
	SelectResult mResult = null;
	
	public CmdSelect(ImapSession session, String mailbox) {
		mSession = session;
		mCmd.append(String.format("%s SELECT %s", mTag, mailbox)); //TODO: need remove '/'
		mResult = new SelectResult(mSession.getURI());
		
		// install event handler
		mEHs.put(ExistsData.class, new IEventHandler<Void>() {		
			public Void handle(Event event) {
				onExistsData((ExistsData)event);
				return null;
			}
		});
		
		mEHs.put(RecentData.class, new IEventHandler<Void>() {		
			public Void handle(Event event) {
				onRecentData();
				return null;
			}
		});
		
		mEHs.put(RCSUidValidity.class, new IEventHandler<Void>() {		
			public Void handle(Event event) {
				onRCSUidValidity((RCSUidValidity)event);
				return null;
			}
		});
		
		mEHs.put(RCSUidNext.class, new IEventHandler<Void>() {		
			public Void handle(Event event) {
				onRCSUidNext((RCSUidNext)event);
				return null;
			}
		});
	}
	
	protected void onExistsData(ExistsData event) {
		mResult.setExists(event.getNumber());
	}

	protected void onRecentData() {
		//TODO: add it
		mResult.setRecent(0);
	}

	protected void onRCSUidValidity(RCSUidValidity event) {
		mResult.setUidValidity(event.getNumber());
	}

	protected void onRCSUidNext(RCSUidNext event) {
		mResult.setUidNext(event.getNumber());
	}
	
	@Override
	protected Event onPublishResult(ImapTaggedResponse event) {
		return mResult;
	}
}

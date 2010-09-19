package client.protocol.imap.task;

import client.core.Core;
import client.core.model.Event;
import client.core.model.IEventHandler;
import client.protocol.imap.ImapSession;
import client.protocol.imap.ImapConnection.State;
import client.protocol.imap.bean.ContinueReq;
import client.protocol.imap.bean.ExistsData;
import client.protocol.imap.bean.ImapTaggedResponse;
import client.protocol.imap.event.ImapSessionOn;
import client.protocol.imap.event.QuitIdle;
import client.protocol.imap.event.TearDownIdle;
import client.protocol.imap.event.ImapIdling;

public class CmdIdle extends ImapCommand {
	public CmdIdle(ImapSession session) {
		mSession = session;
		mCmd.append(String.format("%s IDLE", mTag));
		
		mEHs.put(ContinueReq.class, new IEventHandler<Void>() {
			public Void handle(Event event) {
				Core.I().push(new ImapIdling(mSession));
				return null;
			}
		});		
	}
	
	protected Event onPublishResult(ImapTaggedResponse event) {
		Core.I().push(new QuitIdle(mSession));
		return null;
	}
}

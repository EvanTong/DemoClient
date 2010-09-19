package client.protocol.imap.task;

import client.core.model.Event;
import client.core.model.IEventHandler;
import client.protocol.imap.ImapSession;
import client.protocol.imap.bean.ImapTaggedResponse;
import client.protocol.imap.bean.SearchData;
import client.protocol.imap.event.NoopResult;

public class CmdNoop extends ImapCommand {
	NoopResult mResult = null;
	
	public CmdNoop(ImapSession session) {
		mSession = session;
		mCmd.append(String.format("%s NOOP", mTag));
		mResult = new NoopResult(session.getURI());
	}
	
	@Override
	protected Event onPublishResult(ImapTaggedResponse event) {
		if(event.isOK()) {
			mResult.setResult(NoopResult.NOOP_ok);
		}
		return mResult;
	}
}

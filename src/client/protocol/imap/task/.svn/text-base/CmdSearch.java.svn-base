package client.protocol.imap.task;

import client.core.model.Event;
import client.core.model.IEventHandler;
import client.protocol.imap.ImapConnection;
import client.protocol.imap.ImapSession;
import client.protocol.imap.bean.ImapTaggedResponse;
import client.protocol.imap.bean.MailboxData;
import client.protocol.imap.bean.SearchData;
import client.protocol.imap.event.ImapConnectionClose;
import client.protocol.imap.event.SearchResult;


public class CmdSearch extends ImapCommand {
	boolean mUseUid = false;
	SearchResult mResult = new SearchResult();
	
	// TODO: enhance it, make search command build more flex.
	public CmdSearch(ImapSession session, String cmd) {
		mSession = session;
		mCmd.append(mTag).append(" ").append(cmd);
		mEHs.put(SearchData.class, new IEventHandler<Void>() {
			public Void handle(Event event) {
				onSearchData((SearchData)event);
				return null;
			}
		});
	}
	
	/**
	 * Collect the search result
	 * TODO: need remove duplication ??? I don't meet such case now :P
	 * @param data
	 */
	private void onSearchData(SearchData data) {
		mResult.addResult(data.getSearchResult());
	}
	
	/**
	 * Publish the command result
	 * @see client.protocol.imap.task.ImapCommand#onPublishResult(client.protocol.imap.bean.ImapTaggedResponse)
	 */
	protected Event onPublishResult(ImapTaggedResponse event) {
		if(event.isOK()) {
			return mResult;
		}
		//TODO: how to handle failure ??? I don't see it now
		return null;
	}
	
}

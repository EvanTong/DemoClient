package client.protocol.imap.task;

import client.core.model.Event;
import client.protocol.imap.ImapSession;
import client.protocol.imap.bean.ImapTaggedResponse;
import client.protocol.imap.event.LoginResult;

public class CmdLogin extends ImapCommand {
	public CmdLogin(ImapSession session, String user, String password) {
		mSession = session;
		mCmd.append(String.format("%s LOGIN %s %s", mTag, user, password));
	}
	
	protected Event onPublishResult(ImapTaggedResponse event) {
		ImapTaggedResponse tr = (ImapTaggedResponse)event;
		LoginResult       ret = new LoginResult();
		
		int code = LoginResult.LOGIN_FAILED_unkown;
		
		if(tr.isOK() && tr.getTag().equals(mTag)) {
			System.err.println("OK we are geting there!");
			// login succes
			code = LoginResult.LOGIN_OK;
			
		} else if (tr.isNo() || tr.isBad()) {
			// login failed
		}
		
		return ret.setResultCode(code);
	}
}

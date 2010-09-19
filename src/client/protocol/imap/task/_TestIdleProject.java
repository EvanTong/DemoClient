package client.protocol.imap.task;

import java.io.IOException;
import java.net.URI;
import java.util.Vector;

import client.core.ConnectionManager;
import client.core.model.Event;
import client.core.model.Task;
import client.core.model.project.Project;
import client.core.model.project.TodoItem;
import client.core.model.project.TriggerEventHandler;
import client.protocol.imap.ImapConnection;
import client.protocol.imap.ImapSession;
import client.protocol.imap.ImapConnection.LoggedIn;
import client.protocol.imap.bean.Response;
import client.protocol.imap.event.ImapSessionOn;

public class _TestIdleProject extends Project {
	ImapSession    mSession  = null;
	
	@Override
	protected void onStart() {
		try {
			mSession.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public _TestIdleProject(ImapSession session) {
		super("_TestIdleProject");
		mSession = session;		
		mSession.subscribeTo(getSelfListenerGroup());
		
		TodoItem todo = new TodoItem();
		todo.addEventHandler(ImapSessionOn.class, new TriggerEventHandler(getProjectController()){
			@Override
			public Task handle(Event event) {
				return new CmdNoop(mSession);	
			}
		});
		addTodoItem(todo);
	}
	
	@Override
	protected void onFinish() {
		System.out.println("_TestIdleProject OVER..... TRY RESTORE IDLE");
		mSession.startIdle();
	}
}

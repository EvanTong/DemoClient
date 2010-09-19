package client.core.model.project;

import java.util.HashMap;

import client.core.model.Event;
import client.core.model.IEventHandler;
import client.core.model.Task;
import client.protocol.imap.task.ImapCommand;

public class TodoItem implements ITodo{
	public enum Status {
		DONE,
		CANCELED,
		ON_GOGING,
		NOT_START,
	};
	
	private HashMap<Object, TriggerEventHandler> mEHandlers = new HashMap<Object, TriggerEventHandler>();
	private Status                               mStatus    = Status.NOT_START;

	
	final public Task launchTask(Event triggerEvent) {
		IEventHandler<Task> eh = getEventHandler(triggerEvent.getClass());
		return (eh == null) ? null : eh.handle(triggerEvent);
	}
	
	/**
	 * @param klass The {@link Event} klass
	 * @param eventHandler
	 * @return The previous {@link IEventHandler} associated with key 
	 * or null if there's no mapping for the key
	 */
	public IEventHandler<Task> addEventHandler(Object klass, TriggerEventHandler eventHandler) {
		return mEHandlers.put(klass, eventHandler);
	}
	
	/**
	 * Get specify {@link IEventHandler} by key
	 * @param klass
	 * @return
	 */
	public TriggerEventHandler getEventHandler(Object klass) {
		return mEHandlers.get(klass);
	}
	
}

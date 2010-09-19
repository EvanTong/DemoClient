package client.core;

import java.util.HashMap;

import client.core.model.Event;
import client.core.model.EventListener;
import client.core.model.ListenerGroup;
import client.core.model.Task;

/**
 * Define the basic service provided by client core
 * @author amas
 */
public interface ICore {
	//---------------------------------------------------------------[ Task ]
	public void reinit();
	public void exec(Task task);
	public void cron(Task task);
	
	//---------------------------------------------------------------[ Event ]
	public void push(Event event);
	public void broadcast(Event event);
	
	//---------------------------------------------------------------[ EventListener ]
	public void addListener(String groupUri, EventListener listener);
	public void addListener(EventListener listener); //???
	public void removeListener(String groupUri, EventListener listener);
	public void removeListener(EventListener listener);
	
	//---------------------------------------------------------------[ EventListener.Group ]
	public void addListenerGroup(String uri);
	public ListenerGroup removeListenerGroup(String uri); // need return ???
	public void clearAllListener(String uri);
	
	//---------------------------------------------------------------[ Connection ]
	//public void createTcpConnection();
	
	
	//---------------------------------------------------------------[ Debug ]
	public void dump();
}

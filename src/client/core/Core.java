package client.core;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import client.core.model.Event;
import client.core.model.EventListener;
import client.core.model.ListenerGroup;
import client.core.model.Task;

// A warper class 
public class Core implements ICore{
	//---------------------------------------------------------------[ Store ]
	ConcurrentHashMap <String,String> mCache = new ConcurrentHashMap<String,String>();
	public void cache(String key, String value) {
		mCache.put(key, value);
	}
	
	public String cache(String data) {
		 String key = UUID.randomUUID().toString();
		 mCache.put(key, data);
		 return key;
	}
	
	// support encode
	public String getCached(String key) {
		return mCache.remove(key);
	}
	
	public void clearCache() {
		mCache = null;
		mCache = new ConcurrentHashMap<String,String>();
	}
	
	//---------------------------------------------------------------[ Singleton ]
	private Core() {}
	private static Core  sInstance = new Core();
	public static Core I() {
		return sInstance;
	}
	
	//---------------------------------------------------------------[ Members ]
	private String       mVersion  = "0.1";
	private EventManager      mEM  = EventManager.I();
	private TaskManager       mTM  = TaskManager.I();
	
	//---------------------------------------------------------------[ Task ]
	public void reinit() {}
	
	public void exec(Task task) {
		mTM.exec(task);
	}
	
	// TODO: schedule task
	public void cron(Task task) {
		
	}
	
	/**
	 * Schedule a loop task with specify interval (ms)
	 * @param task the task
	 * @param interval time interval (ms)
	 */
	public void cron(Task task, int interval) {
		
	}
	
	//---------------------------------------------------------------[ Event ]
	public void push(Event event) {
		mEM.push(event);
	}
	
	public void broadcast(Event event) {
		
	}
	
	//---------------------------------------------------------------[ EventListener ]
	public void addListener(String groupUri, EventListener listener) {
		mEM.addListener(groupUri, listener);
	}
	
	public void addListener(EventListener listener) {
		mEM.addListener(listener);
	}
	
	public void removeListener(String groupUri, EventListener listener){
		mEM.removeListener(groupUri, listener);
	}
	
	public void removeListener(EventListener listener) {
		mEM.removeListener(listener);
	}
	
	//---------------------------------------------------------------[ EventListener.Group ]
	public void addListenerGroup(String groupUri) {
		mEM.addListenerGroup(groupUri);
	}
	
	public ListenerGroup removeListenerGroup(String groupUri) {
		return mEM.removeListenerGroup(groupUri);
	}
	
	public boolean existedListenerGroup(String groupUri) {
		return mEM.existed(groupUri);
	}
	
	//---------------------------------------------------------------[ Connection ]
	//public void createTcpConnection();
	
	
	//---------------------------------------------------------------[ Debug ]
	public void dump() {
		
	}
	
	//---------------------------------------------------------------[ ETC ]
	public String getVersion() {
		return mVersion;
	}
	
	// TODO: add finall notify event ???
	public void clearAllListener(String uri) {
		System.err.println("CORE : CLEAR LG >>> " + uri);
		mEM.clearAllListener(uri);
	}
}

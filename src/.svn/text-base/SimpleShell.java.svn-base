

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import javax.naming.Context;

import org.parboiled.BaseActions;
import org.parboiled.Node;
import org.parboiled.Parboiled;
import org.parboiled.ReportingParseRunner;
import org.parboiled.support.InputBuffer;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;


import client.core.ConnectionManager;
import client.core.Core;
import client.core.EventManager;
import client.core.ImapSessionManager;
import client.core.TaskManager;
import client.core.model.Event;
import client.core.model.EventListener;
import client.core.model.Task;
import client.core.model.TimeStamp;
import client.core.model.TimeStamp.Tag;
import client.core.model.net.Connection;
import client.core.model.project.Project;
import client.core.test.EventFactory;
import client.core.test.TaskFactory;
import client.protocol.imap.ImapConnection;
import client.protocol.imap.ImapReceiver;
import client.protocol.imap.ImapSession;
import client.protocol.imap.bean.*;
import client.protocol.imap.event.TearDownIdle;
import client.protocol.imap.task.CmdSearch;
import client.protocol.imap.task.LoadMail;
import client.protocol.imap.task._TestIdleProject;


/**
 * Provide interactive shell for testing & debugging
 * @author amas
 *
 */
class SocketFactory {
	static public Socket createPlainSocket(String host, int port) throws IOException {
		Socket s = new Socket(host, port);
		return s;
	}
}

class EventPrinter implements EventListener {
	public EventPrinter(String name) {
		mName = name;
	}
	String mName = "";
	
	public void onEvent(Event event) {
		System.err.println("======================["+mName+"]");
		System.err.println(event);
		System.err.println("------------------------");
	}
	
}

class Receiver extends Thread {
	InputStream mInStream      = null;
	BufferedReader mReader     = null;
	
	public Receiver(InputStream istream) {
		mInStream = istream;
		mReader = new BufferedReader(new InputStreamReader(istream));
	}
	
	public void run() {
		int ch = 0;
		String line = "";
		while (true) {
			try {
				//ch = mInStream.read();
				line = mReader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Socket is closed");
				System.exit(0);
			}
		
			//System.out.write(ch);
			System.out.println(line);
		}
		
	}
}


public class SimpleShell {
	public static String PROMOTE = "$ ";
	private static SimpleShell sInstance = new  SimpleShell();
	
	public static SimpleShell I() {
		return sInstance;
	}
	
	public void startup() {
		InputStreamReader is = new InputStreamReader(System.in);
		BufferedReader    br = new BufferedReader(is);
		boolean         quit = false;
		
		printHello();
		
		while(!quit) {
			String line = "";
			System.out.print(PROMOTE);
			
			try {
				line = br.readLine()+"\r\n";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			quit = cmdParser(line);
		}
		
		System.out.println("OVER! 88");
	}
	
	private void printHello() {
		System.out.println("\n======================\n");
	}
	
	private boolean cmdParser(String text) {
		System.out.println("IN: " + text);
		text = text.trim();
		if("conn".equals(text)) {
			cmd_conn();
		} else if("test".equalsIgnoreCase(text)) {
			cmd_test();
		} else if("event".equalsIgnoreCase(text)){
			cmd_event();
		} else if("listen".equalsIgnoreCase(text)) {
			cmd_listen();
		} else if("quit".equalsIgnoreCase(text)) {
			return true;
		} else if("login".equalsIgnoreCase(text)) {
			cmd_login();
		} else if("pro".equalsIgnoreCase(text)) {
			cmd_pro();
		} else if("ppp".equalsIgnoreCase(text)) {
			cmd_project();
		} else if("imap".equalsIgnoreCase(text)) {
			cmd_imap();
		} else if("idle+".equalsIgnoreCase(text)) {
			cmd_idle();
	    } else {
			
			System.out.println("BAD COMMAND: " + text);
		}
		return false;
	}
	
	public void cmd_idle() {
		try {
			ImapSession session = new ImapSession(new URI("imap://patest1234:chimei@imap.aol.com:143/INBOX"));
			
			while(true) {
				_TestIdleProject testIdle = new _TestIdleProject(session);
				Core.I().exec(testIdle);
				Thread.sleep(5*60*1000);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static int sInc = 1;
	public void cmd_imap() {
		ImapConnection conn = ConnectionManager.I().createImapConnection("imap.aol.com",
					143, 
					ImapConnection.Security.Plain, 
					"patest000@aol.com", 
					"chimei");
		try {
			conn.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		InputStreamReader is = new InputStreamReader(System.in);
		BufferedReader    br = new BufferedReader(is);
		boolean         quit = false;
		
		printHello();
		
		while(!quit) {
			String line = "";
			System.out.print("# ");
			
			try {
				line = br.readLine();
				if(line.contains("sss")) {
					//Task t = new CmdSearch(conn, "SEARCH ALL");
					//Core.I().exec(t);
					continue;
				}
				conn.sendLine("X"+(sInc++)+" "+line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		

			quit = cmdParser(line);
		}
	}
	
	public void cmd_pro() {
		try {
			//_DownloadAll da1 = new _DownloadAll(new URI("imap://patest1234:chimei@imap.aol.com:143"));
			//Core.I().exec(da1);
			ImapSession session = new ImapSession(new URI("imap://patest1234:chimei@imap.aol.com:143/INBOX"));
			session.start();
			Thread.sleep(30*1000);
			//Event e = new TearDownIdle(session.getURI());
			//Core.I().push(e);
			
			//LoadMail lm = new LoadMail(session,0,null);
			//Core.I().exec(lm);
			//session.startIdle();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//_DownloadAll da2 = new _DownloadAll("imap.aol.com", 143, Security.Plain, "patest000", "chimei");
		//_DownloadAll da3 = new _DownloadAll("imap.gmail.com", 993, Security.SSL, "zhoujb.cn", "32767a#@&^&A");

	}
	
	public void cmd_login() {
		while(true) {
			try {
				ImapSessionManager.I().getSession(new URI("imap://patest1234:chimei@imap.aol.com:143/INBOX"));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void cmd_project() {
		Project p  = TaskFactory.I().createTestProject();
		Core.I().exec(p);
		Project p2 = TaskFactory.I().createTestProject();
		Core.I().exec(p2);
		Project p3 = TaskFactory.I().createTestProject();
		Core.I().exec(p3);
		new Thread() {
			public void run() {
				Project p4 = TaskFactory.I().createTestProject();
				Core.I().exec(p4);
			}
		}.start();

	}
	
	
	class TestListener implements EventListener {
		String mDesc = "";
		public TestListener(String desc) {
			mDesc = desc;
		}
		public void onEvent(Event event) {
			System.out.println(mDesc + " RECV : " + event);
		}	
	}
	
	void SLEEP(long s) {
		try {
			Thread.sleep(s);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cmd_listen() {
		EventManager em = EventManager.I();
		TestListener l1 = new TestListener("<LISTEN_01>");
		String lgname = "xxxx12345:///";
		TestListener l2 = new TestListener("l2");
		em.addListener(lgname,l1);
		em.addListener(lgname,l2);
//		for(Event e: EventFactory.I().createTestEvent(100)) {
//			em.push(e);
//		}
		SLEEP(1000);
		new Thread() {
			public void run() {
			for(int i=1; i<3; ++i) {
				//EventManager.I().addListener("xxxx12345:///", new EventPrinter("<"+i+">"));
			}
			}
		}.start();
		
		TaskManager tm  = TaskManager.I();
		TaskFactory tf  = TaskFactory.I();

		for(Task t : tf.createRandomTestTask(10, 1000, 1500)) {
			Core.I().exec(t);
		}
		
		new Thread() {
			public void run() {
				for(Task t : TaskFactory.I().createRandomTestTask(12, 1000, 1500)) {
					Core.I().exec(t);
				}
			};
		}.start();
		
		new Thread() {
			public void run() {
				for(Task t : TaskFactory.I().createRandomTestTask(15, 1000, 1500)) {
					Core.I().exec(t);
				}
			};
		}.start();
		
		new Thread() {
			public void run() {
				for(Task t : TaskFactory.I().createRandomTestTask(21, 1000, 1500)) {
					Core.I().exec(t);
				}
			};
		}.start();
		
		System.out.println("=====================================");
		System.out.println("= Listener Group Info               =");
		System.out.println("=====================================");
		em.ls();
	}
	
	public void cmd_event() {
		EventManager em = EventManager.I();
		new Thread() {
			public void run() {
				for(Event e: EventFactory.I().createTestEvent(190)) {
					EventManager.I().push(e);
				}	
			}
		}.start();
		
		new Thread() {
			public void run() {
				for(Event e: EventFactory.I().createTestEvent(1300)) {
					EventManager.I().push(e);
				}	
			}
		}.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(Event e: EventFactory.I().createTestEvent(109)) {
			em.push(e);
		}
	}
	
	public void cmd_test() {
		TaskManager tm  = TaskManager.I();
		TaskFactory tf  = TaskFactory.I();

		for(Task t : tf.createRandomTestTask(100, 1500, 3*1000)) {
			Core.I().exec(t);
		}
	}
	
	public void cmd_conn() {
		
	}
}

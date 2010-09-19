package client.protocol.imap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;

import client.core.Core;
import client.core.model.Event;
import client.core.model.EventListener;
import client.core.model.Notifiers;
import client.core.model.TimeStamp;
import client.core.model.TimeStamp.Tag;
import client.core.model.net.Receiver;
import client.core.model.net.ResponseParser;
import client.protocol.imap.bean.Greeting;
import client.protocol.imap.bean.RespCondAuth;
import client.protocol.imap.bean.RespCondState;
import client.protocol.imap.event.ImapConnectionClose;

/**
 *  All interactions transmitted by client and IMAP server are in the form of
 *  lines, that is, strings that end with a CRLF.  The protocol receiver
 *  of an IMAP4rev1 client or server is either reading a line, or is
 *  reading a sequence of octets with a known count followed by a line.
 */
public class ImapReceiver extends Receiver {
	private long mByteConter = 0;
	
	/**
	 * @param istream input stream
	 * @param eventTo where to push the event
	 */
	public ImapReceiver(InputStream istream, Notifiers eventTo) {
		mEventTo       = eventTo;
		mInStream      = istream;
		try {
			mReader        = new CRLFTerminatedReader(istream, "UTF-8");
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}		
		setName("ImapReceiver@"+hashCode());
	}
	
	//TODO: move to constructor 
	public Greeting getGreeting() throws IOException {
		if(mReader != null) {
			String line = ((CRLFTerminatedReader) mReader).readLine();
			System.err.print("SERVER GREETING : " + line);
			if(line != null) {				
				if(line.matches("\\* OK .*$")) {
					String[] sa = line.split(" ");
					return new Greeting(new RespCondAuth(sa[1], "I don't care!!!"));
				}
			}
		}
		return null;
	}
		
	@Override
	public void run() {
		boolean continueRead = false;
		StringBuffer lineBuffer = new StringBuffer();
		System.err.println(getName()+" start");
		while (!mStop) {
			if(!continueRead) {
				lineBuffer.delete(0, lineBuffer.length());				
			}
			
			try {
				String line = ((CRLFTerminatedReader) mReader).readLine();
				lineBuffer.append(line);
				if (lineBuffer.length() == 0 || line == null) {
					// TODO: send event?
					System.err.println("RECEIVER THREAD OVER");
					shutdown();
					pushEvent(new ImapConnectionClose("read end"));
					break;
				}
				
				int literalN = parseOctetsLen(lineBuffer.toString());
				if(literalN > 0) { 
					byte[] literal = new byte[literalN];
					continueRead = true;
					// TODO: comment out
					TimeStamp _t_ = new TimeStamp();
					_t_.touch(Tag.START_TIME);
					int result = ((CRLFTerminatedReader) mReader).readNext(literal, literalN);
					_t_.touch(Tag.END_TIME);
					//~~
					System.out.println("READER >>> coast "+_t_.getLifeTimeSec()+" sec / recv: " + result + " byte");
					if (literalN == result) {
						String uuid = Core.I().cache(new String(literal));
						lineBuffer.append(uuid);	
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+result + " byte ");
						System.out.println(new String(literal));
						System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+uuid);
						continue;						
					} else {
						System.err.println("............................incomplete literal:" + result + " skip: " + (literalN-result));
						mReader.skip((literalN-result));
						continue;
					}		
				}
				
				continueRead = false;
				TimeStamp _t_ = new TimeStamp();
				_t_.touch(Tag.START_TIME);
				ImapEvent resp = FastImapParser.parseLine(lineBuffer.toString());
				_t_.touch(Tag.END_TIME);
				//System.err.println("PARSER>>> coast "+_t_.getLifeTimeSec()+" sec");
					
				if(resp != null) {
					pushEvent(resp);
				}
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				shutdown();
				pushEvent(new ImapConnectionClose(e.toString()));
			} catch (IOException e) {
				shutdown();
				e.printStackTrace();
				pushEvent(new ImapConnectionClose(e.toString()));
			} catch (Exception e) {
				shutdown();
				e.printStackTrace();
				pushEvent(new ImapConnectionClose(e.toString()));
			}
		}
	}		
		
	/**
	 * e.g.:
	 * ...BODY[1]{1205}\r\n
	 * @param text
	 * @return n or failed -1
	 */
	private static int parseOctetsLen(String text) {
		if(text == null) // XXX: remove this check, since we can assure itis not empty
			return -1;
		if(text.matches("^.*\\{\\d\\d*\\}$")) {
			int s = text.lastIndexOf('{');
			int e = text.lastIndexOf('}');
			return Integer.parseInt(text.substring(s+1,e));
		}
		return -1;
	}
	
	public final void shutdown() {
		try {
			mInStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mStop = true;
	}
}

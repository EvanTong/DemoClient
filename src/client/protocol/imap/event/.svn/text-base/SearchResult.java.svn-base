package client.protocol.imap.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import client.core.model.Event;

public class SearchResult extends Event {
	boolean isUid  = false;
	Vector<Integer> mResult = new Vector<Integer>();
	
	public SearchResult() {
		
	}
	
	public void addResult(Vector<Integer> result) {
		for(Integer i: result) {
			mResult.add(i);
		}
	}

	public String join() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mResult.size() - 1; ++i) {
			sb.append(mResult.get(i)).append(',');
		}
		sb.append(mResult.lastElement());
		return sb.toString().trim();
	}
	
	public String joinExcept(HashSet<Integer> set) {
		if(set != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mResult.size(); ++i) {
				if(!set.contains(mResult.get(i))) {
					System.out.println(">>>>>>>>>>>" + mResult.get(i));
					sb.append(mResult.get(i)).append(",");
				}
			}
			return sb.substring(0, sb.length() - 1 > 0 ? sb.length() - 1 : 0); //FIXME: may out of bond
		} else {
			return join();
		}
	}
}

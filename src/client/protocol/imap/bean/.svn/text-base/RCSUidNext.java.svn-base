package client.protocol.imap.bean;

public class RCSUidNext extends RespCondState {
	private long mNumber = -1;
	public RCSUidNext(long uidValidity) {
		mNumber = uidValidity;
		mResult   = Result.OK;
	}
	
	public long getNumber() {
		return mNumber;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(').append(super.toString()).append(" ").append(getNumber()).append(')');
		return sb.toString();
	}
}
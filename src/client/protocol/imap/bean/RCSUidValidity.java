package client.protocol.imap.bean;

public class RCSUidValidity extends RespCondState{
	private long mNumber = -1;
	public RCSUidValidity(long uidValidity) {
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

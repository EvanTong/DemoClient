package client.core.model;

/**
 * Sometimes you want handle some event later, so this class can be used
 * as a event place holder, just push it.
 * @author amas
 */
public class HandleMeEvent extends Event {
	private String mException = null;
	
	public HandleMeEvent(Exception ex) {
		if(ex != null) {
			mException = ex.getLocalizedMessage();
		} else {
			Throwable t = new Throwable();
			mException = t.getStackTrace().toString();
		}
	}
	
	@Override
	public String toString() {
		return String.format("(%s EXCEPTION %s)", super.toString(), mException);
	}
}

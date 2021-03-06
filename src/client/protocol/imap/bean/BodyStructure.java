package client.protocol.imap.bean;

import java.util.HashMap;

import client.protocol.imap.ImapEvent;

public abstract class BodyStructure extends ImapEvent{ //TODO: it may not be a ImapResponse
	private String mMediaType             = "";
	private String mMediaSubType          = "";
	private BodyStructureFields mFields   = null;
	private int    mLevel                 = 0;
	private String mPartSpec              = null;
	
	/**
	 * Add part specifier
	 * See: RFC3501 Fetch Command
	 * @param specNum
	 */
	public void addPartSpec(int specNum) {
		if(specNum <= 0) {
			return;
		}
		
		if(mPartSpec == null || "".equals(mPartSpec)) {
			mPartSpec = ""+specNum;
		} else {
			mPartSpec = String.format("%s.%s", mPartSpec, specNum);
		}
	}
	
	/**
	 * Get part specifier
	 * See: RFC3501 Fetch Command
	 * @return
	 */
	public String getPartSpec() {
		return mPartSpec;
	}
	
	public void setPartSpec(String partSpec) {
		mPartSpec = partSpec;
	}
	
	public int getLevel() {
		return mLevel;
	}
	
	public void setLevel(int level) {
		mLevel = level;
	}

	/**
	 * Return body MIME type with lower case
	 * @return
	 */
	public String getMimeType() {
		return (mMediaType+"/"+mMediaSubType).toLowerCase();
	}
	
	/**
	 * Set MIME type
	 * @param mediaType
	 * @param mediaSubtype
	 */
	public void setMimeType(String mediaType, String mediaSubtype) {
		mMediaType    = mediaType;
		mMediaSubType = mediaSubtype;
	}
	
	/**
	 * Set media sub type
	 * @param mediaSubtype
	 */
	public void setMediaSubtype(String mediaSubtype) {
		mMediaSubType = mediaSubtype;
	}
	
	/**
	 * @param mediaType
	 */
	public void setMediaType(String mediaType) {
		mMediaType = mediaType;
	}
	
	/**
	 * Set fields
	 * @param fields
	 */
	public void setFields(BodyStructureFields fields) {
		mFields = fields;
	}
	
	public BodyStructureFields getFields() {
		return mFields;
	}
	
	@Override
	public String toString() {
		return fill(getLevel())+getMimeType()+" " + mFields + " ["+getPartSpec()+"]";
	}
	
	protected static String fill(int n) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<n; ++i) {
			sb.append("    ");
			if(i+1 == n) {
				sb.append("+-- ");
			}
		}
		return sb.toString();
	}
}

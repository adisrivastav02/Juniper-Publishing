package com.iig.gcp.publishing.service;

import java.util.Map;

public class MetadataChangeDto {

	
	private int feedId;
	private String srcTableName;
	private String tgtTableName;
	private Map<String,String> fields;
	
	
	public int getFeedId() {
		return feedId;
	}
	public void setFeedId(int feedId) {
		this.feedId = feedId;
	}
	public String getSrcTableName() {
		return srcTableName;
	}
	public void setSrcTableName(String srcTableName) {
		this.srcTableName = srcTableName;
	}
	public String getTgtTableName() {
		return tgtTableName;
	}
	public void setTgtTableName(String tgtTableName) {
		this.tgtTableName = tgtTableName;
	}
	public Map<String, String> getFields() {
		return fields;
	}
	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
	
}

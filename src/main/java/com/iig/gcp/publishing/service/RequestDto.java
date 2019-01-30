package com.iig.gcp.publishing.service;

import java.util.Map;

public class RequestDto {
	

	private Map<String, String> header;
	private Map<String, Map<String, String>> body;
	public Map<String, String> getHeader() {
		return header;
	}
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	public Map<String, Map<String, String>> getBody() {
		return body;
	}
	public void setBody(Map<String, Map<String, String>> body) {
		this.body = body;
	}
	@Override
	public String toString() {
		StringBuffer json =new StringBuffer();
		json.append("{");
		for(String k : header.keySet()) {
			json.append(k +":"+header.get(k) + "/n");
		}
		for(String k : body.keySet()) {
			json.append(k +": {");
			Map<String, String> temp = body.get(k);
			for(String key : temp.keySet()) {
				json.append(key +":" + temp.get(key) + "\n");
			}
			json.append("}");
		}
		json.append("}");
		return json.toString();
	}

 
 
}
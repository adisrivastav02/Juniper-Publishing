/**
 * 
 */
package com.iig.gcp.publishing.service;

import java.util.Map;

/**
 * @author sivakumar.r14
 *
 */
public class DataTypeMappingDto {

	
	String src_type;
	String tgt_type;
	Map<String, String> datatypeMapping;
	
	
	public String getSrc_type() {
		return src_type;
	}
	public void setSrc_type(String src_type) {
		this.src_type = src_type;
	}
	public String getTgt_type() {
		return tgt_type;
	}
	public void setTgt_type(String tgt_type) {
		this.tgt_type = tgt_type;
	}
	public Map<String, String> getDatatypeMapping() {
		return datatypeMapping;
	}
	public void setDatatypeMapping(Map<String, String> datatypeMapping) {
		this.datatypeMapping = datatypeMapping;
	}
}

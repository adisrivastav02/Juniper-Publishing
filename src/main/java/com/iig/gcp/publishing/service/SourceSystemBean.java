/**
 * 
 */
package com.iig.gcp.publishing.service;

import java.io.Serializable;

/**
 * @author adisr
 *
 */
public class SourceSystemBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int src_sys_id;
	private String src_unique_name;
	private String src_bkt ;    
	private String src_loc ;    
	private String tgt_prjt;    
	private String tgt_ds  ;    
	private String tgt_type  ;  
	private String src_type;
	private String sa_name;
	
	
	public int getSrc_sys_id() {
		return src_sys_id;
	}
	public void setSrc_sys_id(int src_sys_id) {
		this.src_sys_id = src_sys_id;
	}
	public String getSrc_unique_name() {
		return src_unique_name;
	}
	public void setSrc_unique_name(String src_unique_name) {
		this.src_unique_name = src_unique_name;
	}
	public String getSrc_bkt() {
		return src_bkt;
	}
	public void setSrc_bkt(String src_bkt) {
		this.src_bkt = src_bkt;
	}
	public String getSrc_loc() {
		return src_loc;
	}
	public void setSrc_loc(String src_loc) {
		this.src_loc = src_loc;
	}
	public String getTgt_prjt() {
		return tgt_prjt;
	}
	public void setTgt_prjt(String tgt_prjt) {
		this.tgt_prjt = tgt_prjt;
	}
	public String getTgt_ds() {
		return tgt_ds;
	}
	public void setTgt_ds(String tgt_ds) {
		this.tgt_ds = tgt_ds;
	}
	public String getTgt_type() {
		return tgt_type;
	}
	public void setTgt_type(String tgt_type) {
		this.tgt_type = tgt_type;
	}
	public String getSrc_type() {
		return src_type;
	}
	public void setSrc_type(String src_type) {
		this.src_type = src_type;
	}
	public String getSa_name() {
		return sa_name;
	}
	public void setSa_name(String sa_name) {
		this.sa_name = sa_name;
	}    



}

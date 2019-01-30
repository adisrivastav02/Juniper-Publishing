/**
 * 
 */
package com.iig.gcp.publishing.service;

import java.io.Serializable;

/**
 * @author adisr
 *
 */
public class SourceSystemFileBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int src_sys_id;  
	private String src_file_id;
	private String src_file_name;
	private String src_file_desc;
	private String src_file_type;
	private String src_file_delimiter;
	private String tgt_tbl_name;
	private String src_sch_loc;
	private String src_hdr_cnt;
	private String src_trl_cnt;
	private String src_cnt_start_idx;
	private String src_cnt_end_idx;
	private String data_class_catg;
	private String src_load_type;
	
	public int getSrc_sys_id() {
		return src_sys_id;
	}
	public void setSrc_sys_id(int src_sys_id) {
		this.src_sys_id = src_sys_id;
	}
	public String getSrc_file_id() {
		return src_file_id;
	}
	public void setSrc_file_id(String src_file_id) {
		this.src_file_id = src_file_id;
	}
	public String getSrc_file_name() {
		return src_file_name;
	}
	public void setSrc_file_name(String src_file_name) {
		this.src_file_name = src_file_name;
	}
	public String getSrc_file_desc() {
		return src_file_desc;
	}
	public void setSrc_file_desc(String src_file_desc) {
		this.src_file_desc = src_file_desc;
	}
	public String getSrc_file_type() {
		return src_file_type;
	}
	public void setSrc_file_type(String src_file_type) {
		this.src_file_type = src_file_type;
	}
	public String getSrc_file_delimiter() {
		return src_file_delimiter;
	}
	public void setSrc_file_delimiter(String src_file_delimiter) {
		this.src_file_delimiter = src_file_delimiter;
	}
	public String getTgt_tbl_name() {
		return tgt_tbl_name;
	}
	public void setTgt_tbl_name(String tgt_tbl_name) {
		this.tgt_tbl_name = tgt_tbl_name;
	}
	public String getSrc_sch_loc() {
		return src_sch_loc;
	}
	public void setSrc_sch_loc(String src_sch_loc) {
		this.src_sch_loc = src_sch_loc;
	}
	public String getSrc_hdr_cnt() {
		return src_hdr_cnt;
	}
	public void setSrc_hdr_cnt(String src_hdr_cnt) {
		this.src_hdr_cnt = src_hdr_cnt;
	}
	public String getSrc_trl_cnt() {
		return src_trl_cnt;
	}
	public void setSrc_trl_cnt(String src_trl_cnt) {
		this.src_trl_cnt = src_trl_cnt;
	}
	public String getSrc_cnt_start_idx() {
		return src_cnt_start_idx;
	}
	public void setSrc_cnt_start_idx(String src_cnt_start_idx) {
		this.src_cnt_start_idx = src_cnt_start_idx;
	}
	public String getSrc_cnt_end_idx() {
		return src_cnt_end_idx;
	}
	public void setSrc_cnt_end_idx(String src_cnt_end_idx) {
		this.src_cnt_end_idx = src_cnt_end_idx;
	}
	public String getData_class_catg() {
		return data_class_catg;
	}
	public void setData_class_catg(String data_class_catg) {
		this.data_class_catg = data_class_catg;
	}
	public String getSrc_load_type() {
		return src_load_type;
	}
	public void setSrc_load_type(String src_load_type) {
		this.src_load_type = src_load_type;
	}
}

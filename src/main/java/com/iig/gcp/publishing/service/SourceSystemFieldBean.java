/**
 * 
 */
package com.iig.gcp.publishing.service;

import java.io.Serializable;

/**
 * @author adisr
 *
 */
public class SourceSystemFieldBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int src_sys_id;
	private String src_file_id;
	private int fld_pos_num;
	private String src_sch_name;
	private String src_fld_name;
	private String src_fld_desc;
	private String src_fld_data_typ;
	private String trg_fld_data_typ;
	private String fld_null_flg;
	private String tgt_tbl_prtn_flg;
	private String pii_flg;
	private int fxd_fld_strt_idx;
	private int fxd_fld_end_idx;
	private int fxd_fld_len;
	private String pkey;
	
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
	public int getFld_pos_num() {
		return fld_pos_num;
	}
	public void setFld_pos_num(int fld_pos_num) {
		this.fld_pos_num = fld_pos_num;
	}
	public String getSrc_sch_name() {
		return src_sch_name;
	}
	public void setSrc_sch_name(String src_sch_name) {
		this.src_sch_name = src_sch_name;
	}
	public String getSrc_fld_name() {
		return src_fld_name;
	}
	public void setSrc_fld_name(String src_fld_name) {
		this.src_fld_name = src_fld_name;
	}
	public String getSrc_fld_desc() {
		return src_fld_desc;
	}
	public void setSrc_fld_desc(String src_fld_desc) {
		this.src_fld_desc = src_fld_desc;
	}
	public String getSrc_fld_data_typ() {
		return src_fld_data_typ;
	}
	public void setSrc_fld_data_typ(String src_fld_data_typ) {
		this.src_fld_data_typ = src_fld_data_typ;
	}
	public String getTrg_fld_data_typ() {
		return trg_fld_data_typ;
	}
	public void setTrg_fld_data_typ(String trg_fld_data_typ) {
		this.trg_fld_data_typ = trg_fld_data_typ;
	}
	public String getFld_null_flg() {
		return fld_null_flg;
	}
	public void setFld_null_flg(String fld_null_flg) {
		this.fld_null_flg = fld_null_flg;
	}
	public String getTgt_tbl_prtn_flg() {
		return tgt_tbl_prtn_flg;
	}
	public void setTgt_tbl_prtn_flg(String tgt_tbl_prtn_flg) {
		this.tgt_tbl_prtn_flg = tgt_tbl_prtn_flg;
	}
	public String getPii_flg() {
		return pii_flg;
	}
	public void setPii_flg(String pii_flg) {
		this.pii_flg = pii_flg;
	}
	public int getFxd_fld_strt_idx() {
		return fxd_fld_strt_idx;
	}
	public void setFxd_fld_strt_idx(int fxd_fld_strt_idx) {
		this.fxd_fld_strt_idx = fxd_fld_strt_idx;
	}
	public int getFxd_fld_end_idx() {
		return fxd_fld_end_idx;
	}
	public void setFxd_fld_end_idx(int fxd_fld_end_idx) {
		this.fxd_fld_end_idx = fxd_fld_end_idx;
	}
	public int getFxd_fld_len() {
		return fxd_fld_len;
	}
	public void setFxd_fld_len(int fxd_fld_len) {
		this.fxd_fld_len = fxd_fld_len;
	}
	public String getPkey() {
		return pkey;
	}
	public void setPkey(String pkey) {
		this.pkey = pkey;
	}
	
	

}

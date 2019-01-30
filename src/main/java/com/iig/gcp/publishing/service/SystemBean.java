package com.iig.gcp.publishing.service;

import java.io.Serializable;
import java.util.Date;

public class SystemBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String src_eim_id;
	private String cty_cde;
	private String src_sys_name;
	private String src_sys_desc;
	private Date created_Date;
	private Date updated_Date;
	private String created_by;
	private String updated_by;
	private String metadata_status;
	private String src_type;
	private String tgt_type;
	private String src_bkt;
	private String src_loc;
	private String tgt_prjt;
	private String tgt_ds;
	private String gcp_name;
	private String src_acct_name;
	private String extr_feed_id;

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

	public String getMetadata_status() {
		return metadata_status;
	}

	public void setMetadata_status(String metadata_status) {
		this.metadata_status = metadata_status;
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

	public String getGcp_name() {
		return gcp_name;
	}

	public void setGcp_name(String gcp_name) {
		this.gcp_name = gcp_name;
	}

	public String getSrc_acct_name() {
		return src_acct_name;
	}

	public void setSrc_acct_name(String src_acct_name) {
		this.src_acct_name = src_acct_name;
	}

	public String getExtr_feed_id() {
		return extr_feed_id;
	}

	public void setExtr_feed_id(String extr_feed_id) {
		this.extr_feed_id = extr_feed_id;
	}

	/*
	 * public String getSrc_Sys_id() { return this.src_sys_id; } public void
	 * setSrc_Sys_id(String src_sys_id) { this.src_sys_id = src_sys_id; }
	 */
	public String getSrc_Eim_id() {
		return src_eim_id;
	}

	public void setSrc_Eim_id(String src_eim_id) {
		this.src_eim_id = src_eim_id;
	}

	public String getCty_cde() {
		return cty_cde;
	}

	public void setCty_cde(String cty_cde) {
		this.cty_cde = cty_cde;
	}

	public String getSrc_sys_name() {
		return src_sys_name;
	}

	public void setSrc_sys_name(String src_sys_name) {
		this.src_sys_name = src_sys_name;
	}

	public String getSrc_sys_desc() {
		return src_sys_desc;
	}

	public void setSrc_sys_desc(String src_sys_desc) {
		this.src_sys_desc = src_sys_desc;
	}

	public Date getCreated_Date() {
		return created_Date;
	}

	public void setCreated_Date(Date created_Date) {
		this.created_Date = created_Date;
	}

	public Date getUpdated_Date() {
		return updated_Date;
	}

	public void setUpdated_Date(Date updated_Date) {
		this.updated_Date = updated_Date;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}
}
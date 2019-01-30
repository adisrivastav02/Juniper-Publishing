package com.iig.gcp.publishing.service;

public class ExistingPubBean {

	private String project_name;
	private String service_acc;
	private String target_dataset;
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getService_acc() {
		return service_acc;
	}
	public void setService_acc(String service_acc) {
		this.service_acc = service_acc;
	}
	public String getTarget_dataset() {
		return target_dataset;
	}
	public void setTarget_dataset(String target_dataset) {
		this.target_dataset = target_dataset;
	}
	
	
}

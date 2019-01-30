/**
 * 
 */
package com.iig.gcp.publishing.service;

/**
 * @author sivakumar.r14
 *
 */
public class FilePartitionDetailsDto {

	String file_name;
	String publishing_type;
	String partition_key;
	
	
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getPublishing_type() {
		return publishing_type;
	}
	public void setPublishing_type(String publishing_type) {
		this.publishing_type = publishing_type;
	}
	public String getPartition_key() {
		return partition_key;
	}
	public void setPartition_key(String partition_key) {
		this.partition_key = partition_key;
	}
	
	
}

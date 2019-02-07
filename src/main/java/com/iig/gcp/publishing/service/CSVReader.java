package com.iig.gcp.publishing.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.iig.gcp.utils.ConnectionUtils;
import com.iig.gcp.utils.DBUtils;

@Component
public class CSVReader {

	
	@Autowired
	 private ConnectionUtils connectionUtils;
	
	public final String delimiter=",";
	public  int readSystem( String userName, String tgt_type, String tgt_dataset,  boolean extracted_Source, String service_account_name, String google_proj_name, String ext_feed_id,String pub_feed_name, int project_sequence)
			throws Exception {

		System.out.println("Inside read System----");
		ArrayList<SystemBean> systemBeans = new ArrayList<SystemBean>();
		/*ArrayList<SystemBean> systemBeans = new ArrayList<SystemBean>();
		int lineNumber = 0;
		Storage storage = StorageOptions.getDefaultInstance().getService(); //cloudUtil.getStorageService(service_account_name);
		byte[] content = storage.readAllBytes(blobId);
		InputStream myInputStream = new ByteArrayInputStream(content);
		BufferedReader br = new BufferedReader(new InputStreamReader(myInputStream, StandardCharsets.UTF_8));
		String line = "";
		String[] tempArr;
		Date timeStamp = new Date();
		while ((line = br.readLine()) != null) {

			if (lineNumber == 0) {
				lineNumber++;
				continue;
			}
			lineNumber++;
			SystemBean sysBean = new SystemBean();
			tempArr = line.split(delimiter, -1);
			// Parse .csv file and set the values in object
			//sysBean.setSrc_sys_id(tempArr[0]);
			sysBean.setSrc_Eim_id("");
			
			sysBean.setGcp_name(google_proj_name);
			sysBean.setSrc_acct_name(service_account_name);
			sysBean.setExtr_feed_id(ext_feed_id);
			
			
			sysBean.setCty_cde(tempArr[1]);
			sysBean.setSrc_sys_name(pub_feed_name);
			sysBean.setSrc_sys_desc(pub_feed_name);
			sysBean.setSrc_bkt(tempArr[4]);
			sysBean.setSrc_loc(tempArr[5]);
			sysBean.setTgt_prjt(tempArr[6]);
			sysBean.setTgt_ds(tgt_dataset);
			sysBean.setTgt_type(tgt_type);
			sysBean.setMetadata_status(tempArr[8]);
			sysBean.setCreated_Date(timeStamp);
			sysBean.setUpdated_Date(null);
			sysBean.setCreated_by(username);
			sysBean.setUpdated_by("");
			if (tempArr[0].toLowerCase().contains("ora")) {
				sysBean.setSrc_type("Oracle");
			}
			if (tempArr[0].toLowerCase().contains("sql")) {
				sysBean.setSrc_type("SQLServer");
			}

			System.out.println(sysBean.getSrc_sys_name() + " " + sysBean.getSrc_Eim_id() + " " + sysBean.getCty_cde()
					+ " " + sysBean.getSrc_sys_name() + " " + sysBean.getSrc_sys_desc() + " " + " "
					+ sysBean.getCreated_Date() + " " + sysBean.getUpdated_Date() + " " + sysBean.getCreated_by() + " "
					+ sysBean.getUpdated_by());
			System.out.println(sysBean.getSrc_type() + " " + sysBean.getTgt_type() + " " + sysBean.getTgt_ds());
			// Add system object instance to Array list
			systemBeans.add(sysBean);
		}*/

		//Connection conn = ConnectionUtils.connectToMySql(mysql_ip, mysql_port, db_name, user, password);
		Connection conn =  connectionUtils.getConnection();
		System.out.println("Getting conn----" + conn);

		//int pub_feed_id=1;
		// change code 15-01-2019
		int pub_feed_id = DBUtils.insertSystemTargetValue(conn, systemBeans, extracted_Source,tgt_type,tgt_dataset,service_account_name,google_proj_name,ext_feed_id,pub_feed_name , project_sequence, userName);
		
		// ConnectionUtils.closeQuietly(conn);
		conn.close();
		//br.close();

		return pub_feed_id;
	}
	
	public  void readFile( String userName, String service_account_name,  int pub_feed_id , int project_sequence, String ext_feed_id)
			throws Exception {
		System.out.println("Inside read File----");
		/*ArrayList<FileBean> fileBeans = new ArrayList<FileBean>();
		int lineNumber = 0;
		Storage storage = StorageOptions.getDefaultInstance().getService();//cloudUtil.getStorageService(service_account_name);
		byte[] content = storage.readAllBytes(blobId);
		InputStream myInputStream = new ByteArrayInputStream(content);
		BufferedReader br = new BufferedReader(new InputStreamReader(myInputStream, StandardCharsets.UTF_8));
		String line = "";
		String[] tempArr;
		Date timeStamp = new Date();
		while ((line = br.readLine()) != null) {

			if (lineNumber == 0) {
				lineNumber++;
				continue;
			}
			lineNumber++;
			FileBean fileBean = new FileBean();
			tempArr = line.split(delimiter, -1);

			// Parse .csv file and set the values in object
			fileBean.setSrc_unique_name(tempArr[0]);
			fileBean.setSrc_file_id(tempArr[1]);
			fileBean.setSrc_file_name(tempArr[2]);
			fileBean.setSrc_file_desc(tempArr[3]);
			fileBean.setSrc_file_type(tempArr[4]);
			fileBean.setSrc_file_delimiter(tempArr[5]);
			fileBean.setTgt_tbl_name(tempArr[6]);
			fileBean.setSrc_sch_loc(tempArr[7]);
			fileBean.setSrc_hdr_cnt(tempArr[8]);
			fileBean.setSrc_trl_cnt(tempArr[9]);
			fileBean.setSrc_cnt_start_idx(tempArr[10]);
			fileBean.setSrc_cnt_end_idx(tempArr[11]);
			fileBean.setData_class_catg(tempArr[12]);
			fileBean.setSrc_load_type(tempArr[13]);
			fileBean.setCrtd_dt(timeStamp);
			fileBean.setUptd_dt(null);
			fileBean.setCrtd_by(userName);
			fileBean.setUptd_by("");

			// Add File object instance to Array list
			fileBeans.add(fileBean);
			// System.out.println(fileBean.getsrc);
		}*/

		//Connection conn = ConnectionUtils.connectToMySql(mysql_ip, mysql_port, db_name, user, password);
		Connection conn =   connectionUtils.getConnection();
		
		System.out.println("Getting conn----" + conn);

		DBUtils.insertFileValue(conn,  pub_feed_id, project_sequence, ext_feed_id, userName);
		conn.close();
		//ConnectionUtils.closeQuietly(conn);
		//br.close();

	}
	public void readField( String userName, boolean extracted_Source,String service_account_name, int pub_feed_id, String ext_feed_id)
			throws Exception {
		//ArrayList<FieldBean> fieldBeans = getFieldDetails (blobId,userName,service_account_name);
		Map<String,String> datatypeMapiing = new HashMap<String,String>();
		//Connection conn = ConnectionUtils.connectToMySql(mysql_ip, mysql_port, db_name, user, password);
		Connection conn =  connectionUtils.getConnection();
		
		System.out.println("Getting conn----" + conn);
		/*if(extracted_Source) {
			String tgt_type="BigQuery"; 
			String src_type = DBUtils.getSourceType(pub_feed_id, conn);
			datatypeMapiing = retrieveDatatypeMapping(src_type,tgt_type, conn);
			for (FieldBean fieldBean : fieldBeans) {
				fieldBean.setTrg_fld_data_typ(datatypeMapiing.get(fieldBean.getSrc_fld_data_typ().trim().toUpperCase()));
			} 
		}*/
		DBUtils.insertFieldValue(conn, pub_feed_id, ext_feed_id, userName);
		conn.close();
		

	}
	
	
public ArrayList<FieldBean> getFieldDetails(BlobId blobId, String userName,String service_account_name) throws FileNotFoundException, IOException {
		
		System.out.println("Inside read Field----");
		ArrayList<FieldBean> fieldBeans = new ArrayList<FieldBean>();
		int lineNumber = 0;
		Storage storage = StorageOptions.getDefaultInstance().getService();// cloudUtil.getStorageService(service_account_name);
		byte[] content = storage.readAllBytes(blobId);
		InputStream myInputStream = new ByteArrayInputStream(content);
		BufferedReader br = new BufferedReader(new InputStreamReader(myInputStream, StandardCharsets.UTF_8));
		String line = "";
		String[] tempArr;
		Date timeStamp = new Date();
	
		while ((line = br.readLine()) != null) {

			if (lineNumber == 0) {
				lineNumber++;
				continue;
			}
			lineNumber++;
			FieldBean fieldBean = new FieldBean();
			tempArr = line.split(delimiter, -1);
			// Parse .csv file and set the values in object
			// System.out.println("Integer.parseInt(tempArr[11])"+
			// Integer.parseInt(tempArr[11]));

			fieldBean.setSrc_unique_name(tempArr[0]);
			fieldBean.setSrc_file_id(tempArr[1]);
			fieldBean.setFld_pos_num(Integer.parseInt(tempArr[2]));
			fieldBean.setSrc_sch_name(tempArr[3]);
			fieldBean.setSrc_fld_name(tempArr[4]);
			fieldBean.setSrc_fld_desc(tempArr[5]);
			fieldBean.setSrc_fld_data_typ(tempArr[6]);
			fieldBean.setTrg_fld_data_typ(tempArr[7]);
			fieldBean.setFld_null_flg(tempArr[8]);
			fieldBean.setTgt_tbl_prtn_flg(tempArr[9]);
			fieldBean.setPii_flg(tempArr[10]);
			if (tempArr[11].equals("")) {

				System.out.println("here");
				fieldBean.setFxd_fld_strt_idx(0);

			} else {
				fieldBean.setFxd_fld_strt_idx(Integer.parseInt(tempArr[11]));
			}
			if (tempArr[12].equals("")) {
				fieldBean.setFxd_fld_end_idx(0);
			} else {
				fieldBean.setFxd_fld_end_idx(Integer.parseInt(tempArr[12]));
			}
			if (tempArr[13].equals("")) {
				fieldBean.setFxd_fld_len(0);
			} else {
				fieldBean.setFxd_fld_len(Integer.parseInt(tempArr[13]));
			}
			
			 /* if(!tempArr[13]){
			  
			  
			  }else{ System.out.println("hanji lo print le le");
			  fieldBean.setFxd_fld_len(null); }*/
			 

			fieldBean.setCrtd_dt(timeStamp);
			fieldBean.setUptd_dt(null);
			fieldBean.setCrtd_by(userName);
			fieldBean.setUptd_by(userName);

			// Add File object instance to Array list
			fieldBeans.add(fieldBean);
		}
		br.close();
		return fieldBeans;
	}
	
	private static Map<String,String> retrieveDatatypeMapping(String src_type, String tgt_type, Connection conn) throws SQLException {
		
		return DBUtils.fetchDataTypeMapping(src_type,tgt_type,conn);
	}
}

package com.iig.gcp.publishing.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.JobException;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.iig.gcp.constants.MySqlConstants;
import com.iig.gcp.utils.BigQueryUtils;
import com.iig.gcp.utils.ConnectionUtils;
import com.iig.gcp.utils.DBUtils;

@Service
public class PublishingServiceImpl implements PublishingService {

	@Autowired
	private ConnectionUtils connectionUtils;

	@Autowired
	private IDataPublishingRepository dataPublishingRepository;

	@Override
	public String invokeRest(String json, String url) throws UnsupportedOperationException, Exception {
		String resp = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(MySqlConstants.PUBLISHING_COMPUTE_URL + url);
		System.out.println("url rest: " + MySqlConstants.PUBLISHING_COMPUTE_URL + url);
		postRequest.setHeader("Content-Type", "application/json");
		StringEntity input = new StringEntity(json);
		postRequest.setEntity(input);
		HttpResponse response = httpClient.execute(postRequest);
		String response_string = EntityUtils.toString(response.getEntity(), "UTF-8");
		if (response.getStatusLine().getStatusCode() != 200) {
			resp = "Error" + response_string;
			throw new Exception("Error" + response_string);
		} else {
			System.out.println(response_string);
			resp = response_string;
		}
		return resp;
	}

	@Override
	public String invokePythonRest(String json, String url) throws UnsupportedOperationException, Exception {
		String resp = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(MySqlConstants.PUBLISHING_PYTHON_REST_URL + url);
		System.out.println("url rest: " + MySqlConstants.PUBLISHING_PYTHON_REST_URL + url);
		postRequest.setHeader("Content-Type", "application/json");
		StringEntity input = new StringEntity(json);
		postRequest.setEntity(input);
		HttpResponse response = httpClient.execute(postRequest);
		String response_string = EntityUtils.toString(response.getEntity(), "UTF-8");
		if (response.getStatusLine().getStatusCode() != 200) {
			resp = "Error" + response_string;
			throw new Exception("Error" + response_string);
		} else {
			System.out.println(response_string);
			resp = response_string;
		}
		return resp;
	}

	public Map<String, String> getSysIds(String project) throws Exception {
		Connection conn = null;
		try {
			Map<String, String> src_map = new HashMap<String, String>();
			conn = connectionUtils.getConnection();
			// String s_id="SELECT DISTINCT FEED_ID,FEED_UNIQUE_NAME FROM
			// JUNIPER_EXT_NIFI_STATUS s,JUNIPER_PROJECT_MASTER p WHERE
			// p.PROJECT_SEQUENCE=s.PROJECT_SEQUENCE AND UPPER(s.STATUS) = 'SUCCESS' AND
			// p.project_id='"+project+"'";

			/*String s_id = "select FEED_SEQUENCE,FEED_UNIQUE_NAME from (SELECT * FROM JUNIPER_EXT_FEED_MASTER t OUTER APPLY (SELECT TRIM(target_type) AS target_type            FROM JSON_TABLE(REPLACE(JSON_ARRAY(t.target), ',', '\",\"'),           '$[*]' COLUMNS (target_type VARCHAR2(4000) PATH '$'))) s) f, JUNIPER_EXT_TARGET_CONN_MASTER tm  , JUNIPER_PROJECT_MASTER pm             where    pm.PROJECT_SEQUENCE= f.PROJECT_SEQUENCE and pm.PROJECT_ID='"
					+ project + "' and  f.target_type = tm.TARGET_UNIQUE_NAME    and   UPPER(tm.TARGET_TYPE)='GCS'";
			*/
			
			String s_id = "select distinct f.FEED_SEQUENCE,f.FEED_UNIQUE_NAME  from " +
			" JUNIPER_EXT_FEED_MASTER f, JUNIPER_EXT_FEED_SRC_TGT_LINK t1, JUNIPER_EXT_TARGET_CONN_MASTER tm, JUNIPER_PROJECT_MASTER pm  where f.FEED_SEQUENCE = t1.FEED_SEQUENCE and t1.TARGET_SEQUENCE = tm.TARGET_CONN_SEQUENCE  and pm.PROJECT_SEQUENCE= f.PROJECT_SEQUENCE  and UPPER(tm.TARGET_TYPE)='GCS' and pm.PROJECT_ID='" + project + "' ";
			

			/*String s_id = "SELECT\r\n" + 
					"    f.feed_sequence,\r\n" + 
					"    f.feed_unique_name\r\n" + 
					"FROM\r\n" + 
					"    juniper_project_master pm inner join\r\n" + 
					"    juniper_ext_feed_master f on     pm.project_sequence = f.project_sequence \r\n" + 
					"    inner join JUNIPER_EXT_FEED_SRC_TGT_LINK fl on f.feed_sequence=fl.feed_sequence\r\n" + 
					"    inner join juniper_ext_target_conn_master tm on fl.target_sequence=tm.target_conn_sequence\r\n" + 
					"WHERE\r\n" + 
					" p.project_id= '" + project+ "'" +
					"    AND upper(tm.target_type) = 'GCS'";*/

			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				src_map.put(rs.getString(1), rs.getString(2));
			}
			// ConnectionUtils.closeQuietly(conn);
			
			return src_map;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	public Map<String, String> getExSysIds() throws Exception {
		Connection conn = null;
		try {
			Map<String, String> src_map = new HashMap<String, String>();
			conn = connectionUtils.getConnection();
			String s_id = "select \r\n" + "m.src_sys_id,\r\n" + "m.src_unique_name, \r\n"
					+ "case when e.src_sys_id is null then 'Published' else 'Extracted' end as type \r\n"
					+ "from source_system_master m \r\n" + "left join extraction_master e \r\n"
					+ "on (m.src_sys_id = e.src_sys_id);";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				src_map.put(rs.getString(1) + ":" + rs.getString(3) + ":" + rs.getString(2), rs.getString(2));
			}
			
			return src_map;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	/*public Map<String, String> getMDSysList() throws Exception {
		Connection conn = null;
		try {
			Map<String, String> src_map = new HashMap<String, String>();
			conn = connectionUtils.getConnection();
			String s_id = "SELECT src_sys_id,src_unique_name FROM cdg.source_system_master";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				src_map.put(rs.getString(1), rs.getString(2));
			}
			// connectionUtils.closeQuietly(conn);
			return src_map;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}
*/
	public ArrayList<String> getRunIds(Integer src_id) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		ArrayList<String> run_list = new ArrayList<String>();
		try {
			conn = connectionUtils.getConnection();
			String s_id = "select run_id from cdg.extraction_status where src_sys_id=" + src_id
					+ " and status='Extracted'";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				run_list.add(rs.getString(1));
			}
			// connectionUtils.closeQuietly(conn);
			return run_list;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	@Override
	public Map<String, String> getRunIdsWithDate(Integer src_id, String dateRangeText, String is_new,
			String pub_feed_id) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		Map<String, String> run_list = new TreeMap<String, String>();
		try {
			conn = connectionUtils.getConnection();
			String s_id = null;
			String start_date = "";
			String end_date = "";
			SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			SimpleDateFormat tf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
			if (is_new.equalsIgnoreCase("YES")) {
				if (dateRangeText != null && !dateRangeText.equals("")) {

					if (dateRangeText.contains("-")) {
						String[] dates = dateRangeText.split("-");
						String first_date = dates[0].trim();
						String last_date = dates[1].trim();
						start_date = tf.format(sf.parse(first_date));
						end_date = tf.format(sf.parse(last_date));

					} else {
						String date = dateRangeText.trim();
						start_date = tf.format(sf.parse(date));
						end_date = start_date;
					}
					s_id = "select run_id , extracted_date from JUNIPER_EXT_NIFI_STATUS where FEED_ID="
							+ src_id + " and UPPER(status)='SUCCESS' " + " AND extracted_date>=" + "'" + start_date
							+ "'" + " AND extracted_date <=" + "'" + end_date + "' order by run_id asc";
				} else {
					s_id = "select run_id , extracted_date from JUNIPER_EXT_NIFI_STATUS where FEED_ID="
							+ src_id + " and UPPER(status)='SUCCESS' order by run_id asc";
				}
			} else {
				// s_id="select run_id , extracted_date from
				// JUNIPER_EXT_NIFI_STATUS where FEED_ID="+src_id+" and
				// UPPER(status)='SUCCESS' order by run_id asc";

				//get the pub feed sequence based on google project name and target dataset
				
				s_id = "select f1.RUN_ID, f1.EXTRACTED_DATE  from JUNIPER_EXT_NIFI_STATUS f1,(select RUN_ID  from JUNIPER_EXT_NIFI_STATUS m where m.FEED_ID="
						+ src_id
						+ "   minus select RUN_ID  from JUNIPER_PUB_FEED_STATUS n , (select PUB_FEED_SEQUENCE from JUNIPER_PUB_FEED_DTLS where GCP_PROJ_NAME='"+pub_feed_id.split(":")[1]+"' and FEED_TGT_DS='"+pub_feed_id.split(":")[0]+"') n1 where UPPER(n.status) = 'SUCCESS' and n.PUB_FEED_SEQUENCE= n1.PUB_FEED_SEQUENCE) f2  where f1.RUN_ID = f2.RUN_ID and f1.FEED_ID=" + src_id;

			}

			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				run_list.put(rs.getString(1), rs.getString(2));
			}
			// connectionUtils.closeQuietly(conn);
			return run_list;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	public ArrayList<String> getMDFileList(Integer src_id) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<String> file_list = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			String s_id = "select src_file_id from cdg.pub_src_file_dtls where src_sys_id=" + src_id;
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				file_list.add(rs.getString(1));
			}
			// connectionUtils.closeQuietly(conn);
			return file_list;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	public SourceSystemBean getSourceSystemMetadata(int src_sys_id) throws Exception {
		SourceSystemBean system = new SourceSystemBean();
		Connection conn = null;
		try {

			conn = connectionUtils.getConnection();
			String s_id = "select m.PUB_FEED_SEQUENCE, m.PUB_FEED_NAME ,m.FEED_SRC_BKT,m.FEED_SRC_LOC,m.FEED_TGT_PRJT,m.FEED_TGT_DS,m.FEED_TGT_TYPE,m.FEED_SRC_TYPE , m.SERVICE_ACC_NAME \r\n"
					+ "from JUNIPER_PUB_FEED_DTLS m where m.PUB_FEED_SEQUENCE=" + src_sys_id;
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				system.setSrc_sys_id(rs.getInt(1));
				system.setSrc_unique_name(rs.getString(2));
				system.setSrc_bkt(rs.getString(3));
				system.setSrc_loc(rs.getString(4));
				system.setTgt_prjt(rs.getString(5));
				system.setTgt_ds(rs.getString(6));
				system.setTgt_type(rs.getString(7));
				system.setSrc_type(rs.getString(8));
				system.setSa_name(rs.getString(9));
			}
			// connectionUtils.closeQuietly(conn);
			return system;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	public SourceSystemFileBean getSourceFileMetadata(int src_sys_id, String file_id) throws Exception {
		SourceSystemFileBean file = new SourceSystemFileBean();
		Connection conn = null;
		try {

			conn = connectionUtils.getConnection();
			String s_id = "select FEED_TABLE_name ,FEED_TABLE_desc ,FEED_TABLE_type ,FEED_TABLE_delimiter ,tgt_tbl_name ,\r\n"
					+ "				FEED_sch_loc ,FEED_hdr_cnt ,FEED_trl_cnt ,FEED_cnt_start_idx ,FEED_cnt_end_idx ,data_class_catg ,FEED_load_type\r\n"
					+ "				from JUNIPER_PUB_FEED_FILE_DTLS where PUB_FEED_SEQUENCE=" + src_sys_id
					+ " and FEED_TABLE_id='" + file_id + "'";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				file.setSrc_file_name(rs.getString(1));
				file.setSrc_file_desc(rs.getString(2));
				file.setSrc_file_type(rs.getString(3));
				file.setSrc_file_delimiter(rs.getString(4));
				file.setTgt_tbl_name(rs.getString(5));
				file.setSrc_sch_loc(rs.getString(6));
				file.setSrc_hdr_cnt(rs.getString(7));
				file.setSrc_trl_cnt(rs.getString(8));
				file.setSrc_cnt_start_idx(rs.getString(9));
				file.setSrc_cnt_end_idx(rs.getString(10));
				file.setData_class_catg(rs.getString(11));
				if (rs.getString(12).equalsIgnoreCase("FULL")) {
					file.setSrc_load_type("Full Load");
				} else if (rs.getString(12).equalsIgnoreCase("INCR")) {
					file.setSrc_load_type("Incremental Load");
				}

			}
			// connectionUtils.closeQuietly(conn);
			return file;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	@Override
	public List<SourceSystemFileBean> getSourceFiles(int src_sys_id) throws Exception {
		List<SourceSystemFileBean> files = new ArrayList<SourceSystemFileBean>();
		SourceSystemFileBean file = new SourceSystemFileBean();
		Connection conn = null;
		try {

			conn = connectionUtils.getConnection();
			String s_id = "select Upper(FEED_TABLE_name) as FEED_TABLE_name ,FEED_TABLE_desc ,FEED_TABLE_type ,FEED_TABLE_delimiter ,tgt_tbl_name ,\r\n"
					+ "				FEED_sch_loc ,FEED_hdr_cnt ,FEED_trl_cnt ,FEED_cnt_start_idx ,FEED_cnt_end_idx ,data_class_catg ,FEED_load_type \r\n"
					+ "				from JUNIPER_PUB_FEED_FILE_DTLS  where PUB_FEED_SEQUENCE="
					+ src_sys_id;
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				file = new SourceSystemFileBean();
				file.setSrc_file_name(rs.getString(1));
				file.setSrc_file_desc(rs.getString(2));
				file.setSrc_file_type(rs.getString(3));
				file.setSrc_file_delimiter(rs.getString(4));
				file.setTgt_tbl_name(rs.getString(5));
				file.setSrc_sch_loc(rs.getString(6));
				file.setSrc_hdr_cnt(rs.getString(7));
				file.setSrc_trl_cnt(rs.getString(8));
				file.setSrc_cnt_start_idx(rs.getString(9));
				file.setSrc_cnt_end_idx(rs.getString(10));
				file.setData_class_catg(rs.getString(11));
				if (rs.getString(12).equalsIgnoreCase("FULL")) {
					file.setSrc_load_type("Full Load");
				} else if (rs.getString(12).equalsIgnoreCase("INCR")) {
					file.setSrc_load_type("Incremental Load");
				}
				files.add(file);
			}
			// connectionUtils.closeQuietly(conn);
			conn.close();
			return files;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	public List<SourceSystemFieldBean> getSourceFieldMetadata(int src_sys_id, String file_id) throws Exception {
		List<SourceSystemFieldBean> fields = new ArrayList<SourceSystemFieldBean>();
		SourceSystemFieldBean fieldBean = null;
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			String s_id = "select FEED_fld_pos_num,FEED_sch_name,FEED_fld_name,FEED_fld_desc,FEED_fld_data_typ,\r\n"
					+ "				trg_fld_data_typ,fld_null_flg,tgt_tbl_prtn_flg,pii_flg,fxd_fld_strt_idx,\r\n"
					+ "			fxd_fld_end_idx,fxd_fld_len,pkey from JUNIPER_PUB_FEED_FLD_DTLS \r\n"
					+ "			 where PUB_FEED_SEQUENCE=" + src_sys_id + " and FEED_TABLE_id='" + file_id + "'";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				fieldBean = new SourceSystemFieldBean();
				fieldBean.setFld_pos_num(rs.getInt(1));
				fieldBean.setSrc_sch_name(rs.getString(2));
				fieldBean.setSrc_fld_name(rs.getString(3));
				fieldBean.setSrc_fld_desc(rs.getString(4));
				// fieldBean.setSrc_fld_desc(rs.getString(5));
				fieldBean.setSrc_fld_data_typ(rs.getString(5));
				fieldBean.setTrg_fld_data_typ(rs.getString(6));
				fieldBean.setFld_null_flg(rs.getString(7));
				fieldBean.setTgt_tbl_prtn_flg(rs.getString(8));
				fieldBean.setPii_flg(rs.getString(9));
				fieldBean.setFxd_fld_strt_idx(rs.getInt(10));
				fieldBean.setFxd_fld_end_idx(rs.getInt(11));
				fieldBean.setFxd_fld_len(rs.getInt(12));
				fieldBean.setPkey(rs.getString(13));
				fields.add(fieldBean);
			}
			// connectionUtils.closeQuietly(conn);
			conn.close();
			return fields;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	@Override
	public Map<String, List<String>> getSourceFields(int src_sys_id) throws Exception {
		Map<String, List<String>> fileFields = new HashMap<String, List<String>>();
		List<String> fields = null;
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			String s_id = "select FEED_TABLE_id, FEED_fld_name from JUNIPER_PUB_FEED_FLD_DTLS"
					+ " where PUB_FEED_SEQUENCE=" + src_sys_id
					+ " AND trg_fld_data_typ = 'DATE' OR trg_fld_data_typ = 'TIMESTAMP'  order by FEED_TABLE_id asc";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				if (fileFields.get(rs.getString(1)) == null) {
					fields = new ArrayList<String>();
					// fields.add("NONE");
					// fields.add("LOAD_START_TIME");
					fields.add(rs.getString(2));
					fileFields.put(rs.getString(1), fields);
				} else {
					fileFields.get(rs.getString(1)).add(rs.getString(2));
				}
			}
			conn.close();
			return fileFields;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	@Override
	public Map<String, List<SourceSystemFieldBean>> getSourceFieldMetadata(int src_sys_id) throws Exception {

		Map<String, List<SourceSystemFieldBean>> fileFields = new HashMap<String, List<SourceSystemFieldBean>>();
		List<SourceSystemFieldBean> fields = null;
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			String s_id = "select UPPER(FEED_TABLE_id) as FEED_TABLE_id, FEED_fld_pos_num,FEED_fld_name ,FEED_fld_data_typ,trg_fld_data_typ from JUNIPER_PUB_FEED_FLD_DTLS"
					+ " where PUB_FEED_SEQUENCE=" + src_sys_id + "  order by FEED_TABLE_id asc";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			SourceSystemFieldBean bean = null;
			while (rs.next()) {
				if (fileFields.get(rs.getString(1)) == null) {
					fields = new ArrayList<SourceSystemFieldBean>();
					bean = new SourceSystemFieldBean();
					bean.setSrc_file_id(rs.getString(1));
					bean.setFld_pos_num(rs.getInt(2));
					bean.setSrc_fld_name(rs.getString(3));
					bean.setSrc_fld_data_typ(rs.getString(4));
					bean.setTrg_fld_data_typ(rs.getString(5));
					// fields.add("NONE");
					// fields.add("LOAD_START_TIME");
					fields.add(bean);
					fileFields.put(rs.getString(1), fields);
				} else {
					fields = fileFields.get(rs.getString(1));
					bean = new SourceSystemFieldBean();
					bean.setSrc_file_id(rs.getString(1));
					bean.setFld_pos_num(rs.getInt(2));
					bean.setSrc_fld_name(rs.getString(3));
					bean.setSrc_fld_data_typ(rs.getString(4));
					bean.setTrg_fld_data_typ(rs.getString(5));
					// fields.add("NONE");
					// fields.add("LOAD_START_TIME");
					fields.add(bean);
				}
			}
			return fileFields;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	public Map<String, List<String>> getSourceAllFields(int src_sys_id) throws Exception {
		Map<String, List<String>> fileFields = new HashMap<String, List<String>>();
		List<String> fields = null;
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			String s_id = "select FEED_TABLE_id, FEED_fld_name from JUNIPER_PUB_FEED_FLD_DTLS"
					+ " where PUB_FEED_SEQUENCE=" + src_sys_id + "  order by FEED_TABLE_id asc";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				if (fileFields.get(rs.getString(1)) == null) {
					fields = new ArrayList<String>();
					// fields.add("NONE");
					// fields.add("LOAD_START_TIME");
					fields.add(rs.getString(2));
					fileFields.put(rs.getString(1), fields);
				} else {
					fileFields.get(rs.getString(1)).add(rs.getString(2));
				}
			}
			conn.close();
			return fileFields;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	public ArrayList<String> populateDatasets(String proj_id) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<String> file_list = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			String s_id = "select distinct feed_tgt_ds from JUNIPER_PUB_FEED_DTLS where PROJECT_SEQUENCE IN "
					+ "(select pm.PROJECT_SEQUENCE from JUNIPER_PROJECT_MASTER pm where pm.PROJECT_ID='" + proj_id
					+ "')";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				file_list.add(rs.getString(1));
			}
			return file_list;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	public ArrayList<String> populateSrcDBList() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<String> file_list = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			String s_id = "select distinct src_db_typ from mstr_datatype_link_dtls";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				file_list.add(rs.getString(1));
			}
			return file_list;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	public ArrayList<String> populateTgtDBList() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<String> file_list = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			String s_id = "select distinct tgt_db_typ from mstr_datatype_link_dtls";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				file_list.add(rs.getString(1));
			}
			// connectionUtils.closeQuietly(conn);
			return file_list;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	public List<DataTypeLinkBean> getDataTypeLinkList(String src_db, String tgt_db) throws Exception {
		List<DataTypeLinkBean> dataTypeList = new ArrayList<DataTypeLinkBean>();
		DataTypeLinkBean dataBean = null;
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			String s_id = "select src_data_typ,tgt_data_typ from mstr_datatype_link_dtls where src_db_typ ="
					+ "'" + src_db + "'" + "and tgt_db_typ='" + tgt_db + "'";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				dataBean = new DataTypeLinkBean();
				dataBean.setSrc_data_type(rs.getString(1));
				dataBean.setTgt_data_type(rs.getString(2));
				dataTypeList.add(dataBean);
			}
			// connectionUtils.closeQuietly(conn);
			return dataTypeList;
		}catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	public String getFeedName(String feed_id) throws Exception {
		Connection conn = null;
		String feed_name = null;
		try {
			conn = connectionUtils.getConnection();
			String s_id = "SELECT PUB_FEED_NAME FROM JUNIPER_PUB_FEED_DTLS where PUB_FEED_SEQUENCE="
					+ feed_id;
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				feed_name = rs.getString(1);
			}
			return feed_name;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	public ArrayList<String> populateTables(String dataset) {
		ArrayList<String> tableList = BigQueryUtils.getTables(dataset);
		return tableList;
	}

	public ArrayList<String> populateRunIDs(String ds_name, String table_name) {
		ArrayList<String> tableList = new ArrayList<String>();
		String query = "SELECT distinct run_id FROM " + ds_name + "." + table_name;
		try {
			// TODO: Need to get Project name
			BigQuery bigquery = BigQueryOptions.getDefaultInstance().toBuilder()
					.setProjectId(MySqlConstants.PROJECTNAME).build().getService();
			QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
			// ArrayList<String> fieldList = getFields(dataset, tableName);
			for (FieldValueList row : bigquery.query(queryConfig).iterateAll()) {
				for (int i = 0; i < row.size(); i++) {
					tableList.add(row.get(i).getValue().toString());
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JobException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return tableList;
	}

	@Override
	public Map<String, String> getReconSysIds() throws Exception {
		Connection conn = null;
		try {
			Map<String, String> src_map = new HashMap<String, String>();
			conn = connectionUtils.getConnection();
			String s_id = "SELECT DISTINCT PUB_FEED_SEQUENCE, FEED_UNIQUE_NAME "
					+ "FROM JUNIPER_PUB_FEED_STATUS  WHERE UPPER(STATUS)='SUCCESS'";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				src_map.put(rs.getString(1), rs.getString(2));
			}
			return src_map;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	

	@Override
	public ArrayList<ReconDashboardBean> reconDashData(String proj_id,String db_id,String run_id) throws Exception {
		ArrayList<ReconDashboardBean> reconBeanList = new ArrayList<ReconDashboardBean>();
		ReconDashboardBean reconBean = null;
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			/*
			 * String s_id = "SELECT  DISTINCT src_file_id,cnt_src,cnt_tgt \r\n" +
			 * "FROM batch_audit_job_dtls \r\n" + "WHERE src_sys_id ="+src_id+"\r\n" +
			 * "AND run_id="+"'"+run_id+"'";
			 */
			String s_id = "select ext.TABLE_NAME,ext.TABLE_COUNT, pub.tgt_record_count from \r\n" + 
					"(select FEED_ID,RUN_ID,SUBSTR(TABLE_NAME, INSTR(TABLE_NAME, '.') + 1) AS TABLE_NAME,TABLE_COUNT \r\n" + 
					"from JUNIPER_EXT_TABLE_STATUS_VW)ext,\r\n" + 
					"(select p.PUB_FEED_SEQUENCE, p.FEED_TABLE_ID, p.RUN_ID, p.TGT_RECORD_COUNT , m.EXT_FEED_SEQUENCE from JUNIPER_PUB_FEED_TBL_STATS p , \r\n" + 
					"JUNIPER_PUB_FEED_DTLS m where p.PUB_FEED_SEQUENCE=m.PUB_FEED_SEQUENCE and m.FEED_TGT_PRJT='"+proj_id+"' and m.FEED_TGT_DS='"+db_id+"') pub\r\n" + 
					"where ext.FEED_ID=pub.EXT_FEED_SEQUENCE and ext.run_id = pub.run_id and ext.TABLE_NAME = pub.FEED_TABLE_ID  and pub.run_id='"+run_id+"'";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				reconBean = new ReconDashboardBean();
				reconBean.setSrc_file_id(rs.getString(1));
				reconBean.setSrc_count(rs.getString(2));
				reconBean.setTgt_count(rs.getString(3));
				reconBeanList.add(reconBean);
			}
			// connectionUtils.closeQuietly(conn);
			return reconBeanList;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	@Override
	public Map<String, String> getPubFeedIDs(String project) throws Exception {
		Connection conn = null;
		try {
			Map<String, String> src_map = new HashMap<String, String>();
			conn = connectionUtils.getConnection();
			String s_id = "SELECT p.PUB_FEED_SEQUENCE,p.PUB_FEED_NAME FROM JUNIPER_PUB_FEED_DTLS p, JUNIPER_PROJECT_MASTER m  WHERE p.project_sequence=m.project_sequence and PROJECT_ID='"
					+ project + "'";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				src_map.put(rs.getString(1), rs.getString(2));
			}
			// ConnectionUtils.closeQuietly(conn);
			conn.close();
			return src_map;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	@Override
	public ArrayList<String> populateGoogleProject(String project) throws Exception {
		ArrayList<String> gcpProjectList = new ArrayList<String>();

		String query = "select DISTINCT GCP_PROJECT from JUNIPER_EXT_GCP_MASTER gp, JUNIPER_PROJECT_MASTER p where p.PROJECT_SEQUENCE=gp.PROJECT_SEQUENCE and p.project_id='"
				+ project + "'";
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				gcpProjectList.add(rs.getString(1));
			}
			// connectionUtils.closeQuietly(conn);
			return gcpProjectList;
		}catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}

	}

	@Override
	public int getProjectSequence(String project) throws Exception {
		ArrayList<String> gcpProjectList = new ArrayList<String>();
		int project_Sequence = 0;

		String query = "select PROJECT_SEQUENCE from JUNIPER_PROJECT_MASTER  where project_id='" + project + "'";
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				/*
				 * gcpProjectList.add(rs.getString(1)); System.out.println(rs.getString(1));
				 */
				project_Sequence = rs.getInt(1);
			}
			// connectionUtils.closeQuietly(conn);
			return project_Sequence;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}

	}

	@Override
	public ArrayList<String> populateServiceAccList(String gcp_proj_id, String proj_id) throws Exception {
		ArrayList<String> gcpProjectList = new ArrayList<String>();
		String query = "select SERVICE_ACCOUNT_NAME from JUNIPER_EXT_GCP_MASTER gp, JUNIPER_PROJECT_MASTER p where p.PROJECT_SEQUENCE=gp.PROJECT_SEQUENCE and p.project_id='"
				+ proj_id + "' AND gp.GCP_PROJECT='" + gcp_proj_id + "'";
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				gcpProjectList.add(rs.getString(1));
			}
			// connectionUtils.closeQuietly(conn);
			return gcpProjectList;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}

	}

	@Override
	public int addMetaDataForExtractedSource(RequestDto requestDto, int project_sequence) throws Exception {
		String status = "Success";
		String message = "METADATA INSERTED SUCCESSFULLY";
		Map<String, String> req_body = requestDto.getBody().get("data");
		Map<String, String> req_header = requestDto.getHeader();
		String userName = req_header.get("user");
		String src_sys_dtls = req_body.get("src_sys_id");
		String src_sys_id = src_sys_dtls.split(":")[0];
		String targetType = req_body.get("target_type");
		String targetDataset = req_body.get("target_ex_dataset");
		String run_id_list = req_body.get("run_id_list");
		String gcp_project_name = req_body.get("gcp_pr_name");
		String pub_feed_name = req_body.get("pub_feed_name");
		String service_account_name = req_body.get("sa_name");
		// int project_sequence = 1;

		/*
		 * String meta_data_path = ""; try { String run_id = run_id_list.split(",")[0];
		 * meta_data_path = dataPublishingRepository.getMetaDataPath(src_sys_id,
		 * run_id); if (meta_data_path.equals("")) { //return createResponse("Failed",
		 * "Metadata Not Available for Run Id"); throw new
		 * Exception("Metadata Not Available for Run Id"); }
		 * //dataPublishingRepository.cleanPublishSourceSystem(src_sys_id); } catch
		 * (SQLException e1) { // TODO Auto-generated catch block status = "Failed";
		 * message = "Extraction Job is not completed for this source";
		 * e1.printStackTrace(); //return createResponse(status, message); throw e1; }
		 * 
		 * String systemFilePath = meta_data_path + "mstr_sys_dtls.csv"; String
		 * fileFilePath = meta_data_path + "mstr_file_dtls.csv"; String fieldFilePath =
		 * meta_data_path + "mstr_tbl_fld_dtls.csv";
		 */
		int pub_feed_id = 0;
		try {
			pub_feed_id = dataPublishingRepository.addSystemMetaData(userName, targetType, targetDataset, true,
					service_account_name, gcp_project_name, src_sys_id, pub_feed_name, project_sequence);
			// status="Success";
			// message="SYSTEM DATA INSERTED SUCCESSFULLY";
		} catch (Exception e) {
			e.printStackTrace();
			status = "Failed";
			message = "Error While Adding System Metadata :" + e.getMessage();
			// return createResponse(status, message);
			throw e;
		}

		try {
			dataPublishingRepository.addFileMetaData(userName, service_account_name, pub_feed_id, project_sequence,
					src_sys_id);
			// status="Success";
			// message="SYSTEM DATA INSERTED SUCCESSFULLY";
		} catch (Exception e) {
			e.printStackTrace();
			status = "Failed";
			message = "Error While Adding File Metadata :" + e.getMessage();
			// return createResponse(status, message);
			throw e;
		}

		try {
			dataPublishingRepository.addFieldMetaData(userName, true, service_account_name, pub_feed_id, src_sys_id);
			// status ="Success";
			// message="FIELD DATA INSERTED SUCCESSFULLY";
		} catch (Exception e) {
			e.printStackTrace();
			status = "Failed";
			message = "Error While Adding Field Metadata :" + e.getMessage();
			// return createResponse(status, message);
			throw e;
		}

		return pub_feed_id;
	}

	@Override
	public String saveMetadataChanges(RequestDto requestDto) throws Exception {
		String status = "";
		String message = "";

		MetadataChangeDto dto = null;
		Map<String, String> fields = null;
		List<MetadataChangeDto> dtos = new ArrayList<MetadataChangeDto>();

		Map<String, String> req_body = requestDto.getBody().get("data");
		Map<String, String> req_header = requestDto.getHeader();
		String user_name = req_header.get("user");
		int feed_id = Integer.parseInt(req_body.get("feed_id"));

		int tableCounter = Integer.parseInt(req_body.get("tc"));
		int columnCounts = 0;
		for (int i = 1; i <= tableCounter; i++) {
			dto = new MetadataChangeDto();
			dto.setFeedId(feed_id);
			fields = new HashMap<String, String>();
			dto.setSrcTableName(req_body.get("t" + i + "srcname"));
			dto.setTgtTableName(req_body.get("t" + i + "tgtname"));
			columnCounts = Integer.parseInt(req_body.get("t" + i + "fc"));
			for (int j = 1; j <= columnCounts; j++) {
				fields.put(req_body.get("t" + i + "f" + j), req_body.get("t" + i + "ft" + j));
			}
			dto.setFields(fields);
			dtos.add(dto);
		}
		try {
			dataPublishingRepository.saveMetadataChanges(dtos);
			status = "Success";
			message = "Metadata Changes Saved Successfully";
		} catch (Exception e) {
			e.printStackTrace();
			status = "Failed";
			message = "Error While Updating Metadata Changes :" + e.getMessage();
		}
		return createResponse(status, message);
	}

	@Override
	public String savePartitionDetails(RequestDto requestDto) throws Exception {


		String status = "";
		String message = "";
		Map<String, String> req_body = requestDto.getBody().get("data");
		Map<String, String> req_header = requestDto.getHeader();
		String user_name = req_header.get("user");
		String src_sys_id = req_body.get("feed_id");

		int counter = Integer.parseInt(req_body.get("counter"));

		List<FilePartitionDetailsDto> partitionDetails = new ArrayList<FilePartitionDetailsDto>();
		FilePartitionDetailsDto dto = null;
		for (int i = 1; i <= counter; i++) {
			dto = new FilePartitionDetailsDto();
			dto.setFile_name(req_body.get("table" + String.valueOf(i)));
			dto.setPartition_key(req_body.get("key" + String.valueOf(i)));
			dto.setPublishing_type(req_body.get("type" + String.valueOf(i)));
			partitionDetails.add(dto);
		}
		try {
			dataPublishingRepository.savePartitionDetails(src_sys_id, partitionDetails);
			status = "Success";
			message = "Partition Details Saved Successfully";
		} catch (Exception e) {
			e.printStackTrace();
			status = "Failed";
			message = "Error While Saving Partition Details :" + e.getMessage();
		}
		return createResponse(status, message);
	}

	private String createResponse(String status, String message) {

		return "{ 'status': '" + status + "','message':'" + message + "' }";
	}

	@Override
	public int checkNames(@Valid String sun) throws Exception {
		Connection connection = null;
		int stat = 0;
		PreparedStatement pstm =null;
		try {
			connection = connectionUtils.getConnection();
			pstm = connection.prepareStatement(
					"select PUB_FEED_NAME from JUNIPER_PUB_FEED_DTLS where PUB_FEED_NAME='" + sun + "'");
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				stat = 1;
				break;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		finally {
			pstm.close();
			connection.close();
		}
		return stat;
	}

	@Override
	public ExistingPubBean getExistingPubDetails(String pub_feed_id) throws Exception {
		ExistingPubBean exPubBean = null;
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			String s_id = "select GCP_PROJ_NAME,SERVICE_ACC_NAME,FEED_TGT_DS from JUNIPER_PUB_FEED_DTLS where PUB_FEED_SEQUENCE='"
					+ pub_feed_id + "'";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				exPubBean = new ExistingPubBean();
				exPubBean.setProject_name(rs.getString(1));
				exPubBean.setService_acc(rs.getString(2));
				exPubBean.setTarget_dataset(rs.getString(3));
			}
			// connectionUtils.closeQuietly(conn);
			return exPubBean;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	@Override
	public String updateDataType(RequestDto requestDto) throws Exception {
		String status = "";
		String message = "";
		DataTypeMappingDto dto = new DataTypeMappingDto();

		Map<String, String> d = requestDto.getBody().get("data");
		Map<String, String> h = requestDto.getHeader();
		String serviceAccountName = h.get("service_account");
		String src_db_name = d.get("src_db_name");
		String tgt_db_name = d.get("tgt_db_name");
		int counter = Integer.parseInt(d.get("counter"));
		Map<String, String> mapping = new HashMap<String, String>();
		for (int i = 1; i <= counter; i++) {
			mapping.put(d.get("sdt" + i), d.get("tdt" + i));
		}
		dto.setSrc_type(src_db_name);
		dto.setTgt_type(tgt_db_name);
		dto.setDatatypeMapping(mapping);

		try {
			dataPublishingRepository.updateDataType(dto);
			status = "Success";
			message = "DATA TYPE MAPPING UPDATED SUCCESSFULLY";
		} catch (Exception e) {
			e.printStackTrace();
			status = "Failed";
			message = "FAILED TO UPDATE THE DATATYPE MAPPING" + " & Exception : " + e.getMessage();
		}
		return createResponse(status, message);
	}

	@Override
	public String insertScheduleMetadata( String feed_name,String feed_id, String gcp_details, String project, String cron)
			throws Exception {
		try {
			return DBUtils.insertScheduleMetadata(connectionUtils.getConnection(), feed_name,feed_id,gcp_details, project, cron);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public int getPublishingFeedId(String googleProjectName, String targetDSName) throws Exception {
		return dataPublishingRepository.getPublishingFeedId(googleProjectName, targetDSName);
	}

	@Override
	public String getFeedExtractionType(String feedName) throws Exception {
		return dataPublishingRepository.getFeedExtractionType(feedName);
	}

	@Override
	public void insertScheduleMetadataWithDependent(String extractFeedSequence,String pubFeedSequence, String feedUniqueName, String gcpName,
			String saNAme, String projectId) throws Exception {
		// TODO Auto-generated method stub
		dataPublishingRepository.insertScheduleMetadataWithDependent(extractFeedSequence,pubFeedSequence, feedUniqueName, gcpName, saNAme, projectId);
	}

	@Override
	public String insertOnDemandScheduleMetadata(String feed_name,String feed_id, String gcp_details, String project, String run_id)
			throws Exception {
		try {
			return DBUtils.insertOnDemandScheduleMetadata(connectionUtils.getConnection(), feed_name,feed_id,gcp_details, project, run_id);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public ArrayList<String> getProjList() throws Exception {
		// TODO Auto-generated method stub
				Connection conn = null;
				ArrayList<String> run_list = new ArrayList<String>();
				try {
					conn = connectionUtils.getConnection();
					String s_id = "SELECT DISTINCT A.FEED_TGT_PRJT FROM JUNIPER_PUB_FEED_DTLS A, JUNIPER_PUB_FEED_STATUS B WHERE A.PUB_FEED_SEQUENCE=B.PUB_FEED_SEQUENCE AND UPPER(B.STATUS)='SUCCESS'";
					Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(s_id);
					while (rs.next()) {
						run_list.add(rs.getString(1));
					}
					// connectionUtils.closeQuietly(conn);
					return run_list;
				} catch (Exception e) {
					System.out.println("Exception occured "+e);
					throw e;
				} finally {
					conn.close();
				}
	}

	@Override
	public ArrayList<String> getDBList(String proj_id) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		ArrayList<String> run_list = new ArrayList<String>();
		try {
			conn = connectionUtils.getConnection();
			String s_id = "SELECT DISTINCT A.FEED_TGT_DS FROM JUNIPER_PUB_FEED_DTLS A, JUNIPER_PUB_FEED_STATUS B WHERE A.PUB_FEED_SEQUENCE=B.PUB_FEED_SEQUENCE\r\n" + 
					"AND UPPER(B.STATUS)='SUCCESS' AND UPPER(A.FEED_TGT_PRJT)=UPPER('"+proj_id+"')";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				run_list.add(rs.getString(1));
			}
			// connectionUtils.closeQuietly(conn);
			return run_list;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}

	@Override
	public ArrayList<String> reconRunIDs(@Valid String proj_id, @Valid String db_id) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		ArrayList<String> run_list = new ArrayList<String>();
		try {
			conn = connectionUtils.getConnection();
			String s_id = "SELECT DISTINCT B.RUN_ID FROM JUNIPER_PUB_FEED_DTLS A, JUNIPER_PUB_FEED_STATUS B WHERE A.PUB_FEED_SEQUENCE=B.PUB_FEED_SEQUENCE\r\n" + 
					"AND UPPER(B.STATUS)='SUCCESS' "
					+ "AND UPPER(A.FEED_TGT_PRJT)=UPPER('"+proj_id+"')"
					+ "AND UPPER(A.FEED_TGT_DS)=UPPER('"+db_id+"')";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(s_id);
			while (rs.next()) {
				run_list.add(rs.getString(1));
			}
			// connectionUtils.closeQuietly(conn);
			return run_list;
		} catch (Exception e) {
			System.out.println("Exception occured "+e);
			throw e;
		} finally {
			conn.close();
		}
	}
	
}
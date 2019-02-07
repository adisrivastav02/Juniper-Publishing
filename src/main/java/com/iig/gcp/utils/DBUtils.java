package com.iig.gcp.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.iig.gcp.constants.OracleConstants;
import com.iig.gcp.publishing.service.DataTypeMappingDto;
import com.iig.gcp.publishing.service.FilePartitionDetailsDto;
import com.iig.gcp.publishing.service.MetadataChangeDto;
import com.iig.gcp.publishing.service.ScheduleJobDetails;
import com.iig.gcp.publishing.service.SystemBean;


public class DBUtils {

	private static final Logger LOG = LoggerFactory.getLogger(DBUtils.class);

	static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	static String SPACE = " ";
	static String COMMA = ",";
	static String SEMICOLON = ";";
	static String QUOTE = "\'";

	static String INSERTQUERY = "insert into {$table}({$columns}) values({$data})";
	static String script_loc = "/home/juniper/scripts/publishToBQ.sh";
	static String script_loc_dependent = "/home/juniper/scripts/publishToBQ.sh";

	public static int insertSystemTargetValue(Connection conn, ArrayList<SystemBean> systemBeans,
			boolean extracted_Source,String tgt_type, String tgt_dataset,String service_account_name, String google_proj_name, String ext_feed_id,String pub_feed_name, int project_sequence, String userName) throws SQLException {
		String tableName1 = "source_system_master";
		String tableName2 = "JUNIPER_PUB_FEED_DTLS";
		String src_sys_id = "";
		pub_feed_name = google_proj_name + ":" + tgt_dataset + ":" + ext_feed_id;
		/*for (SystemBean bean : systemBeans) {
			if (extracted_Source) {
				src_sys_id = String.valueOf(fetchSrcSysId(bean.getSrc_sys_name(), conn));
			} else {
				// TODO: Delete the src system from insert
				String insertSysEntry1 = INSERTQUERY.replace("{$table}", tableName1).replace("{$columns}",
						"src_unique_name,src_sys_desc,country_code,created_by,updated_by,created_date,updated_date")
						.replace("{$data}",
								QUOTE + bean.getSrc_sys_name() + QUOTE + COMMA + QUOTE + bean.getSrc_sys_desc() + QUOTE
										+ COMMA + QUOTE + bean.getCty_cde() + QUOTE + COMMA + QUOTE + "user" + QUOTE
										+ COMMA + QUOTE + "user" + QUOTE + COMMA + QUOTE + new Date() + QUOTE + COMMA
										+ QUOTE + new Date() + QUOTE);

				// String columnNames[] = new String[] { "src_sys_id" };

				Statement statement = conn.createStatement();
				int updateCount = statement.executeUpdate(insertSysEntry1, Statement.RETURN_GENERATED_KEYS);
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						System.out.println(generatedKeys.getLong(1));
						src_sys_id = String.valueOf(generatedKeys.getLong(1));
					} else {
						throw new SQLException("Creating failed, no ID obtained.");
					}
				}

			}
			String insertSysEntry2 = INSERTQUERY.replace("{$table}", tableName2).replace("{$columns}",
					"src_sys_id,src_bkt,src_loc,tgt_prjt,tgt_ds,tgt_type,src_type,created_by,updated_by,created_date,updated_date")
					.replace("{$data}", src_sys_id + COMMA + QUOTE + bean.getSrc_bkt() + QUOTE + COMMA + QUOTE
							+ bean.getSrc_loc() + QUOTE + COMMA + QUOTE + bean.getTgt_prjt() + QUOTE + COMMA + QUOTE
							+ bean.getTgt_ds() + QUOTE + COMMA + QUOTE + bean.getTgt_type() + QUOTE + COMMA + QUOTE
							+ bean.getSrc_type() + QUOTE + COMMA + QUOTE + "user" + QUOTE + COMMA + QUOTE + "user"
							+ QUOTE + COMMA + QUOTE + new Date() + QUOTE + COMMA + QUOTE + new Date() + QUOTE);
			
			
			
			String insertSysEntry2 = INSERTQUERY.replace("{$table}", tableName2).replace("{$columns}",
					"PUB_FEED_NAME ,EXT_FEED_SEQUENCE,FEED_SRC_BKT ,FEED_SRC_LOC ,FEED_SRC_TYPE ,FEED_TGT_PRJT ,FEED_TGT_DS ,FEED_TGT_TYPE ,GCP_PROJ_NAME ,SERVICE_ACC_NAME ,PROJECT_SEQUENCE ,CREATED_BY ,UPDATED_BY ,CREATED_DATE, UPDATED_DATE ")
					.replace("{$data}", QUOTE + pub_feed_name + QUOTE + COMMA +  bean.getExtr_feed_id() + COMMA + QUOTE + bean.getSrc_bkt() + QUOTE + COMMA + QUOTE
							+ bean.getSrc_loc() + QUOTE + COMMA + QUOTE
							+ bean.getSrc_type() + QUOTE + COMMA + QUOTE + bean.getTgt_prjt() + QUOTE + COMMA + QUOTE
							+ bean.getTgt_ds() + QUOTE + COMMA + QUOTE + bean.getTgt_type() + QUOTE 
							+ COMMA   + QUOTE + bean.getGcp_name() + QUOTE 
							+ COMMA   + QUOTE + bean.getSrc_acct_name() + QUOTE 
							+ COMMA   +  project_sequence						
							+ COMMA   + QUOTE + "user" + QUOTE + COMMA + QUOTE + "user"
							+ QUOTE + COMMA + QUOTE + new Date() + QUOTE + COMMA + QUOTE + new Date() + QUOTE);
			
			
			String insertSysEntry2 = "select pub_feed_name as PUB_FEED_NAME ,f.FEED_SEQUENCE as EXT_FEED_SEQUENCE , gm.BUCKET_NAME as FEED_SRC_BKT, gm.BUCKET_NAME as FEED_SRC_LOC , sc.SRC_CONN_TYPE as FEED_SRC_TYPE , google_proj_name as FEED_TGT_PRJT , tgt_dataset as FEED_TGT_DS, tgt_type as FEED_TGT_TYPE, google_proj_name as GCP_PROJ_NAME , service_account_name as SERVICE_ACC_NAME, project_sequence as PROJECT_SEQUENCE , '' as CREATED_BY, '' as UPDATED_BY , '' as CREATED_DATE , '' as UPDATED_DATE from (SELECT *" +
					" FROM JUNIPER_EXT_FEED_MASTER t  " +
					" OUTER APPLY (SELECT TRIM(target_type) AS target_type " +
					            " FROM JSON_TABLE(REPLACE(JSON_ARRAY(t.target), ',', '\",\"'), " +
					            " '$[*]' COLUMNS (target_type VARCHAR2(4000) PATH '$'))) s) f , JUNIPER_EXT_SRC_CONN_MASTER sc , JUNIPER_EXT_TARGET_CONN_MASTER tm , JUNIPER_EXT_GCP_MASTER gm where f.target_type = tm.TARGET_UNIQUE_NAME and f.SRC_CONN_SEQUENCE = sc.SRC_CONN_SEQUENCE and gm.GCP_SEQUENCE = tm.GCP_SEQUENCE and f.FEED_SEQUENCE ="+ ext_feed_id +"  and f.PROJECT_SEQUENCE = "+ project_sequence +" and tm.TARGET_TYPE='gcs'" ;
			
			System.out.println("Query " + insertSysEntry2);

			Statement statement2 = conn.createStatement();

			statement2.execute(insertSysEntry2);
		}*/
		
		
		/*String insertSysEntry2 = "insert into " + tableName2 + "  (PUB_FEED_NAME ,EXT_FEED_SEQUENCE,FEED_SRC_BKT ,FEED_SRC_LOC ,FEED_SRC_TYPE ,FEED_TGT_PRJT ,FEED_TGT_DS ,FEED_TGT_TYPE ,GCP_PROJ_NAME ,SERVICE_ACC_NAME ,PROJECT_SEQUENCE ,CREATED_BY ,UPDATED_BY ,CREATED_DATE, UPDATED_DATE ) select '" + pub_feed_name +"' as PUB_FEED_NAME ,f.FEED_SEQUENCE as EXT_FEED_SEQUENCE , gm.BUCKET_NAME as FEED_SRC_BKT, gm.BUCKET_NAME as FEED_SRC_LOC , sc.SRC_CONN_TYPE as FEED_SRC_TYPE ,'"+ google_proj_name + "' as FEED_TGT_PRJT ,'"+ tgt_dataset+ "' as FEED_TGT_DS, '" +tgt_type + "' as FEED_TGT_TYPE, '"+ google_proj_name + "' as GCP_PROJ_NAME , '" + service_account_name +"' as SERVICE_ACC_NAME, '"+  project_sequence + "' as PROJECT_SEQUENCE , '"+userName+"' as CREATED_BY, '"+userName+"' as UPDATED_BY , '"+new Date()+"' as CREATED_DATE , '"+new Date()+"' as UPDATED_DATE from (SELECT *" +
				" FROM JUNIPER_EXT_FEED_MASTER t  " +
				" OUTER APPLY (SELECT TRIM(target_type) AS target_type " +
				            " FROM JSON_TABLE(REPLACE(JSON_ARRAY(t.target), ',', '\",\"'), " +
				            " '$[*]' COLUMNS (target_type VARCHAR2(4000) PATH '$'))) s) f , JUNIPER_EXT_SRC_CONN_MASTER sc , JUNIPER_EXT_TARGET_CONN_MASTER tm , JUNIPER_EXT_GCP_MASTER gm where f.target_type = tm.TARGET_UNIQUE_NAME and f.SRC_CONN_SEQUENCE = sc.SRC_CONN_SEQUENCE and gm.GCP_SEQUENCE = tm.GCP_SEQUENCE and f.FEED_SEQUENCE ="+ ext_feed_id +"  and f.PROJECT_SEQUENCE = "+ project_sequence +" and UPPER(tm.TARGET_TYPE)='GCS'" ;
		*/
		
		
		String insertSysEntry2 = "insert into " + tableName2 + "  (PUB_FEED_NAME ,EXT_FEED_SEQUENCE,FEED_SRC_BKT ,FEED_SRC_LOC ,FEED_SRC_TYPE ,FEED_TGT_PRJT ,FEED_TGT_DS ,FEED_TGT_TYPE ,GCP_PROJ_NAME ,SERVICE_ACC_NAME ,PROJECT_SEQUENCE ,CREATED_BY ,UPDATED_BY ,CREATED_DATE, UPDATED_DATE ) select '" + pub_feed_name +"' as PUB_FEED_NAME ,f.FEED_SEQUENCE as EXT_FEED_SEQUENCE , gm.BUCKET_NAME as FEED_SRC_BKT, gm.BUCKET_NAME as FEED_SRC_LOC , sc.SRC_CONN_TYPE as FEED_SRC_TYPE ,'"+ google_proj_name + "' as FEED_TGT_PRJT ,'"+ tgt_dataset+ "' as FEED_TGT_DS, '" +tgt_type + "' as FEED_TGT_TYPE, '"+ google_proj_name + "' as GCP_PROJ_NAME , '" + service_account_name +"' as SERVICE_ACC_NAME, '"+  project_sequence + "' as PROJECT_SEQUENCE , '"+userName+"' as CREATED_BY, '"+userName+"' as UPDATED_BY , '"+new Date()+"' as CREATED_DATE , '"+new Date()+"' as UPDATED_DATE from " +
				" JUNIPER_EXT_FEED_MASTER f, JUNIPER_EXT_FEED_SRC_TGT_LINK t1, JUNIPER_EXT_TARGET_CONN_MASTER tm, JUNIPER_EXT_GCP_MASTER gm ,JUNIPER_EXT_SRC_CONN_MASTER sc  where f.FEED_SEQUENCE = t1.FEED_SEQUENCE and t1.TARGET_SEQUENCE = tm.TARGET_CONN_SEQUENCE and  gm.GCP_SEQUENCE = tm.GCP_SEQUENCE and  t1.SRC_CONN_SEQUENCE = sc.SRC_CONN_SEQUENCE  and f.FEED_SEQUENCE ="+ ext_feed_id +"  and f.PROJECT_SEQUENCE = "+ project_sequence +" and UPPER(tm.TARGET_TYPE)='GCS'" ;
		
		
		
		System.out.println("Query " + insertSysEntry2);

		Statement statement2 = conn.createStatement();

		statement2.execute(insertSysEntry2);
		
		//TODO: Get the pub feed id based on name
		return getPubFeedId(conn, pub_feed_name);
	}

	private static int getProjectSequence(Connection conn, String project) throws SQLException {

		int project_Sequence = 0;
		String query = "select PROJECT_SEQUENCE from JUNIPER_PROJECT_MASTER where upper(PROJECT_ID) ='"
				+ project.toUpperCase() + "'";
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		if (rs.isBeforeFirst()) {

			rs.next();
			project_Sequence = rs.getInt(1);

		}
		return project_Sequence;

	}

	public static void insertFileValue(Connection conn,  int pub_feed_id , int project_sequence, String ext_feed_id, String userName) throws SQLException {

		String tableName = "JUNIPER_PUB_FEED_FILE_DTLS";
		Map<String, String> src_sys_ids = new HashMap<String, String>();
		/*for (FileBean fBean : fileBeans) {
			String src_sys_id = "";
			if (src_sys_ids.get(fBean.getSrc_unique_name()) == null) {
				// retrieve src_sys_id
				src_sys_id = String.valueOf(fetchSrcSysId(fBean.getSrc_unique_name(), conn));
				src_sys_ids.put(src_sys_ids.get(fBean.getSrc_unique_name()), src_sys_id);
			} else {
				src_sys_id = src_sys_ids.get(fBean.getSrc_unique_name());
			}

			String insertFileEntry = INSERTQUERY.replace("{$table}", tableName).replace("{$columns}",
					"PUB_FEED_SEQUENCE ,FEED_TABLE_id ,FEED_TABLE_name ,FEED_TABLE_desc ,FEED_TABLE_type ,FEED_TABLE_delimiter,tgt_tbl_name ,FEED_sch_loc ,FEED_hdr_cnt  ,FEED_trl_cnt  ,FEED_cnt_start_idx ,FEED_cnt_end_idx ,data_class_catg ,FEED_load_type ,CREATED_BY ,UPDATED_BY ,CREATED_DATE ,UPDATED_DATE")
					.replace("{$data}", pub_feed_id + COMMA + QUOTE + fBean.getSrc_file_id() + QUOTE + COMMA + QUOTE
							+ fBean.getSrc_file_name() + QUOTE + COMMA + QUOTE + fBean.getSrc_file_desc() + QUOTE
							+ COMMA + QUOTE + fBean.getSrc_file_type() + QUOTE + COMMA + QUOTE
							+ fBean.getSrc_file_delimiter() + QUOTE + COMMA + QUOTE + fBean.getTgt_tbl_name() + QUOTE
							+ COMMA + QUOTE + fBean.getSrc_sch_loc() + QUOTE + COMMA + QUOTE + fBean.getSrc_hdr_cnt()
							+ QUOTE + COMMA + QUOTE + fBean.getSrc_trl_cnt() + QUOTE + COMMA + QUOTE
							+ fBean.getSrc_cnt_start_idx() + QUOTE + COMMA + QUOTE + fBean.getSrc_cnt_end_idx() + QUOTE
							+ COMMA + QUOTE + fBean.getData_class_catg() + QUOTE + COMMA + QUOTE
							+ fBean.getSrc_load_type() + QUOTE + COMMA+ QUOTE + dateFormat.format(fBean.getCrtd_dt())
							+ QUOTE + COMMA + QUOTE + dateFormat.format(fBean.getCrtd_dt()) + QUOTE + COMMA + QUOTE
							+ "User" + QUOTE + COMMA + QUOTE + "User" + QUOTE);
			
			String insertFileEntry =  "select pub_feed_id as PUB_FEED_SEQUENCE,  SRC_TABLE as FEED_TABLE_id, SRC_TABLE as FEED_TABLE_name , SRC_TABLE as FEED_TABLE_desc , '' as FEED_TABLE_type, 'A' as FEED_TABLE_delimiter , SRC_TABLE as tgt_tbl_name , null as FEED_sch_loc , null as FEED_hdr_cnt, null as FEED_trl_cnt, null as FEED_cnt_start_idx, null as FEED_cnt_end_idx , null as data_class_catg , FETCH_TYPE as FEED_load_type, '' as CREATED_BY, '' as UPDATED_BY , '' as CREATED_DATE , '' as UPDATED_DATE from JUNIPER_EXT_SRC_FILE_MASTER_VW where FEED_SEQUENCE = " +ext_feed_id ;
			
			System.out.println(insertFileEntry);

			
			 * String insertFileEntry = "INSERT INTO" + SPACE + tableName + SPACE + "VALUES"
			 * + "(" + QUOTE + fBean.getSrc_unique_name() + QUOTE + COMMA + QUOTE +
			 * fBean.getSrc_file_id() + QUOTE + COMMA + QUOTE + fBean.getSrc_file_name() +
			 * QUOTE + COMMA + QUOTE + fBean.getSrc_file_desc() + QUOTE + COMMA + QUOTE +
			 * fBean.getSrc_file_type() + QUOTE + COMMA + QUOTE +
			 * fBean.getSrc_file_delimiter() + QUOTE + COMMA + QUOTE +
			 * fBean.getTgt_tbl_name() + QUOTE + COMMA + QUOTE + fBean.getSrc_sch_loc() +
			 * QUOTE + COMMA + QUOTE + fBean.getSrc_hdr_cnt() + QUOTE + COMMA + QUOTE +
			 * fBean.getSrc_trl_cnt() + QUOTE + COMMA + QUOTE + fBean.getSrc_cnt_start_idx()
			 * + QUOTE + COMMA + QUOTE + fBean.getSrc_cnt_end_idx() + QUOTE + COMMA + QUOTE
			 * + fBean.getData_class_catg() + QUOTE + COMMA + QUOTE +
			 * fBean.getMetadata_sts() + QUOTE + COMMA + QUOTE +
			 * dateFormat.format(fBean.getCrtd_dt()) + QUOTE + COMMA + fBean.getUptd_dt() +
			 * COMMA + QUOTE + fBean.getCrtd_by() + QUOTE + COMMA + QUOTE + "" + QUOTE + ")"
			 * + SEMICOLON;
			 

			LOG.info(insertFileEntry);
			Statement statement = conn.createStatement();

			statement.execute(insertFileEntry);
			LOG.info("FILE META DATA INSERTED SUCCESFULLY!!");
		}*/
		
		String insertFileEntry =  "insert into " + tableName + " (PUB_FEED_SEQUENCE ,FEED_TABLE_id ,FEED_TABLE_name ,FEED_TABLE_desc ,FEED_TABLE_type ,FEED_TABLE_delimiter,tgt_tbl_name ,FEED_sch_loc ,FEED_hdr_cnt  ,FEED_trl_cnt  ,FEED_cnt_start_idx ,FEED_cnt_end_idx ,data_class_catg ,FEED_load_type ,CREATED_BY ,UPDATED_BY ,CREATED_DATE ,UPDATED_DATE) select " + pub_feed_id +" as PUB_FEED_SEQUENCE,  SRC_TABLE as FEED_TABLE_id, SRC_TABLE as FEED_TABLE_name , SRC_TABLE as FEED_TABLE_desc , '' as FEED_TABLE_type, 'A' as FEED_TABLE_delimiter , SRC_TABLE as tgt_tbl_name , null as FEED_sch_loc , null as FEED_hdr_cnt, null as FEED_trl_cnt, null as FEED_cnt_start_idx, null as FEED_cnt_end_idx , null as data_class_catg , FETCH_TYPE as FEED_load_type, '"+userName+"' as CREATED_BY, '"+userName+"' as UPDATED_BY , '"+new Date()+"' as CREATED_DATE , '"+new Date()+"' as UPDATED_DATE from JUNIPER_EXT_FILE_MASTER_VW where FEED_SEQUENCE = " +ext_feed_id ;
		
		System.out.println(insertFileEntry);
		
		Statement statement = conn.createStatement();

		statement.execute(insertFileEntry);

	}

	public static void insertFieldValue(Connection conn, int pub_feed_id, String ext_feed_id, String userName) throws SQLException {

		String tableName = "JUNIPER_PUB_FEED_FLD_DTLS";
		Map<String, String> src_sys_ids = new HashMap<String, String>();

		/*for (FieldBean fBean : fieldBeans) {
			String src_sys_id = "";
			if (src_sys_ids.get(fBean.getSrc_unique_name()) == null) {
				// retrieve src_sys_id
				src_sys_id = String.valueOf(fetchSrcSysId(fBean.getSrc_unique_name(), conn));
				src_sys_ids.put(fBean.getSrc_unique_name(), src_sys_id);
			} else {
				src_sys_id = src_sys_ids.get(fBean.getSrc_unique_name());
			}

			String insertFieldEntry = INSERTQUERY.replace("{$table}", tableName).replace("{$columns}",
					"PUB_FEED_SEQUENCE,FEED_TABLE_id ,FEED_fld_pos_num ,FEED_sch_name ,FEED_fld_name ,FEED_fld_desc ,FEED_fld_data_typ,trg_fld_data_typ ,fld_null_flg ,tgt_tbl_prtn_flg ,pii_flg  ,fxd_fld_strt_idx ,fxd_fld_end_idx ,fxd_fld_len ,pkey ,CREATED_DATE ,UPDATED_DATE ,CREATED_BY ,UPDATED_BY")
					.replace("{$data}", pub_feed_id + COMMA + QUOTE + fBean.getSrc_file_id() + QUOTE + COMMA + QUOTE
							+ +fBean.getFld_pos_num() + QUOTE + COMMA + QUOTE + fBean.getSrc_sch_name() + QUOTE + COMMA
							+ QUOTE + fBean.getSrc_fld_name() + QUOTE + COMMA + QUOTE + fBean.getSrc_fld_desc() + QUOTE
							+ COMMA + QUOTE + fBean.getSrc_fld_data_typ() + QUOTE + COMMA + QUOTE
							+ fBean.getTrg_fld_data_typ() + QUOTE + COMMA + QUOTE + fBean.getFld_null_flg() + QUOTE
							+ COMMA + QUOTE + fBean.getTgt_tbl_prtn_flg() + QUOTE + COMMA + QUOTE + fBean.getPii_flg()
							+ QUOTE + COMMA + QUOTE + fBean.getFxd_fld_strt_idx() + QUOTE + COMMA + QUOTE
							+ fBean.getFxd_fld_end_idx() + QUOTE + COMMA + QUOTE + fBean.getFxd_fld_len() + QUOTE
							+ COMMA + QUOTE + "NA" + QUOTE + COMMA + QUOTE + new Date()
							+ QUOTE + COMMA + QUOTE +  new Date() + QUOTE + COMMA + QUOTE + fBean.getCrtd_by() + QUOTE + COMMA
							+ QUOTE + "" + QUOTE);

			String insertFieldEntry = "select pub_feed_id as PUB_FEED_SEQUENCE, SRC_TABLE as FEED_TABLE_id, 1 as FEED_fld_pos_num, null as FEED_sch_name, SRC_PHY_ATTRIBUTE_NAME as FEED_fld_name, SRC_PHY_ATTRIBUTE_NAME as FEED_fld_desc , SRC_PHY_ATTRIBUTE_TYPE as FEED_fld_data_typ , '' as trg_fld_data_typ ,  null as fld_null_flg ,  null as tgt_tbl_prtn_flg ,  null as pii_flg ,  null as fxd_fld_strt_idx ,  null as fxd_fld_end_idx ,  null as fxd_fld_len ,  null as  pkey, '' as CREATED_DATE , '' as UPDATED_DATE, '' as CREATED_BY, '' as UPDATED_BY  from JUNIPER_EXT_SRC_FIELD_MASTER_VW where FEED_SEQUENCE =" +  ;
			
			 * String insertFieldEntry = "INSERT INTO" + SPACE + tableName + SPACE +
			 * "VALUES" + "(" + QUOTE + src_sys_id + QUOTE + COMMA + QUOTE +
			 * fBean.getSrc_file_id() + QUOTE + COMMA + QUOTE + fBean.getFld_pos_num() +
			 * QUOTE + COMMA + QUOTE + fBean.getSrc_sch_name() + QUOTE + COMMA + QUOTE +
			 * fBean.getSrc_fld_name() + QUOTE + COMMA + QUOTE + fBean.getSrc_fld_desc() +
			 * QUOTE + COMMA + QUOTE + fBean.getSrc_fld_data_typ() + QUOTE + COMMA + QUOTE +
			 * fBean.getTrg_fld_data_typ() + QUOTE + COMMA + QUOTE + fBean.getFld_null_flg()
			 * + QUOTE + COMMA + QUOTE + fBean.getTgt_tbl_prtn_flg() + QUOTE + COMMA + QUOTE
			 * + fBean.getPii_flg() + QUOTE + COMMA + QUOTE + fBean.getFxd_fld_strt_idx() +
			 * QUOTE + COMMA + QUOTE + fBean.getFxd_fld_end_idx() + QUOTE + COMMA + QUOTE +
			 * fBean.getFxd_fld_len() + QUOTE + COMMA + QUOTE +
			 * dateFormat.format(fBean.getCrtd_dt()) + QUOTE + COMMA + fBean.getUptd_dt() +
			 * COMMA + QUOTE + fBean.getCrtd_by() + QUOTE + COMMA + QUOTE + "" + QUOTE +
			 * COMMA + QUOTE + "NA" + QUOTE + ")" + SEMICOLON;
			 

			LOG.info(insertFieldEntry);
			Statement statement = conn.createStatement();

			statement.execute(insertFieldEntry);
			LOG.info("FIELD META DATA INSERTED SUCCESFULLY!!");
		}*/
		
		String insertFieldEntry = "insert into " + tableName + " (PUB_FEED_SEQUENCE,FEED_TABLE_id ,FEED_fld_pos_num ,FEED_sch_name ,FEED_fld_name ,FEED_fld_desc ,FEED_fld_data_typ,trg_fld_data_typ ,fld_null_flg ,tgt_tbl_prtn_flg ,pii_flg  ,fxd_fld_strt_idx ,fxd_fld_end_idx ,fxd_fld_len ,pkey ,CREATED_DATE ,UPDATED_DATE ,CREATED_BY ,UPDATED_BY) select "+pub_feed_id +" as PUB_FEED_SEQUENCE, SRC_TABLE as FEED_TABLE_id, COLUMN_POS as FEED_fld_pos_num, null as FEED_sch_name, SRC_PHY_ATTRIBUTE_NAME as FEED_fld_name, SRC_PHY_ATTRIBUTE_NAME as FEED_fld_desc , SRC_PHY_ATTRIBUTE_TYPE as FEED_fld_data_typ , "+getDataTypeMappingQuery(pub_feed_id, "NVL(SUBSTR(UPPER(SRC_PHY_ATTRIBUTE_TYPE), 0, INSTR(UPPER(SRC_PHY_ATTRIBUTE_TYPE), '(')-1), UPPER(SRC_PHY_ATTRIBUTE_TYPE))" , conn)+" as trg_fld_data_typ ,  null as fld_null_flg ,  null as tgt_tbl_prtn_flg ,  null as pii_flg ,  null as fxd_fld_strt_idx ,  null as fxd_fld_end_idx ,  null as fxd_fld_len ,  null as  pkey, '"+new Date()+"' as CREATED_DATE , '"+new Date()+"' as UPDATED_DATE, '"+userName+"' as CREATED_BY, '"+userName+"' as UPDATED_BY  from JUNIPER_EXT_FIELD_MASTER_VW where FEED_SEQUENCE =" + ext_feed_id ;
		
		//CASE WHEN UPPER(SRC_PHY_ATTRIBUTE_TYPE)='INTEGER' then 'INTEGER' else 'STRING' END
		
		//LOG.info(insertFieldEntry);
		System.out.println(insertFieldEntry);
		Statement statement = conn.createStatement();

		statement.execute(insertFieldEntry);
		

	}
	
	private static String getDataTypeMappingQuery(int pub_feed_id, String sourceColume, Connection conn) throws SQLException {
		String src_type = getSourceType(pub_feed_id, conn);
		String query = "select SRC_DATA_TYP,TGT_DATA_TYP from MSTR_DATATYPE_LINK_DTLS where SRC_DB_TYP='"+src_type.toUpperCase() + "'";
		Statement statement=conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		String buffer = "";
		buffer = "COALESCE( ";
		while(rs.next()) {
			System.out.println("Source Type is " + rs.getString(1));
			buffer = buffer + "CASE WHEN " + sourceColume + "='" + rs.getString(1).toUpperCase() +"' THEN '" +rs.getString(2).toUpperCase() +"' ELSE NULL END,";
			//return rs.getString(1);
		}
		buffer = buffer + " NULL,'STRING')";
		System.out.println("QUERY " + buffer);
		return buffer;
	}

	public static String getSourceType(int pub_feed_id, Connection conn) throws SQLException {
		String query = "select FEED_SRC_TYPE from JUNIPER_PUB_FEED_DTLS where PUB_FEED_SEQUENCE="+pub_feed_id;
		Statement statement=conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		while(rs.next()) {
			System.out.println("Source Type is " + rs.getString(1));
			return rs.getString(1);
			
		}
		return "Oracle";
	}
	
	
	public static Map<String,String> fetchDataTypeMapping(String src_type,String tgt_type, Connection conn) throws SQLException {
		
		Map<String,String> data_types = new HashMap<String,String>();
		if(src_type == null) {
			src_type = "ORACLE";
		}
		src_type = "ORACLE";
		String query = "select distinct src_data_typ,tgt_data_typ from mstr_datatype_link_dtls where src_db_typ='"+src_type+"' and tgt_db_typ='"+tgt_type+"'";
		Statement statement=conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		while(rs.next()) {
			//rs.next();
			data_types.put(rs.getString(1).toUpperCase(), rs.getString(2));
			
		}
		return data_types;
		
	}
	
	
	public static ArrayList<String> getBuckets() {

		ArrayList<String> bucketList = new ArrayList<String>();

		Storage storage = StorageOptions.getDefaultInstance().getService();
		for (Bucket bucket : storage.list().iterateAll()) {
			bucketList.add(bucket.getName());
		}
		return bucketList;
	}

	public static String getMetaDataPath(Connection conn, String src_sys_id, String run_id) throws SQLException {
		String metadata_path = "";
		String query = "select METADATA_PATH from JUNIPER_EXT_NIFI_STATUS where FEED_ID ="
				+ Integer.parseInt(src_sys_id) + " and run_id ='" + run_id + "' and UPPER(status)='SUCCESS'";
		Statement statement;
		try {
			statement = conn.createStatement();

			ResultSet rs = statement.executeQuery(query);
			if (rs.isBeforeFirst()) {

				rs.next();
				metadata_path = rs.getString(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return metadata_path;
	}
	
	
	public static int getPubFeedId(Connection conn, String pub_feed_name) throws SQLException {
		int pub_feed_sequence = 0 ;
		String query = "select PUB_FEED_SEQUENCE from JUNIPER_PUB_FEED_DTLS where PUB_FEED_NAME ='"+pub_feed_name  + "'" ;
		Statement statement;
		try {
			statement = conn.createStatement();

			ResultSet rs = statement.executeQuery(query);
			if (rs.isBeforeFirst()) {

				rs.next();
				pub_feed_sequence = rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return pub_feed_sequence;
	}
	

	
	public static int getPublishingFeedId(Connection conn, String googleProjectName, String targetDSName) throws SQLException {
		int pub_feed_sequence = 0 ;
		String query = "select PUB_FEED_SEQUENCE from JUNIPER_PUB_FEED_DTLS where GCP_PROJ_NAME='"+googleProjectName+"' and FEED_TGT_DS='"+targetDSName+"'";
		Statement statement;
		try {
			statement = conn.createStatement();

			ResultSet rs = statement.executeQuery(query);
			if (rs.isBeforeFirst()) {

				rs.next();
				pub_feed_sequence = rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return pub_feed_sequence;
	}
	
	
	public static String getFeedExtractionType(Connection conn, String feedName) throws SQLException {
		String feedExtractionType = "" ;
		String query = "select EXTRACTION_TYPE from JUNIPER_EXT_FEED_MASTER where FEED_UNIQUE_NAME='"+feedName+"'"; // and FEED_TGT_DS='"+targetDSName+"'";
		Statement statement;
		try {
			statement = conn.createStatement();

			ResultSet rs = statement.executeQuery(query);
			if (rs.isBeforeFirst()) {

				rs.next();
				feedExtractionType = rs.getString(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return feedExtractionType;
	}
	

public static void saveMetadataChanges(List<MetadataChangeDto> dtos, Connection conn)throws SQLException{
	
	String fileTableName = "JUNIPER_PUB_FEED_FILE_DTLS";
	String fieldTableName = "JUNIPER_PUB_FEED_FLD_DTLS";
	for (MetadataChangeDto dto :dtos) {
	String query = "UPDATE "+fileTableName+" set tgt_tbl_name = '" +dto.getTgtTableName()+"' where FEED_TABLE_id ='"+dto.getSrcTableName()+"' and PUB_FEED_SEQUENCE="+dto.getFeedId();
	System.out.println(query);
	LOG.info(query);
	Statement statement = conn.createStatement();
	statement.execute(query);
	for(String key : dto.getFields().keySet()) {
		String query1 = "UPDATE "+fieldTableName+" set trg_fld_data_typ = '" +dto.getFields().get(key)+"' where FEED_fld_name = '"+key+ "' and FEED_TABLE_id ='"+dto.getSrcTableName()+"' and PUB_FEED_SEQUENCE="+dto.getFeedId();
		System.out.println(query1);
		statement.execute(query1);
	}
	}
	LOG.info("Metadata Changes updated Successfully");
}

public static String insertPartitionInfo(Connection conn,String feed_id,List<FilePartitionDetailsDto> partitionDetails) throws SQLException {
	//pub_tgt_partition
	String tableName = "JUNIPER_pub_tgt_partition";
	Date d = new Date();
	for (FilePartitionDetailsDto dto :partitionDetails) {
		try {
			String query = INSERTQUERY.replace("{$table}", tableName).replace("{$columns}",
					"PUB_FEED_SEQUENCE,FEED_TABLE_id,publishing_type,partition_column,crtd_dt,uptd_dt,crtd_by,uptd_by")
					.replace("{$data}", feed_id + COMMA + QUOTE + dto.getFile_name() + QUOTE + COMMA + QUOTE + dto.getPublishing_type() + QUOTE + COMMA + QUOTE + dto.getPartition_key() + QUOTE 
							+ COMMA + QUOTE + dateFormat.format(d) + QUOTE + COMMA +QUOTE + dateFormat.format(d) + QUOTE
							+ COMMA + QUOTE + "ADMIN" + QUOTE + COMMA + QUOTE + "ADMIN" + QUOTE );
			LOG.info(query);
			Statement statement = conn.createStatement();

			statement.execute(query);
		}
		catch(SQLIntegrityConstraintViolationException e) {
			System.out.println("Data Already There");
			String query = "UPDATE "+tableName+" set publishing_type='"+dto.getPublishing_type()+"' , partition_column='"+dto.getPartition_key()+"' where PUB_FEED_SEQUENCE ="+feed_id+" and FEED_TABLE_id='"+dto.getFile_name()+"'";
			LOG.info(query);
			Statement statement = conn.createStatement();
			statement.execute(query);
		}
	
	}
	LOG.info("Partition Info Added for This Feed");
	
	
	return "Partition Info Added for This Feed";
}


public static void updateDataType(DataTypeMappingDto dto, Connection conn)throws SQLException{
	String tableName = "mstr_datatype_link_dtls";
	String src_db_type = dto.getSrc_type();
	String tgt_db_type = dto.getTgt_type();
	Map<String,String> mapping = dto.getDatatypeMapping();
	for(String key: mapping.keySet()) {
		String query = "UPDATE "+tableName+" set tgt_data_typ = '" +mapping.get(key)+"' where src_data_typ ='"+key+"' and src_db_typ='"+src_db_type+"' and tgt_db_typ='"+tgt_db_type+"'";
		System.out.println(query);
		LOG.info(query);
		Statement statement = conn.createStatement();
		statement.execute(query);
	}
	LOG.info("Datatype updated Successfully");
}



public static String insertScheduleMetadata(Connection conn,String feed_name, String gcp_name, String tgt_name, String project,String cron) throws SQLException {

	Statement statement=conn.createStatement();
	String insertQuery="";
	String hourlyFlag="";
	String dailyFlag="";
	String monthlyFlag="";
	String weeklyFlag="";
	String[] temp=cron.split(" ");
	String minutes=temp[0];
	String hours=temp[1];
	String dates=temp[2];
	String months=temp[3];
	String daysOfWeek=temp[4];
	int project_sequence=0;

	project_sequence=getProjectSequence(conn, project);
	
	if(hours.equals("*")&&dates.equals("*")&&months.equals("*")&&(daysOfWeek.equals("*")) ) {
		hourlyFlag="Y";
	}
	if(dates.equals("*")&&months.equals("*")&&daysOfWeek.equals("*")&&!hours.equals("*")&&!minutes.equals("*")) {
		System.out.println("this is a dailyBatch");
		dailyFlag="Y";
	}
	if(months.equals("*")&&daysOfWeek.equals("*")&&!dates.equals("*")&&!hours.equals("*")&&!minutes.equals("*")) {
		System.out.println("this is a monthlyBatch");
		monthlyFlag="Y";
	}
	if(dates.equals("*")&&months.equals("*")&&!minutes.equals("*")&&!hours.equals("*")&&!daysOfWeek.equals("*")) {
		weeklyFlag="Y";
		System.out.println("this is weeklyBatch");
	}

	try {
		if(dailyFlag.equalsIgnoreCase("Y")) {
			if(hours.contains(",")) {
				for(String hour:hours.split(",")) {
					if(minutes.contains(",")) {
						for(String minute:minutes.split(",")) {
							insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
									.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
									.replace("{$data}",OracleConstants.QUOTE+feed_name+"_dailyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+"_dailyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+hour+":"+minute+":00"+OracleConstants.QUOTE
											+OracleConstants.COMMA+project_sequence);
							System.out.println(insertQuery);
							statement.execute(insertQuery);


						}
					}else {
						insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
								.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
								.replace("{$data}",OracleConstants.QUOTE+feed_name+"_dailyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+feed_name+"_dailyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+hour+":"+minutes+":00"+OracleConstants.QUOTE
										+OracleConstants.COMMA+project_sequence);;
										System.out.println(insertQuery);
										statement.execute(insertQuery);
					}
				}
			}else {
				if(minutes.contains(",")) {
					for(String minute:minutes.split(",")) {
						insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
								.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
								.replace("{$data}",OracleConstants.QUOTE+feed_name+"_dailyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+feed_name+"_dailyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+hours+":"+minute+":00"+OracleConstants.QUOTE
										+OracleConstants.COMMA+project_sequence);
						System.out.println(insertQuery);
						statement.execute(insertQuery);


					}
				}else {
					insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
							.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
							.replace("{$data}",OracleConstants.QUOTE+feed_name+"_dailyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
									+OracleConstants.QUOTE+feed_name+"dailyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
									+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
									+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
									+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
									+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
									+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
									+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
									+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
									+OracleConstants.QUOTE+hours+":"+minutes+":00"+OracleConstants.QUOTE
									+OracleConstants.COMMA+project_sequence);
					System.out.println(insertQuery);
					statement.execute(insertQuery);
				}
			}
		} if(monthlyFlag.equalsIgnoreCase("Y")) {
			if(dates.contains(",")) {
				for(String date:dates.split(",")) {
					if(hours.contains(",")) {
						for(String hour:hours.split(",")) {
							if(minutes.contains(",")) {
								for(String minute:minutes.split(",")) {
									insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
											.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
											.replace("{$data}",OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+date+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+hour+":"+minute+":00"+OracleConstants.QUOTE
													+OracleConstants.COMMA+project_sequence);
									System.out.println(insertQuery);
									statement.execute(insertQuery); 
								}

							}
							else {
								insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
										.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
										.replace("{$data}",OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+date+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+hour+":"+minutes+":00"+OracleConstants.QUOTE
												+OracleConstants.COMMA+project_sequence);
								System.out.println(insertQuery);
								statement.execute(insertQuery);
							}
						}




					}else {
						if(minutes.contains(",")) {
							for(String minute:minutes.split(",")) {
								insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
										.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
										.replace("{$data}",OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+date+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+hours+":"+minute+":00"+OracleConstants.QUOTE
												+OracleConstants.COMMA+project_sequence);
								System.out.println(insertQuery);
								statement.execute(insertQuery); 
							}

						} 			else {
							insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
									.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
									.replace("{$data}",OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+date+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+hours+":"+minutes+":00"+OracleConstants.QUOTE
											+OracleConstants.COMMA+project_sequence);
							System.out.println(insertQuery);
							statement.execute(insertQuery); 
						}	
					}
				}
			}
			else {
				if(hours.contains(",")) {
					for(String hour:hours.split(",")) {
						if(minutes.contains(",")) {
							for(String minute:minutes.split(",")) {
								insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
										.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
										.replace("{$data}",OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+OracleConstants.COMMA
												+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+dates+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+hour+":"+minute+":00"+OracleConstants.QUOTE
												+OracleConstants.COMMA+project_sequence);
								System.out.println(insertQuery);
								statement.execute(insertQuery); 
							}

						}
						else {
							insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
									.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
									.replace("{$data}",OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+dates+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+hour+":"+minutes+":00"+OracleConstants.QUOTE
											+OracleConstants.COMMA+project_sequence);
							System.out.println(insertQuery);
							statement.execute(insertQuery);
						}
					}




				}else {
					if(minutes.contains(",")) {
						for(String minute:minutes.split(",")) {
							insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
									.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
									.replace("{$data}",OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+dates+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+hours+":"+minute+":00"+OracleConstants.QUOTE
											+OracleConstants.COMMA+project_sequence);
							System.out.println(insertQuery);
							statement.execute(insertQuery); 
						}

					}else {
						insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
								.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
								.replace("{$data}",OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+feed_name+"_monthlyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+dates+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+hours+":"+minutes+":00"+OracleConstants.QUOTE
										+OracleConstants.COMMA+project_sequence);
						System.out.println(insertQuery);
						statement.execute(insertQuery);
					}	
				}
			}
		}if(weeklyFlag.equalsIgnoreCase("Y")) {
			if(daysOfWeek.contains(",")) {
				for(String day:daysOfWeek.split(",")) {
					if(hours.contains(",")) {
						for(String hour:hours.split(",")) {
							if(minutes.contains(",")) {
								for(String minute:minutes.split(",")) {
									insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
											.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
											.replace("{$data}",OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+day+OracleConstants.QUOTE+OracleConstants.COMMA
													+OracleConstants.QUOTE+hour+":"+minute+":00"+OracleConstants.QUOTE
													+OracleConstants.COMMA+project_sequence);
									statement.execute(insertQuery);
								}
							}else {
								insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
										.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
										.replace("{$data}",OracleConstants.QUOTE+feed_name+"_weekPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+day+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+hour+":"+minutes+":00"+OracleConstants.QUOTE
												+OracleConstants.COMMA+project_sequence);
								statement.execute(insertQuery);
							}
						}
					}else {

						if(minutes.contains(",")) {
							for(String minute:minutes.split(",")) {
								insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
										.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
										.replace("{$data}",OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+day+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+hours+":"+minute+":00"+OracleConstants.QUOTE
												+OracleConstants.COMMA+project_sequence);
								statement.execute(insertQuery);
							}
						}else {
							insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
									.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
									.replace("{$data}",OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+day+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+hours+":"+minutes+":00"+OracleConstants.QUOTE
											+OracleConstants.COMMA+project_sequence);
							statement.execute(insertQuery);
						}
					}
				}
			}else {
				if(hours.contains(",")) {
					for(String hour:hours.split(",")) {
						if(minutes.contains(",")) {
							for(String minute:minutes.split(",")) {
								insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
										.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
										.replace("{$data}",OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+daysOfWeek+OracleConstants.QUOTE+OracleConstants.COMMA
												+OracleConstants.QUOTE+hour+":"+minute+":00"+OracleConstants.QUOTE
												+OracleConstants.COMMA+project_sequence);
								statement.execute(insertQuery);
							}
						}else {
							insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
									.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
									.replace("{$data}",OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+daysOfWeek+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+hour+":"+minutes+":00"+OracleConstants.QUOTE
											+OracleConstants.COMMA+project_sequence);
							statement.execute(insertQuery);
						}
					}
				}else {

					if(minutes.contains(",")) {
						for(String minute:minutes.split(",")) {
							insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
									.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
									.replace("{$data}",OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+daysOfWeek+OracleConstants.QUOTE+OracleConstants.COMMA
											+OracleConstants.QUOTE+hours+":"+minute+":00"+OracleConstants.QUOTE
											+OracleConstants.COMMA+project_sequence);
							statement.execute(insertQuery);
						}
					}else {
						insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
								.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,daily_flag,job_schedule_time,project_id")
								.replace("{$data}",OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+feed_name+"_weeklyPublish"+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+gcp_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+daysOfWeek+OracleConstants.QUOTE+OracleConstants.COMMA
										+OracleConstants.QUOTE+hours+":"+minutes+":00"+OracleConstants.QUOTE
										+OracleConstants.COMMA+project_sequence);
						statement.execute(insertQuery);
					}
				}
			}
		}
	}catch(SQLException e) {
		return e.getMessage();
	}finally {
		conn.close();
	}
	return "success";
}

public static String insertOnDemandScheduleMetadata(Connection conn,String feed_name, String feed_id, String gcp_details, String project, String run_id) throws SQLException {
	Statement statement=conn.createStatement();
	String insertQuery="";
	int project_sequence=0;
	project_sequence=getProjectSequence(conn, project);
	
	try {

	insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
			.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,argument_2,argument_3,argument_4,argument_5,daily_flag,job_schedule_time,project_id,feed_id,SCHEDULE_TYPE")
			.replace("{$data}",OracleConstants.QUOTE+feed_name+"_OneTimePublish"+OracleConstants.QUOTE+OracleConstants.COMMA
					+OracleConstants.QUOTE+feed_name+"_OneTimePublish"+OracleConstants.QUOTE+OracleConstants.COMMA
					+OracleConstants.QUOTE+feed_name+"_OneTimePublish"+OracleConstants.QUOTE+OracleConstants.COMMA
					+OracleConstants.QUOTE+script_loc+OracleConstants.QUOTE+OracleConstants.COMMA
					+OracleConstants.QUOTE+feed_name+OracleConstants.QUOTE+OracleConstants.COMMA
					+OracleConstants.QUOTE+gcp_details +OracleConstants.QUOTE+OracleConstants.COMMA
					//+OracleConstants.QUOTE+tgt_name+OracleConstants.QUOTE+OracleConstants.COMMA
					+OracleConstants.QUOTE+project_sequence+OracleConstants.QUOTE+OracleConstants.COMMA
					+OracleConstants.QUOTE+run_id+OracleConstants.QUOTE+OracleConstants.COMMA
					+OracleConstants.QUOTE+"O"+OracleConstants.QUOTE+OracleConstants.COMMA
					+OracleConstants.QUOTE+"Y"+OracleConstants.QUOTE+OracleConstants.COMMA
					+OracleConstants.QUOTE+"00:00:00"+OracleConstants.QUOTE
					+OracleConstants.COMMA+project_sequence+OracleConstants.COMMA
					+OracleConstants.QUOTE+feed_id+OracleConstants.QUOTE+OracleConstants.COMMA
			+OracleConstants.QUOTE+"O"+OracleConstants.QUOTE);
	
	
	System.out.println(insertQuery);
	statement.execute(insertQuery);
	}
	catch(SQLException e) {
		throw e;
	}finally {
		conn.close();
	}
	
	return "success";
}


public static void insertScheduleMetadataWithDependent(Connection conn,String extractFeedSequence,String pubFeedSequence, String feedUniqueName,String gcpName,String saNAme, String projectId)throws Exception{
	
	ScheduleJobDetails dto = getExtractionJobDetails(conn,extractFeedSequence,projectId);
	String insertQuery="";
	try {
		Statement statement=conn.createStatement();
		insertQuery=OracleConstants.INSERTQUERY.replace("{$table}", OracleConstants.SCHEDULETABLE)
				.replace("{$columns}", "job_id,job_name,batch_id,command,argument_1,ARGUMENT_2,ARGUMENT_3,ARGUMENT_4, ARGUMENT_5, PREDESSOR_JOB_ID_1, IS_DEPENDENT_JOB, COMMAND_TYPE, PROJECT_ID, FEED_ID , JOB_ID,DAILY_FLAG,WEEKLY_FLAG,MONTHLY_FLAG,YEARLY_FLAG,JOB_SCHEDULE_TIME,WEEK_RUN_DAY,MONTH_RUN_VAL,MONTH_RUN_DAY,WEEK_NUM_MONTH")
				.replace("{$data}",OracleConstants.QUOTE+feedUniqueName+"_PublishFollowedByExtract"+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+feedUniqueName+"_PublishFollowedByExtract"+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+feedUniqueName+"_PublishFollowedByExtract"+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+script_loc_dependent+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+pubFeedSequence+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+feedUniqueName+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+gcpName+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+saNAme+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+projectId+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+"PREDESSOR_JOB_ID_1"+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+"YES"+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+"shell"+OracleConstants.QUOTE
						+OracleConstants.COMMA+projectId+OracleConstants.COMMA
						+OracleConstants.QUOTE+"FeedID"+OracleConstants.QUOTE+OracleConstants.COMMA
						
						+OracleConstants.QUOTE+dto.getJOB_ID()+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+dto.getDAILY_FLAG()+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+dto.getWEEKLY_FLAG()+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+dto.getMONTHLY_FLAG()+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+dto.getYEARLY_FLAG()+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+dto.getJOB_SCHEDULE_TIME()+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+dto.getWEEK_RUN_DAY()+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+dto.getMONTH_RUN_VAL()+OracleConstants.QUOTE+OracleConstants.COMMA
						+OracleConstants.QUOTE+dto.getMONTH_RUN_DAY()+OracleConstants.QUOTE+OracleConstants.COMMA
						+dto.getWEEK_NUM_MONTH()
						);
		System.out.println(insertQuery);
		statement.execute(insertQuery);
	}
	catch(Exception e) {
		e.printStackTrace();
		throw e;
	}
}

private static ScheduleJobDetails getExtractionJobDetails(Connection conn, String feedId, String ProjectId) throws SQLException {
	
	ScheduleJobDetails dto = new ScheduleJobDetails();
	String query = "select JOB_ID,DAILY_FLAG,WEEKLY_FLAG,MONTHLY_FLAG,YEARLY_FLAG,JOB_SCHEDULE_TIME,WEEK_RUN_DAY,MONTH_RUN_VAL,MONTH_RUN_DAY,WEEK_NUM_MONTH from JUNIPER_SCH_MASTER_JOB_DETAIL jd, JUNIPER_PROJECT_MASTER pm where jd.PROJECT_SEQUENCE = pm.PROJECT_ID and jd.FEED_ID='"+feedId+"' and pm.PROJECT_ID='" +ProjectId +"'"; // and FEED_TGT_DS='"+targetDSName+"'";
	Statement statement;
	try {
		statement = conn.createStatement();

		ResultSet rs = statement.executeQuery(query);
		if (rs.isBeforeFirst()) {
			rs.next();
			dto.setJOB_ID(rs.getString(1));
			dto.setDAILY_FLAG(rs.getString(2));
			dto.setWEEKLY_FLAG(rs.getString(3));
			dto.setMONTHLY_FLAG(rs.getString(4));
			dto.setYEARLY_FLAG(rs.getString(5));
			dto.setJOB_SCHEDULE_TIME(rs.getString(6));
			dto.setWEEK_RUN_DAY(rs.getString(7));
			dto.setMONTH_RUN_VAL(rs.getString(9));
			dto.setMONTH_RUN_DAY(rs.getString(1));
			dto.setWEEK_NUM_MONTH(rs.getInt(1));
		}

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw e;
	}
	return dto;
}

}
		
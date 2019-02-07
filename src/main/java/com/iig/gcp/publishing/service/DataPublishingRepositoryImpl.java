package com.iig.gcp.publishing.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iig.gcp.utils.ConnectionUtils;
import com.iig.gcp.utils.DBUtils;

@Repository
public class DataPublishingRepositoryImpl implements IDataPublishingRepository{

	
	@Autowired
	private ConnectionUtils connectionUtils;
	
	@Autowired
	private CSVReader csvReader;
	
	
	@Override
	public int addSystemMetaData( String user_Name, String target_type,
			String target_dataset, boolean extracted_Source, String service_account_name, String google_proj_name, String ext_feed_id,String pub_feed_name, int project_sequence) throws Exception {
		System.out.println("Inside addSystemMetaData Repository");
		int src_sys_id = 0;
		//String gsFileDetailsPath = systemMetaDataPath;
		/*if (gsFileDetailsPath != null && !gsFileDetailsPath.isEmpty()) {
			String[] arr = gsFileDetailsPath.split("/", 4);
			if (arr[2] != null && arr[3] != null && !arr[2].isEmpty() && !arr[3].isEmpty()) {
				Blob blob;
				try {
					//blob = cloudUtil.getStorageService(service_account_name).get(BlobId.of(arr[2], arr[3]));
					blob = StorageOptions.getDefaultInstance().getService().get(BlobId.of(arr[2], arr[3]));
					src_sys_id = csvReader.readSystem(blob.getBlobId(), user_Name, target_type, target_dataset, extracted_Source, service_account_name,google_proj_name,ext_feed_id,pub_feed_name, project_sequence);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}

		}*/
		src_sys_id = csvReader.readSystem( user_Name, target_type, target_dataset, extracted_Source, service_account_name,google_proj_name,ext_feed_id,pub_feed_name, project_sequence);
		return src_sys_id;
	}

	@Override
	public void addFileMetaData(String user_Name, String service_account_name ,int pub_feed_id, int project_sequence, String ext_feed_id) throws Exception {
		//String gsFileDetailsPath = fileMetaDataPath;
		/*if (gsFileDetailsPath != null && !gsFileDetailsPath.isEmpty()) {
			String[] arr = gsFileDetailsPath.split("/", 4);
			if (arr[2] != null && arr[3] != null && !arr[2].isEmpty() && !arr[3].isEmpty()) {
				Blob blob;
				try {
					//blob = cloudUtil.getStorageService(service_account_name).get(BlobId.of(arr[2], arr[3]));
					blob = StorageOptions.getDefaultInstance().getService().get(BlobId.of(arr[2], arr[3]));
					csvReader.readFile(blob.getBlobId(), user_Name, service_account_name,  pub_feed_id, project_sequence);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
		}*/
		csvReader.readFile( user_Name, service_account_name,  pub_feed_id, project_sequence, ext_feed_id);
	}

	@Override
	public void addFieldMetaData( String user_Name, boolean extracted_Source, String service_account_name, int pub_feed_id,String ext_feed_id) throws Exception {

		//String gsFieldDetailsPath = fieldMetaDataPath;
		/*if (gsFieldDetailsPath != null && !gsFieldDetailsPath.isEmpty()) {
			String[] arr = gsFieldDetailsPath.split("/", 4);
			if (arr[2] != null && arr[3] != null && !arr[2].isEmpty() && !arr[3].isEmpty()) {
				try {
					//Blob blob = cloudUtil.getStorageService(service_account_name).get(BlobId.of(arr[2], arr[3]));
					Blob blob = StorageOptions.getDefaultInstance().getService().get(BlobId.of(arr[2], arr[3]));
					csvReader.readField(blob.getBlobId(), user_Name, extracted_Source, service_account_name, pub_feed_id, ext_feed_id);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
		}*/
		
		csvReader.readField( user_Name, extracted_Source, service_account_name, pub_feed_id, ext_feed_id);
		
	}

	@Override
	public String getMetaDataPath(String src_sys_id, String run_id) throws Exception {
		System.out.println("System id selected is :  " + src_sys_id);
		String path = "";
		if (src_sys_id != null) {

			Connection conn = null;
			try {
				//conn = ConnectionUtils.connectToMySql(mysql_ip, mysql_port, db_name, user, password);
				conn = connectionUtils.getConnection();
				path = DBUtils.getMetaDataPath(conn, src_sys_id, run_id);
				//ConnectionUtils.closeQuietly(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				conn.close();
			}

		}
		return path;

	}
	
	
	@Override
	public void saveMetadataChanges(List<MetadataChangeDto> dtos)throws Exception{
			Connection conn = null;
			try {
				conn = connectionUtils.getConnection();
				DBUtils.saveMetadataChanges(dtos, conn);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finally {
				conn.close();
			}

		
	}
	
	
	@Override
	public void savePartitionDetails(String feed_id, List<FilePartitionDetailsDto> partitionDetails)throws Exception{
		Connection conn;
		conn = connectionUtils.getConnection();
		try {
		//insert metadata change info for this feed id
		//DBUtils.insertMetadataChangeInfo(conn,feed_id,allow_table_addition,allow_column_addition_removal);
		//insert partition info for this feed id
		DBUtils.insertPartitionInfo(conn,feed_id,partitionDetails);
		}catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	@Override
	public void updateDataType(DataTypeMappingDto dto)throws Exception{
		Connection conn = null;
		try {
			conn = connectionUtils.getConnection();
			DBUtils.updateDataType(dto, conn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			conn.close();
		}
	}

	@Override
	public int getPublishingFeedId(String googleProjectName, String targetDSName) throws Exception {
		int  publishingFeedId = 0;

			Connection conn = null;
			try {
				//conn = ConnectionUtils.connectToMySql(mysql_ip, mysql_port, db_name, user, password);
				conn = connectionUtils.getConnection();
				publishingFeedId = DBUtils.getPublishingFeedId(conn, googleProjectName, targetDSName);
				//ConnectionUtils.closeQuietly(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				conn.close();
			}

		return publishingFeedId;
	}

	@Override
	public String getFeedExtractionType(String feedName) throws Exception {
		String  extractionType = "";

		Connection conn = null;
		try {
			//conn = ConnectionUtils.connectToMySql(mysql_ip, mysql_port, db_name, user, password);
			conn = connectionUtils.getConnection();
			extractionType = DBUtils.getFeedExtractionType(conn, feedName);
			//ConnectionUtils.closeQuietly(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			conn.close();
		}

	return extractionType;
	}

	@Override
	public void insertScheduleMetadataWithDependent(String extractFeedSequence,String pubFeedSequence, String feedUniqueName, String gcpName,
			String saNAme, String projectId) throws Exception {

		Connection conn = null;
		try {
			//conn = ConnectionUtils.connectToMySql(mysql_ip, mysql_port, db_name, user, password);
			conn = connectionUtils.getConnection();
			DBUtils.insertScheduleMetadataWithDependent(conn, extractFeedSequence,pubFeedSequence, feedUniqueName, gcpName,saNAme,projectId);
			//ConnectionUtils.closeQuietly(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			conn.close();
		}

		
	}
	
	
	/*@Override
	public void cleanSourceSystem(String src_sys_id) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("System id selected is :  " + src_sys_id);
		if (src_sys_id != null) {
			Connection conn = null;
			try {
				//conn = ConnectionUtils.connectToMySql(mysql_ip, mysql_port, db_name, user, password);
				conn = connectionUtils.getConnection();
				DBUtils.removeSourceSystem(conn, Integer.parseInt(src_sys_id));
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				conn.close();
			}

		}
		
	}

	@Override
	public void cleanPublishSourceSystem(String src_sys_id) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("System id selected is :  " + src_sys_id);
		if (src_sys_id != null) {
			Connection conn = null;
			try {
				//conn = ConnectionUtils.connectToMySql(mysql_ip, mysql_port, db_name, user, password);
				conn = connectionUtils.getConnection();
				DBUtils.removePublishSourceSystem(conn, Integer.parseInt(src_sys_id));
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finally {
				conn.close();
			}

		}
		
	}*/

}

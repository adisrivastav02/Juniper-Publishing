package com.iig.gcp.publishing.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/*import com.datapublish.dto.DataTypeMappingDto;
import com.datapublish.dto.FilePartitionDetailsDto;
import com.datapublish.dto.MetadataChangeDto;
import com.datapublish.dto.PublishDto;*/

public interface IDataPublishingRepository {

	//public String addSystemMetaData(String systemMetaDataPath, String user_Name,String target_type, String target_dataset, boolean extracted_Source, String service_account_name, String google_proj_name, String ext_feed_id) throws Exception;
	public int addSystemMetaData( String user_Name, String target_type,
			String target_dataset, boolean extracted_Source, String service_account_name, String google_proj_name, String ext_feed_id, String pub_feed_name, int project_sequence) throws Exception;
	public void addFileMetaData( String user_Name, String service_account_name,  int pub_feed_id, int project_sequence, String ext_feed_id)throws Exception;
	public void addFieldMetaData(String user_Name, boolean extracted_Source, String service_account_name, int pub_feed_id,String ext_feed_id)throws Exception;
	public void savePartitionDetails(String feed_id, List<FilePartitionDetailsDto> partitionDetails)throws Exception;
	//public String publishData(PublishDto dto) throws Exception;
	//public void refreshMetaData(String src_sys_id)throws Exception;
	
	public String getMetaDataPath(String src_sys_id, String run_id) throws SQLException;
	//public void cleanSourceSystem(String src_sys_id) throws SQLException;
	//public void cleanPublishSourceSystem(String src_sys_id) throws SQLException;
	
	//public void resetBigQuery(String datasetId, String tableId, String runId , String service_account_name) throws InterruptedException, IOException;
	
	public void saveMetadataChanges(List<MetadataChangeDto> dtos)throws SQLException;
	public void updateDataType(DataTypeMappingDto dto)throws Exception;
	public int getPublishingFeedId(String googleProjectName, String targetDSName)throws Exception;
	public String getFeedExtractionType(String feedName)throws Exception;
	public void insertScheduleMetadataWithDependent(String extractFeedSequence,String pubFeedSequence, String feedUniqueName,String gcpName,String saNAme, String projectId)throws Exception;
	
}
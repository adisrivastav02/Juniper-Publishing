package com.iig.gcp.publishing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;


public interface PublishingService {

		public String invokeRest(String json,String url) throws UnsupportedOperationException, Exception ;
		public String invokePythonRest(String json,String url) throws UnsupportedOperationException, Exception;
		public ArrayList<String> populateDatasets(String proj_id) throws Exception;
		public ArrayList<String> populateTables(String dataset);
		public ArrayList<String> populateRunIDs(String ds_name,String table_name);
		public Map<String, String> getSysIds(String project) throws Exception;
		public Map<String, String> getExSysIds() throws Exception;
		//public Map<String, String> getMDSysList() throws Exception;
		public ArrayList<String> getRunIds(Integer src_id) throws Exception;
		public Map<String, String> getRunIdsWithDate(Integer src_id ,String dateRangeText, String is_new, String pub_feed_id) throws Exception;
		public ArrayList<String> getMDFileList(Integer src_id) throws Exception;
		public SourceSystemBean getSourceSystemMetadata(int src_sys_id) throws Exception;
		public SourceSystemFileBean getSourceFileMetadata(int src_sys_id, String file_id) throws Exception;
		public List<SourceSystemFileBean> getSourceFiles(int src_sys_id) throws Exception;
		public List<SourceSystemFieldBean> getSourceFieldMetadata(int src_sys_id, String file_id) throws Exception;
		public Map<String, List<SourceSystemFieldBean>> getSourceFieldMetadata(int src_sys_id) throws Exception;
		public ArrayList<String> populateSrcDBList() throws Exception;
		public ArrayList<String> populateTgtDBList() throws Exception;
		public List<DataTypeLinkBean> getDataTypeLinkList(String src_db, String tgt_db) throws Exception;
		public String getFeedName(String feed_id) throws Exception;
		public Map<String, List<String>> getSourceFields(int src_sys_id) throws Exception;
		public Map<String, List<String>> getSourceAllFields(int src_sys_id) throws Exception;
		public Map<String, String>  getReconSysIds() throws Exception; 
		public ArrayList<ReconDashboardBean> reconDashData(String proj_id,String db_id,String run_id) throws Exception;
		public Map<String, String> getPubFeedIDs(String project) throws Exception;
		public ArrayList<String> populateGoogleProject(String project) throws Exception;
		public ArrayList<String> populateServiceAccList(@Valid String gcp_proj_id, String proj_id) throws Exception;
		public int addMetaDataForExtractedSource( RequestDto requestDto, int project_sequence)throws Exception;
		public String saveMetadataChanges(RequestDto requestDto) throws Exception ;
		public String savePartitionDetails( RequestDto requestDto) throws Exception;
		public int checkNames(@Valid String sun) throws Exception;
		public ExistingPubBean getExistingPubDetails(String pub_feed_id) throws Exception;
		public int getProjectSequence(String project) throws Exception;
		public String updateDataType(RequestDto requestDto) throws Exception;
		public String insertOnDemandScheduleMetadata(String feed_name, String feed_id, String gcp_details, String project, String run_id)throws Exception;
		public String insertScheduleMetadata(String feed_name, String gcp_name, String tgt_name,String project,String cron) throws Exception;
		public void insertScheduleMetadataWithDependent(String extractFeedSequence,String pubFeedSequence, String feedUniqueName,String gcpName,String saNAme, String projectId)throws Exception;
		public int getPublishingFeedId(String googleProjectName, String targetDSName)throws Exception;
		public String getFeedExtractionType(String feedName)throws Exception;
		public ArrayList<String> getProjList() throws Exception;
		public ArrayList<String> getDBList(@Valid String proj_id) throws Exception;
		public ArrayList<String> reconRunIDs(@Valid String proj_id, @Valid String db_id) throws Exception;
	}

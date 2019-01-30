package com.iig.gcp.publishing.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;


public interface PublishingService {

		public String invokeRest(String json,String url) throws UnsupportedOperationException, Exception ;
		public String invokePythonRest(String json,String url) throws UnsupportedOperationException, Exception;
		public ArrayList<String> populateDatasets(String proj_id);
		public ArrayList<String> populateTables(String dataset);
		public ArrayList<String> populateRunIDs(String ds_name,String table_name);
		public Map<String, String> getSysIds(String project);
		public Map<String, String> getExSysIds();
		public Map<String, String> getMDSysList();
		public ArrayList<String> getRunIds(Integer src_id);
		public Map<String, String> getRunIdsWithDate(Integer src_id ,String dateRangeText, String is_new, String pub_feed_id);
		public ArrayList<String> getMDFileList(Integer src_id);
		public SourceSystemBean getSourceSystemMetadata(int src_sys_id);
		public SourceSystemFileBean getSourceFileMetadata(int src_sys_id, String file_id);
		public List<SourceSystemFileBean> getSourceFiles(int src_sys_id);
		public List<SourceSystemFieldBean> getSourceFieldMetadata(int src_sys_id, String file_id);
		public Map<String, List<SourceSystemFieldBean>> getSourceFieldMetadata(int src_sys_id);
		public ArrayList<String> populateSrcDBList();
		public ArrayList<String> populateTgtDBList();
		public List<DataTypeLinkBean> getDataTypeLinkList(String src_db, String tgt_db);
		public String getFeedName(String feed_id);
		public Map<String, List<String>> getSourceFields(int src_sys_id);
		public Map<String, List<String>> getSourceAllFields(int src_sys_id);
		public Map<String, String>  getReconSysIds(); 
		public ArrayList<String> reconRunIDs(Integer src_id);
		public ArrayList<ReconDashboardBean> reconDashData(Integer src_id, String run_id);
		public Map<String, String> getPubFeedIDs(String project);
		public ArrayList<String> populateGoogleProject(String project);
		public ArrayList<String> populateServiceAccList(@Valid String gcp_proj_id, String proj_id);
		public int addMetaDataForExtractedSource( RequestDto requestDto, int project_sequence)throws Exception;
		public String saveMetadataChanges(RequestDto requestDto) throws Exception ;
		public String savePartitionDetails( RequestDto requestDto) throws Exception;
		public int checkNames(@Valid String sun);
		public ExistingPubBean getExistingPubDetails(String pub_feed_id);
		public int getProjectSequence(String project);
		public String updateDataType(RequestDto requestDto) throws Exception;
		public String insertScheduleMetadata(String feed_name,String project,String cron) throws Exception;
		public void insertScheduleMetadataWithDependent(String extractFeedSequence,String pubFeedSequence, String feedUniqueName,String gcpName,String saNAme, String projectId)throws Exception;
		public int getPublishingFeedId(String googleProjectName, String targetDSName)throws Exception;
		public String getFeedExtractionType(String feedName)throws Exception;
	}

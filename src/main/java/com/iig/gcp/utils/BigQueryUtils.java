package com.iig.gcp.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.api.gax.paging.Page;
import com.google.auth.Credentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.FieldList;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.JobException;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.Table;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.iig.gcp.constants.MySqlConstants;

@Component
public class BigQueryUtils {

	public static ArrayList<String> getDataSet() {

		ArrayList<String> dataSetList = new ArrayList<String>();
		BigQuery bigquery = BigQueryOptions.getDefaultInstance()
				.toBuilder()
				.setProjectId(MySqlConstants.PROJECTNAME)
				.build()
				.getService();
		
		for (Dataset dataset : bigquery.listDatasets().iterateAll()) {
			String dataset_name=dataset.getDatasetId().getDataset();
			dataSetList.add(dataset_name);
		}
		return dataSetList;
	}

	public static ArrayList<String> getTables(String dataset) {

		ArrayList<String> tableList = new ArrayList<String>();
		BigQuery bigquery = BigQueryOptions.getDefaultInstance()
				.toBuilder()
				.setProjectId(MySqlConstants.PROJECTNAME)
				.build()
				.getService();
		Iterator<Table> tables=bigquery.listTables(dataset).iterateAll().iterator();
		while(tables.hasNext()){
			Table tableId = tables.next();
			String table_name=tableId.getTableId().getTable();
			tableList.add(table_name);
		}
		return tableList;
	}

	public static ArrayList<Map<String, String>> getTableData(String dataset, String tableName) {
		ArrayList<Map<String, String>> sampleDataList=new ArrayList<Map<String, String>>();
		HashMap<String, String> sampleDataMap=new HashMap<String, String>();
		String query = "SELECT * FROM " + dataset + "." + tableName + " LIMIT 6";
		System.out.println("Query To Fetch the Sample Records: " + query);
		try {
			BigQuery bigquery = BigQueryOptions.getDefaultInstance().toBuilder()
					.setProjectId(MySqlConstants.PROJECTNAME).build().getService();
			QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
			ArrayList<String> fieldList = getFields(dataset, tableName);
			for (FieldValueList row : bigquery.query(queryConfig).iterateAll()) {
				for (int j = 0; j < fieldList.size(); j++) {
					for (int i = 0; i < row.size(); i++) {
						if (i == j) {
							sampleDataMap.put(fieldList.get(j), row.get(i).getValue().toString());
						}
					}
				}
				sampleDataList.add(sampleDataMap);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JobException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("before :"+sampleDataList.get(0));
		return sampleDataList;
	}

	public static ArrayList<String> getFields(String dataset, String tablename) {

		//TODO: Update project name information
		ArrayList<String> fieldList = new ArrayList<String>();
		BigQuery bigquery = BigQueryOptions.getDefaultInstance()
				.toBuilder()
				.setProjectId(MySqlConstants.PROJECTNAME)
				.build()
				.getService();

		FieldList fields = bigquery.getTable(dataset, tablename).getDefinition().getSchema().getFields();
		for (Field field : fields) {
			fieldList.add(field.getName());	
		}
		return fieldList;
	}

}
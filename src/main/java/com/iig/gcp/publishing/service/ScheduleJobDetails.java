/**
 * 
 */
package com.iig.gcp.publishing.service;

/**
 * @author sivakumar.r14
 *
 */
public class ScheduleJobDetails {

	private String JOB_ID;
	private String DAILY_FLAG;
	private String WEEKLY_FLAG;
	private String MONTHLY_FLAG;
	private String YEARLY_FLAG;
	private String JOB_SCHEDULE_TIME;
	private String WEEK_RUN_DAY;
	private String MONTH_RUN_VAL;
	private String MONTH_RUN_DAY;
	private int WEEK_NUM_MONTH;
	
	public String getJOB_ID() {
		return JOB_ID;
	}
	public void setJOB_ID(String jOB_ID) {
		JOB_ID = jOB_ID;
	}
	public String getDAILY_FLAG() {
		return DAILY_FLAG;
	}
	public void setDAILY_FLAG(String dAILY_FLAG) {
		DAILY_FLAG = dAILY_FLAG;
	}
	public String getWEEKLY_FLAG() {
		return WEEKLY_FLAG;
	}
	public void setWEEKLY_FLAG(String wEEKLY_FLAG) {
		WEEKLY_FLAG = wEEKLY_FLAG;
	}
	public String getMONTHLY_FLAG() {
		return MONTHLY_FLAG;
	}
	public void setMONTHLY_FLAG(String mONTHLY_FLAG) {
		MONTHLY_FLAG = mONTHLY_FLAG;
	}
	public String getYEARLY_FLAG() {
		return YEARLY_FLAG;
	}
	public void setYEARLY_FLAG(String yEARLY_FLAG) {
		YEARLY_FLAG = yEARLY_FLAG;
	}
	
	public String getJOB_SCHEDULE_TIME() {
		return JOB_SCHEDULE_TIME;
	}
	public void setJOB_SCHEDULE_TIME(String jOB_SCHEDULE_TIME) {
		JOB_SCHEDULE_TIME = jOB_SCHEDULE_TIME;
	}
	
	
	public String getWEEK_RUN_DAY() {
		return WEEK_RUN_DAY;
	}
	public void setWEEK_RUN_DAY(String wEEK_RUN_DAY) {
		WEEK_RUN_DAY = wEEK_RUN_DAY;
	}
	public String getMONTH_RUN_VAL() {
		return MONTH_RUN_VAL;
	}
	public void setMONTH_RUN_VAL(String mONTH_RUN_VAL) {
		MONTH_RUN_VAL = mONTH_RUN_VAL;
	}
	public String getMONTH_RUN_DAY() {
		return MONTH_RUN_DAY;
	}
	public void setMONTH_RUN_DAY(String mONTH_RUN_DAY) {
		MONTH_RUN_DAY = mONTH_RUN_DAY;
	}
	public int getWEEK_NUM_MONTH() {
		return WEEK_NUM_MONTH;
	}
	public void setWEEK_NUM_MONTH(int wEEK_NUM_MONTH) {
		WEEK_NUM_MONTH = wEEK_NUM_MONTH;
	}
	
	
	
}

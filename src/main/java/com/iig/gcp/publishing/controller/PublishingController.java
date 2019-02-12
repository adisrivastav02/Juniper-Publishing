package com.iig.gcp.publishing.controller;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iig.gcp.CustomAuthenticationProvider;
import com.iig.gcp.publishing.service.DataTypeLinkBean;
import com.iig.gcp.publishing.service.PublishingService;
import com.iig.gcp.publishing.service.ReconDashboardBean;
import com.iig.gcp.publishing.service.RequestDto;
import com.iig.gcp.publishing.service.SourceSystemBean;
import com.iig.gcp.publishing.service.SourceSystemFieldBean;
import com.iig.gcp.publishing.service.SourceSystemFileBean;


@Controller
@SessionAttributes(value = { "user_name",  "project" , "jwt"})
public class PublishingController {

	@Autowired
	private PublishingService ps;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Value( "${parent.front.micro.services}" )
	private String parent_micro_services;
	
	@RequestMapping(value = {"/parent"}, method = RequestMethod.GET)
	public ModelAndView parentHome(ModelMap modelMap,HttpServletRequest request, Authentication auth) throws JSONException {
		CustomAuthenticationProvider.MyUser m = (CustomAuthenticationProvider.MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		JSONObject jsonObject= new JSONObject();
		jsonObject.put("userId", m.getName());
		jsonObject.put("project", m.getProject());
		jsonObject.put("jwt", m.getJwt());
		//response.getWriter().write(jsonObject.toString());
		modelMap.addAttribute("jsonObject",jsonObject.toString());
		return new ModelAndView("redirect:" + "//"+parent_micro_services+"/fromChild", modelMap);
		//System.out.println(m.getJwt());
		//return null;
		
	}
	private void authenticationByJWT(String name, String token) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(name, token);
        Authentication authenticate = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
	}
	
	@RequestMapping(value = { "/", "/login"}, method = RequestMethod.GET)
	public ModelAndView unixExtractionHome(@Valid @ModelAttribute("jsonObject") String jsonObject,ModelMap modelMap,HttpServletRequest request) throws JSONException {
		
		//Validate the token at the first place
		JSONObject jsonModelObject = null;
		try {
		
		if(modelMap.get("jsonObject")== null || modelMap.get("jsonObject").equals("")) {
			//TODO: Redirect to Access Denied Page
			return new ModelAndView("/login");
		}
		jsonModelObject = new JSONObject( modelMap.get("jsonObject").toString());
		authenticationByJWT(jsonModelObject.get("userId").toString()+":"+jsonModelObject.get("project").toString(), jsonModelObject.get("jwt").toString());
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("/login");
			//redirect to Login Page
		}
		request.getSession().setAttribute("user_name", jsonModelObject.get("userId"));
		request.getSession().setAttribute("project", jsonModelObject.get("project"));
		
		return new ModelAndView("/index");
	}
	
	
	@RequestMapping(value="/publishing/publishingHome",method=RequestMethod.GET)
	public ModelAndView publishingHome() {
		return new ModelAndView("/publishing/publishingHome");
	}
	
	@RequestMapping(value="/publishing/resetMetadataHome",method=RequestMethod.GET)
	public ModelAndView resetMetadataHome(@Valid ModelMap model,HttpServletRequest request) {
		ArrayList<String> allDatabases;
		try {
			allDatabases = ps.populateDatasets((String)request.getSession().getAttribute("project"));
			model.addAttribute("allDatabases", allDatabases);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("/publishing/resetMetadataHome");
	}
	
/*	@RequestMapping(value="/publishing/viewMetadataHome",method=RequestMethod.GET)
		public ModelAndView viewMetadataHome(@Valid ModelMap model) {
		Map<String,String> viewSSID = ps.getMDSysList();
		//get Source DB list
		ArrayList<String> srcDBList= ps.populateSrcDBList();
		ArrayList<String> tgtDBList= ps.populateTgtDBList();
		model.addAttribute("viewSSID", viewSSID);
		model.addAttribute("srcDBList", srcDBList);
		model.addAttribute("tgtDBList", tgtDBList);
		return new ModelAndView("/publishing/viewMetadataHome");
	}
	
	*/
	@RequestMapping(value="/publishing/editMetadataHome",method=RequestMethod.GET)
	public ModelAndView editMetadataHome(@Valid ModelMap model) {
		ArrayList<String> srcDBList;
		try {
			srcDBList = ps.populateSrcDBList();
			ArrayList<String> tgtDBList= ps.populateTgtDBList();
		//Map<String,String> editSSID = ps.getMDSysList();
		//model.addAttribute("editSSID", editSSID);
		model.addAttribute("srcDBList", srcDBList);
		model.addAttribute("tgtDBList", tgtDBList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("/publishing/editMetadataHome");
	}
	
	@RequestMapping(value="/publishing/reconDashboard",method=RequestMethod.GET)
	public ModelAndView reconDashboard(@Valid ModelMap model,HttpServletRequest request) {
		ArrayList<String> proj_list;
		try {
			proj_list = ps.getProjList();
			model.addAttribute("proj_list", proj_list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	return new ModelAndView("/publishing/reconDashboard");
	}
	
	@RequestMapping(value="/publishing/reconDSList",method=RequestMethod.POST)
	public ModelAndView reconDSList(@Valid @ModelAttribute("proj_id")String proj_id,ModelMap model,HttpServletRequest request) {
		ArrayList<String> db_list;
		try {
			db_list = ps.getDBList(proj_id);
			model.addAttribute("db_list", db_list);
			model.addAttribute("proj_id", proj_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	return new ModelAndView("/publishing/reconDashboard0");
	}
		
	@RequestMapping(value="/publishing/publishingAddMetaData",method=RequestMethod.GET)
	public ModelAndView publishingAddMetadataHome(@Valid @ModelAttribute("tgt_val")String tgt_val, ModelMap model,HttpServletRequest request) {
		ArrayList<String> allDatabases;
		try {
			allDatabases = ps.populateDatasets((String)request.getSession().getAttribute("project"));
			ArrayList<String> googleProjectList= ps.populateGoogleProject((String)request.getSession().getAttribute("project"));
			Map<String,String> srcSysIds = ps.getSysIds((String)request.getSession().getAttribute("project"));
			Map<String,String> feedID = ps.getPubFeedIDs((String)request.getSession().getAttribute("project"));
			model.addAttribute("allDatabases", allDatabases);
			model.addAttribute("googleProjectList", googleProjectList);
			model.addAttribute("feedID", feedID);
			model.addAttribute("tgt_val", "BigQuery");
			model.addAttribute("srcSysIds", srcSysIds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("/publishing/publishingAddMetaData");
	}

	//Populate list of Service Account
	@RequestMapping(value="/publishing/serviceAccountList",method=RequestMethod.POST)
	public ModelAndView serviceAccountList(@Valid @ModelAttribute("gcp_proj_id")String gcp_proj_id, ModelMap model,HttpServletRequest request) {
		ArrayList<String> sa_list;
		try {
			sa_list = ps.populateServiceAccList(gcp_proj_id,(String)request.getSession().getAttribute("project"));
			model.addAttribute("sa_list", sa_list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("/publishing/publishingServiceAccountList");
	}
	
	@RequestMapping(value="/publishing/publishingScheduler",method=RequestMethod.POST)
	public ModelAndView publishingScheduler(@Valid @ModelAttribute("pub_tgt_val")String pub_tgt_val, ModelMap model) {
		Map<String, String> exSrcSysId;
		try {
			exSrcSysId = ps.getExSysIds();
			model.addAttribute("exSrcSysId", exSrcSysId);
			model.addAttribute("pub_tgt_val", pub_tgt_val);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("/publishing/publishingScheduler");
	}
	
	/* Add Metadata controller for new system
	@RequestMapping(value="/publishing/publishingAddMetadata1",method=RequestMethod.POST)
	public ModelAndView extractionManageConnection1(@Valid @ModelAttribute("x")String x,@ModelAttribute("target_type")String tgt_typ,ModelMap model) throws UnsupportedOperationException, Exception {
		     String resp = ps.invokeRest(x,"publish/addMetadata/newSource");
		     ArrayList<String> allDatabases= ps.populateDatasets();
		     model.addAttribute("allDatabases", allDatabases);
			 model.addAttribute("tgt_val", tgt_typ);
			 String status = "";
			 if (resp != null) {
				 status = "Success";
				 String feed_id = resp;
				 Integer src_id=Integer.parseInt(feed_id);
				    SourceSystemBean feed_name = ps.getSourceSystemMetadata(src_id);
				       
				    model.addAttribute("feed_id", feed_id);
				    model.addAttribute("feed_name", feed_name);
				   // model.addAttribute("next_button_active", "active");
				    //Success or Failed status
				    
				    List<SourceSystemFileBean> fileList;
					
					fileList=ps.getSourceFiles(src_id);
					Integer entry_cnt=fileList.size();
					model.addAttribute("fileList", fileList);
					model.addAttribute("entry_cnt", entry_cnt);
					Map<String, List<String>> filefields = ps.getSourceFields(src_id);
					model.addAttribute("fileFields", filefields);
					
					Map<String, List<SourceSystemFieldBean>> fileAllfields = ps.getSourceFieldMetadata(src_id);
					model.addAttribute("fileAllfields", fileAllfields);
				
					Map<String, String> publishingType = new HashMap<String,String>();
					publishingType.put("Full Load", "TRUNCATE&LOAD");
					publishingType.put("Incremental Load", "APPEND");
					model.addAttribute("publishingType", publishingType);
					
					model.addAttribute("next_button_active", "active");
					
					model.addAttribute("src_type", "Published");
			 }else {
				 status ="Failed";
			 }
			//Success or Failed status
			    String status0[] = resp.toString().split(":");
			    System.out.println(status0[0]+" value "+status0[1]+" value3: "+status0[2]);
			    String status1[]=status0[1].split(",");
			    String status=status1[0].replaceAll("\'","").trim();
			    String message0=status0[2];
			    String message=message0.replaceAll("[\'}]","").trim();
			    String final_message=status+": "+message;
			    System.out.println("final: "+final_message);
			    if(status.equalsIgnoreCase("Failed"))
			    {
			    	model.addAttribute("errorString", "Error While Adding Metadata");
			    } 
			    else if(status.equalsIgnoreCase("Success"))
			    {
			    	model.addAttribute("successString", "Metadata Added Successfully");
			    }
		    return new ModelAndView("/publishing/publishingAddMetaData");
	}	
	*/
	 //Add Metadata controller for extracted system
	@RequestMapping(value="/publishing/publishingExtract1",method=RequestMethod.POST)
	public ModelAndView addMetadataExtraction1(@Valid @ModelAttribute("y") String y,
			@ModelAttribute("target_type") String tgt_typ, @ModelAttribute("src_sys_id") String feed_id,
			@ModelAttribute("run_id_list") String run_id, ModelMap model, HttpServletRequest request)
			throws UnsupportedOperationException, Exception {
		// String resp = ps.invokeRest(y,"publish/addMetadata/extractedSource");

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(y);
		RequestDto requestDto = gson.fromJson(object, RequestDto.class);
		
		//TODO: Get the Project ID based on project name
		//(String)request.getSession().getAttribute("project");
		Integer src_id = 0;
		String resp = "{ 'status': 'Success','message':'FEED DETAILS SAVED SUCCESSFULLY' }";
		try {
			if(requestDto.getBody().get("data").get("is_new").equalsIgnoreCase("YES")) {
				int project_sequence = ps.getProjectSequence((String)request.getSession().getAttribute("project"));
				src_id = ps.addMetaDataForExtractedSource(requestDto, project_sequence);
			}else {
				//Get the Id based on the Google Project name and Target DS
				//src_id = Integer.parseInt(requestDto.getBody().get("data").get("pub_feed_id"));
				String targetDataset = requestDto.getBody().get("data").get("target_ex_dataset");
				String gcp_project_name = requestDto.getBody().get("data").get("gcp_pr_name");
				src_id = ps.getPublishingFeedId(gcp_project_name, targetDataset);
			}
				
		}
		catch(Exception e) {
			resp = "{ 'status': 'Failed','message':'FAILED TO SAVE FEED DETAILS' }";
		}
		model.addAttribute("run_id", run_id);
		ArrayList<String> allDatabases = ps.populateDatasets((String)request.getSession().getAttribute("project"));
		Map<String, String> srcSysIds = ps.getSysIds((String) request.getSession().getAttribute("project"));
		ArrayList<String> googleProjectList= ps.populateGoogleProject((String)request.getSession().getAttribute("project"));
		Map<String,String> feedID = ps.getPubFeedIDs((String)request.getSession().getAttribute("project"));
		
		model.addAttribute("srcSysIds", srcSysIds);
		model.addAttribute("feedID", feedID);
		model.addAttribute("allDatabases", allDatabases);
		model.addAttribute("tgt_val", tgt_typ);
		model.addAttribute("googleProjectList", googleProjectList);
		//Integer src_id = Integer.parseInt(feed_id);
		SourceSystemBean feed_name = ps.getSourceSystemMetadata(src_id);

		model.addAttribute("feed_id", src_id);
		model.addAttribute("feed_name", feed_name);
		model.addAttribute("ext_feed_name", feed_id.split(":")[1]);
		model.addAttribute("ext_feed_name", feed_id.split(":")[1]);
		// model.addAttribute("next_button_active", "active");
		// Success or Failed status
		model.addAttribute("ext_feed_extraction_type", ps.getFeedExtractionType(feed_id.split(":")[1]));
		model.addAttribute("project_id",(String)request.getSession().getAttribute("project"));
		model.addAttribute("is_new",requestDto.getBody().get("data").get("is_new").toUpperCase());
		
		List<SourceSystemFileBean> fileList;

		fileList = ps.getSourceFiles(src_id);
		Integer entry_cnt = fileList.size();
		model.addAttribute("fileList", fileList);
		model.addAttribute("entry_cnt", entry_cnt);
		Map<String, List<String>> filefields = ps.getSourceFields(src_id);
		model.addAttribute("fileFields", filefields);

		Map<String, List<SourceSystemFieldBean>> fileAllfields = ps.getSourceFieldMetadata(src_id);
		model.addAttribute("fileAllfields", fileAllfields);

		Map<String, String> publishingType = new HashMap<String, String>();
		publishingType.put("Full Load", "TRUNCATE&LOAD");
		publishingType.put("Incremental Load", "APPEND");
		model.addAttribute("publishingType", publishingType);

		model.addAttribute("src_type", "Extracted");

		String status0[] = resp.toString().split(":");
		String status1[] = status0[1].split(",");
		String status = status1[0].replaceAll("\'", "").trim();
		String message0 = status0[2];
		String message = message0.replaceAll("[\'}]", "").trim();
		String final_message = status + ": " + message;
		if (status.equalsIgnoreCase("Failed")) {
			model.addAttribute("errorString", final_message);
		} else if (status.equalsIgnoreCase("Success")) {
			model.addAttribute("successString", final_message);
			model.addAttribute("next_button_active", "active");
		}
		/*
		 * JSONObject jObject = new JSONObject(resp); String
		 * status=jObject.get("status").toString(); String
		 * msg=jObject.get("message").toString(); System.out.println("status: "+status);
		 * System.out.println("msg: "+msg);
		 * 
		 * if(status.equalsIgnoreCase("success")) {
		 * model.addAttribute("successString",msg); } else
		 * if(status.equalsIgnoreCase("failed")) {
		 * model.addAttribute("errorString",msg); }
		 */

		return new ModelAndView("/publishing/publishingAddMetaData");
	}
	

	@RequestMapping(value="/publishing/finalAddMetadata1",method=RequestMethod.POST)
	public ModelAndView finalAddMetadataTemp(@Valid @ModelAttribute("z")String z, ModelMap model) throws UnsupportedOperationException, Exception {
			//String resp = ps.invokeRest(z,"publish/confirmFeedDetails");
			//Thread.sleep(5000);
			String resp="{ 'status': 'Success','message':'FEED DETAILS SAVED SUCCESSFULLY' }";
		    String status0[] = resp.toString().split(":");
		    String status1[]=status0[1].split(",");
		    String status=status1[0].replaceAll("\'","").trim();
		    String message0=status0[2];
		    String message=message0.replaceAll("[\'}]","").trim();
		    String final_message=status+": "+message;
		    if(status.equalsIgnoreCase("Failed"))
		    {
		    	model.addAttribute("errorString", final_message);
		    } 
		    else if(status.equalsIgnoreCase("Success"))
		    {
		    	model.addAttribute("successString", final_message);
		    }
	return new ModelAndView("redirect:/publishing/publishingExtract1#step-2");
	}
	
	
	
	@RequestMapping(value="/publishing/savePartitionInfo",method=RequestMethod.POST)
	public ModelAndView savePartitionInfo(@Valid @ModelAttribute("z")String z, ModelMap model) throws UnsupportedOperationException, Exception {
		//confirmFeedDetails
		
		//String resp = ps.invokeRest(z,"publish/savePartitionInfo");
		
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(z);
		RequestDto requestDto = gson.fromJson(object, RequestDto.class);
		String resp = ps.savePartitionDetails(requestDto);
		
		String status0[] = resp.toString().split(":");
	    String status1[]=status0[1].split(",");
	    String status=status1[0].replaceAll("\'","").trim();
	    String message0=status0[2];
	    String message=message0.replaceAll("[\'}]","").trim();
	    String final_message=status+": "+message;
	    if(status.equalsIgnoreCase("Failed"))
	    {
	    	model.addAttribute("errorString", final_message);
	    } 
	    else if(status.equalsIgnoreCase("Success"))
	    {
	    	model.addAttribute("successString", final_message);
	    }
		return new ModelAndView("/publishing/publishingPartitioning");
	}
	
	@RequestMapping(value="/publishing/saveMetadataChanges",method=RequestMethod.POST)
	public ModelAndView saveMetadataChanges(@Valid @ModelAttribute("z")String z,@ModelAttribute("src_sys_id")String src_sys_id,  ModelMap model) throws UnsupportedOperationException, Exception {
		/*ArrayList<String> runIdList;
		Integer src_id=Integer.parseInt(src_sys_id);
		runIdList=ps.getRunIds(src_id); 
		model.addAttribute("runIdList", runIdList);*/
		if(! z.isEmpty()) {
		//String resp = ps.invokeRest(z,"publish/saveMetadataChanges");
		
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(z);
		RequestDto requestDto = gson.fromJson(object, RequestDto.class);
		
		String resp = ps.saveMetadataChanges(requestDto);
		String status0[] = resp.toString().split(":");
	    String status1[]=status0[1].split(",");
	    String status=status1[0].replaceAll("\'","").trim();
	    String message0=status0[2];
	    String message=message0.replaceAll("[\'}]","").trim();
	    String final_message=status+": "+message;
	    if(status.equalsIgnoreCase("Failed"))
	    {
	    	model.addAttribute("errorString", final_message);
	    } 
	    else if(status.equalsIgnoreCase("Success"))
	    {
	    	model.addAttribute("successString", final_message);
	    }
		}
		List<SourceSystemFileBean> fileList;
		int feed_id = Integer.parseInt(src_sys_id);
		fileList=ps.getSourceFiles(feed_id);
		Integer entry_cnt=fileList.size();
		model.addAttribute("fileList", fileList);
		model.addAttribute("entry_cnt", entry_cnt);
		
		
		Map<String, String> publishingType = new HashMap<String,String>();
		publishingType.put("Full Load", "TRUNCATE&LOAD");
		publishingType.put("Incremental Load", "APPEND");
		model.addAttribute("publishingType", publishingType);
		
		Map<String, List<String>> filefields = ps.getSourceFields(feed_id);
		model.addAttribute("fileFields", filefields);
		
		return new ModelAndView("/publishing/publishingPartitioning");
	}
	
	
	/* Add Metadata controller for extracted system
	@RequestMapping(value="/publishing/finalAddMetadata",method=RequestMethod.POST)
	public ModelAndView finalAddMetadata(@Valid @ModelAttribute("z")String z, ModelMap model) throws UnsupportedOperationException, Exception {
		    System.out.println("inside final add "+z);
			String resp = ps.invokeRest(z,"publish/confirmFeedDetails");
			//Thread.sleep(5000);
			//String resp="{ 'status': 'Success','message':'FEED DETAILS SAVED SUCCESSFULLY' }";
		    String status0[] = resp.toString().split(":");
		    System.out.println(status0[0]+" value "+status0[1]+" value3: "+status0[2]);
		    String status1[]=status0[1].split(",");
		    String status=status1[0].replaceAll("\'","").trim();
		    String message0=status0[2];
		    String message=message0.replaceAll("[\'}]","").trim();
		    String final_message=status+": "+message;
		    System.out.println("final: "+final_message);
		    if(status.equalsIgnoreCase("Failed"))
		    {
		    	model.addAttribute("errorString", final_message);
		    } 
		    else if(status.equalsIgnoreCase("Success"))
		    {
		    	model.addAttribute("successString", final_message);
		    }
	return new ModelAndView("/publishing/addMetaDataHome");
	}*/
	
	/* Publish Metadata controller for existing system*/
	@RequestMapping(value="/publishing/publishingExtracted1",method=RequestMethod.POST)
	public ModelAndView publishingExtracted1(@Valid @ModelAttribute("p")String x, @ModelAttribute("pub_type")String pub_type, ModelMap model, HttpServletRequest request) throws UnsupportedOperationException, Exception {
			String resp = "";
			try {
		if (pub_type.equalsIgnoreCase("BQ Load")) {
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(x);
			RequestDto requestDto = gson.fromJson(object, RequestDto.class);
			String sch_type = requestDto.getBody().get("data").get("deploy_type");
			if (sch_type.equalsIgnoreCase("Time_Based")) {
				String sch_freq = requestDto.getBody().get("data").get("sch_freq");
				String feed_unique_name = requestDto.getBody().get("data").get("feed_unique_name");
				String src_sys_id = requestDto.getBody().get("data").get("src_sys_id");
				String pub_feed_id =  src_sys_id.split(":")[0] ;
				String saName= requestDto.getBody().get("data").get("sa_name");
				String feed_name = feed_unique_name + "_" + src_sys_id.split(":")[2] + "_" + src_sys_id.split(":")[3];
				ps.insertScheduleMetadata(feed_unique_name,pub_feed_id,src_sys_id.split(":")[2]+":" +src_sys_id.split(":")[3]+":"+ saName, (String)request.getSession().getAttribute("project"), sch_freq);
				resp = "{ 'status': 'Success','message':'PUBLISHING JOB SCHEDULED SUCCESSFULLY' }";
			} else if (sch_type.equalsIgnoreCase("Event_Based")) {
				String extractFeedSequence = requestDto.getBody().get("data").get("src_sys_id").split(":")[4];
				String pubFeedSequence = requestDto.getBody().get("data").get("src_sys_id").split(":")[0];
				String feedUniqueName = requestDto.getBody().get("data").get("feed_unique_name");
				String gcpName = requestDto.getBody().get("data").get("gcp_name");
				String saNAme= requestDto.getBody().get("data").get("sa_name");
				String projectId= (String)request.getSession().getAttribute("project");
				ps.insertScheduleMetadataWithDependent(extractFeedSequence,pubFeedSequence, feedUniqueName, gcpName,saNAme,projectId);
				resp = "{ 'status': 'Success','message':'PUBLISHING JOB SCHEDULED SUCCESSFULLY' }";
			} else {
				
				String sch_freq = requestDto.getBody().get("data").get("sch_freq");
				String feed_unique_name = requestDto.getBody().get("data").get("feed_unique_name");
				String src_sys_id = requestDto.getBody().get("data").get("src_sys_id");
				String feed_name = feed_unique_name + "_" + src_sys_id.split(":")[2] + "_" + src_sys_id.split(":")[3];
				String pub_feed_id =  src_sys_id.split(":")[0] ;
				String saName= requestDto.getBody().get("data").get("sa_name");
				String run_id = requestDto.getBody().get("data").get("run_id");
				ps.insertOnDemandScheduleMetadata(feed_unique_name,pub_feed_id,src_sys_id.split(":")[2]+":" +src_sys_id.split(":")[3]+":"+ saName, (String)request.getSession().getAttribute("project"), run_id);
				resp = "{ 'status': 'Success','message':'PUBLISHING JOB SCHEDULED SUCCESSFULLY' }";
			}
			//resp = ps.invokePythonRest(x, "publish/publishData");
		}else {
			 resp = ps.invokeRest(x,"publish/publishData");
			}
		ArrayList<String> allDatabases = ps.populateDatasets((String)request.getSession().getAttribute("project"));
		ArrayList<String> googleProjectList= ps.populateGoogleProject((String)request.getSession().getAttribute("project"));
		Map<String,String> srcSysIds = ps.getSysIds((String)request.getSession().getAttribute("project"));
		Map<String,String> feedID = ps.getPubFeedIDs((String)request.getSession().getAttribute("project"));
		model.addAttribute("allDatabases", allDatabases);
		model.addAttribute("googleProjectList", googleProjectList);
		model.addAttribute("feedID", feedID);
		model.addAttribute("tgt_val", "BigQuery");
		model.addAttribute("srcSysIds", srcSysIds);
		  //Success or Failed status'
		   // String resp="{ 'status': 'Success','message':'PUBLISHING JOB TRIGGERED SUCCESSFULLY FOR THIS FEED' }";
		    String status0[] = resp.toString().split(":");
		    String status1[]=status0[1].split(",");
		    String status=status1[0].replaceAll("\'","").trim();
		    String message0=status0[2];
		    String message=message0.replaceAll("[\'}]","").trim();
		    String final_message=status+": "+message;
		    if(status.equalsIgnoreCase("Failed"))
		    {
		    	model.addAttribute("errorString", final_message);
		    } 
		    else if(status.equalsIgnoreCase("Success"))
		    {
		    	model.addAttribute("successString", final_message);
		    }
			}
			catch(Exception e) {
				 String final_message="FAILED"+": "+ e.getMessage();
				 model.addAttribute("errorString", final_message);
			}
		return new ModelAndView("/publishing/publishingAddMetaData");
	}
	
	
	@RequestMapping(value="/publishing/runIds",method=RequestMethod.POST)
	public ModelAndView publishingRunIds(@Valid @ModelAttribute("src_sys_id")String src_sys_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		ArrayList<String> runIdList;
		Integer src_id=Integer.parseInt(src_sys_id);
		try {
			runIdList=ps.getRunIds(src_id);
			model.addAttribute("runIdList", runIdList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new ModelAndView("/publishing/publishingSch0");
	}
	

	@RequestMapping(value="/publishing/addMetadataRunIds",method=RequestMethod.POST)
	public ModelAndView publishingAddMetadataRunIds(@Valid @ModelAttribute("src_sys_id")String src_sys_id,@ModelAttribute("pub_feed_id")String pub_feed_id, @ModelAttribute("is_new")String is_new,  @ModelAttribute("dateRangeText")String dateRangeText , ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		Map<String, String> runIdList;
		Integer src_id=Integer.parseInt(src_sys_id.split(":")[0]);
		try {
			runIdList=ps.getRunIdsWithDate(src_id, dateRangeText,is_new,pub_feed_id);
			model.addAttribute("runIdList", runIdList);
			model.addAttribute("feed_id",src_sys_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new ModelAndView("/publishing/publishingSch1");
	}
	
	@RequestMapping(value="/publishing/existingPublishRunIds",method=RequestMethod.POST)
	public ModelAndView existingPublishRunIds(@Valid @ModelAttribute("src_sys_id")String src_sys_id,@ModelAttribute("target_dataset")String target_dataset, @ModelAttribute("is_new")String is_new,  @ModelAttribute("dateRangeText")String dateRangeText , ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		Map<String, String> runIdList;
		Integer src_id=Integer.parseInt(src_sys_id.split(":")[0]);
		try {
			runIdList=ps.getRunIdsWithDate(src_id, dateRangeText,is_new,target_dataset);
			//ExistingPubBean bean= new ExistingPubBean();
			model.addAttribute("runIdList", runIdList);
			model.addAttribute("feed_id",src_sys_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new ModelAndView("/publishing/publishingSch1");
		
		
	/*	System.out.println("pub_Feed_id: "+pub_feed_id);
		model.addAttribute("runIdList", runIdList);
		model.addAttribute("feed_id",src_sys_id);
		//Existing Pub details
		model.addAttribute("service_acc",bean.getService_acc());
		model.addAttribute("proj_name",bean.getProject_name());
		model.addAttribute("target_ds",bean.getTarget_dataset());
		return new ModelAndView("/publishing/publishingSch2");*/
	}
	
	/*@RequestMapping(value="/publishing/viewMDSysList",method=RequestMethod.POST)
	public ModelAndView viewMDSysList(@Valid @ModelAttribute("src_sys_id")String src_sys_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		Integer src_id=Integer.parseInt(src_sys_id);
		SourceSystemBean system = new SourceSystemBean();
		system=ps.getSourceSystemMetadata(src_id);
		model.addAttribute("systemBean", system);
		return new ModelAndView("/publishing/viewSysMetadata0");
	}
	
	@RequestMapping(value="/publishing/viewMDFileList",method=RequestMethod.POST)
	public ModelAndView viewMDFileList(@Valid @ModelAttribute("src_sys_id")String src_sys_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		ArrayList<String> fileList;
		Integer src_id=Integer.parseInt(src_sys_id);
		fileList=ps.getMDFileList(src_id); 
		model.addAttribute("fileList", fileList);
		model.addAttribute("sys_id",src_id);
		return new ModelAndView("/publishing/viewFileMetadata0");
	}
	
	@RequestMapping(value="/publishing/viewFileMDInfo",method=RequestMethod.POST)
	public ModelAndView viewMDInfo(@Valid @ModelAttribute("file_id")String file_id,@ModelAttribute("sys_id")String src_sys_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		Integer src_id=Integer.parseInt(src_sys_id);
		SourceSystemFileBean file = new SourceSystemFileBean();
		file=ps.getSourceFileMetadata(src_id, file_id);
		model.addAttribute("fileBean", file);
		return new ModelAndView("/publishing/viewFileMetadata1");
	}
	
	@RequestMapping(value="/publishing/viewMDFieldList",method=RequestMethod.POST)
	public ModelAndView viewMDFieldList(@Valid @ModelAttribute("src_sys_id")String src_sys_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		ArrayList<String> fileList;
		Integer src_id=Integer.parseInt(src_sys_id);
		fileList=ps.getMDFileList(src_id); 
		model.addAttribute("fileList", fileList);
		model.addAttribute("sys_id",src_id);
		return new ModelAndView("/publishing/viewFieldMetadata0");
	}
	
	@RequestMapping(value="/publishing/viewFieldMDInfo",method=RequestMethod.POST)
	public ModelAndView viewFieldMDInfo(@Valid @ModelAttribute("file_id")String file_id,@ModelAttribute("sys_id")String src_sys_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		Integer src_id=Integer.parseInt(src_sys_id);
		List<SourceSystemFieldBean> fields = new ArrayList<SourceSystemFieldBean>();
		fields=ps.getSourceFieldMetadata(src_id, file_id);
		model.addAttribute("fieldBeanList", fields);
		return new ModelAndView("/publishing/viewFieldMetadata1");
	}

	//Edit Metadata Information
	//Edit System Information
	@RequestMapping(value="/publishing/editMDSysList",method=RequestMethod.POST)
	public ModelAndView editMDSysList(@Valid @ModelAttribute("src_sys_id")String src_sys_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		Integer src_id=Integer.parseInt(src_sys_id);
		SourceSystemBean system = new SourceSystemBean();
		system=ps.getSourceSystemMetadata(src_id);
		model.addAttribute("systemBean", system);
		return new ModelAndView("/publishing/editSysMetadata0");
	}*/
	
	//Rest API call for System Edit
	/*@RequestMapping(value="/publishing/updateSysMD",method=RequestMethod.POST)
	public ModelAndView updateSysMD(@Valid @ModelAttribute("x")String x, ModelMap model) throws UnsupportedOperationException, Exception {
		   	//String resp = ps.invokeRest(x,"publish/editSysMD");
		Thread.sleep(5000);
		String resp="{ 'status': 'Success','message':'FEED DETAILS UPDATED SUCCESSFULLY' }";
	    String status0[] = resp.toString().split(":");
	    System.out.println(status0[0]+" value "+status0[1]+" value3: "+status0[2]);
	    String status1[]=status0[1].split(",");
	    String status=status1[0].replaceAll("\'","").trim();
	    String message0=status0[2];
	    String message=message0.replaceAll("[\'}]","").trim();
	    String final_message=status+": "+message;
	    System.out.println("final: "+final_message);
	    if(status.equalsIgnoreCase("Failed"))
	    {
	    	model.addAttribute("errorString", final_message);
	    } 
	    else if(status.equalsIgnoreCase("Success"))
	    {
	    	model.addAttribute("successString", final_message);
	    }
	    ArrayList<String> srcDBList= ps.populateSrcDBList();
		ArrayList<String> tgtDBList= ps.populateTgtDBList();
		Map<String,String> editSSID = ps.getMDSysList();
		model.addAttribute("editSSID", editSSID);
		model.addAttribute("srcDBList", srcDBList);
		model.addAttribute("tgtDBList", tgtDBList);
		return new ModelAndView("/publishing/editMetadataHome");
	}
	//Edit File Information
	@RequestMapping(value="/publishing/editMDFileList",method=RequestMethod.POST)
	public ModelAndView editMDFileList(@Valid @ModelAttribute("src_sys_id")String src_sys_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		ArrayList<String> fileList;
		Integer src_id=Integer.parseInt(src_sys_id);
		fileList=ps.getMDFileList(src_id); 
		model.addAttribute("fileList", fileList);
		model.addAttribute("sys_id",src_id);
		return new ModelAndView("/publishing/editFileMetadata0");
	}
	
	//Edit File Information
	@RequestMapping(value="/publishing/editFileMDInfo",method=RequestMethod.POST)
	public ModelAndView editFileMDInfo(@Valid @ModelAttribute("file_id")String file_id,@ModelAttribute("sys_id")String src_sys_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		Integer src_id=Integer.parseInt(src_sys_id);
		SourceSystemFileBean file = new SourceSystemFileBean();
		file=ps.getSourceFileMetadata(src_id, file_id);
		model.addAttribute("fileBean", file);
		return new ModelAndView("/publishing/editFileMetadata1");
	}
	
	//Rest API call for File Edit
		@RequestMapping(value="/publishing/updateFileMD",method=RequestMethod.POST)
		public ModelAndView updateFileMD(@Valid @ModelAttribute("x")String x, ModelMap model) throws UnsupportedOperationException, Exception {
			    //String resp = ps.invokeRest(x,"publish/editFileMD");
			Thread.sleep(5000);
			String resp="{ 'status': 'Success','message':'FILE DETAILS UPDATED SUCCESSFULLY' }";
		    String status0[] = resp.toString().split(":");
		    System.out.println(status0[0]+" value "+status0[1]+" value3: "+status0[2]);
		    String status1[]=status0[1].split(",");
		    String status=status1[0].replaceAll("\'","").trim();
		    String message0=status0[2];
		    String message=message0.replaceAll("[\'}]","").trim();
		    String final_message=status+": "+message;
		    System.out.println("final: "+final_message);
		    if(status.equalsIgnoreCase("Failed"))
		    {
		    	model.addAttribute("errorString", final_message);
		    } 
		    else if(status.equalsIgnoreCase("Success"))
		    {
		    	model.addAttribute("successString", final_message);
		    }
		    ArrayList<String> srcDBList= ps.populateSrcDBList();
			ArrayList<String> tgtDBList= ps.populateTgtDBList();
			Map<String,String> editSSID = ps.getMDSysList();
			model.addAttribute("editSSID", editSSID);
			model.addAttribute("srcDBList", srcDBList);
			model.addAttribute("tgtDBList", tgtDBList);
			return new ModelAndView("/publishing/editMetadataHome");
		}
		
	//Edit Field Information
	@RequestMapping(value="/publishing/editMDFieldList",method=RequestMethod.POST)
	public ModelAndView editMDFieldList(@Valid @ModelAttribute("src_sys_id")String src_sys_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		ArrayList<String> fileList;
		Integer src_id=Integer.parseInt(src_sys_id);
		fileList=ps.getMDFileList(src_id); 
		model.addAttribute("fileList", fileList);
		model.addAttribute("sys_id",src_id);
		return new ModelAndView("/publishing/editFieldMetadata0");
	}
	
	//Edit Field Information
	@RequestMapping(value="/publishing/editFieldMDInfo",method=RequestMethod.POST)
	public ModelAndView editFieldMDInfo(@Valid @ModelAttribute("file_id")String file_id,@ModelAttribute("sys_id")String src_sys_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
		Integer src_id=Integer.parseInt(src_sys_id);
		List<SourceSystemFieldBean> fields = new ArrayList<SourceSystemFieldBean>();
		fields=ps.getSourceFieldMetadata(src_id, file_id);
		model.addAttribute("fieldBeanList", fields);
		return new ModelAndView("/publishing/editFieldMetadata1");
	}
	
	//Rest API call for Field Edit
	@RequestMapping(value="/publishing/updateFieldMD",method=RequestMethod.POST)
	public ModelAndView updateFieldMD(@Valid @ModelAttribute("x")String x, ModelMap model) throws UnsupportedOperationException, Exception {
		    //String resp = ps.invokeRest(x,"publish/editFieldMD");
		Thread.sleep(5000);
		String resp="{ 'status': 'Success','message':'FIELD DETAILS UPDATED SUCCESSFULLY' }";
	    String status0[] = resp.toString().split(":");
	    System.out.println(status0[0]+" value "+status0[1]+" value3: "+status0[2]);
	    String status1[]=status0[1].split(",");
	    String status=status1[0].replaceAll("\'","").trim();
	    String message0=status0[2];
	    String message=message0.replaceAll("[\'}]","").trim();
	    String final_message=status+": "+message;
	    System.out.println("final: "+final_message);
	    if(status.equalsIgnoreCase("Failed"))
	    {
	    	model.addAttribute("errorString", final_message);
	    } 
	    else if(status.equalsIgnoreCase("Success"))
	    {
	    	model.addAttribute("successString", final_message);
	    }
	    ArrayList<String> srcDBList= ps.populateSrcDBList();
		ArrayList<String> tgtDBList= ps.populateTgtDBList();
		Map<String,String> editSSID = ps.getMDSysList();
		model.addAttribute("editSSID", editSSID);
		model.addAttribute("srcDBList", srcDBList);
		model.addAttribute("tgtDBList", tgtDBList);
		return new ModelAndView("/publishing/editMetadataHome");
	}*/
	//View Source & Target DataType Details
	@RequestMapping(value="/publishing/viewDataTypeLinkDetails",method=RequestMethod.POST)
	public ModelAndView viewDataTypeLinkDetails(@Valid @ModelAttribute("src_db")String src_db,@ModelAttribute("tgt_db")String tgt_db, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
			List<DataTypeLinkBean> dataTypeInfo = new ArrayList<DataTypeLinkBean>();
			try {
				dataTypeInfo=ps.getDataTypeLinkList(src_db, tgt_db);
				model.addAttribute("dataTypeInfo", dataTypeInfo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("/publishing/viewDataTypeLink");
	}
	//View Source & Target DataType Details
	@RequestMapping(value="/publishing/editDataTypeLinkDetails",method=RequestMethod.POST)
	public ModelAndView editDataTypeLinkDetails(@Valid @ModelAttribute("src_db")String src_db,@ModelAttribute("tgt_db")String tgt_db, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
			List<DataTypeLinkBean> dataTypeInfo = new ArrayList<DataTypeLinkBean>();
			try {
				dataTypeInfo=ps.getDataTypeLinkList(src_db, tgt_db);
				model.addAttribute("dataTypeInfo", dataTypeInfo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("/publishing/editDataTypeLink");
	}
	//Rest API call for Update Datatype
		@RequestMapping(value="/publishing/updateDataType",method=RequestMethod.POST)
		public ModelAndView updateDataType(@Valid @ModelAttribute("x")String x, ModelMap model) throws UnsupportedOperationException, Exception {
			//String resp = ps.invokeRest(x,"publish/updateDataType");
			
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(x);
			RequestDto requestDto = gson.fromJson(object, RequestDto.class);
			String resp = ps.updateDataType(requestDto);
			
			//Thread.sleep(5000);
			//String resp="{ 'status': 'Success','message':'DATATYPE MAPPING UPDATED SUCCESSFULLY' }";
		    String status0[] = resp.toString().split(":");
		    String status1[]=status0[1].split(",");
		    String status=status1[0].replaceAll("\'","").trim();
		    String message0=status0[2];
		    String message=message0.replaceAll("[\'}]","").trim();
		    String final_message=status+": "+message;
		    if(status.equalsIgnoreCase("Failed"))
		    {
		    	model.addAttribute("errorString", final_message);
		    } 
		    else if(status.equalsIgnoreCase("Success"))
		    {
		    	model.addAttribute("successString", final_message);
		    }
		    ArrayList<String> srcDBList= ps.populateSrcDBList();
			ArrayList<String> tgtDBList= ps.populateTgtDBList();
			//Map<String,String> editSSID = ps.getMDSysList();
			//model.addAttribute("editSSID", editSSID);
			model.addAttribute("srcDBList", srcDBList);
			model.addAttribute("tgtDBList", tgtDBList);
			return new ModelAndView("/publishing/editMetadataHome");
		}
		
		//Reset Table Information
		@RequestMapping(value="/publishing/resetTable",method=RequestMethod.POST)
		public ModelAndView resetTable(@Valid @ModelAttribute("ds_name")String ds_name, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
			ArrayList<String> tableList= ps.populateTables(ds_name);
			model.addAttribute("tableList", tableList);
			return new ModelAndView("/publishing/resetTable0");
		}
		
		//Reset Table Information
		@RequestMapping(value="/publishing/resetRunIDTableList",method=RequestMethod.POST)
		public ModelAndView resetRunIDTableList(@Valid @ModelAttribute("ds_name")String ds_name, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
			ArrayList<String> tableList= ps.populateTables(ds_name);
			model.addAttribute("tableList", tableList);
			return new ModelAndView("/publishing/resetRunID0");
		}
		
		//Reset RUN ID Information
		@RequestMapping(value="/publishing/resetRunIDList",method=RequestMethod.POST)
		public ModelAndView resetRunIDList(@Valid @ModelAttribute("ds_name")String ds_name,@ModelAttribute("table_name")String table_name, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
			ArrayList<String> runIDList= ps.populateRunIDs(ds_name,table_name);
			model.addAttribute("runIDList", runIDList);
			return new ModelAndView("/publishing/resetRunID1");
		}
		
		/* Add Metadata controller for new system*/
		@RequestMapping(value="/publishing/resetPublishing",method=RequestMethod.POST)
		public ModelAndView resetPublishing(@Valid @ModelAttribute("x")String x,@ModelAttribute("target_type")String tgt_typ,ModelMap model,HttpServletRequest request) throws UnsupportedOperationException, Exception {
			     String resp = ps.invokeRest(x,"publish/resetMetadata");
			    
				//Success or Failed status
				    String status0[] = resp.toString().split(":");
				    String status1[]=status0[1].split(",");
				    String status=status1[0].replaceAll("\'","").trim();
				    String message0=status0[2];
				    String message=message0.replaceAll("[\'}]","").trim();
				    String final_message=status+": "+message;
				    
				    ArrayList<String> allDatabases= ps.populateDatasets((String)request.getSession().getAttribute("project"));
					model.addAttribute("allDatabases", allDatabases);
					
					
					/*ArrayList<String> tableList= ps.populateTables(ds_name);
					model.addAttribute("tableList", tableList);*/
					
					
				    if(status.equalsIgnoreCase("Failed"))
				    {
				    	model.addAttribute("errorString", final_message);
				    } 
				    else if(status.equalsIgnoreCase("Success"))
				    {
				    	model.addAttribute("successString", final_message);
				    }
			    return new ModelAndView("/publishing/resetMetadataHome");
		}	
		
		//Recon Dashboard RUN ID Information
		@RequestMapping(value="/publishing/reconRunIds",method=RequestMethod.POST)
		public ModelAndView reconRunIds(@Valid @ModelAttribute("db_id")String db_id,@Valid @ModelAttribute("proj_id")String proj_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
			ArrayList<String> runIDList;
			try {
				runIDList = ps.reconRunIDs(proj_id,db_id);
				model.addAttribute("runIDList", runIDList);
				model.addAttribute("db_id", db_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("/publishing/reconDashboard2");
		} 
		//Recon Dashboard values
		@RequestMapping(value="/publishing/reconDashboardValues",method=RequestMethod.POST)
		public ModelAndView reconDashboardValues(@Valid @ModelAttribute("proj_id")String proj_id,@Valid @ModelAttribute("db_id")String db_id,@Valid @ModelAttribute("run_id")String run_id, ModelMap model) throws IOException, ClassNotFoundException, SQLException {
			ArrayList<ReconDashboardBean> reconDataList;
			try {
				reconDataList = ps.reconDashData(proj_id,db_id,run_id);
				model.addAttribute("reconDataList",reconDataList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("/publishing/reconDashboard1");
		}
		
		@RequestMapping(value = "/publishing/systemDetails1", method = RequestMethod.POST)
		public ModelAndView systemDetails1(@Valid @RequestParam(value = "sun", required = true) String sun, ModelMap model) throws UnsupportedOperationException, Exception {
			int stat = ps.checkNames(sun);
			model.addAttribute("stat", stat);
			return new ModelAndView("/publishing/searchTextBox");
		}
		
		@RequestMapping(value = { "/publishing/error"}, method = RequestMethod.GET)
		public ModelAndView error(ModelMap modelMap,HttpServletRequest request) {
			
			return new ModelAndView("/index");
		}
}


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	 <div class="form-group row" id="runidLabel2">
	       <div class="col-md-3">
	       	<label>Run Id's to be Published <span style="color:red">*</span></label>
	        </div>
	 </div>
	<div class="form-group">
        	<select class="form-control" id="ex_pub_col_name" name="ex_pub_col_name" multiple="multiple">
				<option value="*">Select All</option>
				<c:forEach items="${runIdList}" var="runId">
					<option value="${runId.key}">${runId.value} - ${runId.key}</option>
				</c:forEach>
			   </select>
       
		</div>
		
		<div class="form-group row" >
          <div class="col-md-3">
             <label>Google Project Name<span style="color:red">*</span></label> 
          </div>
          <div class="col-md-9">
            <input type="text" class="form-control has-error" placeholder="enter dataset name" id="exPub_gcp_proj_name" name="exPub_gcp_proj_name" value="${proj_name}" disabled>
          </div>
    </div>
 	<div class="row ">
      <div class="col-md-3">
         <label>Service Account Name<span style="color:red">*</span></label> 
      </div>
      <div class="form-group col-md-9" >
         <input type="text" class="form-control has-error" placeholder="enter dataset name" id="exPub_sa_name" name="exPub_sa_name" value="${service_acc}" disabled>
      </div>
   	</div>
    <div class="form-group row ">
      <div class="col-md-3">
         <label>Target Dataset<span style="color:red">*</span></label> 
      </div>
      <div class="form-group col-md-9" >
         <input type="text" class="form-control has-error" placeholder="enter dataset name" id="exPub_target_dataset" name="exPub_target_dataset" value="${target_ds}" disabled>
      </div>
   </div>

<script>
	var x3=document.getElementById("ex_pub_col_name");
	multi(x3);
</script>



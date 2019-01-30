<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<div class="row" >
          <div class="col-md-3">
             <label>Google Project Name<span style="color:red">*</span></label> 
          </div>
          <div class="col-md-9">
            <input type="text" class="form-control has-error" placeholder="enter dataset name" id="exPub_gcp_proj_name" name="exPub_gcp_proj_name" >
          </div>
    </div>
 	<div class="row ">
      <div class="col-md-3">
         <label>Service Account Name<span style="color:red">*</span></label> 
      </div>
      <div class="form-group col-md-9" >
         <input type="text" class="form-control has-error" placeholder="enter dataset name" id="exPub_sa_name" name="exPub_sa_name" >
      </div>
   	</div>
    <div class="row ">
      <div class="col-md-3">
         <label>Target Dataset<span style="color:red">*</span></label> 
      </div>
      <div class="form-group col-md-9" >
         <input type="text" class="form-control has-error" placeholder="enter dataset name" id="exPub_target_dataset" name="exPub_target_dataset" >
      </div>
   </div>
   
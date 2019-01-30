<jsp:include page="../cdg_header.jsp" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>
function jsonConstructDS()
{
	//alert('Success')
	var d = document.getElementById('tableId1')
	if(d){
		document.getElementById('tableId').value = d.value
	}
	
	var d1 = document.getElementById('tableId2')
	if(d1){
		document.getElementById('tableId').value = d1.value
	}
	
	/* if(document.getElementById('reset_run3').value != "")
	{
		document.getElementById('reset_table2').value="";
		document.getElementById('reset_runID').value=document.getElementById('reset_run3').value;
		document.getElementById('reset_table').value=document.getElementById('reset_table3').value;
	} else if(document.getElementById('reset_table2').value != "")
	{
		document.getElementById('reset_table3').value="";
		document.getElementById('reset_run3').value="";
		document.getElementById('reset_table').value=document.getElementById('reset_table2').value;
	}  */
	var data = {};
	$(".form-control1").serializeArray().map(function(x){data[x.name] = x.value;});
	var x = '{"header":{"user":"cdg_admin","service_account":"Publishing_CDG_UK","reservoir_id":"R0001","event_time":"today"},"body":{"data":'+JSON.stringify(data)+'}}';
	//alert(x);
	document.getElementById('x').value = x;
  	console.log(x);
	
}
</script>
<div class="main-panel">
<div class="content-wrapper">
<div class="row">
   <div class="col-12 grid-margin stretch-card">
      <div class="card">
         <div class="card-body">
            <h4 class="card-title">Data Publishing</h4>
            <p class="card-description">
               Reset Metadata
            </p>
             <%
              if(request.getAttribute("successString") != null) {
             %>
            	<div class="alert alert-success" id="success-alert">
    				<button type="button" class="close" data-dismiss="alert">x</button>
    		 		${successString}
				</div>
            <%
               }
               %>
            <%
             if(request.getAttribute("errorString") != null) {
             %>
            	<div class="alert alert-danger" id="error-alert">
    				<button type="button" class="close" data-dismiss="alert">x</button>
    		 		${errorString}
				</div>
            <%
             }
            %>
     
<form class="forms-sample" id="resetDataset"	name="resetDataset" method="POST"	action="/publishing/resetPublishing" 	enctype="application/json">
			<input type="hidden" name="x" id="x">
			<input class="form-control1" type="hidden" name="tableId" id="tableId" >
			
		<div id="ds1" >
          	<div class="form-group row flex-v-center" id="sa1">
	   			 <div class="col-md-3">
					<label class="align-middle">Service Account Name<span style="color:red">*</span></label> 
				</div>
				<div class="col-md-9">
				<select class="form-control form-control1" id="sa_list" name="sa_list">
					<option value="" selected disabled>Select Service account...</option>
				<c:forEach items="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.getServiceAccountNames()}" var="sa">
					<option value="${sa}">${sa}</option>
				</c:forEach>
				</select>
				</div>
			</div> 
	          <div class="form-group row flex-v-center">
	   			 <div class="col-md-3">
					<label class="align-middle">Dataset Name<span style="color:red">*</span></label> 
				</div>
				<div class="col-md-9">
				<select class="form-control form-control1" id="datasetId" name="datasetId">
					<option value="" selected disabled>Select Dataset...</option>
				<c:forEach items="${allDatabases}" var="dataSetList">
					<option value="${dataSetList}">${dataSetList}</option>
				</c:forEach>
					</select> 
				</div>
				</div>
			</div>		 
	   	<div id="reset_type" style="display: none;">
	   		<div class="form-group row flex-v-center" id="sa1">
	   			 <div class="col-md-3">
					<label>Reset Type<span style="color:red">*</span></label>
				</div>
		       	<div class="col-md-3">
	         		     <div class="form-check form-check-info">
	                        <label class="form-check-label">
	                        <input type="radio" class="form-check-input" name="reset" id="reset_dataset" value="Reset Dataset" checked="checked">
	                        Drop Dataset
	                        </label>
	                     </div>
	          	</div>
	        	<div class="col-md-3">
	                     <div class="form-check form-check-info">
	                        <label class="form-check-label">
	                        <input type="radio" class="form-check-input" name="reset" id="reset_table" value="Reset Table">
	                        Drop Table
	                        </label>
	            		</div>
	            </div> 
	            <div class="col-md-3">
                     	<div class="form-check form-check-info">
	                        <label class="form-check-label">
	                        <input type="radio" class="form-check-input" name="reset" id="reset_runID" value="Reset RunID">
	                        Delete Records by RunID
	                        </label>
            			</div>
            	</div> 
            	</div> 
            </div>
           <br>
       <div id="loadDSTableList" style="display: none;" ></div>
       <div id="loadDSRunIDList" style="display: none;" ></div>
		<br>
		<div class="form-group" id="bt1" >
			<button type="submit" class="btn btn-rounded btn-gradient-info mr-2" onclick="jsonConstructDS();">
					Reset
				</button>
		</div>
</form>
</div>	
<script>
$(document).ready(function () {	
	$("#datasetId").on("change", function(){
		$("#reset_type").show();
		document.getElementById('reset_dataset').checked = false;
		document.getElementById('reset_table').checked = false;
		document.getElementById('reset_runID').checked = false;
		$("#loadDSTableList").hide();
		$("#loadDSRunIDList").hide();
	})
	$("#reset_dataset").on("change", function(){
		
		$("#loadDSTableList").hide();
		$("#loadDSRunIDList").hide();
	})	
	$("#reset_table").on("change", function(){
		reset_table
		var ds_name = document.getElementById('datasetId').value;	
	 		$.post('${pageContext.request.contextPath}/publishing/resetTable', {
   			ds_name : ds_name
   		}, function(data) {
   			$('#loadDSTableList').html(data)
   		});
   		$("#loadDSTableList").show();
		$("#loadDSRunIDList").hide();
	})
	$("#reset_runID").on("change", function(){
		var ds_name = document.getElementById('datasetId').value;	
			$.post('${pageContext.request.contextPath}/publishing/resetRunIDTableList', {
				ds_name : ds_name
			}, function(data) {
				$('#loadDSRunIDList').html(data)
		});
		$("#loadDSTableList").hide();
		$("#loadDSRunIDList").show();	
	})
		
    $("#success-alert").hide();
    	$("#success-alert").fadeTo(10000,10).slideUp(2000, function(){
    });   
	$("#error-alert").hide();
    	$("#error-alert").fadeTo(10000,10).slideUp(2000, function(){
    });
});
</script>
</div></div></div>
<jsp:include page="../cdg_footer.jsp" />

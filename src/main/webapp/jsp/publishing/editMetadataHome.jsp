<jsp:include page="../cdg_header.jsp" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>
function jsonEditSys()
{
	var data = {};
	$(".form-control1").serializeArray().map(function(x){data[x.name] = x.value;});
	var x = '{"header":{"user":"cdg_admin","service_account":"Publishing_CDG_UK","reservoir_id":"R0001","event_time":"today"},"body":{"data":'+JSON.stringify(data)+'}}';
	      document.getElementById('x').value = x;
	      document.getElementById('editSystem').submit();
}
function jsonEditFile()
{
	var data = {};
	$(".form-control2").serializeArray().map(function(x){data[x.name] = x.value;});
	var x = '{"header":{"user":"cdg_admin","service_account":"Publishing_CDG_UK","reservoir_id":"R0001","event_time":"today"},"body":{"data":'+JSON.stringify(data)+'}}';
	//alert(x);      
		document.getElementById('x').value = x;
	      document.getElementById('editFile').submit();
}
function jsonEditField()
{
	var data = {};
	$(".form-control3").serializeArray().map(function(x){data[x.name] = x.value;});
	var x = '{"header":{"user":"cdg_admin","service_account":"Publishing_CDG_UK","reservoir_id":"R0001","event_time":"today"},"body":{"data":'+JSON.stringify(data)+'}}';
	//alert(x);      
	document.getElementById('x').value = x;
	      document.getElementById('editField').submit();
}

function updateDataType1()
{
	//alert('success');
	var data = {};
	$(".form-control1").serializeArray().map(function(x){data[x.name] = x.value;});
	var x = '{"header":{"user":"cdg_admin","service_account":"Publishing_CDG_UK","reservoir_id":"R0001","event_time":"today"},"body":{"data":'+JSON.stringify(data)+'}}';
	//alert(x);      
	document.getElementById('x').value = x;
	document.getElementById('editField').submit();
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
               Edit Metadata
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
      <ul class="nav nav-pills nav-fill" id="pills-tab" role="tablist">
		 <!--  <li class="nav-item">
		    <a class="nav-link active" id="pills-system" data-toggle="pill" href="#pills-system-conn" role="tab" aria-controls="pills-system" aria-selected="true">System Details</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="pills-file" data-toggle="pill" href="#pills-file-conn" role="tab" aria-controls="pills-file" aria-selected="false">File Details</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="pills-field" data-toggle="pill" href="#pills-field-conn" role="tab" aria-controls="pills-field" aria-selected="false">Field Details</a>
		  </li> -->
		   <li class="nav-item">
		    <a class="nav-link active" id="pills-mstr-dt" data-toggle="pill" href="#pills-mstr-dt-conn" role="tab" aria-controls="pills-mstr-dt" aria-selected="false">Data Type Mapping - Source to Target </a>
		  </li>
	</ul>
<div class="tab-content" id="pills-tabContent">
  <%-- <div class="tab-pane fade show active" id="pills-system-conn" role="tabpanel" aria-labelledby="pills-system">
   <form class="forms-sample" id="editSystem"
   						name="editSystem" method="POST"
							action="/publishing/updateSysMD" 
						enctype="application/json">
   <fieldset class="fs">
		<div class="form-group row flex-v-center">
   			 <div class="col-md-3">
				<label class="align-middle">System Name<span style="color:red">*</span></label> 
			</div>
			<div class="col-md-9">
			<select class="form-control form-control1" id="sys_src_sys_id" name="sys_src_sys_id">
				<option value="" selected disabled>select source system...</option>
				<c:forEach var="myMap" items="${editSSID}">
	        		<option value="${myMap.key}"><c:out value="${myMap.value}"/></option>
	     		</c:forEach>
			</select>
			</div>
		</div>
   		<div id="loadSystemList"></div>
   		<br>
   		<div class="form-group" id="bt1" style="display: none;" >
			<button type="submit" class="btn btn-rounded btn-gradient-info mr-2" onclick="jsonEditSys()">
					Update
				</button>
		</div>
	</fieldset>
   </form>
   </div>
   <div class="tab-pane fade" id="pills-file-conn" role="tabpanel" aria-labelledby="pills-file">
   <form class="forms-sample" id="editFile"
   						name="editFile" method="POST"
							action="/publishing/updateFileMD" 
						enctype="application/json">
   <fieldset class="fs">
   		<div class="form-group row">
   			 <div class="col-md-3">
				<label class="align-bottom">System Name<span style="color:red">*</span></label> 
			</div>
			<div class="col-md-9">
				<select class="form-control form-control2" id="file_src_sys_id" name="file_src_sys_id">
				<option value="" selected disabled>select source system...</option>
				<c:forEach var="myMap" items="${editSSID}">
	        		<option value="${myMap.key}"><c:out value="${myMap.value}"/></option>
	     		</c:forEach>
				</select>
			</div>
		</div>
			<div id="loadMDFileList"></div>
			
  </fieldset>
   </form>
   </div>
   <div class="tab-pane fade" id="pills-field-conn" role="tabpanel" aria-labelledby="pills-field">
   <form class="forms-sample" id="updateFieldMD"
   						name="updateFieldMD" method="POST"
							action="/publishing/updateFieldMD" 
						enctype="application/json">
   <fieldset class="fs">
   			<div class="form-group row">
   			 <div class="col-md-3">
				<label class="align-middle">System Name<span style="color:red">*</span></label> 
			</div>
			<div class="col-md-9">
				<select class="form-control form-control3" id="fld_src_sys_id" name="fld_src_sys_id">
				<option value="" selected disabled>select source system...</option>
				<c:forEach var="myMap" items="${editSSID}">
	        		<option value="${myMap.key}"><c:out value="${myMap.value}"/></option>
	     		</c:forEach>
				</select>
			</div>
			</div>
			<div id="loadMDFieldList"></div>
				
   </fieldset>
   </form>
   </div> --%> 
   <div class="tab-pane  fade show active" id="pills-mstr-dt-conn" role="tabpanel" aria-labelledby="pills-mstr-dt">
   <form class="forms-sample" id="updateDataType"
   						name="updateDataType" method="POST"
							action="${pageContext.request.contextPath}/publishing/updateDataType" 
						enctype="application/json">
						
						 <input type="hidden" name="x" id="x">
   <fieldset class="fs">
   			<div class="form-group row">
   			 <div class="col-md-3">
				<label>Source DataBase Name<span style="color:red">*</span></label> 
			</div>
			<div class="col-md-9">
				<select class="form-control form-control1" id="src_db_name" name="src_db_name" onchange="getDataTypeList()">
				<option value="" selected disabled>select source database...</option>
					<c:forEach items="${srcDBList}" var="dbList">
						<option value="${dbList}">${dbList}</option>
					</c:forEach>
				</select>
			</div>
			</div>
			<div class="form-group row">
   			 <div class="col-md-3">
				<label>Target DataBase Name<span style="color:red">*</span></label> 
			</div>
			<div class="col-md-9">
				<select class="form-control form-control1" id="tgt_db_name" name="tgt_db_name" onchange="getDataTypeList()">
				<option value="" selected disabled>select target database...</option>
					<c:forEach items="${tgtDBList}" var="tgtdbList">
						<option value="${tgtdbList}">${tgtdbList}</option>
					</c:forEach>
				</select>
			</div>
			</div>
			<div id="loadDataTypeInfo"></div>
			<br>
   		<div class="form-group" id="bt4" style="display: none;" >
			<button type="submit" class="btn btn-rounded btn-gradient-info mr-2" id="editDataType" onclick="updateDataType1();">
					Update
				</button>
		</div>		
   </fieldset>
   </form>
   </div>
</div> 
</div></div></div></div>
<script>
$(document).ready(function () {
	$("#sys_src_sys_id").change(function() {
		$("#bt1").show();
		var src_sys_id = $(this).val();	
		$.post('/publishing/editMDSysList', {
			src_sys_id : src_sys_id
		}, function(data) {
			$('#loadSystemList').html(data)
		});
	})
    $("#file_src_sys_id").change(function() {
    	var src_sys_id = $(this).val();	
		$.post('/publishing/editMDFileList', {
			src_sys_id : src_sys_id
		}, function(data) {
			$('#loadMDFileList').html(data)
		});
	})
	$("#fld_src_sys_id").change(function() {
		var src_sys_id = $(this).val();	
		$.post('/publishing/editMDFieldList', {
			src_sys_id : src_sys_id
		}, function(data) {
			$('#loadMDFieldList').html(data)
		});
	})
});
function getDataTypeList(val) {
    var src_db = $("#src_db_name").val();
    var tgt_db = $("#tgt_db_name").val();
    if(!src_db) {
		alert("Please select Source Database!!");
	}
    if(src_db && tgt_db) {
    	$("#bt4").show();
    	$.post('/publishing/editDataTypeLinkDetails', {
    		src_db : src_db,
    		tgt_db : tgt_db
		}, function(data) {
			$('#loadDataTypeInfo').html(data)
		});
  }
}
</script>
<jsp:include page="../cdg_footer.jsp" />
<script>
$(document).ready(function () {
    $("#success-alert").hide();
           $("#success-alert").fadeTo(10000,10).slideUp(4000, function(){
           });   
    $("#error-alert").hide();
           $("#error-alert").fadeTo(10000,10).slideUp(4000, function(){
            });
    
});
</script>
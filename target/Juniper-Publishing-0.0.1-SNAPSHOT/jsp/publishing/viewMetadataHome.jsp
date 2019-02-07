<jsp:include page="../cdg_header.jsp" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="main-panel">
<div class="content-wrapper">
<div class="row">
   <div class="col-12 grid-margin stretch-card">
      <div class="card">
         <div class="card-body">
            <h4 class="card-title">Data Publishing</h4>
            <p class="card-description">
               View Metadata
            </p>
      <ul class="nav nav-pills nav-fill" id="pills-tab" role="tablist">
		  <li class="nav-item">
		    <a class="nav-link active" id="pills-system" data-toggle="pill" href="#pills-system-conn" role="tab" aria-controls="pills-system" aria-selected="true">System Details</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="pills-file" data-toggle="pill" href="#pills-file-conn" role="tab" aria-controls="pills-file" aria-selected="false">File Details</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="pills-field" data-toggle="pill" href="#pills-field-conn" role="tab" aria-controls="pills-field" aria-selected="false">Field Details</a>
		  </li>
		 <!--  <li class="nav-item">
		    <a class="nav-link" id="pills-mstr-dt" data-toggle="pill" href="#pills-mstr-dt-conn" role="tab" aria-controls="pills-mstr-dt" aria-selected="false">Data Type Link</a>
		  </li> -->
	</ul>
<div class="tab-content" id="pills-tabContent">
  <div class="tab-pane fade show active" id="pills-system-conn" role="tabpanel" aria-labelledby="pills-system">
   <form class="forms-sample" id="viewSystem"
   						name="viewSystem" method="POST"
							action="${pageContext.request.contextPath}/publishing/viewSystem" 
						enctype="application/json">
   <fieldset class="fs">
		<div class="form-group row">
   			 <div class="col-md-3">
				<label>System Name<span style="color:red">*</span></label> 
			</div>
			<div class="col-md-9">
			<select class="form-control form-control1" id="sys_src_sys_id" name="sys_src_sys_id">
				<option value="" selected disabled>select source system...</option>
				<c:forEach var="myMap" items="${viewSSID}">
	        		<option value="${myMap.key}"><c:out value="${myMap.value}"/></option>
	     		</c:forEach>
			</select>
			</div>
		</div>
   		<div id="loadSystemList"></div>
	</fieldset>
   </form>
   </div>
   <div class="tab-pane fade" id="pills-file-conn" role="tabpanel" aria-labelledby="pills-file">
   <form class="forms-sample" id="viewFile"
   						name="viewFile" method="POST"
							action="${pageContext.request.contextPath}/publishing/viewFile" 
						enctype="application/json">
   <fieldset class="fs">
   		<div class="form-group row">
   			 <div class="col-md-3">
				<label>System Name<span style="color:red">*</span></label> 
			</div>
			<div class="col-md-9">
				<select class="form-control form-control1" id="file_src_sys_id" name="file_src_sys_id">
				<option value="" selected disabled>select source system...</option>
				<c:forEach var="myMap" items="${viewSSID}">
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
   <form class="forms-sample" id="viewField"
   						name="publishingAddMetadata" method="POST"
							action="${pageContext.request.contextPath}/publishing/viewField" 
						enctype="application/json">
   <fieldset class="fs">
   			<div class="form-group row">
   			 <div class="col-md-3">
				<label>System Name<span style="color:red">*</span></label> 
			</div>
			<div class="col-md-9">
				<select class="form-control form-control1" id="fld_src_sys_id" name="fld_src_sys_id">
				<option value="" selected disabled>select source system...</option>
				<c:forEach var="myMap" items="${viewSSID}">
	        		<option value="${myMap.key}"><c:out value="${myMap.value}"/></option>
	     		</c:forEach>
				</select>
			</div>
			</div>
			<div id="loadMDFieldList"></div>	
   </fieldset>
   </form>
   </div>
 <%--   <div class="tab-pane fade" id="pills-mstr-dt-conn" role="tabpanel" aria-labelledby="pills-mstr-dt">
   <form class="forms-sample" id="viewMstrDataType"
   						name="viewMstrDataType" method="POST"
							action="/publishing/viewMstrDataType" 
						enctype="application/json">
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
   </fieldset>
   </form>
   </div> --%>
</div> 
</div></div></div></div>
<script>
$(document).ready(function () {
    $("#file_src_sys_id").change(function() {
		var src_sys_id = $(this).val();	
		$.post('${pageContext.request.contextPath}/publishing/viewMDFileList', {
			src_sys_id : src_sys_id
		}, function(data) {
			$('#loadMDFileList').html(data)
		});
	})
	$("#sys_src_sys_id").change(function() {
		var src_sys_id = $(this).val();	
		$.post('${pageContext.request.contextPath}/publishing/viewMDSysList', {
			src_sys_id : src_sys_id
		}, function(data) {
			$('#loadSystemList').html(data)
		});
	})
	$("#fld_src_sys_id").change(function() {
		var src_sys_id = $(this).val();	
		$.post('${pageContext.request.contextPath}/publishing/viewMDFieldList', {
			src_sys_id : src_sys_id
		}, function(data) {
			$('#loadMDFieldList').html(data)
		});
	})
});

function getDataTypeList(val) {
    var src_db = $("#src_db_name").val();
    var tgt_db = $("#tgt_db_name").val();
    if(!src_db)
	{
		alert("Please select Source Database!!");
	}
    if(src_db && tgt_db) {
    	$.post('${pageContext.request.contextPath}/publishing/viewDataTypeLinkDetails', {
    		src_db : src_db,
    		tgt_db : tgt_db
		}, function(data) {
			$('#loadDataTypeInfo').html(data)
		});
    }
}
</script>
<jsp:include page="../cdg_footer.jsp" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%-- <div class="form-group row">
	<div class="col-md-3">
		<label>Select Run ID<span style="color:red">*</span></label>
	</div>
	<div class="col-md-9">
		<select name="run_id"
		id="run_id" class="form-control form-control2">
		<option value="" selected disabled>Select Run ID...</option>
		<c:forEach items="${runIdList}" var="runId">
			<option value="${runId}">${runId}</option>
		</c:forEach>
		</select>
	</div>

</div> --%>

	<!--<div class="form-group">
		 <div class="row"><div class="col-md-3"><label>Run Id's to be Published <span style="color:red">*</span></label> </div>
		
		<div class="col-md-9"><div class="input-group">
                      <input id="d" class="form__daterange">
                      <div class="input-group-append">
                        <button class="btn btn-sm btn-facebook" type="button" onclick="document.getElementById('d').focus();">
                          <i class="mdi mdi-calendar-multiple"></i>
                        </button>
                        ( Filter Run Id Based on Date Range )
                      </div>
                    </div> </div> </div> -->
		     <div class="form-group row" id="runidLabel1">
				   <div class="col-md-3">
					       	<label>Run Id's to be Published <span style="color:red">*</span></label>
			        </div>
	 			</div>	
	 			<div class="form-group">
				<select class="form-control" id="col_name" name="col_name" multiple="multiple">
				<option value="*">Select All</option>
				<c:forEach items="${runIdList}" var="runId">
					<option value="${runId.key}">${runId.value} - ${runId.key}</option>
				</c:forEach>
			   </select>
			   </div>
			   
			   
			  <%--  <c:forEach var="myMap" items="${srcSysIds}">
                                             <option value="${myMap.key}">
                                                <c:out value="${myMap.value}"/>
                                             </option>
                                          </c:forEach> --%>
                                          
                                          
		<!-- </div>-->

<script>
	var x3=document.getElementById("col_name");
	multi(x3);
	
	/* $(document).ready(function () {
		
		$("#d").daterange({
	             onClose: function (dateRangeText) {
	             	alert(dateRangeText);
	             	var src_sys_id = document.getElementById('feed_id');
   					var feed_id = src_sys_id.value;
	             	alert(feed_id);
	             	$.post('/publishing/addMetadataRunIds', {
	           			src_sys_id : feed_id
	           		}, function(data) {
	           			$('#loadRunIds').html(data)
	           		});
	             	}
	         });
		}); */
</script>



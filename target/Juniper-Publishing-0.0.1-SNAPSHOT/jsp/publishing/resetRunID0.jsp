<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
$(document).ready(function () {
   $("#tableId2").change(function() {
	  	var table_name = $(this).val();
		var ds_name = document.getElementById('ds_name3').value;
		$.post('${pageContext.request.contextPath}/publishing/resetRunIDList', {
			table_name : table_name,
			ds_name : ds_name
		}, function(data) {
			$('#loadDSRunIDs').html(data)
		});
	})
	
});
</script>
<input type="hidden" id="ds_name3" name="ds_name3" value="${ds_name}">
<div class="form-group row">
	<div class="col-md-3">
		<label class="align-middle">Table Name<span style="color:red">*</span></label> 
	</div>
	<div class="col-md-9">
	<select name="tableId2" id="tableId2" class="form-control">
		<option value="" selected disabled>Select table name..</option>
		<c:forEach items="${tableList}" var="tableId">
			<option value="${tableId}">${tableId}</option>
		</c:forEach>
	</select>
	</div>
</div>
<div id="loadDSRunIDs"></div>

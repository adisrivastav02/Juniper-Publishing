<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
$(document).ready(function () {
	$("#db_id").change(function() {
		var db_id = $(this).val();	
		var proj_id=document.getElementById('proj_id').value;
		$.post('/publishing/reconRunIds', {
			proj_id : proj_id,
			db_id : db_id
			}, function(data) {
				$('#reconRunIDList').html(data)
			});
	})
});	
</script>
<input type="hidden" id="proj_id" name="proj_id" value="${proj_id}">
<div class="form-group row">
	<div class="col-md-3">
		<label class="align-middle">Dataset Name<span style="color:red">*</span></label> 
	</div>
	<div class="col-md-9">
	<select name="db_id" id="db_id" class="form-control">
		<option value="" selected disabled>Select dataset name..</option>
		<c:forEach items="${db_list}" var="db">
			<option value="${db}">${db}</option>
		</c:forEach>
	</select>
	</div>
</div>
<div id="reconRunIDList"></div>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
$(document).ready(function () {
   $("#field_id_md").change(function() {
	   $("#bt3").show();
		var file_id = $(this).val();
		var sys_id = document.getElementById('field_sys_id').value;
		$.post('/publishing/editFieldMDInfo', {
			file_id : file_id,
			sys_id : sys_id
		}, function(data) {
			$('#loadFieldMDInformation').html(data)
		});
	})
	
});
</script>
<input type="hidden" name="field_sys_id" id="field_sys_id" value="${src_sys_id}">
<div class="form-group row">
	<div class="col-md-3">
		<label class="align-middle">File Name<span style="color:red">*</span></label> 
	</div>
	<div class="col-md-9">
	<select name="field_id_md" id="field_id_md" class="form-control form-control1">
		<option value="" selected disabled>Select file name..</option>
		<c:forEach items="${fileList}" var="fileId">
			<option value="${fileId}">${fileId}</option>
		</c:forEach>
	</select>
	</div>
</div>
	<br>
	<div id="loadFieldMDInformation"></div>
<div class="form-group" id="bt3" style="display: none;">
		<button type="submit" class="btn btn-rounded btn-gradient-info mr-2" onclick="jsonEditField()">
			Update
		</button>
</div>		  

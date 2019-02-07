<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
$(document).ready(function () {
   $("#file_id_md").change(function() {
	   $("#bt2").show();
		var file_id = $(this).val();
		var sys_id = document.getElementById('sys_id').value;
		$.post('/publishing/editFileMDInfo', {
			file_id : file_id,
			sys_id : sys_id
		}, function(data) {
			$('#loadFileMDInformation').html(data)
		});
	})
	
});
</script>
<input type="hidden" name="sys_id" id="sys_id" value="${src_sys_id}">
<div class="form-group row">
	<div class="col-md-3">
		<label class="align-middle">File Name<span style="color:red">*</span></label> 
	</div>
	<div class="col-md-9">
	<select name="file_id_md" id="file_id_md" class="form-control form-control1">
		<option value="" selected disabled>Select file name..</option>
		<c:forEach items="${fileList}" var="fileId">
			<option value="${fileId}">${fileId}</option>
		</c:forEach>
	</select>
	</div>
</div>
	<br>
	<div id="loadFileMDInformation"></div>
<div class="form-group" id="bt2" style="display: none;">
			<button type="submit" class="btn btn-rounded btn-gradient-info mr-2" onclick="jsonEditFile()">
					Update
				</button>
		</div>
	  

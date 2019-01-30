<jsp:include page="../cdg_header.jsp" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
function publishGraph(){
	var src_sys_id = document.getElementById('src_sys_id').value;
	var recon_run_id = document.getElementById('recon_runId').value;
	$.post('/publishing/reconDashboardValues', {
			src_sys_id : src_sys_id,
			recon_run_id : recon_run_id
		}, function(data) {
			$('#loadDashboard').html(data);
	}); 
}
</script>
<div class="main-panel">
	<div class="content-wrapper">
		<div class="row">
			<div class="col-12 grid-margin stretch-card">
				<div class="card">
					<div class="card-body">
						<h4 class="card-title">Reconciliation Dashboard</h4>
						<p class="card-description">Data Publishing</p>
		<div>
				<input type="hidden" name="x" id="x"> 
					<div class="form-group row">
						<div class="col-md-3">
							<label>Source System Name<span style="color: red">*</span></label>
						</div>
						<div class="col-md-9">
							<select class="form-control form-control1" id="src_sys_id"
								name="src_sys_id">
								<option value="" selected disabled>select source
									system...</option>
								<c:forEach var="myMap" items="${srcSysId}">
									<option value="${myMap.key}"><c:out
											value="${myMap.value}" /></option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div id="loadRunIds"></div>
					<br>
					<div class="form-group">
						<button type="submit" class="btn btn-rounded btn-gradient-info mr-2"  data-toggle="modal" data-target="#chartModal"
						onclick="publishGraph();">Publish!</button>
					</div>
					<div id="loadDashboard"></div>
				</div>
			</div>
		</div>
	</div>
</div>
		<script>
$(document).ready(function () {
	$("#src_sys_id").change(function() {
		var src_sys_id = $(this).val();	
		$.post('/publishing/reconRunIds', {
				src_sys_id : src_sys_id
			}, function(data) {
				$('#loadRunIds').html(data)
			});
	})
});	
</script>
<jsp:include page="../cdg_footer.jsp" />
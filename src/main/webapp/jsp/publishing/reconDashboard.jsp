<jsp:include page="../cdg_header.jsp" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
function publishGraph(){
	var proj_id = document.getElementById('proj_id').value;
	var run_id = document.getElementById('run_id').value;
	var db_id = document.getElementById('db_id').value;
	$.post('/publishing/reconDashboardValues', {
			proj_id : proj_id,
			db_id : db_id,
			run_id : run_id
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
							<label>Google Project<span style="color: red">*</span></label>
						</div>
						<div class="col-md-9">
							<select class="form-control form-control1" id="proj_id"
								name="proj_id">
								<option value="" selected disabled>select google project...</option>
								<c:forEach items="${proj_list}" var="proj">
										<option value="${proj}">${proj}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div id="reconDSList"></div>
					<br>
					<div class="form-group">
						<button type="submit" class="btn btn-rounded btn-gradient-info mr-2"  data-toggle="modal" data-target="#chartModal"
						onclick="publishGraph();">Reconcile!</button>
					</div>
					<div id="loadDashboard"></div>
				</div>
			</div>
		</div>
	</div>
</div>
		<script>
$(document).ready(function () {
	$("#proj_id").change(function() {
		var proj_id = $(this).val();	
		$.post('/publishing/reconDSList', {
			proj_id : proj_id
			}, function(data) {
				$('#reconDSList').html(data)
			});
	})
});	
</script>
<jsp:include page="../cdg_footer.jsp" />
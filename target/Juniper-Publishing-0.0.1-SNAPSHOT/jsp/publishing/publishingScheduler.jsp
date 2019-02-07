<jsp:include page="../cdg_header.jsp" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
function jsonconstruct()
{
	document.getElementById('sch_freq').value = cron_construct();
	var data = {};
	$(".form-control1").serializeArray().map(function(x){data[x.name] = x.value;});
	var x = '{"header":{"user":"cdg_admin","service_account":"Publishing_CDG_UK","reservoir_id":"R0001","event_time":"today"},"body":{"data":'+JSON.stringify(data)+'}}';
	      document.getElementById('x').value = x;
	     // alert(x);
	     // console.log(x);
	      //document.getElementById('publishingExtracted').submit();
}

</script>
<div class="main-panel">
	<div class="content-wrapper">
		<div class="row">
			<div class="col-12 grid-margin stretch-card">
				<div class="card">
					<div class="card-body">
						<h4 class="card-title">Data Publishing</h4>
						<p class="card-description">Publish Data</p>
						<%
               if (request.getAttribute("successString") != null) {
               %>
						<p class="text-success h4">${successString}</p>
						<%
               }
               %>
						<%
               if (request.getAttribute("errorString") != null) {
               %>
						<p class="text-danger h4">${errorString}</p>
						<%
               }
               %>
						<div>
							<form class="forms-sample" id="publishingExtracted"
								name="publishingExtracted" method="POST"
								action="${pageContext.request.contextPath}/publishing/publishingExtracted1"
								enctype="application/json">
								<input type="hidden" name="x" id="x"> <input
									type="hidden" class="form-control1" name="sch_freq"
									id="sch_freq"> <input type="hidden"
									class="form-control1" id="target_type" name="target_type"
									value="${pub_tgt_val}"> <input type="hidden"
									class="form-control1" name="src_type" id="src_type"
									value="Extracted"> <input type="hidden"
									class="form-control1" id="deploy_type" name="deploy_type">

								<div class="col-sm-3">
									<label>Source System Name<span style="color: red">*</span></label>
								</div>
								<div class="form-group">
									<select class="form-control form-control1" id="src_sys_id"
										name="src_sys_id">
										<option value="" selected disabled>select source
											system...</option>
										<c:forEach var="myMap" items="${exSrcSysId}">
											<option value="${myMap.key}"><c:out
													value="${myMap.value}" /></option>
										</c:forEach>
									</select>
								</div>
								<label class="col-sm-3 col-form-label">Publishing Type<span
									style="color: red">*</span></label>
								<div class="form_group row">
									<div class="col-sm-3">
										<div class="form-check form-check-info">
											<label class="form-check-label"> <input type="radio"
												class="form-check-input" name="pub_type" id="deploy_type1"
												value="OnDemand"> On Demand
											</label>
										</div>
									</div>
									<div class="col-sm-3">
										<div class="form-check form-check-info">
											<label class="form-check-label"> <input type="radio"
												class="form-check-input" name="pub_type" id="deploy_type2"
												value="Event_Based"> Event Based
											</label>
										</div>
									</div>
									<div class="col-sm-3">
										<div class="form-check form-check-info">
											<label class="form-check-label"> <input type="radio"
												class="form-check-input" name="pub_type" id="deploy_type3"
												value="Time_Based"> Time Based
											</label>
										</div>
									</div>
								</div>
								<br>
								<div class=form-group id="rsrv_div" style="display: none;">
									<div class="col-sm-3">
										<label>Reservoir ID<span style="color: red">*</span></label>
									</div>
									<div class="form-group">
										<select class="form-control form-control1" id="reservoir_id"
											name="reservoir_id">
											<option value="" selected disabled>select reservoir
												id...</option>
											<option value="R0001">R0001</option>
										</select>
									</div>
								</div>
								<div class="form-group" id="sch" style="display: none;">
									<jsp:include page="../cdg_scheduler.jsp" />
								</div>
								<br>
								<div id="loadRunIds" style="display: none;"></div>
								<br>
								<div class="form-group">
									<button type="submit"
										class="btn btn-rounded btn-gradient-info mr-2"
										onclick="jsonconstruct();">Publish</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script>
$(document).ready(function () {
	$("#src_sys_id").change(function() {
		var src_sys_id = $(this).val();	
		//slect new or extracted
		var type = src_sys_id.split(":")[1];
		var a=document.getElementById('src_type');
		a.value="Extracted";
		if(a.value == 'Published')
			{
			$("#loadRunIds").hide();
		} else {
			$.post('/publishing/runIds', {
				src_sys_id : src_sys_id.split(":")[0]
			}, function(data) {
				$('#loadRunIds').html(data)
			});
		}
	})
	$("#deploy_type1").on("change", function(){
		if(document.getElementById('src_type').value == 'Published')
		{
			$("#loadRunIds").hide();
		} 
		else if(document.getElementById('src_type').value == 'Extracted')
		{ 
			$("#loadRunIds").show();
		}
		$("#sch").hide();
		$("#rsrv_div").hide();
		document.getElementById('deploy_type').value="OnDemand";
	})
	$("#deploy_type2").on("change", function(){
		$("#rsrv_div").show();
		$("#sch").hide();
		$("#loadRunIds").hide();
		document.getElementById('deploy_type').value="Event_Based";
	})	
	$("#deploy_type3").on("change", function(){
		$("#rsrv_div").show();
		$("#sch").show();
		$("#loadRunIds").hide();
		document.getElementById('deploy_type').value="Time_Based";
	})
	
	//
});	
</script>
		<jsp:include page="../cdg_footer.jsp" />
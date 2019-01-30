<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<input type="hidden" id="src_sys_id" name="src_sys_id" value="${src_sys_id}">
<div class="form-group row">
	<div class="col-md-3">
		<label class="align-middle">Run ID<span style="color:red">*</span></label> 
	</div>
	<div class="col-md-9">
	<select name="recon_runId" id="recon_runId" class="form-control">
		<option value="" selected disabled>Select RUN ID..</option>
		<c:forEach items="${runIDList}" var="runID">
			<option value="${runID}">${runID}</option>
		</c:forEach>
	</select>
	</div>
</div>

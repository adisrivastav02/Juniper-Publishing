<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="form-group">
	<div class="col-sm-3">
		<label>Select Run ID<span style="color:red">*</span></label> 
	</div>
	<select name="run_id" id="run_id" class="form-control form-control1">
		<option value="" selected disabled>Select Run ID...</option>
		<c:forEach items="${runIdList}" var="runId">
			<option value="${runId}">${runId}</option>
		</c:forEach>
	</select>
</div>
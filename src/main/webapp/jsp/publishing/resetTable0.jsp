<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="form-group row">
	<div class="col-md-3">
		<label class="align-middle">Table Name<span style="color:red">*</span></label> 
	</div>
	<div class="col-md-9">
	<select name="tableId1" id="tableId1" class="form-control">
		<option value="" selected disabled>Select table name..</option>
		<c:forEach items="${tableList}" var="tableId">
			<option value="${tableId}">${tableId}</option>
		</c:forEach>
	</select>
	</div>
</div>

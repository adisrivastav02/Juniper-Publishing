<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<table class="table table-hover table-bordered shadow p-3 mb-5 bg-white rounded">
					<thead>
						<tr class="table-info">
							<th class="text-center"><b>Source Data Type</b></th>
							<th class="text-center"><b>Target Data Type</b></th>
						</tr>
					</thead>
					<c:forEach items="${dataTypeInfo}" var="field">
						<tr class="table-default">
							<td>${field.src_data_type}</td>
							<td>${field.tgt_data_type}</td>
						</tr>
					</c:forEach>
</table>
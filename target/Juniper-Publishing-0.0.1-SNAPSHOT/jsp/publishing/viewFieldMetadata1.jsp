<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<table class="table table-hover table-bordered shadow p-3 mb-5 bg-white rounded">
					<thead>
						<tr class="table-info">
							<th class="text-center"><b>Field Position</b></th>
							<th class="text-center"><b>Field Name</b></th>
							<th class="text-center"><b>Source Data Type</b></th>
							<th class="text-center"><b>Target Data Type</b></th>
							<th class="text-center"><b>Primary Key</b></th>
						</tr>
					</thead>
					<c:forEach items="${fieldBeanList}" var="field">
						<tr class="table-default">
							<td>${field.fld_pos_num}</td>
							<td>${field.src_fld_name}</td>
							<td>${field.src_fld_data_typ}</td>
							<td>${field.trg_fld_data_typ}</td>
							<td>${field.pkey}</td>
						</tr>
					</c:forEach>
</table>
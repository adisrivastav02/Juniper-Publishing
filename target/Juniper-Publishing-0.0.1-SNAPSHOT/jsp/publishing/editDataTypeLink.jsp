<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<table class="table table-hover table-bordered shadow p-3 mb-5 bg-white rounded">
					<thead>
						<tr class="table-info">
							<th class="text-center"><b>Source Data Type</b></th>
							<th class="text-center"><b>Target Data Type</b></th>
						</tr>
					</thead>
					<c:set var="column_count" value="1" scope="page" />
					<c:forEach items="${dataTypeInfo}" var="field">
						<tr class="table-default">
							<%-- <td><input type="text" class="form-control3" ${field.src_data_type}</td> --%>
							<td> <input type="text" class="form-control1"  id="sdt${column_count}" name="sdt${column_count}" value="${field.src_data_type}" readonly="readonly" style="border: none;"> </td>
							<td>
								<select class="form-control form-control1"  id="tdt${column_count}" name="tdt${column_count}">
									<option value="${field.tgt_data_type}">${field.tgt_data_type}</option>
									<option value="String">STRING</option>
									<option value="Date">DATE</option>
									<option value="Int">INTEGER</option>
									<option value="Number">FLOAT</option>
									<option value="TIMESTAMP">TIMESTAMP</option>
								</select>
							</td>			
						</tr>
						<c:set var="column_count" value="${column_count + 1}" scope="page"/>
					</c:forEach>
</table>
<input type="hidden" class="form-control1" name="counter" id="counter" value="${column_count - 1}" />
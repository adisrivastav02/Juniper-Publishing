<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="table-responsive">
	<table class="table table-hover shadow  table-bordered p-3 bg-white rounded">
		<thead>
			<tr class="table-info">
				<th class="text-center">Source File ID</th>
				<th class="text-center">Source Count</th>
				<th class="text-center">Target Count</th>
			</tr>
		</thead>
		<c:forEach items="${reconDataList}" var="file" varStatus="file_cnt">
			<tr class="table-default">
				<td>${file.src_file_id}</td>
				<td>${file.src_count}</td>
				<td>${file.tgt_count}</td>
			</tr>
		</c:forEach>
	</table>
</div>
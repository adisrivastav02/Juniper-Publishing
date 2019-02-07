<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="table-responsive">
                <table class="table table-bordered shadow  table-bordered p-3 mb-5 bg-white rounded">
							<thead>
								<tr class="table-info">
									<th class="text-center"><b>S.No.</b></th>
									<th class="text-center"><b>Table Name</b></th>
									<th class="text-center"><b>Extracted Type</b></th>
									<th class="text-center"><b>Publishing Type</b></th>
									<th class="text-center"><b>Partition Key</b></th>
								</tr>
							</thead>
		
					<c:forEach items="${fileList}" var="file" varStatus="file_cnt">
						<tr class="table-default">
							<td>${file_cnt.count}</td>
							<td id="table${file_cnt.count}">
								<input type="hidden" class="form-control form-control4"  name="table${file_cnt.count}"  value="${file.src_file_name}">${file.src_file_name}
							</td>
							<td>${file.src_load_type}</td>
							<td>
									<select class="form-control form-control4" id="type${file_cnt.count}" name="type${file_cnt.count}">
										<option value="${publishingType[file.src_load_type]}">${publishingType[file.src_load_type]}</option>
										<option value="Append">APPEND</option>
										<option value="Truncate&Load">TRUNCATE & LOAD</option>
									</select>
							</td>
							<td>
									<select class="form-control form-control4" id="key${file_cnt.count}" name="key${file_cnt.count}">
									<option value="NONE">NONE</option>
									<option value="LOAD_START_TIME">PUBLISHED_TIME</option>
										<c:forEach var="myMap" items="${fileFields[file.src_file_name]}">
										<option value="${myMap}"><c:out value="${myMap}"/></option>
     									</c:forEach>
									</select>
							</td>
						</tr>
					</c:forEach>
						
				</table>
				</div>

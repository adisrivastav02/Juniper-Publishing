<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<table class="table table-hover shadow  table-bordered p-3 mb-5 bg-white rounded" >
    <thead>
      <tr class="table-info">
        <th class="text-center"><b>Field Name</b></th>
        <th class="text-center"><b>Value</b></th>
        </tr>
    </thead>
    <tbody>
      <tr class="table-default">
        <td>Source System ID</td>
        <td><input type="text" class="form-control" id="sys_sys_id" value="${systemBean.src_sys_id}" readonly="readonly"></td>
      </tr>      
      <tr class="table-default">
        <td>Source Name</td>
        <td><input type="text" class="form-control" id="sys_src_name" value="${systemBean.src_unique_name}" readonly="readonly"></td>
        </tr>
      <tr class="table-default">
        <td>Source Bucket</td>
        <td><input type="text" class="form-control" id="sys_src_bkt" value="${systemBean.src_bkt}"></td>
        </tr>
      <tr class="table-default">
        <td>Source Location</td>
        <td><input type="text" class="form-control" id="sys_src_loc" value="${systemBean.src_loc}"></td>
        </tr>
      <tr class="table-default">
        <td>Target Project</td>
        <td><input type="text" class="form-control" id="sys_tgt_prjt" value="${systemBean.tgt_prjt}"></td>
       </tr>
      <tr class="table-default">
        <td>Target Dataset</td>
        <td><input type="text" class="form-control" id="sys_tgt_ds" value="${systemBean.tgt_ds}"></td>
      </tr>
      <tr class="table-default">
        <td>Target Type</td>
        <td><input type="text" class="form-control" id="sys_tgt_type" value="${systemBean.tgt_type}"></td>
      </tr>
      <tr class="table-default">
        <td>Source Type</td>
        <td><input type="text" class="form-control" id="sys_src_type" value="${systemBean.src_type}"></td>
      </tr>
    </tbody>
  </table>
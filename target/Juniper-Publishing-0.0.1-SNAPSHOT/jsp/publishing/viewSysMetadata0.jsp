<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<table class="table table-hover table-bordered shadow p-3 mb-5 bg-white rounded">
    <thead>
      <tr class="table-info">
        <th class="text-center"><b>Field Name</b></th>
        <th class="text-center"><b>Value</b></th>
        </tr>
    </thead>
    <tbody>
      <tr class="table-default">
        <td>Source System ID</td>
        <td>${systemBean.src_sys_id}</td>
      </tr>      
      <tr class="table-default">
        <td>Source Name</td>
        <td>${systemBean.src_unique_name}</td>
        </tr>
      <tr class="table-default">
        <td>Source Bucket</td>
        <td>${systemBean.src_bkt}</td>
        </tr>
      <tr class="table-default">
        <td>Source Location</td>
        <td>${systemBean.src_loc}</td>
        </tr>
      <tr class="table-default">
        <td>Target Project</td>
        <td>${systemBean.tgt_prjt}</td>
       </tr>
      <tr class="table-default">
        <td>Target Dataset</td>
        <td>${systemBean.tgt_ds}</td>
      </tr>
      <tr class="table-default">
        <td>Target Type</td>
        <td>${systemBean.tgt_type}</td>
      </tr>
      <tr class="table-default">
        <td>Source Type</td>
        <td>${systemBean.src_type}</td>
      </tr>
    </tbody>
  </table>
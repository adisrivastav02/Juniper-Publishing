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
        <td>Source File Name</td>
        <td>${fileBean.src_file_name}</td>
      </tr>      
      <tr class="table-default">
        <td>Source File Description</td>
        <td>${fileBean.src_file_desc}</td>
        </tr>
      <tr class="table-default">
        <td>Source File Type</td>
        <td>${fileBean.src_file_type}</td>
        </tr>
      <tr class="table-default">
        <td>Source File Delimiter</td>
        <td>${fileBean.src_file_delimiter}</td>
        </tr>
      <tr class="table-default">
        <td>Target Table Name</td>
        <td>${fileBean.tgt_tbl_name}</td>
       </tr>
      <tr class="table-default">
        <td>Source Schema Location</td>
        <td>${fileBean.src_sch_loc}</td>
      </tr>
      <tr class="table-default">
        <td>Source Header Count</td>
        <td>${fileBean.src_hdr_cnt}</td>
      </tr>
      <tr class="table-default">
        <td>Source Trailer Count</td>
        <td>${fileBean.src_trl_cnt}</td>
      </tr>
       <tr class="table-default">
        <td>Source Load Type</td>
        <td>${fileBean.src_load_type}</td>
      </tr>
    </tbody>
  </table>
 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="row " id="sa_dtls" >
      <div class="col-md-3">
         <label>Service Account Name<span style="color:red">*</span></label> 
      </div>
      <div class="form-group col-md-9" >
         <select class="form-control form-control2" id="sa_name" name="sa_name">
            <option value="" selected disabled>Select Service account...</option>
            <c:forEach items="${sa_list}" var="sa">
               <option value="${sa}">${sa}</option>
            </c:forEach>
         </select>
      </div>
   </div>
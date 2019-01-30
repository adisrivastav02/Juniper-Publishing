 <jsp:include page="cdg_header.jsp" />
 
 <div class="main-panel">
        <div class="content-wrapper" >
          <div class="row">
            <div class="col-md-4 stretch-card grid-margin">
              <div class="card bg-gradient-warning card-img-holder text-white">
                <div class="card-body">
                  <img src="${pageContext.request.contextPath}/assets/img/circle.svg" class="card-img-absolute" alt="circle-image"/>
                  <h3 class="font-weight-normal mb-3" ><a class="nav-link text-white" href="/admin/onboardProject"> Add Project </a></h3>
                 </div>
              </div>
            </div>
			
			
            <div class="col-md-4 stretch-card grid-margin">
              <div class="card bg-gradient-success card-img-holder text-white">
                <div class="card-body">
                  <img src="${pageContext.request.contextPath}/assets/img/circle.svg" class="card-img-absolute" alt="circle-image"/>
                  <h3 class="font-weight-normal mb-3"><a class="nav-link text-white" href="/admin/SystemOnboard"> Add System </a></h3>
                 
                </div>
              </div>
            </div>

			
			<div class="col-md-4 stretch-card grid-margin">
              <div class="card bg-gradient-danger card-img-holder text-white">
                <div class="card-body">
                  <img src="${pageContext.request.contextPath}/assets/img/circle.svg" class="card-img-absolute" alt="circle-image"/>
                  <h3 class="font-weight-normal mb-3"><a class="nav-link text-white" href="/admin/userOnboarding"> Add User </a></h3>
                  
                </div>
              </div>
            </div>
                      </div>
<div class="row">
            <div class="col-md-4 stretch-card grid-margin">
              <div class="card bg-gradient-info card-img-holder text-white">
                <div class="card-body">
                  <img src="${pageContext.request.contextPath}/assets/img/circle.svg" class="card-img-absolute" alt="circle-image"/>
                  <h3 class="font-weight-normal mb-3"> <a class="nav-link text-white" href="/admin/usertogrplink"> Add User to a Group </a></h3>
                 
                </div>
              </div>
            </div>
        
          </div>
          </div>

<jsp:include page="cdg_footer.jsp" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${page.addStyle('<link rel="stylesheet" href="/assets/login.css">') }

<c:set var="content" scope="request">
	<h1 class="text-center" style="margin-botton: 0px">La eTienda</h1>
	<h1 class="text-center" style="margin-top: 0px; padding-top: 0px;"><small>${lang.out('Welcome') }</small></h1>
	
	<div class="row">
		<div class="col-xs-12 col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6 col-lg-offset-4 col-lg-4 col">
			<c:if test="${sessionScope.sessionUser == null }">
				<div>
					<!-- Nav tabs -->
					<ul class="nav nav-tabs" role="tablist" style="padding-left: 5px;">
						<li role="presentation" class="${pathParts[0] == 'signup' ? 'active' : 'unactive'}"><a href="${page.rootUrl}/session/signup">${lang.out('Signup') }</a></li>
						<li role="presentation" class="${pathParts[0] == 'login' ? 'active' : 'unactive'}"><a href="${page.rootUrl}/session/login">${lang.out('Login') }</a></li>
						<li role="presentation" class="${pathParts[0] == 'password' ? 'active' : 'unactive'}"><a href="${page.rootUrl}/session/password">${lang.out('Forgot password')}</a></li>
					</ul>
					
					<!-- tab panes -->
					<div class="tab-content form_container">
					
						<c:choose>
							<c:when test="${pathParts[0] == 'signup'}">
								<jsp:include page="/WEB-INF/jsp/dap/signup.jsp"></jsp:include>
								${signup_form}
							</c:when>
							<c:when test="${pathParts[0] == 'login'}">
								<jsp:include page="/WEB-INF/jsp/dap/login.jsp"></jsp:include>
								${login_form}
							</c:when>
							<c:when test="${pathParts[0] == 'password'}">
								<jsp:include page="/WEB-INF/jsp/dap/password.jsp"></jsp:include>
								${password_form}
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:if>
			<c:if test="${sessionScope.sessionUser != null }">
				<h3 class="text-center">${lang.out('It seems that have already logged in')}</h3>
				<div class="text-center">
					<a href="${page.rootUrl}/home" class="btn btn-primary btn-lg">${lang.out('Click here to go Home') }</a>
				</div>
			</c:if>
		</div>
	</div>
</c:set>

<jsp:include page="/WEB-INF/jsp/templates/login.jsp"></jsp:include>
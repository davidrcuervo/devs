<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${page.addStyle("<link rel='stylesheet' href='/assets/home.css' />") }
${page.addScript("<script src='/assets/home.js'></script>") }

<c:set scope="request" var="title" value="Home" />
<c:set scope="request" var="csshomecols" value="col-xs-12 col-sm-4 col-md-4 col-lg-3 jsLink"/>

<c:set scope="request" var="content">
	<h1 class="text-center" style="margin-bottom: 100px; margin-top: 150px">La &nbsp;eTienda</h1>
	<div class="row">
		<c:if test="${sessionScope.sessionUser == null }">
			<div class="${csshomecols}" onclick="jsLink('${page.rootUrl}/session/login')">
				<div class="text-center"><span class="glyphicon glyphicon-user" style="font-size: 150px;"></span></div>
				<h3 class="text-center" style="margin-top: 0px;">${lang.out('Login') }</h3>
			</div>
		</c:if>
		<c:if test="${sessionScope.sessionUser != null }">
			<div class="${csshomecols}" onclick="jsLink('${page.rootUrl}/session/logout')">
				<div class="text-center"><span class="glyphicon glyphicon-log-out" style="font-size: 150px;"></span></div>
				<h3 class="text-center" style="margin-top: 0px;">${lang.out('Close Session') }</h3>
			</div>
			
			<c:if test="${acl.isPartOf('managers') }">
				<c:set var="tempUrl" scope="request" value="${page.getLinkFromRootUrl('/crud')}" />
					<div class="${csshomecols}" onclick="jsLink('${tempUrl}')">
						<div class="text-center"><span class="glyphicon glyphicon-cog" style="font-size: 150px;"></span></div>
						<h3 class="text-center" style="margin-top: 0px;">${lang.out('Manage') }</h3>
					</div>
				<c:remove var="tempUrl" scope="request" />
			</c:if>
			
		</c:if>
		<div class="${csshomecols}" onclick="jsLink('${page.rootUrl}/media/video')">
			<div class="text-center"><span class="glyphicon glyphicon-facetime-video" style="font-size: 150px;"></span></div>
			<h3 class="text-center" style="margin-top: 0px;">${lang.out('Videos')}</h3>
		</div>
		<div class="${csshomecols}">
			<div class="text-center"><span class="glyphicon glyphicon-picture" style="font-size: 150px;"></span></div>
			<h3 class="text-center" style="margin-top: 0px;">${lang.out('Images')}</h3>
		</div>
		<div class="${csshomecols}" onclick="jsLink('${page.rootUrl}/wiki')">
			<div class="text-center"><span class="glyphicon glyphicon-file" style="font-size: 150px;"></span></div>
			<h3 class="text-center" style="margin-top: 0px;">${lang.out('Wiki')}</h3>
		</div>
		<div class="${csshomecols}" onclick="jsLink('${page.rootUrl}/lang')">
			<div class="text-center"><span class="glyphicon glyphicon-globe" style="font-size: 150px;"></span></div>
			<h3 class="text-center" style="margin-top: 0px;">${lang.out('Languages')}</h3>
		</div>
	</div>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>

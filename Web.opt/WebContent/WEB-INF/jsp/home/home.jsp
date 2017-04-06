<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${page.addStyle("<link rel='stylesheet' href='/assets/home.css' />") }
${page.addScript("<script src='/assets/home.js'></script>") }

<c:set scope="request" var="title" value="Home" />
<c:set scope="request" var="csshomecols" value="col-xs-12 col-sm-4 col-md-4 col-lg-3 jsLink"/>

<c:set scope="request" var="content">
	<h1 class="text-center" style="margin-bottom: 100px; margin-top: 150px">La &nbsp;eTienda</h1>
	<div class="row">
		<div class="${csshomecols}" onclick="jsLink('${page.rootUrl}/media/video')">
			<div class="text-center"><span class="glyphicon glyphicon-facetime-video" style="font-size: 150px;"></span></div>
			<h3 class="text-center" style="margin-top: 0px;">VIDEOS</h3>
		</div>
		<div class="${csshomecols}">
			<div class="text-center"><span class="glyphicon glyphicon-picture" style="font-size: 150px;"></span></div>
			<h3 class="text-center" style="margin-top: 0px;">IMAGES</h3>
		</div>
		<div class="${csshomecols}" onclick="jsLink('${page.rootUrl}/wiki')">
			<div class="text-center"><span class="glyphicon glyphicon-file" style="font-size: 150px;"></span></div>
			<h3 class="text-center" style="margin-top: 0px;">WIKI</h3>
		</div>
	</div>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>

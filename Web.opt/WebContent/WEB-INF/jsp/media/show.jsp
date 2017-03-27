<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="content">
	<div>Hello World</div>
	<div>${pathParts[1] }</div>
	<div>${page.urlWithPattern}/video/${pathParts[1]}</div>
	
	
	<video controls style="max-width: 100%; height: auto;">
		<source src="${page.urlWithPattern}/video/${pathParts[1]}.mp4" type="video/mp4" />
		<source src="${page.urlWithPattern}/video/${pathParts[1]}.webm" type="video/webm" />
		<p>Your browser cannot play this video.</p>
	</video>
</c:set>

<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>
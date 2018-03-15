<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="content">
	<c:if test="${pathParts[1] == 'create' }">
		<h1 class="text-center">Creation crud</h1>
			<div>${forma.smallTest }</div>
			<div>${forma.print }</div>
	</c:if>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>
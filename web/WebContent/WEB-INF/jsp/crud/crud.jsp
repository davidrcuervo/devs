<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<c:set scope="request" var="content">
	<c:if test="${pathParts[1] == 'create' }">
		<h1 class="text-center">Creation crud</h1>
			<%-- <div>${forma.print }</div> --%>
			<t:form />
	</c:if>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>
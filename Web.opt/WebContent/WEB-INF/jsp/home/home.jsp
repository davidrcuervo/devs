<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="title" value="home" />

<c:set scope="request" var="content">
	<h1 class="text-center">HOME</h1>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>

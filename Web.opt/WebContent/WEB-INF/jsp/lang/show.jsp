<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="title" value="Langes" />
<jsp:include page="/WEB-INF/jsp/forms/langs.jsp"></jsp:include>

<c:set scope="request" var="content">
	<h3 class="text-center">Langes</h3>
	<div>${lang_form}</div>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>
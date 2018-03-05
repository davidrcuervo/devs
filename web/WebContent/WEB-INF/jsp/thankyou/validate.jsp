<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="title" value="Password Validate" />

<c:set scope="request" var="content">
	<h1 class="text-center">${lang.out('Thank You')}</h1>
	<h3 class="text-center">${lang.out('You have confirmed you password successfully')}</h3>
	<p> ${lang.out('You are ready to go')}!!!</p>
	<a href="${page.rootUrl}/session/login" class="btn btn-primary">${lang.out('Click here to LogIn')}</a>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/thankyou.jsp"></jsp:include>
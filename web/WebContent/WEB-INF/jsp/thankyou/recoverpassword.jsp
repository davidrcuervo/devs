<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="title" value="La eTienda. Password Successful Reset" />

<c:set scope="request" var="content">
	<h1 class="text-center">${lang.out('Thank You')}!!!</h1>
	<h3 class="text-center">${lang.out('Your password has been successfully reseted') }</h3>
	<p class="text-center">${lang.out('Now, LogIn into the site and enjoy the content') }</p>
	<div class="text-center">
		<a href="${page.getLinkFromRootUrl('/session/login')}" class="btn btn-primary">${lang.out('Go to LogIn') }</a>
	</div>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/thankyou.jsp"></jsp:include>
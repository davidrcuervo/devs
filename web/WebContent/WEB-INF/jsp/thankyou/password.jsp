<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="title" value="La eTienda. Password Recovery" />

<c:set scope="request" var="content">
	<h1 class="text-center">${lang.out('Thank You')}!!!</h1>
	<h3 class="text-center">${lang.out('You have reset your password') }</h3>
	<p class="text-center">${lang.out('An eMail has been sent to your address. Please follow the instructions to reset your password') }</p>
	<div class="text-center">
		<a href="${page.getLinkFromRootUrl('/home')}" class="btn btn-primary">${lang.out('Go to LogIn') }</a>
	</div>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/thankyou.jsp"></jsp:include>
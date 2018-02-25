<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="title" value="Signup Succesfull" />

<c:set scope="request" var="content">
	<h1>Thank You</h1>
	<p>You have signed up successfully</p>
	<p>Please do not hesitate to confirm you email address and password</p>
	<a href="${pager.rootUrl}" class="btn btn-primary">Continue to home page</a>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/thankyou.jsp"></jsp:include>
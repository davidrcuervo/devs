<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="emailSubject" scope="request" value="La-eTienda. Password has been reset" />

<c:set var="content" scope="request">
	<table>
		<tr><td>${lang.out('Hello')} ${user.cn} ${user.sn }.</td></tr>
		<tr><td>${lang.out('Your password has been reseted successfully.') }</td></tr>
		<tr><td><a href="${page.getLinkFromRootUrl('/home')}">${lang.out('Click here to go back to La eTienda')}</a></td></tr>
	</table>
</c:set>

<jsp:include page="/WEB-INF/jsp/templates/email.jsp"></jsp:include>
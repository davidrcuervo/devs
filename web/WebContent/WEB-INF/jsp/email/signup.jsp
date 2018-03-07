<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="emailSubject" scope="request" value="La-eTienda. Password recovery" />
<c:set var="content" scope="request">
	<table>
		<tr>
			<td>	Welcome ${user.cn} ${user.sn }. You have registered successfully.</td>
		</tr>
		<tr>
			<td>Please, validate you email address and password use the link below</td>
		</tr>
		<tr>
			<td><a href="${page.urlWithPattern}/validate/${page.simpleEncrypt(user.uid)}">Click here to validate eMail and password</a></td>
		</tr>	
	</table>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/email.jsp"></jsp:include>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.laetienda.tomcat.Page" %>
<%@ page import="com.laetienda.entities.User" %>

<c:set var="emailSubject" scope="request" value="La-eTienda. Password recovery" />
<%  
	User user = (User)request.getAttribute("user");
	Page pag = (Page)request.getAttribute("page");
	String linkForPassword = "/recoverpassword/" + pag.simpleEncrypt(user.getUid() + ":" + user.getEmail());
	request.setAttribute("linkForPassword", linkForPassword);
%>

<c:set var="content" scope="request">
	<table>
		<tr>
			<td>${lang.out('Hello')} ${user.cn} ${user.sn }. ${lang.out('You are trying to reset your password')}.</td>
		</tr>
		<tr>
			<td>${lang.out('Please, use the link below to reset your password') }</td>
		</tr>
		<tr>
			<td><a href="${page.getLinkFromUrlWithPattern(linkForPassword)}">${lang.out('Click here to recover your password')}</a></td>
		</tr>
		<tr>
			<td>${linkForPassword }</td>
		</tr>
	</table>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/email.jsp"></jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="content">
	<div><a href="../../${notes.file.parentFile.name}/">${notes.file.parentFile.name}</a> -> ${notes.file.name}</div>
	
	<c:if test="${notes.file.isDirectory()}">
		<div><b>FOLDERS</b></div>
		<c:forEach var="folder" items="${notes.folders}">
			<div><a href="./${folder}/">${folder}</a></div>
		</c:forEach>
		
		<div><b>NOTES</b></div>
		<c:forEach var="file" items="${notes.files}">
			<div><a href="./${file}/">${file}</a></div>
		</c:forEach>
	</c:if>
	
	<c:if test="${notes.file.isFile()}">
		<div>${notes.print }</div>
	</c:if>
	
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>
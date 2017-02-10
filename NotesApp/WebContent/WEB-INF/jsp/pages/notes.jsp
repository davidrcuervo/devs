<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${page.addStyle('<link rel="stylesheet" href="/assets/styles/notes.css">')}
${page.addScript('<script src="/assets/scripts/notes.js"></script>')}

<c:set scope="request" var="content">
	<div>
		<a href="${page.rootUrl}"><span class="glyphicon glyphicon-home"></span>&nbsp; Home</a> &nbsp; | &nbsp;
		<a href="${page.url}/..">${notes.file.parentFile.name}</a> -> ${notes.file.name}
	</div>
	
	<c:if test="${notes.file.isDirectory()}">
		<div><b>FOLDERS</b></div>
		<c:forEach var="folder" items="${notes.folders}">
			<div><a href="${page.url}/${page.getEncodeUrl(folder)}">${folder}</a></div>
		</c:forEach>
		
		<div><b>NOTES</b></div>
		<c:forEach var="file" items="${notes.files}">
			<div><a href="${page.url}/${page.getEncodeUrl(file)}">${file}</a></div>
		</c:forEach>
	</c:if>
	
	<c:if test="${notes.file.isFile()}">
		<div>${notes.print }</div>
	</c:if>
	
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>
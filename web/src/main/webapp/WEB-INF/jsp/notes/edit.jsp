<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${page.addStyle('<link rel="stylesheet" href="/assets/notes.css">')}
${page.addScript('<script src="/assets/notes.js"></script>')}

<c:set scope="request" var="title" value="Edit: ${notes.file.name}" />

<c:set scope="request" var="content">

	<textarea id="">${notes.read}</textarea>

	<c:set scope="request" var="nav_fast_button">
		${nav_fast_button}
		<a title="save" href="${page.url}${page.addVar('action', 'save')}" class="btn btn-default btn-sm" data-toggle="tooltip" data-placement="bottom"><span class="glyphicon glyphicon-floppy-disk"></span></a>
		<a title="close" href="${page.url}${page.addVar('action', 'close')}" class="btn btn-default btn-sm" data-toggle="tooltip" data-placement="bottom" ><span class="glyphicon glyphicon-remove"></span></a>
	</c:set>

</c:set>

<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/WEB-INF/jsp/templates/web.jsp">
	<c:set  var="content">
		<div class="row">
			<div class="col-xs-12 col-md-4">
				<a href="${page.urlWithPattern}/${forma.form.name}/new" class="btn btn-primary btn-lg" role="button"><span class="glyphicon glyphicon-plus"></span> ${lang.out('New')} ${forma.form.name}</a>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12" style="height: 300px; border: 2px solid #000000">
				
			</div>
		</div>
	</c:set>
</c:import>
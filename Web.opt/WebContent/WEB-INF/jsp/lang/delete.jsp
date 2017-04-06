<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="delete_modal">

	<div class="modal show" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<a class="close" href="${page.urlWithPattern}?page=${pageNumber}"><span aria-hidden="true">&times;</span></a>
					<h4 class="modal-title">Warning</h4>
				</div>
				<div class="modal-body">
					<p>Please confirm to remove this entry.</p>
				</div>
				<div class="modal-footer">
					<form method="post">
						<input type="hidden" name="id" value="${pathParts[1] }" />
						<a href="${page.urlWithPattern}?page=${pageNumber}" class="btn btn-danger"><span class="glyphicon glyphicon-remove"> Cancel</span></a>
						<button type="submit" name="submit" value="lang_delete" class="btn btn-default"><span class="glyphicon glyphicon-ok"> Confirm</span></button>
					</form>
					<c:if test="${langInstance.errors['lang_delete'] != null}">
						<div class="text-danger text-center">
							<c:forEach var="error" items="${langInstance.errors['lang_delete'] }">
								<small>${error}</small><br />
							</c:forEach>
						</div>
					</c:if>
				</div>
			</div>
		</div>
	</div>

</c:set>

<jsp:include page="show.jsp"></jsp:include>
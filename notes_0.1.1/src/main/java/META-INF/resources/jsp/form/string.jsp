<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="form-group">
	<label for="uid">${lang.out(fieldName)}</label>
			<div class="input-group input-group-lg">
			<span class="input-group-addon">
				<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
			</span>
		<input type="text" class="form-control" id="${fieldName }" name="${fieldName }" placeholder="${lang.out(fieldName)}" />
	</div>
	<c:if test="${entidad.errors[fieldName] != null }">
		<div class="text-danger text-center">
			<c:forEach var="error" items="${user.errors[fieldName] }">
				<small>${lang.out(error) }</small>
			</c:forEach>
		</div>
	</c:if>	
</div>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="content">
	<h1 class="text-center">${lang.out('Welcome Back')}!</h1>
	<h3 class="text-center">${lang.out('Please validate your password')}</h3>
	<form method="post">
		<div class="form-group">
			<label for="uid">${lang.out('Username')}</label>
			<div class="input-group input-group-lg">
				<span class="input-group-addon">
					<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
				</span>
				<input type="text" class="form-control" id="uid" name="uid" placeholder="${lang.out('Username')}" value="${user.uid }" readonly="readonly" />
			</div>
		</div>
		<div class="form-group">
			<label for="password"><span class="" aria-hidden="true"></span> ${lang.out('Password') }:</label>
			<div class="input-group input-group-lg">
				<span class="input-group-addon">
					<span class="glyphicon glyphicon-lock" aria-hidden="true"></span>
				</span>
				<input class="form-control" type="password" id="password" name="password" placeholder="${lang.out('Type your password') }">
			</div>
		</div>

		<button type="submit" name="submit" value="validate" class="btn btn-primary btn-block">${lang.out('Validate') }</button>	
		<c:if test="${user.errors['user'] != null}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${user.errors['user'] }">
					<small>${lang.out(error)}</small><br />
				</c:forEach>
			</div>
		</c:if>
		
	</form>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/login.jsp"></jsp:include>
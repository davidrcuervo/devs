<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="content">
	<h1 class="text-center" style="margin-botton: 0px">La eTienda</h1>
	<h1 class="text-center" style="margin-top: 0px; padding-top: 0px;"><small>${lang.out('Recover your password') }</small></h1>
	<form method="post">
		<div class="form-group">
			<label for="password"><span class="" aria-hidden="true"></span> ${lang.out('Password') }:</label>
			<div class="input-group input-group-lg">
				<span class="input-group-addon">
					<span class="glyphicon glyphicon-lock" aria-hidden="true"></span>
				</span>
				<input class="form-control" type="password" id="password" name="password" placeholder="${lang.out('Type your password') }">
			</div>
			<c:if test="${user.errors['password'] != null}">
				<div class="text-danger text-center">
					<c:forEach var="error" items="${user.errors['password'] }">
						<small>${lang.out(error)}</small><br />
					</c:forEach>
				</div>
			</c:if>
		</div>
		
		<div class="form-group">
			<label for="password_confirm"><span class="" aria-hidden="true"></span> ${lang.out('Confirm password') }:</label>
			<div class="input-group input-group-lg">
				<span class="input-group-addon">
					<span class="glyphicon glyphicon-lock" aria-hidden="true"></span>
				</span>
				<input class="form-control" type="password" id="password_confirm" name="password_confirm" placeholder="${lang.out('Confirm your password') }">
			</div>
		</div>
		
		<button type="submit" name="submit" value="passwordrecover" class="btn btn-primary btn-block">${lang.out('Reset Password') }</button>
			
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
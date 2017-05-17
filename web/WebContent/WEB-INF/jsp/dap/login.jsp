<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="login_form">
	<form method="post">
		<div class="form-group">
			<label for="login_email">${lang.out('Email') }:</label>
			<div class="input-group input-group-lg">
				<span class="input-group-addon">
					<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>
				</span>
				<input type="text" class="form-control" id="login_email" name="username" placeholder="${lang.out('Email address') }" value="${auth.username }">
			</div>
			<c:if test="${auth.errors['username'] != null}">
				<div class="text-danger text-center">
					<c:forEach var="error" items="${auth.errors['username'] }">
						<small>${lang.out(error) }</small><br />
					</c:forEach>
				</div>
			</c:if>
		</div>
		<div class="form-group">
			<label for="login_password"><span class="" aria-hidden="true"></span> ${lang.out('Password') }:</label>
			<div class="input-group input-group-lg">
				<span class="input-group-addon">
					<span class="glyphicon glyphicon-lock" aria-hidden="true"></span>
				</span>
				<input class="form-control" type="password" id="login_password" name="password" placeholder="${lang.out('Type your password') }">
			</div>
			<c:if test="${auth.errors['password'] != null}">
				<div class="text-danger text-center">
					<c:forEach var="error" items="${auth.errors['password'] }">
						<small>${lang.out(error) }</small><br />
					</c:forEach>
				</div>
			</c:if>
		</div>
			
		<button type="submit" name="submit" value="login" class="btn btn-primary btn-lg btn-block"><span class="glyphicon glyphicon-log-in"></span>  ${lang.out('Login') }</button>
		<c:if test="${auth.errors['login'] != null}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${auth.errors['login'] }">
					<small>${lang.out(error) }</small><br />
				</c:forEach>
			</div>
		</c:if>
	</form>
</c:set>
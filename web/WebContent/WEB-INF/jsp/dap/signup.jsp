<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="signup_form">
	
	<form method="post">
		<div class="form-group">
			<label for="email">${lang.out('Email') }:</label>
			<div class="input-group input-group-lg">
				<span class="input-group-addon">
					<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>
				</span>
				<input type="text" class="form-control" id="email" name="email" placeholder="${lang.out('Email address') }" value="${user.username }">
			</div>
			<c:if test="${user.errors['username'] != null}">
				<div class="text-danger text-center">
					<c:forEach var="error" items="${user.errors['username'] }">
						<small>${lang.out(error)}</small><br />
					</c:forEach>
				</div>
			</c:if>
		</div>
		
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
			<label for="password_confirm"><span class="" aria-hidden="true"></span> ${lang.out('Password') }:</label>
			<div class="input-group input-group-lg">
				<span class="input-group-addon">
					<span class="glyphicon glyphicon-lock" aria-hidden="true"></span>
				</span>
				<input class="form-control" type="password" id="password_confirm" name="password_confirm" placeholder="${lang.out('Confirm your password') }">
			</div>
		</div>
		
		<div class="form-group">
			<label for="language_select">${lang.out('Prefered language') }</label>
			<select id="language_select" class="form-control" name="language">
				<option value="no">${lang.out('Select a language') }</option>
				<option value="en" ${user.language.value == 'en' ? 'selected' : '' }>English</option>
				<option value="es" ${user.language.value == 'es' ? 'selected' : '' }>Español</option>
				<option value="es" ${user.language.value == 'fr' ? 'selected' : '' }>Francais</option>
			</select>
			<c:if test="${user.errors['language'] != null}">
				<div class="text-danger text-center">
					<c:forEach var="error" items="${user.errors['language'] }">
						<small>${lang.out(error)}</small><br />
					</c:forEach>
				</div>
			</c:if>
		</div>
		
		<button type="submit" name="submit" value="signup" class="btn btn-primary btn-block">${lang.out('Signup') }</button>
		
		<c:if test="${user.errors['user'] != null}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${user.errors['user'] }">
					<small>${lang.out(error)}</small><br />
				</c:forEach>
			</div>
		</c:if>
	</form>
</c:set>
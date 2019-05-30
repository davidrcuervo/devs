<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="signup_form">
	<c:if test="${ UserSignupForm != 'success' }">
		<form method="post">
			<div class="form-group">
				<label for="uid">${lang.out('Username')}</label>
				<div class="input-group input-group-lg">
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
					</span>
					<input type="text" class="form-control" id="uid" name="uid" placeholder="${lang.out('Username')}" value="${user.uid }"/>
				</div>
				<c:if test="${user.errors['uid'] != null }">
					<div class="text-danger text-center">
						<c:forEach var="error" items="${user.errors['uid'] }">
							<small>${lang.out(error) }</small>
						</c:forEach>
					</div>
				</c:if>
			</div>
		
		
			<div class="form-group">
				<label for="cn">${lang.out('First Name')}</label>
				<div class="input-group input-group-lg">
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
					</span>
					<input type="text" class="form-control" id="cn" name="cn" placeholder="${lang.out('First Name')}" value="${user.cn }" />
				</div>
				<c:if test="${user.errors['cn'] != null }">
					<div class="text-danger text-center">
						<c:forEach var="error" items="${user.errors['cn'] }">
							<small>${lang.out(error)}</small>
						</c:forEach>
					</div>
				</c:if>
			</div>
			
			<div class="form-group">
				<label for="sn">${lang.out('Last Name')}</label>
				<div class="input-group input-group-lg">
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
					</span>
					<input type="text" class="form-control" id="sn" name="sn" placeholder="${lang.out('Last Name')}" value="${user.sn }" />
				</div>
				<c:if test="${user.errors['sn'] != null }">
					<div class="text-danger text-center">
						<c:forEach var="error" items="${user.errors['sn'] }">
							<small>${lang.out(error)}</small>
						</c:forEach>
					</div>
				</c:if>
			</div>
			
			<div class="form-group">
				<label for="email">${lang.out('Email') }:</label>
				<div class="input-group input-group-lg">
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>
					</span>
					<input type="email" class="form-control" id="email" name="email" placeholder="${lang.out('Email address') }" value="${user.email }">
				</div>
				<c:if test="${user.errors['email'] != null}">
					<div class="text-danger text-center">
						<c:forEach var="error" items="${user.errors['email'] }">
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
					<c:forEach var="option" items="${vars.options('languages') }">
						<option value="${option.name}">${option.description}</option>
					</c:forEach>
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
	</c:if>
	<c:if test="${UserSignupForm == 'success'} ">
			<div>
				${lang.out('The user has beed registered succesfully')}
			</div>
	</c:if>
</c:set>
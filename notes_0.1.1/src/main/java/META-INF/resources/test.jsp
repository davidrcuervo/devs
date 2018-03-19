<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1>Hello Hidden JSP World!!!</h1>

<form method="post">
	<div class="form-group">
		<label for="uid">${lang.out('Username')}</label>
		<div class="input-group input-group-lg">
			<span class="input-group-addon">
				<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
			</span>
			<input type="text" class="form-control" id="uid" name="uid" placeholder="${lang.out('Username')}" value="${user.uid }"/>
		</div>
		<c:if test="${empty user}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${user.errors['uid'] }">
					<small>${lang.out(error) }</small>
				</c:forEach>
			</div>
		</c:if>
	</div>
	
	<div class="form-group">
		<label for="select_id">${lang.out('label')}</label>
		<select id="select_id" name="${name }" class="form-control">
	  		<option value="none" selected>${lang.out("Select an option")}</option>
	  		<option value="${value }">2</option>
	  		<option value="${value }">3</option>
	  		<option value="${value }">4</option>
	  		<option value="${value }">5</option>
		</select>
	</div>
	
	
	<button type="submit" name="submit" value="signup" class="btn btn-primary btn-block">${lang.out('Signup') }</button>
			
	<c:if test="${empty user}">
		<div class="text-danger text-center">
			<c:forEach var="error" items="${user.errors['user'] }">
				<small>${lang.out(error)}</small><br />
			</c:forEach>
		</div>
	</c:if>
</form>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<form method="post">
	<c:forEach var="input" items="${forma.form.inputs }">
		<c:choose>
			<c:when test="${input.type eq 'string' }">
				<c:set scope="request" var="tempId" value="id_form_${forma.form.name}_input_${input.name}" />
				<c:set scope="request" var="tempIdErrorStatus" value="id_form_${forma.form.name}_inputError2Status_${input.name}" />
				
				<div class="form-group ${forma.hasError(input) ? 'has-error has-feedback' : '' }">  
					<label class="control-label" for="${requestScope.tempId}">${lang.out(input.name)}</label>
					<c:if test="${not empty input.glyphicon}">
						<div class="input-group input-group-lg">
							<span class="input-group-addon">
								<span class="glyphicon ${input.glyphicon}" aria-hidden="true"></span>
							</span> 
					</c:if>  
						<input 	type="text" 
								class="form-control" 
								id="${requestScope.tempId}" 
								name="${input.name }" 
								placeholder="${empty input.placeholder ? lang.out(input.placeholder) : ''}" 
								value="${user.uid }"
								${forma.hasError(input) ? 'aria-describedby="' : ''}${forma.hasError(input) ? tempIdErrorStatus : ''}${forma.hasError(input) ? '"' : ''}
								/> 
						<c:if test="${forma.hasError(input)}">
							<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
							<span id="${requestScope.tempIdErrorStatus}" class="sr-only">(error)</span>
						</c:if> 
					<c:if test="${not empty input.glyphicon}">
						</div>
					</c:if> 
					<c:if test="${forma.hasError(input)}">
						<div class="text-danger text-center">
							<c:forEach var="error" items="${forma.entidad.get(input.name)}">
								<small>${lang.out(error) }</small>
							</c:forEach>
						</div>
					</c:if> 
				</div> 
				<c:remove var="tempIdErrorStatus" scope="request" />
				<c:remove var="tempId" scope="request" /> 
			</c:when>
			<c:otherwise>
				<div>This type of input is not defined</div>
			</c:otherwise>
		</c:choose>
	</c:forEach>	
	<c:forEach var="row" items="${forma.advanced}">
		<div class="form-group form-group-sm">
			<label for="${row.id}">${lang.out(row.label) }</label>
			<select id="${requestScope.tempId}" name="group" class="form-control">
				<option value="none">${lang.out('Select An Option')}</option>
				<c:forEach var="option" items="${row.options }">
					<option value="${option.id}" ${option.id eq row.selected ? 'selected' : ''}>${option.name}</option>
				</c:forEach>
			</select>
		</div>
	</c:forEach>
	
	<button type="submit" name="submit" value="${forma.form.name}" class="btn btn-primary btn-block">${lang.out(forma.submitText)}</button>
	<c:if test="${not empty forma.entidad.errors[forma.form.name]}">
		<div class="text-danger text-center">
			<c:forEach var="error" items="${forma.entidad.errors[forma.form.name]}">
				<small>${lang.out(error)}</small>			
			</c:forEach>
		</div>
	</c:if>
</form>


<%-- 
		<div class="form-group">
			<c:set scope="request" var="tempId" value="id_form_${forma.form.name}_input_permission_owner" />
			<label for="${requestScope.tempId}">${lang.out('Set Owner:') }</label>
			<select id="${requestScope.tempId}" name="owner" class="form-control">
				<c:choose>
					<c:when test="${empty sessionScope.sessionUser}">
						<option value="none" selected>${lang.out("Select an option") }</option>
					</c:when>  				 
					<c:when test="${forma.action eq 'create' }">				 
						<option value="none">${lang.out('Select an option')}</option>
						<option value="${sessionScope.sessionUser.id}" selected>${sessionScope.sessionUser.getFullName(requestScope.dap)}</option>
						<c:set scope="request" var="editors" value="${requestScope.acl.findUsersInAcl(forma.entidad.write) }" />
					</c:when>
					<c:when test="${forma.action eq 'edit'}">
						<option value="none">${lang.out('Select an option')}</option>
						<option value="${forma.entidad.owner.id}" selected>${forma.entidad.owner.getFullName(requestScope.dap)}</option>
						<c:set scope="request" var="editors" value="${requestScope.acl.findUsersInAcl(forma.entidad.write)}" />
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
						
				<c:forEach var="editor" items="${requestScope.editors }">
					<option value="${editor.id}">${editor.getFullName(requestScope.dap)}</option>
				</c:forEach>		
			</select>
			<c:remove scope="request" var="tempId" />
		</div>
	--%>

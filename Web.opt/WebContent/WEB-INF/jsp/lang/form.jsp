<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="lang_form">
	<form method="post" class="">
		<div class="form-group row">
			<div class="col-xs-12 col-sm-2 col-md-2 col-lg-2 col-lg-offset-1">
				<label class="sr-only" for="identifier">Identifier</label>
				<input type="text" class="form-control" name="identifier" id="identifier" placeholder="Identifier" value="${langInstance.identifier}">
				<c:if test="${langInstance.errors['identifier'] != null}">
					<div class="text-danger text-center">
						<c:forEach var="error" items="${langInstance.errors['identifier'] }">
							<small>${error}</small><br />
						</c:forEach>
					</div>
				</c:if>
			</div>
			<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">
				<label class="sr-only" for="english">English</label>
				<input type="text" class="form-control" name="english" id="english" placeholder="English" value="${langInstance.english}">
				<c:if test="${langInstance.errors['english'] != null}">
					<div class="text-danger text-center">
						<c:forEach var="error" items="${langInstance.errors['english'] }">
							<small>${error}</small><br />
						</c:forEach>
					</div>
				</c:if>
			</div>
			<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">
				<label class="sr-only" for="spanish">Spanish</label>
				<input type="text" class="form-control" name="spanish" id="spanish" placeholder="Spanish" value="${langInstance.spanish}">
				<c:if test="${langInstance.errors['spanish'] != null}">
					<div class="text-danger text-center">
						<c:forEach var="error" items="${langInstance.errors['spanish'] }">
							<small>${error}</small><br />
						</c:forEach>
					</div>
				</c:if>
			</div>
			<div class="col-xs-12 col-sm-2 col-md-2 col-lg-2">
				<label class="sr-only" for="french">French</label>
				<input type="text" class="form-control" name="french" id="french" placeholder="French" value="${langInstance.french}">
				<c:if test="${langInstance.errors['french'] != null}">
					<div class="text-danger text-center">
						<c:forEach var="error" items="${langInstance.errors['french'] }">
							<small>${error}</small><br />
						</c:forEach>
					</div>
				</c:if>
			</div>
			<div class="col-xs-12 col-sm-2 col-md-2 col-lg-2">
				<div class="row">
					<div class="col-xs-6">
						<input type="hidden" name="id" value="${langInstance.id}" />
						<button type="submit" name="submit" value="lang_add" class="btn btn-default"><span class="glyphicon glyphicon-floppy-disk"></span> Save</button>
					</div>
					<div class="col-xs-6">
						<a href="${page.urlWithPattern}" class="btn btn-default"><span class="glyphicon glyphicon-remove-circle"></span> Cancel</a>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-6">
						<c:if test="${langInstance.errors['form'] != null}">
							<div class="text-danger text-center">
								<c:forEach var="error" items="${langInstance.errors['form'] }">
									<small>${error}</small><br />
								</c:forEach>
							</div>
						</c:if>
					</div>
					<div class="col-xs-6">
						<c:if test="${langFormSuccess != null }">
							<div class="text-success">
								<small>${langFormSuccess}</small>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</form>
</c:set>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="title" value="Langes" />

<c:set scope="request" var="langInstance" value="${langAddInstance}" />
<c:set scope="request" var="langFormSuccess" value="${langAddFormSuccess}" />
<jsp:include page="form.jsp"></jsp:include>

<c:set scope="request" var="content">
	<div>
		<h3 class="text-center">Langes</h3>
		<hr />
		<div>${lang_form}</div>
		<hr />
	</div>
	<div>
		<h4 class="text-center" style="padding: 20px 0px">Current phrases</h4>
		<c:forEach var="langRow" items="${langList}">
			<c:if test="${langRow.id == langEntity.id}">
				<div style="height: 25px;"></div>
				
				<c:set scope="request" var="langInstance" value="${langRow}"/>
				<c:set scope="request" var="langFormSuccess" value="${langEditFormSuccess}" />
				<c:if test="${langEditInstance != null}">
					<c:set scope="request" var="langInstance" value="${langEditInstance}"/>
				</c:if> 
				
				<jsp:include page="form.jsp"></jsp:include>
				
				<div>${lang_form}</div>
				<div style="height: 25px;"></div>
			</c:if>
			<c:if test="${langRow.id != langEntity.id}">
				<div class="row" style="padding: 5px 0px">
					<div class="col-xs-12 col-sm-2 col-md-2 col-lg-2 col-lg-offset-1">${langRow.identifier}</div>
					<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">${langRow.english}</div>
					<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">${langRow.spanish}</div>
					<div class="col-xs-12 col-sm-2 col-md-2 col-lg-2">${langRow.french}</div>
					<div class="col-xs-12 col-sm-2 col-md-2 col-lg-2">
						<div class="row">
							<div class="col-xs-6">
								<a href="${page.urlWithPattern}/edit/${langRow.id}?page=${pageNumber}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-edit"></span> Edit</a>
							</div>
							<div class="col-xs-6">
								<a href="${page.urlWithPattern}/delete/${langRow.id}?page=${pageNumber}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-remove"></span> Delete</a>
							</div>
						</div>
					</div>
				</div>
			</c:if>
		</c:forEach>
	</div>
	<div>${delete_modal}</div>
</c:set>
<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>
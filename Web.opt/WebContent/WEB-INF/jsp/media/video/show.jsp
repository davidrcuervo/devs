<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="form.jsp"></jsp:include>

<c:set scope="request" var="content">

	<c:if test="${successMessage != null}">
		<div class="alert alert-success alert-dismissable fade in">
			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
			<div class="text-center"><strong>Success!</strong> ${successMessage}</div>
		</div>
	</c:if>

	<div class="row">
		
		<div class="col-xs-12 col-sm-4 col-sm-push-8">
			<h3 class="text-center">Upload a video</h3>
			${video_form}
		</div>
		<div class="col-xs-12 col-sm-8 col-sm-pull-4">
			<h2 class="text-center">Videos</h2>
			<div class="row row-eq-height">
				<c:forEach var="v" items="${multimedia.videosList}">
					<div class="col-xs-12 col-sm-6 col-md-4">
						<div class="thubnail">
							<video no-controls preload="metadata" style="width: 100%; height: 200px;">
								<source src="${page.urlWithPattern}/src/${v.url}.mp4" type="video/mp4" />
								<source src="${page.urlWithPattern}/src/${v.url}.webm" type="video/webm" />	
							</video>
							<div class="caption">
								<p class="text-center" style="white-space: nowrap; overflow: hidden;"><strong>${v.name}</strong></p>
								<div><a class="btn btn-default btn-block" href="${page.urlWithPattern}/video/${v.url}">Select</a></div>
								<p>${v.description}</p>
								
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
		
	</div>
</c:set>

<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>
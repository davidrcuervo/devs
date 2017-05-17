<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${page.addStyle("<link rel='stylesheet' href='/assets/multimedia.css' />") }

<jsp:include page="form.jsp"></jsp:include>

<c:set scope="request" var="content">
	<div class="row" style="margin-left: 0; margin-right: 0">
		
		<div class="col-xs-12 col-sm-4 col-sm-push-8">
			<h3 class="text-center">Upload a video</h3>
			${video_form}
		</div>
		<div class="col-xs-12 col-sm-8 col-sm-pull-4">
			<h2 class="text-center">Videos</h2>
			
			<div class="row" style="margin-left: 0; margin-right: 0">
				<c:forEach var="v" items="${multimedia.videosList}">
				
					<div class="col-xs-12 col-sm-6 col-md-6">
						<div class="thumbnail thumbnail_clickable" onclick="loadVideo('${page.urlWithPattern}/video/${v.url}')">
							<video no-controls preload="metadata" style="width: 100%; <%-- height: 200px; --%> vertical-align: bottom;">
								<source src="${page.urlWithPattern}/src/${v.url}.mp4" type="video/mp4" />
								<source src="${page.urlWithPattern}/src/${v.url}.webm" type="video/webm" />	
							</video>
							<div class="caption">
								<%--<div><a class="btn btn-default btn-block" href="${page.urlWithPattern}/video/${v.url}">Select</a></div>  --%>
								<p class="text-center" style="white-space: nowrap; overflow: hidden;"><strong>${v.name}</strong></p>
								<p style="height: 50px; overflow: hidden;">${v.description}</p>
								
							</div>
						</div>
					</div>
				
				</c:forEach>
			</div>
		</div>
		
	</div>
</c:set>

<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="content">
	<h1 class="text-center">${video.name}</h1>
	<p class="text-center">${video.description}</p>
	
	<video controls style="max-width: 100%; height: auto; margin: 0 auto; display: block;">
		<source src="${page.urlWithPattern}/src/${video.url}.mp4" type="video/mp4" />
		<source src="${page.urlWithPattern}/src/${video.url}.webm" type="video/webm" />
		<p>Your browser cannot play this video.</p>
	</video>
	
	<h4><small>Use the following code to embed your video:</small></h4>
	
	<div class="row">
		<div class="col-xs-12">
			<pre style="width: 100%;">
&lt;video controls style="max-width: 100%; height: auto;"&gt;
	&lt;source src="${page.urlWithPattern}/src/${video.url}.mp4" type="video/mp4" /&gt;
	&lt;source src="${page.urlWithPattern}/src/${video.url}.webm" type="video/webm" /&gt;
	&lt;p&gt;Your browser cannot play this video.&lt;/p&gt;
&lt;/video&gt;
			</pre>
		</div>
	</div>
</c:set>

<jsp:include page="/WEB-INF/jsp/templates/web.jsp"></jsp:include>
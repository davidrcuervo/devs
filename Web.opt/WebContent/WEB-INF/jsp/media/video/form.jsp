<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${page.addScript("<script src='/assets/multimedia.js'></script>") }

<c:set var="video_form" scope="request">
	<form method="post" enctype="multipart/form-data" onsubmit="return getUploadProgress('${page.urlWithPattern}/ajax');">
		<div class="from-group">
			<label for="videoNameForm">Video name:</label>
			<input type="text" class="form-control" id="videoNameForm" name="name" placeholder="Title of Video" />
		</div>
		<div class="form-group">
			<label for="videDescriptionForm">Description:</label>
			<input type="text" class="form-control" id="videoDescriptionForm" name="description" placeholder="Description of video" />
		</div>
		<div class="form-group">
			<label for="videoFileForm" class="control-label sr-only">Video:</label>
			<input type="file" id="videoFileForm" name="video" />
			<c:if test="${video.errors['file'] != null}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${video.errors['file'] }">
					<small>${error}</small><br />
				</c:forEach>
			</div>
		</c:if>
		</div>
		
		<button id="video_form_btn_submit" type="submit" value="video" name="submit" class="btn btn-primary btn-block">Submit</button>
		<c:if test="${video.errors['video'] != null}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${video.errors['video'] }">
					<small>${error}</small><br />
				</c:forEach>
			</div>
		</c:if>
	</form>
	<div style="visibility: hidden; margin-top: 20px;" id="container_progres_bar">
		<div style="margin-top: 10px; margin-bottom: 2px;">Upload progress: </div>
		<div class="progress">
			<div id="upload_progress_bar" class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuein="0" aria-valuemax="100" style="width: 0%;">0%</div>
		</div>
		<div style="margin-top: 10px; margin-bottom: 2px;">Encoding progress</div>
		<div class="progress">
			<div id="encoding_progress_bar" class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuein="0" aria-valuemax="100" style="width: 0%;">0%</div>
		</div>
	</div>
	<c:if test="${sessionScope.json != null}">
		<script>
			var upload = {
					"progress" : true,
					"reload" : "${sessionScope.json.reload != null ? true : false}",
					"serverAddress": "${page.urlWithPattern}/ajax",
			};
		</script>
	</c:if>
	
</c:set>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="lang_form">

	<form method="post" class="form-inline">
		<div class="form-group">
			<label class="sr-only" for="identifier">Identifier</label>
			<input type="text" class="form-control" id="identifier" placeholder="Identifier">
		</div>
		<div class="form-group">
			<label class="sr-only" for="english">English</label>
			<input type="text" class="form-control" id="english" placeholder="English">
		</div>
		<div class="form-group">
			<label class="sr-only" for="spanish">Spanish</label>
			<input type="text" class="form-control" id="spanish" placeholder="Spanish">
		</div>
		<div class="form-group">
			<label class="sr-only" for="french">French</label>
			<input type="text" class="form-control" id="french" placeholder="French">
		</div>
		<button type="submit" value="lang_form" class="btn btn-default"><span class="glyphicon glyphicon-floppy-disk"></span></button>
	</form>
</c:set>
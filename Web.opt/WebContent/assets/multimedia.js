function getUploadProgress(address){

	var randomId = Math.floor(Math.random() * 1001);
		
	$('#progressId').val(randomId);
	$('#container_progres_bar').css("visibility", "visible");
	
	function requestProgress(urlAddress){
		var result;
		
		$.ajax({
			url: urlAddress,
			async: false,
			type: 'GET',
			data: {
				format: 'json'
			},
			success: function(data){
				console.log(data);
				result = data;
			},
			error: function(){
				
			},
		});
		
		return result;
	}
	
	var progressFunction = setInterval(function(){
		var data;
		
		data = requestProgress(address + "uploadFileProgress");
		$('#upload_progress_bar').attr('aria-valuenow', data.porcentage).css('width', data.porcentage + "%").text(data.porcentage + "%");
		
		data = requestProgress(address + randomId);
		$('#encoding_progress_bar').attr('aria-valuenow', data.porcentage).css('width', data.porcentage + "%").text(data.porcentage + "%");
		
	} ,3000);
	
	return true;
}
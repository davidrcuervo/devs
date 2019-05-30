$(window).load(function(){
	if(typeof upload !== 'undefined' && upload.progress == true){
		getUploadProgress(upload.serverAddress);
	}
});

function getUploadProgress(serverAddress){

	$('#container_progres_bar').css("visibility", "visible");
	$('#video_form_btn_submit').addClass("disabled");
	
	var progressFunction = setInterval(function(){
		var data = requestProgress(serverAddress);
		
		if(data.error != null){
			
			$('#container_progres_bar').html('<div class="alert alert-danger alert-dismissable fade in">'
					+ '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' 
					+ '<div class="text-center"><strong>Error!</strong> ' + data.error + '</div>'
					+ '</div>');
			
			closeConnection(serverAddress);
			
		}else if(data.success != null){
			
			$('#container_progres_bar').html('<div class="alert alert-success alert-dismissable fade in">'
					+ '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' 
					+ '<div class="text-center"><strong>Success!</strong> ' + data.success + '</div>'
					+ '</div>');
			
			if(typeof upload !== 'undefined' && upload.reload == "true"){
				closeConnection(serverAddress);
			}else{
				$.ajax({url: serverAddress + "/reload", async: false, type: 'GET'});
				location.reload();
			}
			
			
		}else{
			if(data.upload != null)
				$('#upload_progress_bar').attr('aria-valuenow', data.upload).css('width', data.upload + "%").text(data.upload + "%");
			
			if(data.upload != null)
				$('#encoding_progress_bar').attr('aria-valuenow', data.encode).css('width', data.encode + "%").text(data.encode + "%");
		}
	} ,3000);
	
	function requestProgress(urlAddress){
		var result;
		
		$.ajax({
			url: urlAddress  + "/get",
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
	
	function closeConnection(serverAddress){
		clearInterval(progressFunction);
		$.ajax({url: serverAddress + "/close", async: false, type: 'GET'});
		$('#video_form_btn_submit').removeClass("disabled");
	}
	
	return true;
}

function loadVideo(url){
	window.location = url;
}
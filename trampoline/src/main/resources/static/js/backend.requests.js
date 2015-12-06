function setWorkingDirectory(){
	$.ajax({
	    url : "/setworkingdirectory",
	    type: "POST",
	    data : {path: $("#input-workingdirectory").val()},
	    success: function(data, textStatus, jqXHR) { location.reload(); }
	});
}
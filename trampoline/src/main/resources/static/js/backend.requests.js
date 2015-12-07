function setWorkingDirectory(){
	$.ajax({
	    url : "/setworkingdirectory",
	    type: "POST",
	    data : {path: $("#input-workingdirectory").val()},
	    success: function(data, textStatus, jqXHR) { location.reload(); }
	});
}

function setNewMicroservice(){
	$.ajax({
	    url : "/setnewmicroservice",
	    type: "POST",
	    data : {name: $("#input-newmicroservice-name").val(), pomLocation: $("#input-newmicroservice-pomlocation").val(), defaultPort: $("#input-newmicroservice-defaultport").val()},
	    success: function(data, textStatus, jqXHR) { location.reload(); }
	});
}
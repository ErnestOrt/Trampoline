function updateStartInstanceForm(){
    if($("#input-start-microservice").val() != -1){
        $.ajax({
        	    url : "/instances/instanceinfo",
        	    type: "POST",
        	    data : {id: $("#input-start-microservice").val()},
        	    success: function(data, textStatus, jqXHR) {
        	        $("#input-start-port").val(data.defaultPort);
        	        $("#input-start-prefix").val(data.actuatorPrefix);
        	        $("#input-start-pom").val(data.pomLocation);
        	        $("#input-start-arguments").val(data.vmArguments);
        	    }
        	});
    }else{
        $("#input-start-port").val("");
        $("#input-start-prefix").val("");
        $("#input-start-pom").val("");
        $("#input-start-arguments").val("");
    }
}

function startInstance(){
    if($("#input-start-microservice").val() == "-1" || $("#input-start-port").val() == '' || $("#input-start-prefix").val() == '' || $("#input-start-pom").val() == '' || $("#input-start-arguments").val() == ''){
    			checkEachNewMicroserviceFromField();
    }else{
        $.ajax({
            url : "/instances/startinstance",
            type: "POST",
            data : {id: $("#input-start-microservice").val(), port: $("#input-start-port").val(), vmArguments: $("#input-start-arguments").val()},
            success: function(data, textStatus, jqXHR) { location.reload(); }
        });
	}
}

function cleaningNewMicroserviceFrom(){
	$("#form-start-microservice").removeClass("has-error");
	$("#form-start-port").removeClass("has-error");
	$("#form-start-arguments").removeClass("has-error");

	$("#form-start-microservice").removeClass("has-success");
    $("#form-start-port").removeClass("has-success");
    $("#form-start-arguments").removeClass("has-success");
}

function checkEachNewMicroserviceFromField(){
    if($("#input-start-microservice").val() == '-1'){
		$("#form-start-microservice").addClass("has-error");
	}else{
	    $("#form-start-microservice").addClass("has-success");
	}

	if($("#input-start-port").val() == ''){
        $("#form-start-port").addClass("has-error");
    }else{
        $("#form-start-port").addClass("has-success");
    }

    if($("#input-start-arguments").val() == ''){
        $("#form-start-arguments").addClass("has-error");
    }else{
        $("#form-start-arguments").addClass("has-success");
    }
}


function updateStatusInstances(){
	$("span[id^='label-status-']").each(function(i, item) {
		$.ajax({
		    url : "/instances/health",
		    type: "POST",
		    data : {id: item.id.replace("label-status-", "")},
		    success: function(data, textStatus, jqXHR) {
		    	$("#"+item.id).removeClass("label-warning");
		    	if(data == "deployed"){
		    		 $("#"+item.id).removeClass("label-success");
                     $("#"+item.id).removeClass("label-danger");

		    		 $("#"+item.id).addClass("label-success");
		    		 $("#"+item.id).text("Deployed");
		    	}else{
		    	    $("#"+item.id).removeClass("label-success");
                	$("#"+item.id).removeClass("label-danger");

		    		$("#"+item.id).addClass("label-danger");
		    		$("#"+item.id).text("Not Deployed")
		    	}
		    }
		});
  });
}

setInterval(updateStatusInstances, 15000);

$( document ).ready(function() {
	updateStatusInstances();
});
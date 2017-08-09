function setMavenBinaryLocation(){
	if($("#input-mavenbinarylocation").val() == ''){
		$("#form-group-mavenbinarylocation").addClass("has-error");
	}else{
		$.ajax({
		    url : "/setmavenbinarylocation",
		    type: "POST",
		    data : {path: $("#input-mavenbinarylocation").val()},
		    success: function(data, textStatus, jqXHR) { location.reload(); }
		});
	}
}

function setMavenHomeLocation(){
	if($("#input-mavenhomelocation").val() == ''){
		$("#form-group-mavenhomelocation").addClass("has-error");
	}else{
		$.ajax({
		    url : "/setmavenhomelocation",
		    type: "POST",
		    data : {path: $("#input-mavenhomelocation").val()},
		    success: function(data, textStatus, jqXHR) { location.reload(); }
		});
	}
}

function setNewMicroservice(){
	if($("#input-hidden-mavenbinarylocation").val() == ''){
		$("#form-group-mavenbinarylocation").addClass("has-error");
	}else{
		cleaningNewMicroserviceFrom();
		if($("#input-newmicroservice-name").val() == '' || $("#input-newmicroservice-pomlocation").val() == '' || $("#input-newmicroservice-defaultport").val() == ''){
			checkEachNewMicroserviceFromField();
		}else{
			$.ajax({
			    url : "/setnewmicroservice",
			    type: "POST",
			    data : {name: $("#input-newmicroservice-name").val(), pomLocation: $("#input-newmicroservice-pomlocation").val(), defaultPort: $("#input-newmicroservice-defaultport").val(),
			    	    actuatorPrefix: $("#input-newmicroservice-actuatorprefix").val(),
			    	    vmArguments: $("#input-newmicroservice-vmarguments").val()},
			    success: function(data, textStatus, jqXHR) { location.reload(); }
			});
		}
	}
}

function cleaningNewMicroserviceFrom(){
	$("#form-newmicroservice-name").removeClass("has-error");
	$("#form-newmicroservice-pomlocation").removeClass("has-error");
	$("#form-newmicroservice-defaultport").removeClass("has-error");
}

function checkEachNewMicroserviceFromField(){
	if($("#input-newmicroservice-name").val() == ''){
		$("#form-newmicroservice-name").addClass("has-error");
	}
	if($("#input-newmicroservice-pomlocation").val() == ''){
		$("#form-newmicroservice-pomlocation").addClass("has-error");
	}
	if($("#input-newmicroservice-defaultport").val() == ''){
		$("#form-newmicroservice-defaultport").addClass("has-error");
	}
}

function removeMicroservice(microserviceId){
	$.ajax({
	    url : "/removemicroservice",
	    type: "POST",
	    data : {id: microserviceId},
	    success: function(data, textStatus, jqXHR) { location.reload(); }
	});
}

function startInstance(microserviceId){
	$.ajax({
	    url : "/startinstance",
	    type: "POST",
	    data : {id: microserviceId, port: $("#input-port-" + microserviceId).val(), vmArguments: $("#input-vmarguments-" + microserviceId).val()},
	    success: function(data, textStatus, jqXHR) { location.reload(); }
	});
}

function killInstance(instanceId){
	$.ajax({
	    url : "/killinstance",
	    type: "POST",
	    data : {id: instanceId},
	    success: function(data, textStatus, jqXHR) { location.reload(); }
	});
}

function removeNotDeployedInstances(){
	$.ajax({
	    url : "/removenotdeployedinstances",
	    type: "POST",
	    success: function(data, textStatus, jqXHR) { location.reload(); }
	});
}

function updateStatusInstances(){
	$("span[id^='label-status-']").each(function(i, item) {
		$.ajax({
		    url : "/health",
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
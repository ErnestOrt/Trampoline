function setMavenLocation(){
	$.ajax({
	    url : "/setmavenlocation",
	    type: "POST",
	    data : {path: $("#input-mavenlocation").val()},
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
	    data : {id: microserviceId, port: $("#input-port-" + microserviceId).val()},
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
		$("#"+item.id).removeClass("label-success");
		$("#"+item.id).removeClass("label-danger");
		$.ajax({
		    url : "/health",
		    type: "POST",
		    data : {id: item.id.replace("label-status-", "")},
		    success: function(data, textStatus, jqXHR) { 
		    	if(data == "deployed"){
		    		 $("#"+item.id).removeClass("label-warning");
		    		 $("#"+item.id).addClass("label-success");
		    		 $("#"+item.id).text("Deployed");
		    	}else{
		    		$("#"+item.id).removeClass("label-warning");
		    		$("#"+item.id).addClass("label-danger");
		    		$("#"+item.id).text("Not Deployed")
		    	}
		    }
		});
  });
}

setInterval(updateStatusInstances, 5000);

$( document ).ready(function() {
	updateStatusInstances();
});
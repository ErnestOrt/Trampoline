function setMavenLocation(){
	if($("#input-mavenlocation").val() == ''){
		$("#form-group-mavenlocation").addClass("has-error");
	}else{
		$.ajax({
		    url : "/setmavenlocation",
		    type: "POST",
		    data : {path: $("#input-mavenlocation").val()},
		    success: function(data, textStatus, jqXHR) { location.reload(); }
		});
	}
}

function setNewMicroservice(){
	if($("#input-hidden-mavenlocation").val() == ''){
		$("#form-group-mavenlocation").addClass("has-error");
	}else{
		cleaningNewMicroserviceFrom();
		if($("#input-newmicroservice-name").val() == '' || $("#input-newmicroservice-pomlocation").val() == '' || $("#input-newmicroservice-defaultport").val() == ''){
			checkEachNewMicroserviceFromField();
		}else{
			$.ajax({
			    url : "/setnewmicroservice",
			    type: "POST",
			    data : {name: $("#input-newmicroservice-name").val(), pomLocation: $("#input-newmicroservice-pomlocation").val(), defaultPort: $("#input-newmicroservice-defaultport").val()},
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

function showMetrics(instanceId, name, port){
    $("#metrics-title").html(name+" : "+port);
    $("#modal-metrics").modal("show");
    $.ajax({
    	    url : "/metrics",
    	    type: "POST",
    	    data : {id: instanceId},
    	    success: function(data, textStatus, jqXHR) {
    	        dates = [];
                 dataMemoryFree = [];
                 usedHeapKB=[];
              $.each(data, function (index, value) {

                  dates.push(value.date);
                  dataMemoryFree.push(value.freeMemoryKB);
                  usedHeapKB.push(value.usedHeapKB)
                });
                var ctx = document.getElementById('myChart').getContext('2d');
                new Chart(ctx, {
                // The type of chart we want to create
                type: 'line',

                // The data for our dataset
                data: {
                    labels: dates,
                    datasets: [{
                        label: "Memory Free KB",
                        backgroundColor: 'rgba(255, 99, 132, 0.2)',
                        borderColor: 'rgba(255, 99, 132, 1)',
                        data: dataMemoryFree,
                    },
                    {
                        label: "Heap Used KB",
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        data: usedHeapKB,
                    }]
                },

                // Configuration options go here
                options: {}
            });
    	    }
    	});
}

function showTraces(instanceId, name, port){
    $("#traces-title").html(name+" : "+port);
    $("#timeline-content").html("");
    $("#modal-traces").modal("show");
    $.ajax({
    	    url : "/traces",
    	    type: "POST",
    	    data : {id: instanceId},
    	    success: function(data, textStatus, jqXHR) {
    	        $("#timeline-content").html('<div class="line text-muted"></div>')
              $.each(data, function (index, value) {

                    $("#timeline-content").append('<article class="panel '+(value.status == '200' ? 'panel-success' : 'panel-danger')+' panel-outline">'+
                                                    '<div class="panel-heading icon">'+
                                                    '</div>'+
                                                    '<div class="panel-body">'+
                                                        '<strong>'+value.date+'</strong> ' + value.path + ' ' + value.method+ ' ' + value.status+
                                                    '</div>'+
                                                '</article>');
                });
    	    }
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
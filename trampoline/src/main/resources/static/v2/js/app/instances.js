var metricsCharts;

function startGroup(){
    if($("#input-start-group").val() == "-1"){
        $("#form-start-group").addClass("has-error");
    }else{
        $('.front-loading').show();
        $.ajax({
            url : "/instances/startgroup",
            type: "POST",
            data : {id: $("#input-start-group").val()},
            success: function(data, textStatus, jqXHR) {
              location.reload();
            },
            error: function (request, status, error) {
                $('.front-loading').hide();
                showNotification('danger', "Error occurred when trying to start a group. Check Logs for more info");
             }
        });
    }
}

function addExternalInstance(){
    if($("#input-external-instance").val() == "-1"){
        $("#form-external-instance").addClass("has-error");
    }else{
        $('.front-loading').show();
        $.ajax({
            url : "/instances/addexternalinstance",
            type: "POST",
            data : {id: $("#input-external-instance").val()},
            success: function(data, textStatus, jqXHR) {
              location.reload();
            },
            error: function (request, status, error) {
                $('.front-loading').hide();
                showNotification('danger', "Error occurred when trying to add an esternal instance. Check Logs for more info");
             }
        });
    }
}

function restartInstance(instanceId){
    $('.front-loading').show();
    $.ajax({
        url : "/instances/restartinstance",
        type: "POST",
        data : {id: instanceId},
        success: function(data, textStatus, jqXHR) { location.reload(); },
        error: function (request, status, error) {
            $('.front-loading').hide();
            showNotification('danger', "Error occurred when trying to restart instance. Check Logs for more info");
        }
    });
}

function killInstance(instanceId){
    $('.front-loading').show();
	$.ajax({
	    url : "/instances/killinstance",
	    type: "POST",
	    data : {id: instanceId},
	    success: function(data, textStatus, jqXHR) { location.reload(); },
	    error: function (request, status, error) {
            $('.front-loading').hide();
            showNotification('danger', "Error occurred when trying to kill instance. Check Logs for more info");
         }
	});
}

function updateStartInstanceForm(){
    if($("#input-start-microservice").val() != -1){
        $('.front-loading').show();
        $.ajax({
        	    url : "/instances/instanceinfo",
        	    type: "POST",
        	    data : {id: $("#input-start-microservice").val()},
        	    success: function(data, textStatus, jqXHR) {
        	        $("#input-start-port").val(data.defaultPort);
        	        $("#input-start-prefix").val(data.actuatorPrefix);
        	        $("#input-start-pom").val(data.pomLocation);
        	        $("#input-start-arguments").val(data.vmArguments);
        	        $("#input-start-buildtool").val(data.buildTool)
        	        $('.front-loading').hide();
        	    },
        	    error: function (request, status, error) {
                    $('.front-loading').hide();
                     alert(request.responseText);
                 }
        	});
    }else{
        $("#input-start-port").val("");
        $("#input-start-prefix").val("");
        $("#input-start-pom").val("");
        $("#input-start-arguments").val("");
    }
}

function validateNewInstance(){
    cleaningNewMicroserviceFrom();
    if($("#input-start-microservice").val() == "-1" || $("#input-start-port").val() == '' || $("#input-start-pom").val() == ''){
    			checkEachNewMicroserviceFromField();
    }else{
        $.ajax({
            url : "/instances/checkport",
            type: "POST",
            data : {port: $("#input-start-port").val()},
            success: function(data, textStatus, jqXHR) {
                if(data == true){
                    startInstance();
                }else{
                    $("#form-start-port").removeClass("has-error");
                    $("#form-start-port").removeClass("has-success");
                    $("#form-start-port").addClass("has-error");
                    showNotification('danger', "PORT <b>"+$("#input-start-port").val()+"</b> is already used by other instances or other process. Remove old instance before creating a new instance on this port.");
                }
            },
            error: function (request, status, error) {
                 $('.front-loading').hide();
                 showNotification('danger', "Error occurred when trying to validate instance. Check Logs for more info");
              }
        });
	}
}

function startInstance(){
    $('.front-loading').show();
    $.ajax({
        url : "/instances/startinstance",
        type: "POST",
        data : {id: $("#input-start-microservice").val(), port: $("#input-start-port").val(), vmArguments: $("#input-start-arguments").val()},
        success: function(data, textStatus, jqXHR) { location.reload(); },
        error: function (request, status, error) {
             $('.front-loading').hide();
              showNotification('danger', "Error occurred when trying to start instance. Check Logs for more info");
          }
    });
}

function cleaningNewMicroserviceFrom(){
	$("#form-start-microservice").removeClass("has-error");
	$("#form-start-port").removeClass("has-error");

	$("#form-start-microservice").removeClass("has-success");
    $("#form-start-port").removeClass("has-success");
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

		    	     if( $("#"+item.id).text() != "Deployed"){
		    	        if( $("#"+item.id).text() == "Not Deployed"){
		    	            showNotification('success', "Microservice has been <b>successfully</b> deployed");
                         }
                         $("#"+item.id).removeClass("label-success");
                         $("#"+item.id).removeClass("label-danger");

                         $("#"+item.id).addClass("label-success");
                         $("#"+item.id).text("Deployed");
                     }


		    	}else{

		    	    if( $("#"+item.id).text() != "Not Deployed"){
		    	         if( $("#"+item.id).text() == "Deployed"){
		    	             showNotification('danger', "Microservice has been <b>stopped</b>");
                         }
                         $("#"+item.id).removeClass("label-success");
                         $("#"+item.id).removeClass("label-danger");

                         $("#"+item.id).addClass("label-danger");
                         $("#"+item.id).text("Not Deployed")
                     }


		    	}
		    }
		});
  });
}

setInterval(updateStatusInstances, 15000);


function showMetrics(instanceId, name, port){
    $("#metrics-title").html(name+" : "+port);
    $("#modal-metrics").modal("show");
    $.ajax({
    	    url : "/instances/metrics",
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
                metricsCharts.config.data = {
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
                                          }

              metricsCharts.update();
    	    },
            error: function (request, status, error) {
               $('.front-loading').hide();
                showNotification('danger', "Error occurred when trying to retrieve metrics. Check Logs for more info");
            }
    	});
}

function showTraces(instanceId, name, port){
    $("#traces-title").html(name+" : "+port);
    $("#timeline-content").html("");
    $("#modal-traces").modal("show");
    $.ajax({
    	    url : "/instances/traces",
    	    type: "POST",
    	    data : {id: instanceId},
    	    success: function(data, textStatus, jqXHR) {
    	        $("#timeline-content").html('<div class="line text-muted"></div>')
              $.each(data, function (index, value) {

                    $("#timeline-content").append('<article class="panel '+(value.status == '200' ? 'panel-success' : 'panel-danger')+' panel-outline">'+
                                                    '<div class="panel-heading icon">'+
                                                    '</div>'+
                                                    '<div class="panel-body">'+
                                                        '<strong>'+value.date+'</strong> ' + value.status + ' ' + value.method+ ' ' + value.path+
                                                    '</div>'+
                                                '</article>');
                });
    	    },
            error: function (request, status, error) {
               $('.front-loading').hide();
                showNotification('danger', "Error occurred when trying to retrieve traces. Check Logs for more info");
            }
    	});
}

function showInfo(instanceId, name, port){
    $('.front-loading').show();
    $("#info-title").html(name+" : "+port);
    $.ajax({
    	    url : "/instances/info",
    	    type: "POST",
    	    data : {id: instanceId},
    	    success: function(data, textStatus, jqXHR) {
                $("#modal-info-pomLocation").text(data.pomLocation);
                $("#modal-info-git-branch").text(data.branch);
                $("#modal-info-git-commit-message").text(data.commitMessage);
                $("#modal-info-git-commit-owner").text(data.commitOwner);
                $("#modal-info-git-commit-date").text(data.commitDate);

                $('.front-loading').hide();
                $("#modal-instance-info").modal("show");
    	    },
    	    error: function (request, status, error) {
              $('.front-loading').hide();
               showNotification('danger', "Error occurred when trying to retrieve git info. Check Logs for more info");
           }
    	});
}

$( document ).ready(function() {
	updateStatusInstances();
	var ctx = document.getElementById('metrics-chart').getContext('2d');
        metricsCharts = new Chart(ctx, {
            type: 'line',
            data: {
                labels: [],
                datasets: [{
                    label: "Memory Free KB",
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    data: [],
                },
                {
                    label: "Heap Used KB",
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    data: [],
                }]
            },
            options: {}
        });
});

function showNotification(notificationType, notificationMessage){
    $.notify({
       icon: "ti-more-alt",
       message: notificationMessage

    },{
        type: notificationType,
        timer: 3000,
        placement: {
            from: 'top',
            align: 'right'
        }
    });
}
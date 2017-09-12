function changeSelectedClass(element) {
    if($(element).hasClass("btn-default")){
        $(element).removeClass("btn-default");
        $(element).addClass("btn-success");
    }else{
        $(element).addClass("btn-default");
        $(element).removeClass("btn-success");
    }
}

function createGroup(){
    idsMicroservicesGroup = [];
    $( ".microservice-group-form.btn-success" ).each(function( index ) {
        idsMicroservicesGroup.push($( this ).data("id"));
    });
    if($("#input-groupname").val() == ''){
        $("#form-groupname").addClass("has-error");
    }else if(idsMicroservicesGroup.length<2){
        $("#form-groupname").removeClass("has-error");
        $.notify({
            icon: "ti-more-alt",
            message: "You must select <b>at least two microservices</b> to register a group. <b>Remember to register microservices first</b>."

         },{
             type: 'danger',
             timer: 3000,
             placement: {
                 from: 'top',
                 align: 'right'
             }
         });
    }else{
        $("#modal-microservice-information").modal("show");
         $.ajax({
                url : "/settings/setmicroservicesgroup",
                type: "POST",
                data : {name:  $("#input-groupname").val(),
                        idsMicroservicesGroup: idsMicroservicesGroup},
                success: function(data, textStatus, jqXHR) {location.reload();},
                error: function (request, status, error) {
                     $('.front-loading').hide();
                       alert(request.responseText);
                   }
            });
    }
}

function setMavenInformation(){
	if($("#input-mavenhomelocation").val() == ''){
		$("#form-mavenhomelocation").addClass("has-error");
	}else{
	    $('.front-loading').show();
		$.ajax({
		    url : "/settings/setmaven",
		    type: "POST",
		    data : {mavenHomeLocationPath: $("#input-mavenhomelocation").val(),
		            mavenBinaryLocationPath: $("#input-mavenbinarylocation").val()},
		    success: function(data, textStatus, jqXHR) { location.reload(); },
             error: function (request, status, error) {
                  $('.front-loading').hide();
                   alert(request.responseText);
               }
		});
	}
}

function setNewMicroservice(){
	if($("#input-hidden-mavenhomelocation").val() == '' && $("#input-newmicroservice-build-tool").val() == 'maven'){
		$("#form-mavenhomelocation").addClass("has-error");
	}else{
		cleaningNewMicroserviceFrom();
		if($("#input-newmicroservice-name").val() == '' || $("#input-newmicroservice-pomlocation").val() == '' || $("#input-newmicroservice-defaultport").val() == ''){
			checkEachNewMicroserviceFromField();
		}else{
		    $('.front-loading').show();
			$.ajax({
			    url : "/settings/setnewmicroservice",
			    type: "POST",
			    data : {name: $("#input-newmicroservice-name").val(), pomLocation: $("#input-newmicroservice-pomlocation").val(), defaultPort: $("#input-newmicroservice-defaultport").val(),
			    	    actuatorPrefix: $("#input-newmicroservice-actuatorprefix").val(), vmArguments: $("#input-newmicroservice-vmarguments").val(),
			    	    buildTool: $("#input-newmicroservice-build-tool").val()},
			    success: function(data, textStatus, jqXHR) { location.reload(); },
                 error: function (request, status, error) {
                      $('.front-loading').hide();
                       alert(request.responseText);
                   }
			});
		}
	}
}

function loadConfigurations() {
    if($("#input-hidden-mavenhomelocation").val() === '' && $("#input-newmicroservice-build-tool").val() === 'maven'){
        $("#form-mavenhomelocation").addClass("has-error");
    } else {
        cleaningNewMicroserviceFrom();
        var location = $("#input-newmicroservice-pomlocation").val();
        if (location === '') {
            $("#form-newmicroservice-pomlocation").addClass("has-error");
        } else {
            $('.front-loading').show();
            $.ajax({
                url: "/settings/load?location=" + location,
                type: "POST",
                success: function (data) {
                    $('.front-loading').hide();
                    if (data.status === 'success') {
                        $("#input-newmicroservice-defaultport").val(data.body.port);
                        notify('Configuration file founded', 'success');
                    } else {
                        notify('Configuration file not founded', 'danger');
                    }
                },
                error: function () {
                    $('.front-loading').hide();
                    notify('Configuration file not founded', 'danger');
                }
            });
        }
    }
}

function cleaningNewMicroserviceFrom(){
	$("#form-newmicroservice-name").removeClass("has-error");
	$("#form-newmicroservice-pomlocation").removeClass("has-error");
	$("#form-newmicroservice-defaultport").removeClass("has-error");

	$("#form-newmicroservice-name").removeClass("has-success");
    $("#form-newmicroservice-pomlocation").removeClass("has-success");
    $("#form-newmicroservice-defaultport").removeClass("has-success");
}

function checkEachNewMicroserviceFromField(){
	if($("#input-newmicroservice-name").val() == ''){
		$("#form-newmicroservice-name").addClass("has-error");
	}else{
	    $("#form-newmicroservice-name").addClass("has-success");
	}
	if($("#input-newmicroservice-pomlocation").val() == ''){
		$("#form-newmicroservice-pomlocation").addClass("has-error");
	}else{
        $("#form-newmicroservice-pomlocation").addClass("has-success");
    }
	if($("#input-newmicroservice-defaultport").val() == ''){
		$("#form-newmicroservice-defaultport").addClass("has-error");
	}else{
        $("#form-newmicroservice-defaultport").addClass("has-success");
    }
}

function removeMicroservice(microserviceId){
    $('.front-loading').show();
	$.ajax({
	    url : "/settings/removemicroservice",
	    type: "POST",
	    data : {id: microserviceId},
	    success: function(data, textStatus, jqXHR) { location.reload(); },
        error: function (request, status, error) {
              $('.front-loading').hide();
               alert(request.responseText);
           }
	});
}

function showMicroserviceInformation(microserviceId){
   $('.front-loading').show();
	$.ajax({
	    url : "/settings/microserviceinfo",
	    type: "POST",
	    data : {id: microserviceId},
	    success: function(data, textStatus, jqXHR) {
	        $("#modal-microservice-name").text(data.name);
	        $("#modal-microservice-buildTool").text(data.buildTool);
	        $("#modal-microservice-pomLocation").text(data.pomLocation);
	        $("#modal-microservice-defaultPort").text(data.defaultPort);
	        $("#modal-microservice-actuatorPrefix").text(data.actuatorPrefix);
	        $("#modal-microservice-vmArguments").text(data.vmArguments);
	        $('.front-loading').hide();
	        $("#modal-microservice-information").modal("show");
	    },
        error: function (request, status, error) {
                 $('.front-loading').hide();
               alert(request.responseText);
           }
	});
}


function showGroupInformation(groupId){
   $('.front-loading').show();
	$.ajax({
	    url : "/settings/groupinfo",
	    type: "POST",
	    data : {id: groupId},
	    success: function(data, textStatus, jqXHR) {
	        $("#modal-group-name").text(data.name);
	        $("#modal-group-list").text(data.microservicesNames);
	        $('.front-loading').hide();
	         $("#modal-group-information").modal("show");
	    },
        error: function (request, status, error) {
                 $('.front-loading').hide();
               alert(request.responseText);
           }
	});
}


function removeGroup(groupId){
    $('.front-loading').show();
	$.ajax({
	    url : "/settings/removegroup",
	    type: "POST",
	    data : {id: groupId},
	    success: function(data, textStatus, jqXHR) { location.reload(); },
        error: function (request, status, error) {
              $('.front-loading').hide();
               alert(request.responseText);
           }
	});
}

$( document ).ready(function() {
    $(".front-loading").hide();
    $(".front-loading").height($("body").height());
});

function notify(message, type) {
    $.notify({
        icon: "ti-more-alt",
        message: message
        // message: "You must select <b>at least two microservices</b> to register a group. <b>Remember to register microservices first</b>."

    },{
        type: type,
        // type: 'danger',
        timer: 3000,
        placement: {
            from: 'top',
            align: 'right'
        }
    });
}
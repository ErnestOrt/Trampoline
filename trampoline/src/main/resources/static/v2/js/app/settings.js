var selectedMicroserviceId;

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
			    	    buildTool: $("#input-newmicroservice-build-tool").val(), gitLocation: $("#input-newmicroservice-gitLocation").val()},
			    success: function(data, textStatus, jqXHR) { location.reload(); },
                 error: function (request, status, error) {
                      $('.front-loading').hide();
                       alert(request.responseText);
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
    selectedMicroserviceId=microserviceId;
   $('.front-loading').show();
	$.ajax({
	    url : "/settings/microserviceinfo",
	    type: "POST",
	    data : {id: microserviceId},
	    success: function(data, textStatus, jqXHR) {
	        $("#modal-microservice-name").text(data.name);
	        $("#modal-microservice-buildTool").text(data.buildTool);
	        $("#input-update-pomLocation").val(data.pomLocation);
	        $("#input-update-default-port").val(data.defaultPort);
	        $("#input-update-actuator-prefix").val(data.actuatorPrefix);
	        $("#input-update-vm-arguments").val(data.vmArguments);
	        $("#input-update-gitLocation").val(data.gitLocation);
	        $('.front-loading').hide();
	        $("#modal-microservice-information").modal("show");
	    },
        error: function (request, status, error) {
                 $('.front-loading').hide();
               alert(request.responseText);
           }
	});
}

function showGitStatus(microserviceId){
    $.ajax({
    	    url : "/settings/gitmicroservice",
    	    type: "POST",
    	    data : {id: microserviceId},
    	    success: function(data, textStatus, jqXHR) {

    	    },
            error: function (request, status, error) {
                     $('.front-loading').hide();
                   alert(request.responseText);
               }
    	});
}

function updateMicroservice(){
    if($("#input-update-pomLocation").val() == '' || $("#input-update-default-port").val() == ''){

    }else{
        $('.front-loading').show();
        $.ajax({
            url : "/settings/updatemicroservice",
            type: "POST",
            data : {id: selectedMicroserviceId,
                    defaultPort: $("#input-update-default-port").val(),
                    actuatorPrefix: $("#input-update-actuator-prefix").val(),
                    vmArguments: $("#input-update-vm-arguments").val(),
                    pomLocation: $("#input-update-pomLocation").val(),
                    gitLocation: $("#input-update-gitLocation").val()},
            success: function(data, textStatus, jqXHR) { location.reload(); },
             error: function (request, status, error) {
                  $('.front-loading').hide();
                   alert(request.responseText);
               }
        });
    }
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
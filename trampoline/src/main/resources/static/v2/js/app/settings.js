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
    $( ".microservice-group-form.btn-success" ).each(function( index ) {
      console.log( index + ": " + $( this ).text() );
    });
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
    $("#modal-microservice-information").modal("show");
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
	    },
        error: function (request, status, error) {
               alert(request.responseText);
           }
	});
}

$( document ).ready(function() {
    $(".front-loading").hide();
    $(".front-loading").height($("body").height());
});
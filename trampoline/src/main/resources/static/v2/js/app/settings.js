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
	if($("#input-hidden-mavenhomelocation").val() == ''){
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

$( document ).ready(function() {
    $(".front-loading").hide();
    $(".front-loading").height($("body").height());
});
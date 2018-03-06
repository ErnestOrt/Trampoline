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
        showNotification('danger', "You must select <b>at least two microservices</b> to register a group. <b>Remember to register microservices first</b>.");
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
                      showNotification('danger', "Error occurred when trying to create a group. Check Logs for more info");
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
                  showNotification('danger', "Error occurred when trying to setting up maven location. Check Logs for more info");
               }
		});
	}
}

function setNewMicroservice(){
	if($("#input-hidden-mavenhomelocation").val() == '' && $("#input-newmicroservice-build-tool").val() == 'maven'){
		$("#form-mavenhomelocation").addClass("has-error");
	}else{
		cleaningNewMicroserviceFrom();
		if($("#input-newmicroservice-name").val() == '' || $("#input-newmicroservice-pomlocation").val() == '' || $("#input-newmicroservice-defaultport").val() == '' || $("#input-newmicroservice-build-tool").val() == '-1'){
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
                       showNotification('danger', "Error occurred when trying to register a microservice. Check Logs for more info");

                   }
			});
		}
	}
}

function cleaningNewMicroserviceFrom(){
	$("#form-newmicroservice-name").removeClass("has-error");
	$("#form-newmicroservice-pomlocation").removeClass("has-error");
	$("#form-newmicroservice-defaultport").removeClass("has-error");
	$("#form-newmicroservice-build-tool").removeClass("has-error");

	$("#form-newmicroservice-name").removeClass("has-success");
    $("#form-newmicroservice-pomlocation").removeClass("has-success");
    $("#form-newmicroservice-defaultport").removeClass("has-success");
    $("#form-newmicroservice-build-tool").removeClass("has-success");
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
    if($("#input-newmicroservice-build-tool").val() == '-1'){
        $("#form-newmicroservice-build-tool").addClass("has-error");
    }else{
        $("#form-newmicroservice-build-tool").addClass("has-success");
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
               showNotification('danger', "Error occurred when trying to remove a microservice. Check Logs for more info");
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
               showNotification('danger', "Error occurred when trying to retrieve microservice information. Check Logs for more info");
           }
	});
}

function copyMsInfo(microserviceId){
   $('.front-loading').show();
	$.ajax({
	    url : "/settings/microserviceinfo",
	    type: "POST",
	    data : {id: microserviceId},
	    success: function(data, textStatus, jqXHR) {
	        $("#input-newmicroservice-name").val(data.name);
	        $("#input-newmicroservice-build-tool").val(data.buildTool.toLowerCase());
	        $("#input-newmicroservice-pomlocation").val(data.pomLocation);
	        $("#input-newmicroservice-defaultport").val(data.defaultPort);
	        $("#input-newmicroservice-actuatorprefix").val(data.actuatorPrefix);
	        $("#input-newmicroservice-vmarguments").val(data.vmArguments);
	        $("#input-newmicroservice-gitLocation").val(data.gitLocation);
	        $('.front-loading').hide();
	    },
        error: function (request, status, error) {
                 $('.front-loading').hide();
               showNotification('danger', "Error occurred when trying to retrieve microservice information. Check Logs for more info");
           }
	});
}


function showGitStatus(microserviceId){
    $('.front-loading').show();
    $.ajax({
    	    url : "/settings/microservicegitbranches",
    	    type: "POST",
    	    data : {id: microserviceId},
    	    success: function(data, textStatus, jqXHR) {
    	     $('.front-loading').hide();
    	    $("#modal-microservice-git").modal("show");
                $("#ul-branches-git").html("");
                $("#span-current-git-branch").html(data.currentBranch);
                data.branches.forEach(function(item) {
                       $("#ul-branches-git").append(' <li >'
                       +'             <br/><div class="row">'
                       +'                 <div class="col-xs-12">'
                       +'                     <span >'+item+'</span>'
                       +'                 </div>'
                       +'                 <div class="col-xs-12 text-right">'
                       +'                     <btn class="btn btn-sm btn-success btn-icon" onclick="checkoutAndPull(\''+microserviceId+'\', \''+item+'\')">Checkout and Pull</btn>'
                       +'                     <btn class="btn btn-sm btn-danger btn-icon" onclick="checkoutAndPullAndRestart(\''+microserviceId+'\', \''+item+'\')">Checkout and Pull and Restart Instances</btn>'
                       +'                 </div>'
                       +'             </div>'
                       +'        </li>');
                    });
    	    },
            error: function (request, status, error) {
                     $('.front-loading').hide();
                     showNotification('danger', "Not possible to retrieve git information... Did you set <i>Git Repo Root Location?</i> Check Logs for more info");
               }
    	});
}

function checkoutAndPullAndRestart(microserviceId, branch){
    $("#modal-microservice-git").modal("hide");
   $('.front-loading').show();
	$.ajax({
	    url : "/settings/checkoutpullbranchrestartinstances",
	    type: "POST",
	    data : {id: microserviceId, branchName: branch},
	    success: function(data, textStatus, jqXHR) {
            $('.front-loading').hide();
	    },
        error: function (request, status, error) {
                 $('.front-loading').hide();
                showNotification('danger', "Error occurred when trying to checkout, pull and restart instances. Check Logs for more info");
           }
	});
}

function checkoutAndPull(microserviceId, branch){
    $("#modal-microservice-git").modal("hide");
   $('.front-loading').show();
	$.ajax({
	    url : "/settings/checkoutpullbranch",
	    type: "POST",
	    data : {id: microserviceId, branchName: branch},
	    success: function(data, textStatus, jqXHR) {
            $('.front-loading').hide();
	    },
        error: function (request, status, error) {
                 $('.front-loading').hide();
              showNotification('danger', "Error occurred when trying to checkout and pull code. Check Logs for more info");
           }
	});
}

function updateMicroservice(){
    $("#form-update-pomLocation").removeClass("has-error");
	$("#form-update-default-port").removeClass("has-error");

    if($("#input-update-pomLocation").val() == '' || $("#input-update-default-port").val() == ''){
        if($("#input-update-pomLocation").val() == '') $("#form-update-pomLocation").addClass("has-error");
        if($("#input-update-default-port").val() == '') $("#form-update-default-port").addClass("has-error");
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
                  showNotification('danger', "Error occurred when updating microservice information. Check Logs for more info");
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
                 showNotification('danger', "Error occurred when trying to show group information. Check Logs for more info");
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
               showNotification('danger', "Error occurred when trying to remove group. Check Logs for more info");
           }
	});
}

function saveGitCred(){
    $('.front-loading').show();
	$.ajax({
	    url : "/settings/git/config/save",
	    type: "POST",
	    data : {user: $("#input-git-user").val(), pass: $("#input-git-pass").val()},
	    success: function(data, textStatus, jqXHR) { location.reload(); },
        error: function (request, status, error) {
              $('.front-loading').hide();
               showNotification('danger', "Error occurred when trying to remove group. Check Logs for more info");
           }
	});
}

function cleanGitCred(){
    $('.front-loading').show();
	$.ajax({
	    url : "/settings/git/config/clean",
	    type: "GET",
	    success: function(data, textStatus, jqXHR) { location.reload(); },
        error: function (request, status, error) {
              $('.front-loading').hide();
               showNotification('danger', "Error occurred when trying to remove group. Check Logs for more info");
           }
	});
}

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

$(document).ready(function(){
    $('[data-toggle="git-popover"]').popover();
});
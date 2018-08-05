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
    delaysMicroservicesGroup = [];

    $( ".group-definition-ms" ).each(function( index ) {
        idsMicroservicesGroup.push($( this ).data("id"));
        delaysMicroservicesGroup.push($( this ).val());
    });

     $.ajax({
            url : "/settings/setmicroservicesgroup",
            type: "POST",
            data : {name:  $("#input-groupname").val(),
                    idsMicroservicesGroup: idsMicroservicesGroup,
                    delaysMicroservicesGroup: delaysMicroservicesGroup},
            success: function(data, textStatus, jqXHR) {location.reload();},
            error: function (request, status, error) {
                 $('.front-loading').hide();
                  showNotification('danger', "Error occurred when trying to create a group. Check Logs for more info");
               }
        });

}

function defineGroup(){
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
        $("#title-group-definition").html($("#input-groupname").val());
        $("#table-group-definition > tbody").html("");

        $( ".microservice-group-form.btn-success" ).each(function( index ) {
            $('#table-group-definition > tbody').append('<tr class="even gradeA"><td>'+$( this ).data("name")+'</td><td><input class="group-definition-ms" data-id="'+$( this ).data("id")+'" type="text" class="form-control border-input" value="0"/></td>'+
                                                        '<td>'+
                                                            '<input id="group-item-'+index+'" class="group-definition-ms-order" value="'+(index+1)+'" onchange="sortGroupRows()"/>'+
                                                        '</td></tr>');
        });

        $("#modal-group-definition").modal("show");
    }
}

function sortGroupRows(){
    var tb = $('#table-group-definition > tbody');
    var rows = tb.find('tr');
    rows.sort(function(a, b) {
    console.log(a)
        var keyA = $(a).find('.group-definition-ms-order').val();
        var keyB = $(b).find('.group-definition-ms-order').val();
        return keyA - keyB;
    });
    $.each(rows, function(index, row) {
        tb.append(row);
    });}

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

function setExternalInstance(){
    var fieldsToCheck = [];
    fieldsToCheck.push("external-instance-name");
    fieldsToCheck.push("external-instance-actuator-prefix");
    fieldsToCheck.push("external-instance-ip");
    fieldsToCheck.push("external-instance-port");
    if(!cheackEmptyValuesForm(fieldsToCheck)){
        $('.front-loading').show();
            $.ajax({
                url : "/settings/setexternalinstance",
                type: "POST",
                data : {name: $("#input-external-instance-name").val(), port: $("#input-external-instance-port").val(),
                        actuatorPrefix: $("#input-external-instance-actuator-prefix").val(), ip: $("#input-external-instance-ip").val()},
                success: function(data, textStatus, jqXHR) { location.reload(); },
                 error: function (request, status, error) {
                      $('.front-loading').hide();
                       showNotification('danger', "Error occurred when trying to register an external instance. Check Logs for more info");

                   }
            });
    }
}

function removeExternalInstance(instanceId){
    $('.front-loading').show();
	$.ajax({
	    url : "/settings/removeexternalinstance",
	    type: "POST",
	    data : {id: instanceId},
	    success: function(data, textStatus, jqXHR) { location.reload(); },
        error: function (request, status, error) {
              $('.front-loading').hide();
               showNotification('danger', "Error occurred when trying to remove an external. Check Logs for more info");
           }
	});
}


function setNewMicroservice(){
	if($("#input-hidden-mavenhomelocation").val() == '' && $("#input-newmicroservice-build-tool").val() == 'maven'){
		$("#form-mavenhomelocation").addClass("has-error");
	}else{
	    var fieldsToCheck = [];
        fieldsToCheck.push("newmicroservice-name");
        fieldsToCheck.push("newmicroservice-pomlocation");
        fieldsToCheck.push("newmicroservice-defaultport");
        fieldsToCheck.push("newmicroservice-build-tool");

        if(!cheackEmptyValuesForm(fieldsToCheck)){
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

function saveGitHttpsCred(){
    $('.front-loading').show();
	$.ajax({
	    url : "/settings/git/https/config/save",
	    type: "POST",
	    data : {user: $("#input-git-user").val(), pass: $("#input-git-pass").val()},
	    success: function(data, textStatus, jqXHR) { location.reload(); },
        error: function (request, status, error) {
              $('.front-loading').hide();
               showNotification('danger', "Error occurred when trying to store HTTPS git Credentials. Check Logs for more info");
           }
	});
}

function saveGitSshCred(){
    $('.front-loading').show();
	$.ajax({
	    url : "/settings/git/ssh/config/save",
	    type: "POST",
	    data : { sshKeyLocation: $("#input-git-ssh-key-location").val(), sshKeyPassword: $("#input-git-ssh-key-password").val()},
	    success: function(data, textStatus, jqXHR) { location.reload(); },
        error: function (request, status, error) {
              $('.front-loading').hide();
               showNotification('danger', "Error occurred when trying to store HTTPS git Credentials. Check Logs for more info");
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
    $("#tab-newmicroservice-git-repo").addClass("active");
    $('#content-file-system').hide();
    $("#tab-git-https-settings").addClass("active");
    $("#content-git-ssh-settings").hide();
});

function showGitSettings(component){
    $('#content-git-https-settings').hide();
    $('#content-git-ssh-settings').hide();
    $('#content-'+component).show();

    $("#tab-git-https-settings").removeClass("active");
    $("#tab-git-ssh-settings").removeClass("active");
    $("#tab-"+component).addClass("active");
}

function showNewMsForm(component){
     $('#content-file-system').hide();
     $('#content-newmicroservice-git-repo').hide();
     $('#content-'+component).show();

    $("#tab-newmicroservice-git-repo").removeClass("active");
    $("#tab-file-system").removeClass("active");
    $("#tab-"+component).addClass("active");
}

function fillFormGitNewMs(){
    var girUrl =$('#input-git-newmicroservice-repo').val();
    var repoName = girUrl.split("/")[girUrl.split("/").length-1].replace('.git','');

    $('#input-git-newmicroservice-name').val(repoName);
    $('#input-git-newmicroservice-destination').val(settingsFolder+"/"+repoName);
    $('#input-git-newmicroservice-pomlocation').val(settingsFolder+"/"+repoName);
    $('#input-git-newmicroservice-gitLocation').val(settingsFolder+"/"+repoName);
}

function setNewMicroserviceFromGit(){
    var fieldsToCheck = [];
    fieldsToCheck.push("git-newmicroservice-repo");
    fieldsToCheck.push("git-newmicroservice-destination");
    fieldsToCheck.push("git-newmicroservice-name");
    fieldsToCheck.push("git-newmicroservice-pomlocation");
    fieldsToCheck.push("git-newmicroservice-defaultport");
    fieldsToCheck.push("git-newmicroservice-build-tool");
    fieldsToCheck.push("git-newmicroservice-gitLocation");

    if($("#input-hidden-mavenhomelocation").val() == '' && $("#input-newmicroservice-build-tool").val() == 'maven'){
    		$("#form-mavenhomelocation").addClass("has-error");
    }else{
        if(!cheackEmptyValuesForm(fieldsToCheck)){
            $('.front-loading').show();
            $.ajax({
                url : "/settings/setnewmicroservice/git",
                type: "POST",
                 data : {gitRepo: $('#input-git-newmicroservice-repo').val(),
                        destinationFolder: $('#input-git-newmicroservice-destination').val(),
                        name: $("#input-git-newmicroservice-name").val(),
                        pomLocation: $("#input-git-newmicroservice-pomlocation").val(),
                        defaultPort: $("#input-git-newmicroservice-defaultport").val(),
                        actuatorPrefix: $("#input-git-newmicroservice-actuatorprefix").val(),
                        vmArguments: $("#input-git-newmicroservice-vmarguments").val(),
                        buildTool: $("#input-git-newmicroservice-build-tool").val(),
                        gitLocation: $("#input-git-newmicroservice-gitLocation").val()},
                success: function(data, textStatus, jqXHR) { location.reload(); },
                 error: function (request, status, error) {
                      $('.front-loading').hide();
                       showNotification('danger', "Error occurred when trying to register a microservice. Check Logs for more info");

                   }
            });
        }
    }
}

function cheackEmptyValuesForm(fieldsToCheck){
    var errors = false;
    fieldsToCheck.forEach(function(field) {
        $("#form-" + field).removeClass("has-error");
        $("#form-" + field).removeClass("has-success");

        if($("#input-" + field).val() == '' || $("#input-" + field).val() == '-1'){
            errors = true
            $("#form-" + field).addClass("has-error");
        }else{
            $("#form-" + field).addClass("has-success");
        }
    });

    return errors;
}



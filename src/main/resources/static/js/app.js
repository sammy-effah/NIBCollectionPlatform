var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    
    $("#userinfo").html("");
    
}

function connect() {
    var socket = new SockJS(contextpath+'/websocket-example');
    
    
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
    
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/requests_for_authz', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
            
           // 
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/user", {}, JSON.stringify({'name': $("#name").val()}));
}

function sendNames(refund) {
if(refund.destination!=null){
    stompClient.send("/app/requests_for_authz", {}, JSON.stringify({'request': refund.destination}));
    }else{
   // stompClient.send("/app/requests_for_authz", {}, JSON.stringify({'request': 'hi'}));
    
    }
    
}


function showGreeting(message) {
	
    //$("#userinfo").append("<tr><td>" + message + "</td></tr>");
    
        if (role=="ROLE_ADMIN"||role=="ROLE_BFS_ADMIN"){
	             
	             
		    	 getRefundIniList();
		    	 
	         }
	         
	         if(role=="ROLE_BFS_SSP_AGENT"){
	         	   //alert(role)  
 				getRefundIniListUsersWithoutAuth()	         	   
	        
	         }
	         
	         getAllCountByType()
			getDistinctCountByType();
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});
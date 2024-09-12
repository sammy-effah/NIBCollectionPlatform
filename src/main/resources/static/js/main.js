'use strict';
//signupForm
var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

var multipleUploadForm = document.querySelector('#multipleUploadForm');
var multipleFileUploadInput = document.querySelector('#multipleFileUploadInput');
var multipleFileUploadError = document.querySelector('#multipleFileUploadError');
var multipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');
var rportalapiurl=$("#rportalapiurl").text();

//alert(rportalapiurl);

function uploadSingleFile(file) {
	
	$("#loader").show();
	
    var formData = new FormData();
    
//	 if( file <1){
//		 alert("please upload file");
//	 }
	 
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", rportalapiurl+"/api/sendfile");
   // alert("successful");
    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = xhr.responseText;
        var message= xhr.responseText.message;
        
        var obj = JSON.parse(response);
        
        $("#loader").hide();
        
        if(xhr.status == 200) {
        	
        	
        	
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>"+obj.message + "</p>";
            singleFileUploadSuccess.style.display = "block";
            
//            
        	swal({   
                title: "Created!",   
                text: "Operation Successful.",   
                type: "success",   
                showCancelButton: false,   
                confirmButtonText: "Ok",   
                closeOnConfirm: true 
            }, function(){ 
           	 
            	
            	 var merchantAlias = $('option:selected').val();
         	  	
         	  	getCustomerList(merchantAlias);
           	 
   				 //location.reload();

				//alert("hi");

                //swal("Deleted!", "Your imaginary file has been deleted.", "success"); 
            });
        	
        } else {
        	
       
        	
            singleFileUploadSuccess.style.display = "none";
        	alert("not successful");

            singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
            
            
         	swal({   
                title: "Failed!",   
                text: "Operation Failed.",   
                showCancelButton: false,   
                confirmButtonText: "Ok",   
                closeOnConfirm: true 
            }, function(){ 
           	 
           	 
   				 //location.reload();

				//alert("hi");

                //swal("Deleted!", "Your imaginary file has been deleted.", "success"); 
            });
        }
    }

    xhr.send(formData);
}

//function uploadMultipleFiles(files) {
//    var formData = new FormData();
//    for(var index = 0; index < files.length; index++) {
//        formData.append("files", files[index]);
//    }
//
//    var xhr = new XMLHttpRequest();
//    xhr.open("POST", "/uploadMultipleFiles");
//
//    xhr.onload = function() {
//        console.log(xhr.responseText);
//        var response = JSON.parse(xhr.responseText);
//        if(xhr.status == 200) {
//            multipleFileUploadError.style.display = "none";
//            var content = "<p>All Files Uploaded Successfully</p>";
//            for(var i = 0; i < response.length; i++) {
//                content += "<p>DownloadUrl : <a href='" + response[i].fileDownloadUri + "' target='_blank'>" + response[i].fileDownloadUri + "</a></p>";
//            }
//            multipleFileUploadSuccess.innerHTML = content;
//            multipleFileUploadSuccess.style.display = "block";
//        } else {
//            multipleFileUploadSuccess.style.display = "none";
//            multipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
//        }
//    }
//
//    xhr.send(formData);
//}

singleUploadForm.addEventListener('submit', function(event){
    var files = singleFileUploadInput.files;
    if(files.length === 0) {
    	
    	
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }else{
    	
    	uploadSingleFile(files[0]);
    
    }
    event.preventDefault();
}, true);


//multipleUploadForm.addEventListener('submit', function(event){
//    var files = multipleFileUploadInput.files;
//    if(files.length === 0) {
//        multipleFileUploadError.innerHTML = "Please select at least one file";
//        multipleFileUploadError.style.display = "block";
//    }
//    uploadMultipleFiles(files);
//    event.preventDefault();
//}, true);


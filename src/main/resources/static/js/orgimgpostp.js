'use strict';
//signupForm
var singleUploadForm = document.querySelector('#singleFormUpload');
var singleFileUploadInput = document.querySelector('#fileupl');
var id= document.querySelector('#orgId');
//var singleFileUploadError = document.querySelector('#singleFileUploadError');
//var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

//var multipleUploadForm = document.querySelector('#multipleUploadForm');
//var multipleFileUploadInput = document.querySelector('#multipleFileUploadInput');
//var multipleFileUploadError = document.querySelector('#multipleFileUploadError');
//var multipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');
//var rportalapiurl=$("#rportalapiurl").text();

//alert(id.value);

function uploadSingleFile(file,orgId) {

	$("#loader").show();
	
    var formData = new FormData();
    
//	 if( file <1){
//		 alert("please upload file");
//	 }
	 
    formData.append("file", file);
    
    formData.append("orgId", orgId.value);
    
	//alert(userId)

    var xhr = new XMLHttpRequest();
    xhr.open("POST", contextPath+"/api/orgcred/img");
   // alert("successful");
    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = xhr.responseText;
        var message= xhr.responseText.message;
        
        var obj = JSON.parse(response);
        
        $("#loader").hide();
        
        if(xhr.status == 200) {
        	
        	
        	
//            singleFileUploadError.style.display = "none";
//            singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>"+obj.result_message + "</p>";
//            singleFileUploadSuccess.style.display = "block";
            var tp;
            var title;
           // alert(obj.status);
           if( obj.status=="00"){
        	   
        	   tp="success"
        	   title="Successful"
        		   
           }else{
        	   
        	   tp="danger"
               title="Failed"
            	   
           }
//            
        	swal({   
                title: title,   
                text: obj.message,   
                type: tp,   
                showCancelButton: false,   
                confirmButtonText: "Ok",   
                closeOnConfirm: true 
            }, function(){ 
           	 

              if( obj.status=="00"){
           	   
    				 location.reload();

           		   
              }
            	 //var merchantAlias = $('option:selected').val();
         	  	
         	  	
           	 
   				 //location.reload();

				//alert("hi");

                //swal("Deleted!", "Your imaginary file has been deleted.", "success"); 
            });
        	
        } 
        
        
//        else {
//        	
//       
//        	
//            singleFileUploadSuccess.style.display = "none";
//        	alert("not successful");
//
//            singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
//            
//            
//         	swal({   
//                title: "Failed!",   
//                text: "Operation Failed.",   
//                showCancelButton: false,   
//                confirmButtonText: "Ok",   
//                closeOnConfirm: true 
//            }, function(){ 
//           	 
//                if( obj.result_code=="00"){
//             	   
//      				 location.reload();
//
//             		   
//                }
//           	 
//
//				//alert("hi");
//
//                //swal("Deleted!", "Your imaginary file has been deleted.", "success"); 
//            });
//        }
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
    var orgId= id;
    if(files.length === 0) {
    	
    	
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }else{
    	
    	uploadSingleFile(files[0],orgId);
    
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


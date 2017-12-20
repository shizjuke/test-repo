// Add Contact
function addContact() {
	
	$.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: "api/contacts",
        dataType: "json",
        data: formToJSON(''),
        success: function(data){
        	// close the popup
            $("#add_new_contact_modal").modal("hide");
     
            // read contacts again
            readContacts();
     
            // clear fields from the popup
            clearForm();
        }
    });
	
}

// delete contact
function deleteContact(id) {
    var conf = confirm("Are you sure, do you really want to delete Contact?");
    if (conf == true) {
    	
    	$.ajax({
            type: 'DELETE',
            contentType: 'application/json',
            url: "api/contacts/" + id,
            dataType: "html",
            success: function(data){
                // read contacts again
                readContacts();
         
            }
        });

    }
}

// get contact
function getContactDetails(id) {
    // Add Contact ID to the hidden field for furture usage
    $("#hidden_contact_id").val(id);
    
    $.ajax({
        type: 'GET',
        url: '/api/contacts/' + id,
        dataType: "json",
        success: function(data){
            // Assing existing values to the modal popup fields
            $("#update_firstname").val(data.firstname);
            $("#update_lastname").val(data.lastname);
            $("#update_homeAddress").val(data.homeAddress);
            $("#update_phoneNumber").val(data.phoneNumber);
            $("#update_email").val(data.email);
            $("#update_note").val(data.note);
        }
    });
    
    // Open modal popup
    $("#update_contact_modal").modal("show");
}

// update contact
function updateContactDetails() {

    // get hidden field value
    var id = $("#hidden_contact_id").val();
    
	$.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: "api/contacts/" + id,
        dataType: "json",
        data: formToJSON('update_'),
        success: function(data){
        	// close the popup
            $("#update_contact_modal").modal("hide");
     
            // read contacts again
            readContacts();
     
        }
    });
 
}

// read all contacts
function readContacts() {
    $.get("api/contacts", {}, function (data, status) {
    	fillContactsTable(data);
    });
}
 
// fill table with contacts
function fillContactsTable(data){

	var tbl = document.createElement("table");
	tbl.className = "table table-bordered table-striped";
	var tbl_hdr = tbl.createTHead();
	var hdr_row = tbl_hdr.insertRow();
	hdr_row.insertCell().innerHTML = "<b>First Name</b>";
	hdr_row.insertCell().innerHTML = "<b>Last Name</b>";
	hdr_row.insertCell().innerHTML = "<b>Home Address</b>";
	hdr_row.insertCell().innerHTML = "<b>Phone</b>";
	hdr_row.insertCell().innerHTML = "<b>Email</b>";
	hdr_row.insertCell().innerHTML = "<b>Note</b>";
	hdr_row.insertCell().innerHTML = "<b>Update</b>";
	hdr_row.insertCell().innerHTML = "<b>Delete</b>";
	
	var tbl_body = document.createElement("tbody");
    $.each(data, function() {
        var tbl_row = tbl_body.insertRow();
        var contactId;
        $.each(this, function(k , v) {
        	if(k === "contactId"){
        		contactId = v;
        		return;
        	}
            var cell = tbl_row.insertCell();
            cell.appendChild(document.createTextNode(v.toString()));
        })  
        createUpdateBtn(tbl_row, contactId);
        createDeleteBtn(tbl_row, contactId);
    })
    if(jQuery.isEmptyObject(data)){
    	var no_data_row = tbl_body.insertRow();
    	no_data_row.innerHTML = "No contacts found";
    	no_data_row.colSpan = 8;
    }
    tbl.appendChild(tbl_body);
    $(".contacts_content").html(tbl);
    
}

// create update button
function createUpdateBtn(tbl_row, id){
	var cell = tbl_row.insertCell();
	var btn = document.createElement('input');
	btn.type = "button";
	btn.className = "btn btn-warning";
	btn.value = "Update";
	btn.onclick = (function(id) {return function() {getContactDetails(id);}})(id);
	cell.appendChild(btn);	
}

// create delete button
function createDeleteBtn(tbl_row, id){
	var cell = tbl_row.insertCell();
	var btn = document.createElement('input');
	btn.type = "button";
	btn.className = "btn btn-danger";
	btn.value = "Delete";
	btn.onclick = (function(id) {return function() {deleteContact(id);}})(id);
	cell.appendChild(btn);	
}
 
// Simple search
function fulltextSearch() {
	
	if($('#phrase').val() === "") {
		readContacts();
		return;
	} 
		
	$.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: "api/search/" + $('#phrase').val(),
        dataType: "json",
        success: function(data){
        	fillContactsTable(data);
        }
    });
	
}

//Advanced search
function advancedSearch() {
	
	$.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: "api/search/",
        dataType: "json",
        data: formToJSON('search_'),
        success: function(data){
        	$("#search_contact_modal").modal("hide");
        	
        	fillContactsTable(data);
        }
    });
	
}

$(document).ready(function () {
    // READ contacts on page load
    readContacts(); // calling function
    
    $('#phrase').on('input', function() {
    	fulltextSearch();
    });
    
});

// validate form
function validateForm(prefix){
	
	var isValid = true;
	
	if($('#firstname').val() === "" || $('#firstname').val().length < 2)  {
		$('#firstname').parent().addClass('has-error');
		if($('#firstname').parent().children('small').length == 0){
			$('#firstname').parent().append('<small class="text-danger">Firstname is required. Min length is 2</small>');
		}
		isValid = false;
	} else {
		$('#firstname').parent().removeClass('has-error');
		$('#firstname').parent().children('small').remove();
	}
	
	if($('#lastname').val() === "" || $('#lastname').val().length < 2)  {
		$('#lastname').parent().addClass('has-error');
		if($('#lastname').parent().children('small').length == 0){
			$('#lastname').parent().append('<small class="text-danger">Lastname is required. Min length is 2</small>');
		}
		isValid = false;
	} else {
		$('#lastname').parent().removeClass('has-error');
		$('#lastname').parent().children('small').remove();
	}
	
	var addressValid = validateAddressFields('');
	
	return isValid && addressValid;
	
}

function validateAddressFields(prefix){
	
	var isValid = true;
	
	if($('#' + prefix + 'homeAddress').val() === "" 
		&& $('#' + prefix + 'phoneNumber').val() === "" 
			&& $('#' + prefix + 'email').val() === "")  {
		
		$('#' + prefix + 'homeAddress').parent().addClass('has-error');
		$('#' + prefix + 'phoneNumber').parent().addClass('has-error');
		$('#' + prefix + 'email').parent().addClass('has-error');

		if($('#' + prefix + 'homeAddress').parent().children('small').length == 0){
			$('#' + prefix + 'homeAddress').parent().append('<small class="text-danger">Email, phone number or home address is required</small>');
		}
		
		isValid = false;
		
	} else {
		$('#' + prefix + 'homeAddress').parent().removeClass('has-error');
		$('#' + prefix + 'phoneNumber').parent().removeClass('has-error');
		$('#' + prefix + 'email').parent().removeClass('has-error');
		
		$('#' + prefix + 'homeAddress').parent().children('small').remove();
		
	}	
	
	if($('#' + prefix + 'email').val().length > 0){
		if(isEmail($('#' + prefix + 'email').val()) == false){
			if($('#' + prefix + 'email').parent().children('small').length == 0){
				$('#' + prefix + 'email').parent().append('<small class="text-danger">Incorrect format</small>');
			}
			isValid = false;
		} else {
			$('#' + prefix + 'email').parent().children('small').remove();
		}
	}
	
	if($('#' + prefix + 'phoneNumber').val().length > 0){
		if(isPhone($('#' + prefix + 'phoneNumber').val()) == false){
			if($('#' + prefix + 'phoneNumber').parent().children('small').length == 0){
				$('#' + prefix + 'phoneNumber').parent().append('<small class="text-danger">Incorrect format. Min. 8 digits</small>');
			}
			isValid = false;
		} else {
			$('#' + prefix + 'phoneNumber').parent().children('small').remove();
		}
	}
	
	return isValid;
	
}

function isEmail(email) {
	var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	return regex.test(email);
}

function isPhone(phone) {
	var regex = /^[0-9]{8,}$/;
	return regex.test(phone);
}

//Helper function to serialize all the form fields into a JSON string
function formToJSON(prefix) {
    return JSON.stringify({
        "firstname": $('#' + prefix + 'firstname').val(),
        "lastname": $('#' + prefix + 'lastname').val(),
        "homeAddress": $('#' + prefix + 'homeAddress').val(),
        "phoneNumber": $('#' + prefix + 'phoneNumber').val(),
        "email": $('#' + prefix + 'email').val(),
        "note": $('#' + prefix + 'note').val()
        });
}

//Helper function to clear form
function clearForm() {
	$("#firstname").val("");
    $("#lastname").val("");
    $("#homeAddress").val("");
    $("#phoneNumber").val("");
    $("#email").val("");
    $("#note").val("");
}
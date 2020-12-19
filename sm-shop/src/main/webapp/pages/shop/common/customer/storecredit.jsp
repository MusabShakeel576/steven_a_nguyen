<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!-- requires functions.jsp -->
<script src="<c:url value="/resources/js/shop-customer.js" />"></script>

<script type="text/javascript">

$(document).ready(function() {


	isFormValid();
	//bind fields to be validated
	$("input[type='text']").on("change keyup paste", function(){
		resetGlobalErrors();
		isFormValid();
	});

	$("input[type='password']").on("change keyup paste", function(){
		resetGlobalErrors();
		isFormValid();
	});

	$("#submitChangePassword").click(function(e) {
		e.preventDefault();//do not submit form
		resetGlobalErrors();
		if(isFormValid()) {
			$('#changePasswordForm').submit();
		}
    });


});

function isFormValid() {

	if($('.alert-error').is(":visible")) {
		return true;
	}

	if($('.alert-success').is(":visible")) {
		return true;
	}

	var msg = isCustomerFormValid($('#changePasswordForm'));

	if(msg!=null) {//disable submit button
		$('#submitChangePassword').addClass('btn-disabled');
		$('#submitChangePassword').prop('disabled', true);
		$('#formError').html(msg);
		$('#formError').show();
		return false;
	} else {
		$('#submitChangePassword').removeClass('btn-disabled');
		$('#submitChangePassword').prop('disabled', false);
		$('#formError').hide();
		return true;
	}
}

function resetGlobalErrors() {
	$('.alert-error').hide();
}


</script>

	<div id="main-content" class="container clearfix row-fluid">
			<div class="span12 col-md-12 no-padding common-row">
			<div class="span8 col-md-8 no-padding">
                <div class="col-md-3">
                    <div class="m-3 bg-white rounded text-center" style="box-shadow: 0px 0px 2px black; padding: 20px">Store Credit $50</div>
                </div>
			</div>
			<div class="span4 col-md-4">
			 	<jsp:include page="/pages/shop/common/customer/customerProfileMenu.jsp" />
			 	<jsp:include page="/pages/shop/common/customer/customerOrdersMenu.jsp" />
			 </div>
			</div>
	</div>
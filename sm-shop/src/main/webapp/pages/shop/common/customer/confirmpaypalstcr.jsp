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
<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<%@ page import="java.sql.*" %>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!-- requires functions.jsp -->
<script src="<c:url value="/resources/js/shop-customer.js" />"></script>

<script>
document.addEventListener("DOMContentLoaded", function(){
    if(sessionStorage.getItem('paypalsuccess') === null){
        document.getElementById("paypalconfirm").style.display = "none";
    }
});
</script>
	<div id="main-content" class="container clearfix row-fluid">
			<div class="span12 col-md-12 no-padding common-row">
			<div class="span8 col-md-8 no-padding"><c:url var="savePayPalStcr" value="/shop/customer/savePayPalStcr.html/${confirm.price}"/>
                <div id="paypalconfirm">
                <p>Confirm your $ ${confirm.price} worth of Store Credits to complete the purchase</p>
                <form:form method="post" action="${savePayPalStcr}" id="savePayPalStcrForm" modelAttribute="paypalstcr">
                      <input type="hidden" id="customerid" name="customerid" value="${customerId}">
                      <input type="hidden" id="price" name="price" value="${confirm.price}">
                      <input type="submit" class="btn btn-success" value="Confirm">
                </form:form>
                </div>
            </div>
			<div class="span4 col-md-4">
			 	<jsp:include page="/pages/shop/common/customer/customerProfileMenu.jsp" />
			 	<jsp:include page="/pages/shop/common/customer/customerOrdersMenu.jsp" />
			 </div>
			</div>
	</div>
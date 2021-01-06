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
<script src="https://www.paypal.com/sdk/js?client-id=AZ1fyYr3JkoZPLlBcLTuQuiajJ6p-arwBWA_e4pCSYXc79xuawcigqwrtScSCg-tvKsp73cIgdEIg7S6"></script>

<script type="text/javascript">
var price = ${specific.price};
paypal.Buttons({
    style : {
        color: 'blue',
        shape: 'pill'
    },
    createOrder: function (data, actions) {
        return actions.order.create({
            purchase_units : [{
                amount: {
                    value: price
                }
            }]
        });
    },
    onApprove: function (data, actions) {
        return actions.order.capture().then(function (details) {
            console.log(details)
            window.location.replace("http://localhost:8080/shop/customer/savePayPalStcr.html/${specific.price}")
        })
    },
    onCancel: function (data) {
        window.location.replace("http://localhost:8080/shop/customer/storecredit.html")
    }
}).render('#paypal-payment-button');

</script>
	<div id="main-content" class="container clearfix row-fluid">
			<div class="span12 col-md-12 no-padding common-row">
			<div class="span8 col-md-8 no-padding">
			<c:choose>
                <c:when test="${specific != null}">
                    <div id="paypal-payment-button">
                    </div>
                </c:when>
                <c:when test="${confirm != null}">
                <c:url var="savePayPalStcr" value="/shop/customer/savePayPalStcr.html/${confirm.price}"/>
                <div class="paypalconfirm" id="paypalconfirm">
                <p>Confirm your $ ${confirm.price} worth of Store Credits to complete the purchase</p>
                <form:form method="post" action="${savePayPalStcr}" id="savePayPalStcrForm" modelAttribute="paypalstcr">
                      <input type="hidden" id="customerid" name="customerid" value="${customerId}">
                      <input type="hidden" id="price" name="price" value="${confirm.price}">
                      <input type="submit" class="btn btn-success" value="Confirm">
                </form:form>
                </div>
                </c:when>
                <c:when test="${orderprice != null}">
                <c:url var="savePayStcr" value="/shop/customer/savePayStcr.html/${orderprice}"/>
                    <form:form method="post" action="${savePayStcr}" id="savePayStcrForm" modelAttribute="paypalstcr">
                          <input type="hidden" id="orderprice" name="orderprice" value="${orderprice}">
                          <input type="submit" class="btn btn-success" value="PAY">
                    </form:form>
                </c:when>
                <c:otherwise>
			<c:forEach var="adstcr" items="${list}" varStatus="counter">
                <div class="col-md-3">
                <a href="/shop/customer/storecredit.html/${adstcr.id}">
                    <div class="m-3 bg-white rounded text-center" style="box-shadow: 0px 0px 2px black; padding: 20px">
                      <p>${adstcr.title}</p>
                      <p>$ ${adstcr.price}</p>
                    </div>
                <a>
                </div>
             </c:forEach>
             </c:otherwise>
             </c:choose>
			</div>
			<div class="span4 col-md-4">
			 	<jsp:include page="/pages/shop/common/customer/customerProfileMenu.jsp" />
			 	<jsp:include page="/pages/shop/common/customer/customerOrdersMenu.jsp" />
			 </div>
			</div>
	</div>
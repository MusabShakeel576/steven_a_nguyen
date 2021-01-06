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

<script type="text/javascript">

</script>
	<div id="main-content" class="container clearfix row-fluid">
			<div class="span12 col-md-12 no-padding common-row">
			<c:choose>
                <c:when test="${orderprice != null}">
                    <form:form method="post" action="/shop/order/savePayStcr.html/${orderprice}" id="savePayStcrForm" modelAttribute="paypalstcr">
                          <input type="hidden" id="customerId" name="customerId" value="${customerId}">
                          <input type="hidden" id="orderprice" name="orderprice" value="${orderprice}">
                          <input type="submit" class="btn btn-success" value="PAY">
                    </form:form>
                </c:when>
                <c:otherwise>
                    <p>Page Not Found</p>
                </c:otherwise>
                </c:choose>
			</div>
	</div>
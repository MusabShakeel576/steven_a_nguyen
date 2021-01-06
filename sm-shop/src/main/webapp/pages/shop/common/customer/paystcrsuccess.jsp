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
<script src="https://www.paypal.com/sdk/js?client-id=AZLJn3FliO20nmfrfktKsfpW_p_V2J7_qPd-vbqNzww1CM46FpdT5nph6nmAXYYQgZBejLm-gzMViYdM"></script>

<script type="text/javascript">

</script>

	<div id="main-content" class="container clearfix row-fluid">
			<div class="span12 col-md-12 no-padding common-row">
			    <div class="alert alert-success" role="alert">
                  Payment by using Store Credit has been successfully completed, please close this tab for security reasons and proceed with your order.
                </div>
			</div>
	</div>
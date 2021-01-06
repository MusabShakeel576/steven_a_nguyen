<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>

<script>



</script>


<div class="tabbable">
	<jsp:include page="/common/adminTabs.jsp" />
		<div class="tab-content">
  			<div class="tab-pane active" id="dir-stcr">
				<div class="sm-ui-component">
				<c:choose>
                    <c:when test="${deleteSuccess != null}">
                        <div class="alert alert-success" role="alert">
                          Store Credit has been Deleted Successfully.
                        </div>
                        <a href="http://localhost:8080/admin/storecredit/directory.html"><button type="button" class="btn btn-primary">Return Back</button></a>
                    </c:when>
                    <c:otherwise>
					<h3><s:message code="label.storecredit.directory" text="Store Credit Directory" /></h3>
					<br/>
                        <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>
                       <table class="table table-striped">
                         <thead>
                           <tr>
                             <th scope="col">Id</th>
                             <th scope="col">Title</th>
                             <th scope="col">Price</th>
                             <th scope="col">Delete</th>
                           </tr>
                         </thead>
                         <tbody>
                         <c:forEach var="adstcr" items="${list}" varStatus="counter">
                           <tr>
                             <th scope="row">${adstcr.id}</th>
                             <td>${adstcr.title}</td>
                             <td>${adstcr.price}</td>
                             <td><a href="/admin/storecredit/deleteAd.html/${adstcr.id}">Delete</a></td>
                           </tr>
                           </c:forEach>
                         </tbody>
                       </table>
                       </c:otherwise>
                       </c:choose>
   		</div>
   	</div>
</div>
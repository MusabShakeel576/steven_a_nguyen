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
                <c:when test="${saveAdSuccess != null}">
                    <div class="alert alert-success" role="alert">
                      Store Credit has been Added Successfully.
                    </div>
                    <a href="http://localhost:8080/admin/storecredit/add.html"><button type="button" class="btn btn-primary">Return Back</button></a>
                </c:when>
                <c:otherwise>
					<h3><s:message code="label.storecredit.add" text="Add Credit Directory" /></h3>
					<br/>
						<c:url var="saveAdStcr" value="/admin/storecredit/saveAd.html"/>
							<form:form method="POST" modelAttribute="adstcr" action="${saveAdStcr}">
                                <form:errors path="*" cssClass="alert alert-error" element="div" />
									<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>
                  					<div class="control-group">
                        				<label><s:message code="label.storecredit.title" text="Title"/></label>
                        				<div class="controls">
                        				<form:input cssClass="input-large" path="title" />
                        				</div>
	                                	<span class="help-inline"><form:errors path="title" cssClass="error" /></span>
	                        		</div>

	                        		<div class="control-group">
                        				<label><s:message code="label.storecredit.price" text="Price"/></label>
                        				<div class="controls">
	                                	<form:input cssClass="input-large" path="price" />
	                                	</div>
                                        <span class="help-inline"><form:errors path="price" cssClass="error" /></span>
	                        		</div>
	                        		<div class="form-actions">
                  						<div class="pull-right">
                  							<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  						</div>
            	 					</div>
            	 			</form:form>
                 </c:otherwise>
                 </c:choose>
   		</div>
   	</div>
</div>
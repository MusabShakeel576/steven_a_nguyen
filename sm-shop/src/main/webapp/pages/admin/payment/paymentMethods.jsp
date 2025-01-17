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
  					
  					
  					<div class="tab-pane active" id="payment-methods">


								<div class="sm-ui-component">
								<h3><s:message code="label.payment.title" text="Payment configuration" /></h3>	
								<br/>
								
								
								


							
							<c:url var="savePaymentMethods" value="/admin/payments/savePaymentMethods.html"/>
							<form:form method="POST" modelAttribute="configuration" action="${savePaymentMethods}">

      							
      								<form:errors path="*" cssClass="alert alert-error" element="div" />
									<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
								
								
								    <div class="control-group">
						      			  		<table class="table table-hover">

									  <c:forEach items="${modules}" var="module">
      			  
						      			  
						      			  			<tr>
						      			  				<td>
						      			  					<c:choose>
						      			  						<c:when test="${configuredModules[module.code]!=null && configuredModules[module.code].active==true}">
						      			  							<img src="<c:url value="/resources/img/icon_green_on.gif"/>" width="12">&nbsp;
						      			  						</c:when>
						      			  						<c:otherwise>
						      			  							<img src="<c:url value="/resources/img/icon_red_on.gif"/>" width="12">&nbsp;
						      			  						</c:otherwise>
						      			  					</c:choose>
						      			  				</td>
						      			  				<td>
						      			  					<label>
						      			  						<c:choose>
						      			  							<c:when test="${module.customModule==true}">
						      			  								<a href="<c:url value="/admin/payments/${module.code}.html"/>"><s:message code="module.payment.${module.code}" text="No label found - ${module.code}"/></a>
						      			  							</c:when>
						      			  							<c:otherwise>
						      			  								<a href="<c:url value="/admin/payments/paymentMethod.html?code="/><c:out value="${module.code}"/>"><s:message code="module.payment.${module.code}" text="No label found - ${module.code}"/></a>
						      			  							</c:otherwise>
						      			  						</c:choose>
						      			  					</label>
						      			  				</td>
						      			  				<td>
						      			  					<c:if test="${module.image!=null}">
						      			  						<img src="<c:url value="/resources/img/payment/${module.image}"/>">
						      			  					</c:if>
						      			  				</td>
						      			  			</tr>

					                  
					                  </c:forEach>

					                  <tr>
                                        <td>
                                            <img src="<c:url value="/resources/img/icon_green_on.gif"/>" width="12">&nbsp;
                                        </td>
                                        <td>
                                            <label>
                                                <a href="<c:url value="/admin/payments/paymentMethods.html"/>"><s:message code="module.payment.storecredit" text="No label found - storecredit"/></a>
                                            </label>
                                        </td>
                                        <td>
                                          <img src="<c:url value="https://i.ibb.co/MB43bZg/storecredit.png"/>">
                                        </td>
                                    </tr>
					     				</table>
						                        
						                  </div>             

            	 			</form:form>
							
							


      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>
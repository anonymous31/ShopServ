<%@page import="ee.ttu.shop.site.Template"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="ee.ttu.shop.product.Product"%>
<%@page import="ee.ttu.shop.catalog.Catalog"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siteDir"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/jsp/head.jsp">
	<jsp:param value="${template }" name="template" />
</jsp:include>
<title>Shop</title>
</head>
<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
<div id="bd" class="container-fluid text-center after-header">
	<div class="row content">
		<div class="col-sm-4 sidenav">

			<jsp:include page="/WEB-INF/jsp/sidebar.jsp"></jsp:include>


		</div>
		<div class="col-sm-8 text-left shadowed bgw">
			<c:if test="${not empty porder }">
				<c:set var="order" value="${porder.order}" />
				<a href="${context }/order/${order.id}/pdf"><strong>PDF</strong></a>


				<div class="table-responsive ">
					<table class="table">
						<thread>
						<tr class="ordersList">
							<td class="col-xs-6"><spring:message code="label.itemname" /></td>
							<td class="col-xs-2"><spring:message code="label.quantity" /></td>
							<td class="col-xs-2"><spring:message code="label.price" /></td>
							<td class="col-xs-2"><spring:message code="label.cost" /></td>
						</tr>
						</thread>

						<tbody>

							<c:forEach var="poi" items="${porder.prepOrderItem }">
								<c:set var="oi" value="${poi.orderItem}" />
								<tr style="margin-top: 15px;">
									<td class="col-xs-6"><a
										href="${context }/${poi.url }/${oi.product.name}-${oi.product.id}">
											<c:out value="${oi.product.name}" />
									</a></td>
									<td class="col-xs-2"><c:out value="${oi.qty} tk." /></td>
									<td class="col-xs-2"><c:out value="${oi.price} EUR" /></td>
									<td class="col-xs-2"><c:out value="${poi.totalPrice} EUR" />
									</td>
								</tr>


							</c:forEach>

							<tr style="margin-top: 15px;">
								<td class="col-xs-8">&nbsp</td>
								<td class="col-xs-2"><spring:message code="label.totalcost" />
									:</td>
								<td class="col-xs-2"><c:out value="${porder.subtotal} EUR" />
								</td>
							</tr>

						</tbody>
					</table>


				</div>



			</c:if>
		</div>
	</div>
</div>
<span id="top-link-block" class="hidden"> <a href="#top"
	class="well well-sm" id="ft-back-button"> <i
		class="glyphicon glyphicon-chevron-up"></i> Back to Top
</a>
</span>
<jsp:include page="/WEB-INF/jsp/footer.jsp" />
</body>
</html>

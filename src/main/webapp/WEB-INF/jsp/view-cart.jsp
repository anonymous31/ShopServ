<%@page import="ee.ttu.shop.site.Template"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="ee.ttu.shop.product.Product"%>
<%@page import="ee.ttu.shop.catalog.Catalog"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siteDir"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:set var="context" value="${pageContext.request.contextPath}" />

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
			<div class="row">
				<c:if test="${not empty cartDetails}">
					<c:forEach var="item" items="${cartDetails.items}">
						<c:set var="pp" value="${item.key}" />
						<c:set var="product" value="${pp.product}" />
						<c:set var="itemprop" value="${item.value}" />
						<form action="${context }/checkout/cart/del" method="POST"
							id="itemForm[${product.id}]">
							<div class="row">
								<div class="col-xs-1">&nbsp</div>
								<div class="col-xs-1">
									<a href="${context }/${pp.url }/${product.name}-${product.id}">
										<img class="img-responsive"
										src="${context }/${product.image }" alt="Korea">
									</a>
								</div>
								<div class="col-xs-3 cartdtl">
									<a href="${context }/${pp.url }/${product.name}-${product.id}">${product.description}</a><br>
								</div>
								<div class="col-xs-2">
									<div style="float: left;">
										<spring:message code="label.amount" />
										:&nbsp&nbsp
									</div>
									<div style="width: 30px; margin-bottom: 5px; float: left;"
										class="topSearchBoxL">
										<div class="topSearchBoxR">
											<input type="text" value="${itemprop.quantity }"
												id="to_basket[${product.id}]" class="prodBoxContQ">
										</div>
									</div>
								</div>
								<div class="col-xs-1">
									<a class="prodBoxUpdBtn"
										onclick="cart.update('${product.id}');"> </a>
								</div>
								<div class="col-xs-2">
									<strong id="rowPrice[${product.id}]"><c:out
											value="${ itemprop.total}"></c:out></strong> EUR
								</div>
								<div class="col-xs-1">
									<input hidden="true" name="product_id" id="product_id"
										value="${product.id }"> <label>

										<button class="btn btn-default" style="border-color: #fff;"
											onclick="document.getElementById('itemForm\\[${product.id}\\]').submit();return false;"
											type="submit">
											<span class="glyphicon glyphicon-remove-circle"
												aria-hidden="true"> </span>
										</button>

									</label>




								</div>
								<div class="col-xs-1">&nbsp</div>
							</div>
						</form>
						<br>
					</c:forEach>
					<div class="row">
						<div style="float: right; margin-right: 50px;">
							<strong id="totalPriceDtl" class="prodBoxContPrice"><c:out
									value="${cartDetails.totalPrice }" /></strong> EUR
						</div>
						<div style="float: right;"><spring:message code="label.totalprice" />:&nbsp</div>
					</div>
					<form:form id="basketForm" action="${context }/checkout/cart"
						method="POST" commandName="basketForm">
						<div class="row" style="font: 17px Tahoma, Arial, Verdana;">
							<div class="col-xs-12">
								<spring:message code="label.recipient" /><br>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-6">
								<spring:message code="label.fname" />:
								<form:input type="text" path="order[first-name]" />
							</div>
							<div class="col-xs-6">
								<spring:message code="label.lname" />:
								<form:input type="text" path="order[last-name]" />
							</div>
						</div>
						<div class="row">
							<div class="col-xs-6">
								<spring:message code="label.phone" />:
								<form:input type="text" path="order[phone]" />
							</div>
							<div class="col-xs-6">
								<spring:message code="label.email" />:
								<form:input type="text" path="order[email]" />
							</div>
						</div>
						<div class="row">
							<div class="col-xs-6">
								<spring:message code="label.address" />:
								<form:input type="text" path="order[recipient_address]" />
							</div>
						</div>
						<div class="row">
							<div class="col-xs-5">&nbsp</div>
							<div class="col-xs-2">
								<input class="greenButton" type="button"
									onclick="document.getElementById('basketForm').submit();return false;"
									value="<spring:message code="label.order" />">
							</div>
							<div class="col-xs-5">&nbsp</div>
					</form:form>
				</c:if>
			</div>
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

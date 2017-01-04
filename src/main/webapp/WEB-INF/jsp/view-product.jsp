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
			<c:set var="product" value="${pp.product}" />
			<div class="col-xs-12 bgw" >
				<div class="row">${product.name}</div>
				<div class=row>
					<div class="col-xs-6">
						<div class=row style="">
							<div class="col-xs-4">
								<a class="prodBoxContBut" onclick="cart.add('${product.id}');">
									<!-- onclick="cart.add('${product.id}',this);" -->
								</a>
							</div>
							<div class="col-xs-4">
								<div style="width: 30px; margin-bottom: 5px;"
									class="topSearchBoxL">
									<div class="topSearchBoxR">
										<input type="text" value="1" id="to_basket[${product.id}]"
											class="prodBoxContQ">
									</div>
								</div>
							</div>
							<div class="col-xs-4 prodBoxContPrice">${product.price}</div>
						</div>
						<div class=row>

							<c:forEach var="stock" items="${product.stocks}">
								<div class="row">
									<div class=col-xs-3>
										<div class="shopProdDetailCell" style="margin: 5 0 5 0px;">
											<c:out value="${stock.quantity}"></c:out>
										</div>
									</div>
									<div class=col-xs-3>
										<div class="shopProdDetailCell" style="margin: 5 0 5 0px;">
											<c:out value="${stock.shipment_type.ship_date}"></c:out>
										</div>
									</div>
									<div class=col-xs-3>
										<div class="shopProdDetailCell" style="margin: 5 0 5 0px;">
											<c:out value="${stock.shop.shop_address}"></c:out>
										</div>
									</div>
									<div class=col-xs-3>
										<div class="shopProdDetailCell" style="margin: 5 0 5 0px;">
											<c:out value="${stock.shop.shop_name}"></c:out>
										</div>
									</div>
								</div>
							</c:forEach>
						</div>
						<div class=row>
							${product.description}<br>
							<c:forEach items="${product.details }" var="detail">
								<div class=row>
									<div class=col-xs-6 style="border: 1px solid #000;">
										<div class="shopProdDetailCell">
											<c:out value="${detail.name }"></c:out>
										</div>
									</div>
									<div class=col-xs-6 style="border: 1px solid #000;">
										<div class="shopProdDetailCell">
											<c:out value="${detail.value }"></c:out>
										</div>
									</div>
								</div>
							</c:forEach>
							<br>
							
							
							<c:if test="${product.type eq 'monitors' }">
								<c:out value="brand" />
								<c:out value="${product.brand}" />
								<br>
								<c:out value="resolution" />
								<c:out value="${product.resolution}" />
								<br>
								<c:out value="screen" />
								<c:out value="${product.screen}" />
								<br>
								<c:out value="Resp_time" />
								<c:out value="${product.resp_time}" />
								<br>
							</c:if>
							<c:if test="${product.type eq 'smartphones' }">
								<c:out value="brand" />
								<c:out value="${product.brand}" />
								<br>
								<c:out value="resolution" />
								<c:out value="${product.resolution}" />
								<br>
								<c:out value="color" />
								<c:out value="${product.color}" />
								<br>
								<c:out value="Battery life" />
								<c:out value="${product.battery_life}" />
								<br>
							</c:if>
							
						</div>
					</div>
					<div class="col-xs-6">
						<a href="${context }/${pp.url }/${product.name}-${product.id}">
							<img class="img-responsive" src="${context}/${product.image }"
							alt="Korea">
						</a>
					</div>
				</div>
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

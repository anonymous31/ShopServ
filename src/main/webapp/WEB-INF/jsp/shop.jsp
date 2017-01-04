<%@page import="ee.ttu.shop.site.Template"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="ee.ttu.shop.product.Product"%>
<%@page import="ee.ttu.shop.catalog.Catalog"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
			<div class="row prodFilterBoxText">
				<c:forEach var="filter" items="${filterBox.filters}">
					<c:out value="${filter.key.name}" />
					<c:forEach var="variant" items="${filter.value.variants}">
						<c:choose>
							<c:when test="${variant.current}">
								<a class="prodFilterBoxTextOn" href="./?${variant.url}">${variant.filter_variant.value}</a>
								&emsp;
							</c:when>
							<c:otherwise>
								<a href="./?${variant.url}">${variant.filter_variant.value}</a>
								&emsp;
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<br>
				</c:forEach>
			</div>
			<div class="row">
				<c:set var="bufInfo">
					<c:if test="${pages!=null}">
						<c:if test="${pages.hasPrevious}">
							&emsp;
							<a class='page-number' href="${context }/${pages.previous}">←</a>
						</c:if>
						<c:if test="${not pages.hasPrevious}">
							&emsp;
							<span class='page-number'>←</span>
						</c:if>

						<c:forEach var="i" items="${pages.pages}">
							<c:if
								test="${(i.index eq 0 or i.index eq pages.pageLinksSize)
					    	and not i.current}">
								<!-- or i.index eq (fn:length(pages.pages)-1)) -->
								<a class='page-number' href="${context }/${i.url}">${i.index + 1}</a>
							</c:if>

						
							<c:if test="${i.current}">
								<strong class='page-number'>${i.index + 1}</strong>
							</c:if>
						</c:forEach>

						<c:if test="${pages.hasNext}">
							<a class='page-number' href="${context }/${pages.next}">→</a>
						</c:if>
						<c:if test="${not pages.hasNext}">
							<span class='page-number'>→</span>
						</c:if>
					</c:if>
				</c:set>
				<c:if test="${not empty bufInfo}">
					<div class="nav">${bufInfo}</div>
				</c:if>

			</div>
			<div class="row">
				<c:forEach var="pp" items="${ppList.pProdsForPage }" varStatus="i">
					<!--<c:if test="${i.count ne 1 and i.count % 3 == 0}">
						</div>
					</c:if>-->
					<c:set var="product" value="${pp.product}" />

					<c:if test="${(i.count-1) % 2 == 0 }">
			</div>
			<div class="row">
				</c:if>

				<div class="col-sm-6">
					<div class="row shadowed shopProdBoxTitleC"
						style="margin: 5 5 0 5px;">
						<c:set var="purl" value="${pp.url }/${product.name}-${product.id}"></c:set>
						<a href='${context }/${purl }'>${product.name}</a>
						<!--  <a href="${pp.url }/${product.name}-${product.id}">${product.name}</a>-->
						<br>
					</div>
					<div class="row shadowed" style="margin: 0 5 0 5px;">
						<div class="col-xs-6 shadowed"
							style="border-width: 1px 1px 0px 1px; box-shadow: 0 0px;">

							
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
						<div class="col-xs-6 shadowed">
							<a href="${context }/${pp.url }/${product.name}-${product.id}">
								<img class="img-responsive" src="${context }/${product.image }"
								alt="Korea">
							</a>
						</div>
					</div>
					<div class="row shadowed shopProdBoxF" style="margin: 0 5 5 5px;">
						<div class="col-xs-8 shopProdBoxPrice" style="padding: 5px;">
							<b><c:out value="${product.price }"></c:out></b>
						</div>
						<div class="col-xs-4">
							<a class=prodBoxContBut
								href="${context }/${pp.url }/${product.name}-${product.id}"></a>
						</div>
					
					</div>
				</div>
				</c:forEach>
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

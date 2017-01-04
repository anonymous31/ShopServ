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

			<div class="table-responsive">
				<table class="table">
					<thread>
					<tr class="ordersList">
						<td class="col-xs-1"><spring:message code="label.invoice" /></td>
						<td class="col-xs-3"><spring:message code="label.date" /></td>
						<td class="col-xs-3"><spring:message code="label.sum" /></td>
						<td class="col-xs-1"><spring:message code="label.orderstatus" /></td>
						<td class="col-xs-4"><spring:message code="label.action" /></td>
					</tr>
					</thread>

					<tbody>

						<c:forEach var="porder" items="${porders }" varStatus="i">
							<c:set var="order" value="${porder.order }" />
							<tr>
								<td style="padding-left: 0px;" class="col-xs-1"><span>
										<a href="${context }/order/${order.id }"><c:out
												value="${order.id }" /></a>
								</span></td>
								<td style="padding-left: 0px;" class="col-xs-3"><siteDir:date
										date="${order.creationDate}" /></td>

								<td style="padding-left: 0px;" class="col-xs-3"><c:out
										value="${porder.subtotal } EUR" /></td>
								<td style="padding-left: 0px;" class="col-xs-1"><c:out
										value="${order.order_status.name }"></c:out></td>
								<td style="padding-left: 0px;" class="col-xs-4"><a
									class="submit" href="${context }/submit/${order.id }/2">Submit</a>
									<a class="submit" href="${context }/submit/${order.id }/3">Reject</a>
								</td>
							</tr>
						</c:forEach>


					</tbody>
				</table>


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

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
			<H1>
				<c:out value="${msg }"/>
			</H1>

			
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

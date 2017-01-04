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
			<H1>Register</H1>

			<form:form modelAttribute="registerDTO" action="${context }/register"
				method="POST" commandName="registerDTO">
				
				<div class="control-group">
					<label for="nick">Nick</label>
					<form:input path="nick" required="required" size="40"
						autofocus="autofocus" />
					<form:errors path="nick" element="span"
						cssClass="error help-inline" for="nick" />
				</div>

				<div class="control-group">
					<label for="email">E-mail</label>
					<form:input path="email" type="email" required="required"
						cssClass="email" size="40" />
					<form:errors path="email" element="span"
						cssClass="error help-inline" for="email" />
				</div>

				<div class="control-group">
					<label for="password">Password</label>
					<form:password path="password" size="40" required="required" />
					<form:errors path="password" element="span"
						cssClass="error help-inline" for="password" />
				</div>

				<div class="control-group">
					<label for="password2">Repeat password</label>
					<form:password path="password2" size="40" required="required" />
					<form:errors path="password2" element="span"
						cssClass="error help-inline" for="password" />
				</div>

				<div class="form-actions">
					<button type=submit class="btn btn-primary">Register</button>
				</div>
			</form:form>
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

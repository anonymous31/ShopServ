<%@page import="ee.ttu.shop.site.Template"%>
<%@page import="java.net.URLEncoder" %>
<%@page import="ee.ttu.shop.product.Product"%>
<%@page import="ee.ttu.shop.catalog.Catalog"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siteDir" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="context" value="${pageContext.request.contextPath}" />

<jsp:include page="/WEB-INF/jsp/head.jsp">
	<jsp:param value="${template }" name="template"/>
</jsp:include>
<title>Shop</title>
</head>
<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>


<div id="bd" class="container-fluid text-center after-header">
	<div class="row content">
		<div class="col-xs-12">
			<h1>Enter</h1>
			
			<c:if test="${param.error == 'true'}">
			    <div class="error">Error. wrong user name or passwd</div>
			</c:if>
			
			<form method=POST action="${context }/login_process">
			  <label style="display:block;">Nick/email:<br><input autofocus autocapitalize="off" type=text name=nick size=40></label>
			  <label style="display:block;">Password:<br><input type=password name=passwd size=40></label>
			  <button type=submit class="btn btn-primary">Enter</button>
			</form>
			
			<div style="font-size: smaller">
			(<a href="${context }/register.jsp">Sign up</a> | <a href="${context }/lostpwd.jsp">Forgot password</a>)
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
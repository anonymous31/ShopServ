<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>


<link rel="stylesheet" type="text/css"
	href="<c:url value="/${template.theme.id}/css/mixed.css"/>">



<c:set var="context" value="${pageContext.request.contextPath}"
	scope="request" />
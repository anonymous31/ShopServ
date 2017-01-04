<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${template != null}">
  <jsp:include page="${template.theme.head}"/>
</c:if>
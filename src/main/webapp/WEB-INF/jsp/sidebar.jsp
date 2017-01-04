<%@page import="ee.ttu.shop.site.Template"%>
<%@page import="ee.ttu.shop.catalog.PreparedCatalogList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.List"%>
<%@page import="ee.ttu.shop.catalog.Catalog"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="context" value="${pageContext.request.contextPath}" />


<div class="row">

	<div class="col-xs-1"></div>
	<div class="col-xs-10 shadowed">
		<div class="well" style="margin-top: 15px;">
			<%!class myWrapper {
		protected boolean found = false;
	}

	public String processChild(Catalog parent, String szOut, myWrapper found, String clicked, String parentUrl,
			String context) {
		for (Catalog child : parent.getChilds()) {
			if (child.getUrlname().equals(clicked)) {
				szOut += "<li ><a style=\"font-weight: bold\" href=\"" + context + "/" + parentUrl + child.getUrlname()
						+ "/\">" + child.getTitle() + "</a>";
			} else {
				szOut += "<li ><a href=\"" + context + "/" + parentUrl + child.getUrlname() + "/\">" + child.getTitle()
						+ "</a>";
			}
			szOut += "<ul style=\"list-style: outside none none;\">";
			if (found.found == false && child.getUrlname().equals(clicked)) {
				found.found = true;
			} else if (found.found) {

			} else {

			}
			szOut += "</ul></li>";
		}
		return szOut;
	}%>
			<%
				PreparedCatalogList prepCatalogList = (PreparedCatalogList) request.getAttribute("prepCatalogList");
				String selected = prepCatalogList.getSelected();
				Template template = (Template) request.getAttribute("template");
				out.print("<ul>");
				
				for (Catalog catalog : prepCatalogList.getCatalogs()) {
					myWrapper found = new myWrapper();
					
					String context = (String) pageContext.getAttribute("context");
					if (catalog.getUrlname().equals(selected)) {
						out.print(
								"<li style=\"list-style-type:square;text-align:left;\"><a style=\"font-weight: bold\"  href=\""
										+ context + "/" + catalog.getUrlname() + "/\">" + catalog.getTitle() + "</a>");
					} else
						out.print("<li style=\"list-style-type:square;text-align:left;\"><a  href=\"" + context + "/"
								+ catalog.getUrlname() + "/\">" + catalog.getTitle() + "</a>");
					out.print("<ul style=\"list-style: outside none none;margin:0px;padding:0px;\">");
					if (catalog.getUrlname().equals(selected)) {
						found.found = true;
					}

					String szOut = "";
					if (selected.length() != 0) {
						szOut = processChild(catalog, szOut, found, selected, "/" + catalog.getUrlname() + "/", context);
					}
					if (found.found)
						out.print(szOut);
					out.print("</ul></li>");

				}
				out.print("</ul>");
			%>
		</div>
	</div>
	<div class="col-xs-1"></div>

</div>

<div class="row">
	<div class="col-xs-12" id="content"></div>
</div>
<div class="row">
	<div class="col-xs-1"></div>
	<div class="col-xs-10 shadowed">
		<div class="well" style="margin-top: 15px;">
			<c:if test="${template.sessionAuthorized}">
				<div class="loginBox logged">
					<div class="logout__but">${template.currentUser.nick}</div>
					<div class="login__content">
						<table style="margin-top: 10px;" border="0" cellspacing="0"
							cellpadding="0" width="100%" id="actions">

							<tbody>
								<tr>
									<td valign="top"><a class="s_btn"
										href="${context }/logout"><spring:message code="label.logout" /></a></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="leftMenuBox">
					<ul id="lmb" class="leftMenuBox1">
						<li class="leftMenuT"><spring:message code="label.menu" /></li>
						<c:if test="${template.emp}">

							<li><a target="_self" href="${context }/unsubmitted/"><spring:message code="label.manageorders" /></a></li>

						</c:if>

						<li><a target="_self" href="${context }/orders/"><spring:message code="label.myorders" /></a></li>

					</ul>
				</div>


			</c:if>
			<c:if test="${not template.sessionAuthorized}">
				<div class="loginBox">
					<div class="b b_sm b_sign-in active"></div>
					<span
						style="margin: 0; padding: 0; border: 0; font-size: 100%; font: inherit; vertical-align: middle;"><spring:message code="label.login" /></span>
					<div class="login__content">
						<form id="loginForm" method=POST
							action="${context }/login_process">
							<label style="display: block;"><spring:message code="label.nick" />:<br> <input
								style="width: 100%;" autofocus autocapitalize="off" type=text
								name=nick size=40></label> <label style="display: block;"><spring:message code="label.pwd" />:<br>
								<input style="width: 100%;" type=password name=passwd size=40></label>
							<!-- <button type=submit class="btn btn-primary">Enter</button>-->
							<a class="s_btn"
								onclick="document.getElementById('loginForm').submit();return false;"
								href="${context }"><spring:message code="label.signin" /></a> <a style="margin-top: 10px;"
								class="s_btn" href="${context }/register"><spring:message code="label.signup" /></a>
						</form>
					</div>
				</div>

			</c:if>
		</div>

	</div>
	<div class="col-xs-1"></div>
</div>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>

<c:set var="context" value="${pageContext.request.contextPath}" />
<body>
	<nav role="navigation" class="navbar navbar-fixed-top">
		<div class="navbar-inverse container-fluid"
			style="background-color: #fff; border-color: #fff;">
			<div class="navbar-header navbar-left pull-left">
				<ul class="nav pull-left">
					<li class="dropdown pull-left"><a class="navbar-brand"
						href="${context}"> <img height="40px" width="40px"
							src="${context}/white/img/x_logo_hd.png" class="theme-icon-home">
					</a></li>
					<li class="dropdown pull-left"><a href="#"
						data-toggle="dropdown" style="color: #777; margin-top: 5px;"
						class="dropdown-toggle"> <span class="icon-bar ib1"
							style="height: 5px; width: 35px;"></span> <span
							class="icon-bar ib2" style="height: 5px; width: 35px;"></span> <span
							class="icon-bar ib3" style="height: 5px; width: 35px;"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a href="${context}" title="Profile"><spring:message
										code="label.contacts" /></a></li>
						</ul></li>
					<li style="position: static;" class="dropdown pull-left"><a
						href="#" data-toggle="dropdown"
						style="color: #777; margin-top: 5px; padding: 13px;"
						class="dropdown-toggle"> <span
							class="glyphicon glyphicon-search"></span>
					</a>
						<ul class="dropdown-menu"
							style="width: 100%; margin: 0px; padding: 0px;">
							<li>
								<form class="navbar-form"
									style="width: 100%; margin: 0px; padding: 0px;">
									<div class="form-group" style="display: inline; width: 100%;">
										<div class="input-group" style="display: table; width: 100%;">
											<input type="text"
												style="width: 100%; font-size: 80px; border: none; opacity: 0.3; padding: 10px 40px;"
												placeholder="Enter text for search" name="s" value=""
												autocomplete="off" autofocus="autofocus" />
										</div>
									</div>
								</form>
							</li>
						</ul></li>
				</ul>
				<button style="background-color: #000;" type="button"
					data-toggle="collapse" data-target=".navbar-collapse"
					class="navbar-toggle">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
			</div>
			<div class="navbar-header navbar-right pull-right">
				<ul class="nav pull-right">
					<li class="dropdown pull-right">
						<div style="background-color: e9eaee;" class="row">
							<div style="" class="col-xs-12">
								<div style="float: left; margin-top: 10px;"
									class="b b_basket2_active b_theme_normal b_sm"></div>
								<div style="margin-left: 30px; margin-right: 0px; width: 180px;">
									<a href="/shopz/checkout/cart">
										<div style="">
											<spring:message code="label.cart" />
											<strong id="totalItms" class="basketBoxInColor">${cart.totalItems}</strong>
											<spring:message code="label.items" />
										</div> <span stlye="white-space:nowrap;"><spring:message
												code="label.totalprice" />: <strong id="totalPrice"
											class="basketBoxInColor">${cart.totalPrice}</strong> EUR </span>
									</a>
								</div>
							</div>

						</div>
					</li>
					<li class="dropdown pull-right" style="background-color: #e9eaee;"><a
						href="#" data-toggle="dropdown"
						style="color: #777; margin-top: 1px;" class="dropdown-toggle">
							<c:set var="langprop" value="${pageContext.response.locale}" /> <c:out
								value="${fn:toUpperCase(langprop) }" /> <span class="caret"></span>
					</a>
						<ul class="dropdown-menu" style="min-width: 50px;">
							<li style="min-width: 50px;"><a href="?lang=ENG"
								title="Profile">ENG</a></li>
							<li style="min-width: 50px;"><a href="?lang=EST"
								title="Profile">EST</a></li>
							<li style="min-width: 50px;"><a href="?lang=RUS"
								title="Profile">RUS</a></li>
						</ul></li>
				</ul>
			</div>
			<div class="visible-xs-block clearfix"></div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav navbar-left">
					<li><a href="/" title="Contact"><i class="fa fa-question"></i>
							<spring:message code="label.contacts" /></a></li>
				</ul>
			</div>
		</div>
	</nav>
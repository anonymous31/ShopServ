package ee.ttu.shop.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ee.ttu.shop.catalog.Catalog;
import ee.ttu.shop.catalog.CatalogPrepareService;
import ee.ttu.shop.catalog.CatalogService;
import ee.ttu.shop.catalog.PreparedCatalogList;
import ee.ttu.shop.filter.AttributeService;
import ee.ttu.shop.product.Product;
import ee.ttu.shop.product.ProductPrepareService;
import ee.ttu.shop.site.Template;
import ee.ttu.shop.util.PageUtil;

@Controller
public class OrderController {

	private static Logger logger = Logger.getLogger(OrderController.class);

	@Autowired
	CatalogService catalogService;

	@Autowired
	CatalogPrepareService catalogPrepareService;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderQueueSender orderQueueSender;

	@Autowired
	OrderPrepareService orderPrepareService;

	@Autowired
	AttributeService attributeService;

	@RequestMapping(value = "/checkout/cart/add", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public CartRespDto addToCard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "product_id", required = true) String prodId,
			@RequestParam(value = "quantity", required = true) String quantity, ModelMap model) {
		CartRespDto resp = orderService.addToCart(request, prodId, quantity);

		return resp;
	}

	@RequestMapping(value = "/checkout/cart/update", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public CartRespDto updateCard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "product_id", required = true) String prodId,
			@RequestParam(value = "quantity", required = true) String quantity, ModelMap model) {
		CartRespDto resp = orderService.updateCart(request, prodId, quantity);

		return resp;
	}

	@RequestMapping(value = "/api/checkout/cart/update", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> updateCardAPI(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "product_id", required = true) String prodId,
			@RequestParam(value = "quantity", required = true) String quantity, ModelMap model) {
		orderService.updateCart(request, prodId, quantity);
		Map<Object, Object> res = new HashMap<>();
		res.put("cart", orderService.getCartInfo(request));
		res.put("cartDetails", orderService.getCart(request));

		return res;
	}

	@RequestMapping(value = {
			"/api/checkout/cart/del" }, produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> delCartItemAPI(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "product_id", required = true) String prodId,
			ModelMap model) throws Exception {
		orderService.deleteCartItem(request, prodId);
		Map<Object, Object> res = new HashMap<>();
		res.put("cart", orderService.getCartInfo(request));
		Cart cart = orderService.getCart(request);
		res.put("cartDetails", cart);

		return res;
	}

	@RequestMapping(value = { "/checkout/cart/del" }, method = RequestMethod.POST)
	public ModelAndView delCartItem(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "product_id", required = true) String prodId, ModelMap model) throws Exception {

		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, "");
		model.addAttribute("prepCatalogList", prepCatalogList);
		orderService.deleteCartItem(request, prodId);
		return new ModelAndView(new RedirectView(request.getContextPath() + "/checkout/cart"));//new ModelAndView("view-cart", model);	
	}

	@RequestMapping(value = { "/checkout/cart" })
	public ModelAndView getCartItems(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, "");
		model.addAttribute("prepCatalogList", prepCatalogList);

		model.addAttribute("basketForm", new OrderDto());
		model.addAttribute("cart", orderService.getCartInfo(request));
		model.addAttribute("cartDetails", orderService.getCart(request));
		return new ModelAndView("view-cart", model);
	}

	@RequestMapping(value = {
			"/api/checkout/cart" }, produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getCartItemsAPI(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception {
		Map<Object, Object> res = new HashMap<>();
		res.put("cart", orderService.getCartInfo(request));
		res.put("cartDetails", orderService.getCart(request));

		return res;
	}

	@RequestMapping(value = {
			"/api/userinfo" }, produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getCartItemsWUserAPI(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception {
		Map<Object, Object> res = new HashMap<>();
		Template tmpl = Template.getTemplate(request);
		res.put("cart", orderService.getCartInfo(request));
		res.put("cartDetails", orderService.getCart(request));
		res.put("user", tmpl.getCurrentUser());
		return res;
	}

	@RequestMapping(value = { "/api/checkout/cart" }, method = RequestMethod.POST)
	@ResponseBody
	public String doOrderAPI(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "first_name", required = true) String first_name,
			@RequestParam(value = "last_name", required = true) String last_name,
			@RequestParam(value = "phone", required = true) String phone,
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "address", required = true) String address,

			ModelMap model) throws Exception {

		Template tmpl = Template.getTemplate(request);

		Order order = new Order();
		order.setFirst_name(first_name);
		order.setLast_name(last_name);
		order.setPhone(phone);
		order.setEmail(email);
		order.setAddress(address);
		int orderId = orderService.createOrder(request, tmpl, order);

		if (orderId != 0) {
			OrderQueueDTO oqdto = new OrderQueueDTO();
			oqdto.setOrderId(orderId);
			orderQueueSender.createOrder(oqdto);
		}

		return "ok";

	}

	@RequestMapping(value = { "/checkout/cart" }, method = RequestMethod.POST)
	public ModelAndView doOrder(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,

			@ModelAttribute("basketForm") OrderDto orderDto, ModelMap model) throws Exception {

		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, "");
		model.addAttribute("prepCatalogList", prepCatalogList);

		Order orderInput = orderDto.getOrderFromDto();
		int orderId = orderService.createOrder(request, tmpl, orderInput);

		model.addAttribute("cart", orderService.getCartInfo(request));

		if (orderId != 0) {
			OrderQueueDTO oqdto = new OrderQueueDTO();
			oqdto.setOrderId(orderId);
			orderService.insertOK(orderId);
			orderQueueSender.createOrder(oqdto);

		}

		return new ModelAndView("order-fin", model);

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = { "/api/orders" })
	@ResponseBody
	public Map<Object, Object> getOrdersAPI(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception {
		Map<Object, Object> res = new HashMap<>();
		Template tmpl = Template.getTemplate(request);

		Map<Integer, List<Order>> oMap = orderService.getOrdersForUser(tmpl, 0, 10500);

		int count = oMap.keySet().iterator().next();
		List<Order> ordersForPage = oMap.get(count);

		List<PreparedOrder> preparedOrders = orderPrepareService.prepareOrders(ordersForPage);

		res.put("porders", preparedOrders);
		res.put("cart", orderService.getCartInfo(request));

		return res;
	};

	public ModelAndView getOrders(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			int page, ModelMap model) throws Exception {

		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, "");
		model.addAttribute("prepCatalogList", prepCatalogList);

		Map<String, String> params = PageUtil.getQueryParameters(request);
		int on_page = attributeService.getOnPage(params, tmpl);

		Map<Integer, List<Order>> oMap = orderService.getOrdersForUser(tmpl, page - 1, on_page);

		if (oMap.keySet().iterator().next() == 0) {
			oMap = orderService.getOrdersForUser(tmpl, 0, on_page);
			page = 1;
		}

		int count = oMap.keySet().iterator().next();
		List<Order> ordersForPage = oMap.get(count);

		List<PreparedOrder> preparedOrders = orderPrepareService.prepareOrders(ordersForPage);

		model.addAttribute("porders", preparedOrders);
		model.addAttribute("cart", orderService.getCartInfo(request));

		int pages = ProductPrepareService.getPageCount(count, on_page);

		if (pages > 1) {
			model.addAttribute("pages", PageUtil.makePages(count, on_page, page, "orders", null, request));
		}

		return new ModelAndView("orders", model);
	};

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = { "/orders/" })
	public ModelAndView getOrdersMain(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		return getOrders(webRequest, request, response, 1, model);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = { "/orders/items-list-{page}/" })
	public ModelAndView getOrdersPage(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("page") int page, ModelMap model) throws Exception {
		return getOrders(webRequest, request, response, page, model);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = { "/api/order/{orderid}" })
	@ResponseBody
	public Map<Object, Object> getOrderByIdAPI(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("orderid") int orderid, ModelMap model) throws Exception {
		Map<Object, Object> res = new HashMap<>();
		Template tmpl = Template.getTemplate(request);

		Order order = orderService.getOrderForUserById(tmpl, orderid);
		res.put("porder", orderPrepareService.prepareOrder(order));
		res.put("cart", orderService.getCartInfo(request));
		return res;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = { "/order/{orderid}" })
	public ModelAndView getOrderById(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("orderid") int orderid, ModelMap model) throws Exception {

		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, "");
		model.addAttribute("prepCatalogList", prepCatalogList);

		Order order = orderService.getOrderForUserById(tmpl, orderid);
		model.addAttribute("porder", orderPrepareService.prepareOrder(order));
		model.addAttribute("cart", orderService.getCartInfo(request));
		return new ModelAndView("order", model);
	}

	@Autowired
	OrderPDFBuilderMail orderPDFBuilderMail;

	@RequestMapping(value = { "/order/{orderid}/pdf" })
	public ModelAndView getOrderPdf(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("orderid") int orderid, ModelMap model) throws Exception {

		Template tmpl = Template.getTemplate(request);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, "");

		model.addAttribute("order", orderService.getOrderForUserById(tmpl, orderid));

		return new ModelAndView("orderView", model);
	}

	public ModelAndView getUnsubmittedOrders(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, int page, ModelMap model) throws Exception {

		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, "");
		model.addAttribute("prepCatalogList", prepCatalogList);

		Map<String, String> params = PageUtil.getQueryParameters(request);
		int on_page = attributeService.getOnPage(params, tmpl);

		Map<Integer, List<Order>> oMap = orderService.getUnsubmittedOrderIdsForPage(page - 1, on_page);
		if (oMap.keySet().iterator().next() == 0) {
			oMap = orderService.getUnsubmittedOrderIdsForPage(0, on_page);
			page = 1;
		}

		int count = oMap.keySet().iterator().next();
		List<Order> ordersForPage = oMap.get(count);

		ordersForPage = orderService.getOrderInfos(ordersForPage);

		List<PreparedOrder> preparedOrders = orderPrepareService.prepareOrders(ordersForPage);
		model.addAttribute("porders", preparedOrders);
		model.addAttribute("cart", orderService.getCartInfo(request));

		int pages = ProductPrepareService.getPageCount(count, on_page);

		if (pages > 1) {
			model.addAttribute("pages", PageUtil.makePages(count, on_page, page, "unsubmitted", null, request));
		}

		return new ModelAndView("unsubmitted", model);

	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@RequestMapping(value = { "/unsubmitted/" })
	public ModelAndView getUnsubmittedOrdersFirst(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception {

		return getUnsubmittedOrders(webRequest, request, response, 1, model);

	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@RequestMapping(value = { "/unsubmitted/items-list-{page}/" })
	public ModelAndView getUnsubmittedOrdersWPage(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("page") int page, ModelMap model) throws Exception {
		return getUnsubmittedOrders(webRequest, request, response, page, model);
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@RequestMapping(value = { "/submit/{orderid}/{param}" })
	public ModelAndView submitOrderById(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("orderid") int orderid, @PathVariable("param") int param, ModelMap model) throws Exception {

		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, "");
		model.addAttribute("prepCatalogList", prepCatalogList);

		logger.info("submitOrderById param " + param);
		orderService.submitOrderById(orderid, param);

		String answer = param == 2 ? "submitted" : "rejected";
		model.addAttribute("msg", "successfully " + answer);
		orderService.remove_OK(orderid);
		return new ModelAndView("system_msg", model);
	}

}

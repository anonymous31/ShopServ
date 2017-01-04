package ee.ttu.shop.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ee.ttu.shop.filter.Filter_variant;
import ee.ttu.shop.product.IProductDao;
import ee.ttu.shop.product.PreparedProduct;
import ee.ttu.shop.product.Product;
import ee.ttu.shop.product.ProductListDto;
import ee.ttu.shop.product.ProductPrepareService;
import ee.ttu.shop.product.ProductService;
import ee.ttu.shop.site.Template;
import ee.ttu.shop.user.User;

@Service
public class OrderService {

	private static Logger logger = Logger.getLogger(OrderService.class);

	@Autowired
	ProductService productService;

	@Autowired
	OrderDao orderDao;

	@Autowired
	@Qualifier("productDao")
	IProductDao iProductDao;

	@Autowired
	ProductPrepareService productPrepareService;

	public CartRespDto getCartInfo(HttpServletRequest request) {
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		CartRespDto resp = new CartRespDto();
		
		if (cart == null) {
			
			cart = new Cart();
		} else {
			resp.setTotalItems(cart.getTotalItems() > 0 ? cart.getTotalItems() : 0);
			resp.setTotalPrice(
					cart.getTotalPrice().compareTo(new BigDecimal(0)) == 1 ? cart.getTotalPrice() : new BigDecimal(0));
		}

		return resp;
	}

	public Cart getCart(HttpServletRequest request) {
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if (cart == null || cart.getItems().size() == 0)
			cart = null;
		return cart;
	}

	public void deleteCartItem(HttpServletRequest request, String prodId) {
		Product product = productService.getProductById(Integer.parseInt(prodId), null);
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if (cart == null)
			cart = new Cart();
		PreparedProduct pp = productPrepareService.prepareProduct(product, Template.getTemplate(request));
		cart.getItems().remove(pp);

		Map<PreparedProduct, ItemProps> items = cart.getItems();
		BigDecimal totalPrice = new BigDecimal(0);
		int totalQty = 0;
		for (PreparedProduct pp1 : items.keySet()) {
			ItemProps prop = items.get(pp1);
			totalPrice = totalPrice.add(prop.getPrice().multiply(new BigDecimal(prop.getQuantity())));
			totalQty += prop.getQuantity();

		}
		cart.setTotalItems(totalQty);
		cart.setTotalPrice(totalPrice);

	}
	
	public void insertOK(int ordId){
		orderDao.insertOK(ordId);
	}
	
	
	
	public void remove_OK(int ordId){
		orderDao.remove_OK(ordId);
	}

	public CartRespDto updateCart(HttpServletRequest request, String prodId, String quantity) {
		Product product = productService.getProductById(Integer.parseInt(prodId), null);
		PreparedProduct pp = productPrepareService.prepareProduct(product, Template.getTemplate(request));
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if (cart == null)
			cart = new Cart();
		Map<PreparedProduct, ItemProps> items = cart.getItems();
		if (!items.containsKey(pp)) {
			items.put(pp, new ItemProps());
		}
		ItemProps props = items.get(pp);
		
		props.setPrice(product.getPrice());
		props.setQuantity(Integer.parseInt(quantity));
		props.setTotal(product.getPrice().multiply(new BigDecimal(Integer.parseInt(quantity))));

		BigDecimal totalPrice = new BigDecimal(0);
		int totalQty = 0;
		for (PreparedProduct pp1 : items.keySet()) {
			ItemProps prop = items.get(pp1);
			totalPrice = totalPrice.add(prop.getPrice().multiply(new BigDecimal(prop.getQuantity())));
			totalQty += prop.getQuantity();

		}

		
		cart.setTotalItems(totalQty);
		cart.setTotalPrice(totalPrice);

		request.getSession().setAttribute("cart", cart);

		CartRespDto resp = new CartRespDto();
		resp.setTotalItems(cart.getTotalItems());
		resp.setTotalPrice(cart.getTotalPrice());
		resp.setRowPrice(product.getPrice().multiply(new BigDecimal(quantity)));

		return resp;
	}

	public CartRespDto addToCart(HttpServletRequest request, String prodId, String quantity) {
		Product product = productService.getProductById(Integer.parseInt(prodId), null);
		PreparedProduct pp = productPrepareService.prepareProduct(product, Template.getTemplate(request));

		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if (cart == null)
			cart = new Cart();
		Map<PreparedProduct, ItemProps> items = cart.getItems();
		if (!items.containsKey(pp)) {
			items.put(pp, new ItemProps());
		}
		ItemProps props = items.get(pp);
		
		props.setPrice((product.getPrice()));
		props.setQuantity(props.getQuantity() + Integer.parseInt(quantity));
		props.setTotal(props.getPrice().multiply(new BigDecimal(props.getQuantity())));
		

		BigDecimal totalPrice = new BigDecimal(0);
		int totalQty = 0;
		for (PreparedProduct pp1 : items.keySet()) {
			ItemProps prop = items.get(pp1);
			totalPrice = totalPrice.add(prop.getPrice().multiply(new BigDecimal(prop.getQuantity())));
		
			totalQty += prop.getQuantity();

		}
		
		cart.setTotalItems(totalQty);
		cart.setTotalPrice(totalPrice);

		request.getSession().setAttribute("cart", cart);

		CartRespDto resp = new CartRespDto();
		resp.setTotalItems(cart.getTotalItems());
		resp.setTotalPrice(cart.getTotalPrice());
		return resp;
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int createOrder(HttpServletRequest request, Template template, Order order) {
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if (cart == null)
			return 0;
		User user = template.getCurrentUser();
		order.setUser(user);

		List<OrderItem> ois = new ArrayList<>();
		Map<PreparedProduct, ItemProps> items = cart.getItems();
		for (PreparedProduct pp : items.keySet()) {
			OrderItem oi = new OrderItem();
			ItemProps prop = items.get(pp);
			oi.setProduct(pp.getProduct());

			oi.setPrice(prop.getPrice());
			oi.setQty(prop.getQuantity());

			ois.add(oi);
		}
		order.setOrderItems(ois);

		request.getSession().setAttribute("cart", null);
		return orderDao.createOrder(order);

	}
	

	public Order getOrderById(int orderId) {
		List<Integer> ids = new ArrayList<>();

		ids.add(orderId);
		List<Order> orders = orderDao.getOrdersByIds(ids);
		for (Order order : orders) {
			if (order.getId() == orderId) {
				List<Product> rgProds = new ArrayList<>();
				for (OrderItem oi : order.getOrderItems()) {

					Product prod = new Product();
					prod.setId(oi.getProduct().getId());
					rgProds.add(prod);
				}
				List<Product> infos = iProductDao.getProductInfo(rgProds, 0);
				for (OrderItem oi : order.getOrderItems()) {
					for (Product prod : infos) {
						if (prod.equals(oi.getProduct())) {
							oi.setProduct(prod);
						}
					}
				}
				return order;
			}
		}
		
		return null;
	}

	public void submitOrderById(int orderId, int param) {
		orderDao.submitOrderById(orderId, param);
	}

	public Order getOrderForUserById(Template template, int orderId) {
		
		List<Integer> ids = new ArrayList<>();;
		Map<Integer, List<Order>> oMap = null;
		if (template.getCurrentUser().isIs_employee()) {
			ids.add(orderId);
		} else {
			int userid = template.getCurrentUser().getId();
			OrderListDTO orderListDto = new OrderListDTO();
			orderListDto.setUserid(userid);
			oMap = orderDao.getOrderIdsByUser(orderListDto);
			int count = oMap.keySet().iterator().next();
			List<Order> ordersForPage = oMap.get(count);
			for (Order o : ordersForPage) {
				ids.add(o.getId());
			}
		}

		List<Order> orders = orderDao.getOrdersByIds(ids);
		for (Order order : orders) {

			if (order.getId() == orderId) {
				List<Product> rgProds = new ArrayList<>();
				for (OrderItem oi : order.getOrderItems()) {

					Product prod = new Product();
					prod.setId(oi.getProduct().getId());
					rgProds.add(prod);
				}
				List<Product> infos = iProductDao.getProductInfo(rgProds, 0);
				for (OrderItem oi : order.getOrderItems()) {
					for (Product prod : infos) {
						if (prod.equals(oi.getProduct())) {
							oi.setProduct(prod);
						}
					}
				}
				return order;
			}

		}

		
		return null;
	}

	public Map<Integer, List<Order>> getUnsubmittedOrderIdsForPage(int page, int ordersPerPage) {
		Map<Integer, List<Order>> oMap = null;

		OrderListDTO orderListDto = new OrderListDTO();
		
		orderListDto.setLmt(ordersPerPage);
		
		orderListDto.setOffset(page * ordersPerPage);

		
		oMap = orderDao.getOrdersByDto(orderListDto);

		return oMap;

	}

	public Map<Integer, List<Order>> getOrdersForUser(Template template,int page, int ordersPerPage) {
		
		Map<Integer, List<Order>> oMap = null;
		OrderListDTO orderListDto = new OrderListDTO();
		orderListDto.setLmt(ordersPerPage);
		orderListDto.setOffset(page * ordersPerPage);
		
		int userid = template.getCurrentUser().getId();
		orderListDto.setUserid(userid);
		
		oMap = orderDao.getOrderIdsByUser(orderListDto);
		int count = oMap.keySet().iterator().next();
		List<Order> orders = new ArrayList<>();
		
		if (count > 0){
			List<Order> ordersForPage = oMap.get(count);
			List<Integer> ids = new ArrayList<>();
			for (Order o : ordersForPage) {
				ids.add(o.getId());
			}
			orders = orderDao.getOrdersByIds(ids);
		}
		
		
		for (Order order : orders) {
			List<Product> rgProds = new ArrayList<>();
			for (OrderItem oi : order.getOrderItems()) {

				Product prod = new Product();
				prod.setId(oi.getProduct().getId());

				rgProds.add(prod);
			}
			List<Product> infos = iProductDao.getProductInfo(rgProds, 0);
			for (OrderItem oi : order.getOrderItems()) {
				for (Product prod : infos) {
					if (prod.equals(oi.getProduct())) {
						oi.setProduct(prod);
					}
				}
			}
		}
		oMap.put(count, orders);
		
		
		return oMap;
	}

	public List<Order> getOrderInfos(List<Order> inputs) {
		List<Integer> ids = new ArrayList<>();
		for (Order o : inputs) {
			ids.add(o.getId());
		}
		List<Order> orders = orderDao.getOrdersByIds(ids);

		
		return orders;
	}
}

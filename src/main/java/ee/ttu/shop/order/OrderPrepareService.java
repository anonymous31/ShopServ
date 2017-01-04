package ee.ttu.shop.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import ee.ttu.shop.catalog.Catalog;
import ee.ttu.shop.product.PreparedProduct;
import ee.ttu.shop.product.Product;
import ee.ttu.shop.product.ProductPrepareService;

@Component
public class OrderPrepareService {

	
public PreparedOrder prepareOrder(Order order) {
	PreparedOrder pOrder = new PreparedOrder();
	pOrder.setOrder(order);
	for (OrderItem oi : order.getOrderItems()) {
		PreparedOrderItem poi = new PreparedOrderItem();
		Product product = oi.getProduct();
		Catalog child = product.getCatalog();
		if (child.getParent() != null) {
			poi.setUrl(child.getParent().getUrlname() + "/" + child.getUrlname());
		} else {
			poi.setUrl(child.getUrlname());
		}
		
		poi.setOrderItem(oi);
		poi.setTotalPrice(oi.getPrice().multiply(new BigDecimal(oi.getQty())));
		pOrder.getPrepOrderItem().add(poi);
		pOrder.setSubtotal(pOrder.getSubtotal().add(oi.getPrice().multiply(new BigDecimal(oi.getQty()))));
		
	}
	return pOrder;
}

public List<PreparedOrder> prepareOrders(List<Order> orders) {
	List<PreparedOrder> poList = new ArrayList<>();
	for (Order order : orders) {
		PreparedOrder pOrder = new PreparedOrder();
		pOrder.setOrder(order);
		for (OrderItem oi : order.getOrderItems()) {
			PreparedOrderItem poi = new PreparedOrderItem();
			Product product = oi.getProduct();
			Catalog child = product.getCatalog();
			if (child.getParent() != null) {
				poi.setUrl(child.getParent().getUrlname() + "/" + child.getUrlname());
			} else {
				poi.setUrl(child.getUrlname());
			}
			
			poi.setOrderItem(oi);
			poi.setTotalPrice(oi.getPrice().multiply(new BigDecimal(oi.getQty())));
			pOrder.getPrepOrderItem().add(poi);
			pOrder.setSubtotal(pOrder.getSubtotal().add(oi.getPrice().multiply(new BigDecimal(oi.getQty()))));
		}
		poList.add(pOrder);
	}
	return poList;
}
}

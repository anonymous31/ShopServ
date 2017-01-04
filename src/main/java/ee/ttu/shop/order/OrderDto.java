package ee.ttu.shop.order;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

public class OrderDto {
	private Map<String, String> order = new HashMap<>();

	public Order getOrderFromDto() {
		Order order = new Order();
		order.setFirst_name(this.order.get("first-name"));
		order.setLast_name(this.order.get("last-name"));
		order.setPhone(this.order.get("phone"));
		order.setEmail(this.order.get("email"));
		order.setAddress(this.order.get("recipient_address"));
		return order;
	}

	public Map<String, String> getOrder() {
		return order;
	}

	public void setOrder(Map<String, String> order) {
		this.order = order;
	}

}

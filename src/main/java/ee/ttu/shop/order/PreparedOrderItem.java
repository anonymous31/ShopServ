package ee.ttu.shop.order;

import java.io.Serializable;
import java.math.BigDecimal;

public class PreparedOrderItem implements Serializable {

	private static final long serialVersionUID = 4687190589460749985L;
	
	private OrderItem orderItem;
	private String url;
	private BigDecimal totalPrice = new BigDecimal(0);
	
	public OrderItem getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
		  return false;
		}

		OrderItem oi = (OrderItem) o;

		return this.orderItem.getProduct().getId()==oi.getProduct().getId();
	}

	@Override
	public int hashCode() {
		return this.orderItem.getProduct().getId();
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
}

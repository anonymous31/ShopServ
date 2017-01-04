package ee.ttu.shop.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

public class PreparedOrder implements Serializable {
	
	private static final long serialVersionUID = 6976140793888571833L;
	private Order order;
	private Set<PreparedOrderItem> prepOrderItem = new LinkedHashSet<>();
	private BigDecimal subtotal = new BigDecimal(0);
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Set<PreparedOrderItem> getPrepOrderItem() {
		return prepOrderItem;
	}
	public void setPrepOrderItem(Set<PreparedOrderItem> prepOrderItem) {
		this.prepOrderItem = prepOrderItem;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	
}

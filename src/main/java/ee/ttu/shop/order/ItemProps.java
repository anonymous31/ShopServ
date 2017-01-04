package ee.ttu.shop.order;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemProps implements Serializable {
	
	private static final long serialVersionUID = 3850409678148247612L;
	
	private BigDecimal price=new BigDecimal(0);
	private int quantity=0;
	private BigDecimal total=new BigDecimal(0);
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	
}

package ee.ttu.shop.order;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartRespDto implements Serializable {
	
	
	private static final long serialVersionUID = 7203407436838511008L;
	
	private BigDecimal totalPrice=new BigDecimal(0);
	private int totalItems;
	private BigDecimal rowPrice = new BigDecimal(0);
	
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	public BigDecimal getRowPrice() {
		return rowPrice;
	}
	public void setRowPrice(BigDecimal rowPrice) {
		this.rowPrice = rowPrice;
	}

}

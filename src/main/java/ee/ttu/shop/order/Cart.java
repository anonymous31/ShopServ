package ee.ttu.shop.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ee.ttu.shop.product.PreparedProduct;
import ee.ttu.shop.product.Product;

public class Cart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1350303494659002041L;

	private BigDecimal totalPrice = new BigDecimal(0);
	private int totalItems;
	private Map<PreparedProduct, ItemProps> items = new HashMap<>();

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

	public Map<PreparedProduct, ItemProps> getItems() {
		return items;
	}

	public void setItems(Map<PreparedProduct, ItemProps> items) {
		this.items = items;
	}

}

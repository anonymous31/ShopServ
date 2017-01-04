package ee.ttu.shop.order;

import java.io.Serializable;
import java.math.BigDecimal;

import ee.ttu.shop.product.PreparedProduct;
import ee.ttu.shop.product.Product;

public class OrderItem implements Serializable {

	
	private static final long serialVersionUID = 6684249639966305439L;

	private BigDecimal price = new BigDecimal(0);
	private int qty;
	private Product product;
	private Order order;
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
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

		return this.product.getId()==oi.getProduct().getId();
	}

	@Override
	public int hashCode() {
		return this.product.getId();
	}
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
}

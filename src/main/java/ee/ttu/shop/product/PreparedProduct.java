package ee.ttu.shop.product;

import ee.ttu.shop.order.OrderItem;

public class PreparedProduct {
	private Product product;
	private String url;
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
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

		PreparedProduct pp = (PreparedProduct) o;

		return this.product.getId()==pp.getProduct().getId();
	}

	@Override
	public int hashCode() {
		return this.product.getId();
	}

}

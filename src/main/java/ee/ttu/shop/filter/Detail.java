package ee.ttu.shop.filter;

import java.io.Serializable;

import ee.ttu.shop.product.Product;

public class Detail implements Serializable {

	private static final long serialVersionUID = -7640651670752170548L;
	private int id;
	private String name;
	private String value;
	private int prodId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Detail detail = (Detail) o;

		return this.id == detail.getId();
	}

	@Override
	public int hashCode() {
		return id;
	}

	public int getProdId() {
		return prodId;
	}

	public void setProdId(int prodId) {
		this.prodId = prodId;
	}
}

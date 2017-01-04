package ee.ttu.shop.filter;

import java.io.Serializable;

public class Filter_variant implements Serializable {

	private static final long serialVersionUID = 3850816502056485010L;
	private int id;
	private String value;
	private String param = "";
	private int orderby;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Filter_variant filter_variant = (Filter_variant) o;

		return this.value.equals(filter_variant.getValue());
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public int getOrderby() {
		return orderby;
	}

	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}
}

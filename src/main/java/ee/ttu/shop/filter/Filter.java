package ee.ttu.shop.filter;

import java.io.Serializable;

public class Filter implements Serializable {

	private static final long serialVersionUID = 596835256105960510L;
	private int id;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

		Filter filter = (Filter) o;

		return this.name.equals(filter.getName());
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}

package ee.ttu.shop.order;

import java.io.Serializable;

public class Order_status implements Serializable {

	
	private static final long serialVersionUID = -5063697949184001108L;
	
	private int id;
	private String name;
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
	
}

package ee.ttu.shop.stock;

import java.io.Serializable;

public class Shipment_type implements Serializable {

	
	private static final long serialVersionUID = 5829103230830801159L;
	private int id;
	private String ship_date;

	public String getShip_date() {
		return ship_date;
	}

	public void setShip_date(String ship_date) {
		this.ship_date = ship_date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

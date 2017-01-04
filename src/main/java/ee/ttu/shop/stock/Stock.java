package ee.ttu.shop.stock;

import java.io.Serializable;

import ee.ttu.shop.product.Product;

public class Stock implements Serializable {

	private static final long serialVersionUID = -2782065696637588341L;
	private int id;
	private int quantity;
	private Shipment_type shipment_type;
	private Shop shop;
	private int prodId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Shipment_type getShipment_type() {
		return shipment_type;
	}

	public void setShipment_type(Shipment_type shipment_type) {
		this.shipment_type = shipment_type;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Stock stock = (Stock) o;

		return this.id == stock.getId();
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

package ee.ttu.shop.stock;

import java.io.Serializable;

public class Shop implements Serializable {

	
	private static final long serialVersionUID = -7982120621298858509L;
	private  int id;
	private String shop_address;
	private String shop_name;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShop_address() {
		return shop_address;
	}
	public void setShop_address(String shop_address) {
		this.shop_address = shop_address;
	}
	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
}

package ee.ttu.shop.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ee.ttu.shop.user.User;

public class Order implements Serializable {

	private static final long serialVersionUID = -8281287513065091365L;

	public static int OrderItemLimit = 3;

	private int id;
	private Timestamp creationDate;
	private String first_name;
	private String last_name;
	private String phone;
	private String email;
	private String address;

	private User user;
	private Order_status order_status;
	private List<OrderItem> orderItems = new ArrayList<>();

	public Order() {
		order_status = new Order_status();
		order_status.setId(1);
		order_status.setName("Unpaid");
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Order_status getOrder_status() {
		return order_status;
	}

	public void setOrder_status(Order_status order_status) {
		this.order_status = order_status;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

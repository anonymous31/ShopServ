package ee.ttu.shop.order;

import java.io.Serializable;

public class OrderQueueDTO implements Serializable{
	
	private static final long serialVersionUID = -5782014243205328325L;
	private int orderId;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

}

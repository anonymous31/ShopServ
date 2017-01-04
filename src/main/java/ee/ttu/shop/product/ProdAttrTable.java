package ee.ttu.shop.product;

import java.io.Serializable;

public class ProdAttrTable implements Serializable {

	private static final long serialVersionUID = -3423911110779708170L;
	
	private int prod_fk;
	private int attribute_fk;
	private String name;
	private int attribute_value_fk;
	private String attributeValue;
	private int orderby;
	
	
	public int getProd_fk() {
		return prod_fk;
	}
	public void setProd_fk(int prod_fk) {
		this.prod_fk = prod_fk;
	}
	public int getAttribute_fk() {
		return attribute_fk;
	}
	public void setAttribute_fk(int attribute_fk) {
		this.attribute_fk = attribute_fk;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAttribute_value_fk() {
		return attribute_value_fk;
	}
	public void setAttribute_value_fk(int attribute_value_fk) {
		this.attribute_value_fk = attribute_value_fk;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	public int getOrderby() {
		return orderby;
	}
	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}
	
	
	
	
	
	
}

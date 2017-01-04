package ee.ttu.shop.order;

public class OrderListDTO {
	
	private int offset = 0;
	private Integer lmt =null;
	private Integer userid=null;
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public Integer getLmt() {
		return lmt;
	}
	public void setLmt(Integer lmt) {
		this.lmt = lmt;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}

}

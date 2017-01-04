package ee.ttu.shop.site;


import ee.ttu.shop.site.Theme;

public class DefaultProfile {

	private int on_page;
	private int price_order;
	
	private DefaultProfile(int page,int price) {
		this.on_page=page;
		this.price_order=price;
	}
	
	public static DefaultProfile getDefaultProfile() {
		return new DefaultProfile(5,0);
	}
	
	public static Theme getDefaultTheme() {
		return Theme.THEMES.get(0);
	}

	public int getOn_page() {
		return on_page;
	}

	public void setOn_page(int on_page) {
		this.on_page = on_page;
	}

	public int getPrice_order() {
		return price_order;
	}

	public void setPrice_order(int price_order) {
		this.price_order = price_order;
	}

}

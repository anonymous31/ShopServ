package ee.ttu.shop.product;

import java.util.ArrayList;
import java.util.List;

import ee.ttu.shop.product.Product;

public class PreparedProductList {

	private List<PreparedProduct> pProdsForPage = new ArrayList<>();

	private Integer totalProds;

	public Integer getTotalProds() {
		return totalProds;
	}

	public void setTotalProds(Integer totalProds) {
		this.totalProds = totalProds;
	}

	public List<PreparedProduct> getpProdsForPage() {
		return pProdsForPage;
	}

	public void setpProdsForPage(List<PreparedProduct> pProdsForPage) {
		this.pProdsForPage = pProdsForPage;
	}

}

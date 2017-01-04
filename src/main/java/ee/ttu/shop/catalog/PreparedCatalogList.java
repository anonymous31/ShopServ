package ee.ttu.shop.catalog;

import java.util.List;

import ee.ttu.shop.catalog.Catalog;

public class PreparedCatalogList {

	private List<Catalog> catalogs;
	private String selected;

	public List<Catalog> getCatalogs() {
		return catalogs;
	}

	public void setCatalogs(List<Catalog> catalogs) {
		this.catalogs = catalogs;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

}

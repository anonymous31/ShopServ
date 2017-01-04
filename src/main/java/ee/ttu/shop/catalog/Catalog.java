package ee.ttu.shop.catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ee.ttu.shop.catalog.Catalog;

public class Catalog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9092077299698027228L;

	private int id;
	private String title;
	private String urlname;
	private String image;

	private int catalog_level;
	private List<Catalog> childs = new ArrayList<>();
	private Catalog parent;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCatalog_level() {
		return catalog_level;
	}

	public void setCatalog_level(int catalog_level) {
		this.catalog_level = catalog_level;
	}

	public List<Catalog> getChilds() {
		return childs;
	}

	public void setChilds(List<Catalog> childs) {
		this.childs = childs;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrlname() {
		return urlname;
	}

	public void setUrlname(String urlname) {
		this.urlname = urlname;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Catalog getParent() {
		return parent;
	}

	public void setParent(Catalog parent) {
		this.parent = parent;
	}
}

package ee.ttu.shop.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ee.ttu.shop.catalog.Catalog;
import ee.ttu.shop.filter.Detail;
import ee.ttu.shop.filter.Filter;
import ee.ttu.shop.filter.Filter_variant;
import ee.ttu.shop.stock.Stock;

public class Product implements Serializable {

	private static final long serialVersionUID = 6941039034344973532L;

	private int id;
	private String name;
	private String description;
	private String image;
	private Catalog catalog;
	private BigDecimal price = new BigDecimal(0);
	private String brand;
	private String resolution;
	private String screen;
	private String Resp_time;
	private String color;
	private String Battery_life;
	private String type;
	private Filter_variant brand_fk;
	private Filter_variant resolution_fk;
	private Filter_variant screen_fk;
	private Filter_variant Resp_time_fk;
	private Filter_variant color_fk;
	private Filter_variant Battery_life_fk;

	private Map<String, String> attrs = new LinkedHashMap<>();

	private Set<Stock> stocks = new LinkedHashSet<>();

	private Set<Detail> details = new LinkedHashSet<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Product product = (Product) o;

		return this.id == product.getId();
	}

	@Override
	public int hashCode() {
		return id;
	}

	public Set<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(Set<Stock> stocks) {
		this.stocks = stocks;
	}

	public Set<Detail> getDetails() {
		return details;
	}

	public void setDetails(Set<Detail> details) {
		this.details = details;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getResp_time() {
		return Resp_time;
	}

	public void setResp_time(String resp_time) {
		Resp_time = resp_time;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Map<String, String> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, String> attrs) {
		this.attrs = attrs;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Filter_variant getBrand_fk() {
		return brand_fk;
	}

	public void setBrand_fk(Filter_variant brand_fk) {
		this.brand_fk = brand_fk;
	}

	public Filter_variant getResolution_fk() {
		return resolution_fk;
	}

	public void setResolution_fk(Filter_variant resolution_fk) {
		this.resolution_fk = resolution_fk;
	}

	public Filter_variant getScreen_fk() {
		return screen_fk;
	}

	public void setScreen_fk(Filter_variant screen_fk) {
		this.screen_fk = screen_fk;
	}

	public Filter_variant getResp_time_fk() {
		return Resp_time_fk;
	}

	public void setResp_time_fk(Filter_variant resp_time_fk) {
		Resp_time_fk = resp_time_fk;
	}

	public Filter_variant getColor_fk() {
		return color_fk;
	}

	public void setColor_fk(Filter_variant color_fk) {
		this.color_fk = color_fk;
	}

	public String getBattery_life() {
		return Battery_life;
	}

	public void setBattery_life(String battery_life) {
		Battery_life = battery_life;
	}

	public Filter_variant getBattery_life_fk() {
		return Battery_life_fk;
	}

	public void setBattery_life_fk(Filter_variant battery_life_fk) {
		Battery_life_fk = battery_life_fk;
	}
}

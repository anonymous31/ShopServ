package ee.ttu.shop.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import ee.ttu.shop.catalog.Catalog;
import ee.ttu.shop.site.Template;

@Component
public class ProductPrepareService {

	private static Logger logger = Logger.getLogger(ProductPrepareService.class);

	public PreparedProduct prepareProduct(Product prod, Template template) {
		prod.setImage(template.getTheme().getId() + "/" + prod.getImage());
		PreparedProduct pp = new PreparedProduct();
		Catalog child = prod.getCatalog();
		if (child.getParent() != null) {
			pp.setUrl(child.getParent().getUrlname() + "/" + child.getUrlname());
		} else {
			pp.setUrl(child.getUrlname());
		}
		pp.setProduct(prod);
		return pp;
	}

	public PreparedProductList prepareProduct(List<Product> prodsForPage, Integer count, Template template) {
		PreparedProductList ppList = new PreparedProductList();
		ppList.setTotalProds(count);
		for (Product prod : prodsForPage) {
			prod.setImage(template.getTheme().getId() + "/" + prod.getImage());
			PreparedProduct pp = new PreparedProduct();
			Catalog child = prod.getCatalog();
			if (child.getParent() != null) {
				pp.setUrl(child.getParent().getUrlname() + "/" + child.getUrlname());
			} else {
				pp.setUrl(child.getUrlname());
			}
			pp.setProduct(prod);
			ppList.getpProdsForPage().add(pp);
		}
		return ppList;
	}

	public static int getPageCount(int prodAmount, int productPerPage) {
		return (int) Math.ceil((int) prodAmount / ((double) productPerPage));
	}

}

package ee.ttu.shop.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ee.ttu.shop.product.IProductDao;
import ee.ttu.shop.site.Template;

@Service
public class AttributeService {

	@Autowired
	@Qualifier("productDao")
	IProductDao productDao;

	public Integer getPriceOrder(Map<String, String> params, Template template) {
		Integer priceOrder = null;
		if (params.containsKey("price")) {
			priceOrder = Integer.valueOf(params.get("price"));
		}
		if (priceOrder != null && !Arrays.asList(0, 1).contains(priceOrder)) {
			priceOrder = template.getDefaultProfile().getPrice_order();
		}
		return priceOrder;
	}

	public int getOnPage(Map<String, String> params, Template template) {
		Integer on_page = 0;
		if (params.containsKey("on_page")) {
			on_page = Integer.valueOf(params.get("on_page"));
			params.remove("on_page");
		}
		if (!Arrays.asList(5, 10, 25, 50, 100, 150, 500, 1000).contains(on_page)) {
			on_page = template.getDefaultProfile().getOn_page();
		}
		return on_page;
	}

	public Integer getOffset(Map<String, String> params, Template template) {
		Integer offset = null;
		if (params.containsKey("offset")) {
			offset = Integer.valueOf(params.get("offset"));
			params.remove("offset");
		}
		return offset;
	}

	public Map<Filter, List<Filter_variant>> getAttrsByCatalog(int catalog, HttpServletRequest request) {

		Map<Filter, List<Filter_variant>> attrs = new LinkedHashMap<>();

		attrs.putAll(productDao.getAttrsByCatalog(catalog));

		{
			Filter filter = new Filter();
			filter.setId(9998);
			filter.setName("price");
			Filter_variant filter_variant2 = new Filter_variant();
			filter_variant2.setId(3);
			filter_variant2.setValue("all");
			Filter_variant filter_variant = new Filter_variant();
			filter_variant.setId(1);
			filter_variant.setValue("cheaper first");
			filter_variant.setParam("0");
			Filter_variant filter_variant1 = new Filter_variant();
			filter_variant1.setId(2);
			filter_variant1.setValue("expensive first");
			filter_variant1.setParam("1");

			if (!attrs.containsKey(filter)) {
				attrs.put(filter, new ArrayList<>());
			}
			List<Filter_variant> variantList = attrs.get(filter);
			variantList.add(filter_variant2);
			variantList.add(filter_variant);
			variantList.add(filter_variant1);
		}

		{
			Filter filter = new Filter();
			filter.setId(9998);
			filter.setName("on_page");
			Filter_variant filter_variant = new Filter_variant();
			filter_variant.setId(1);
			filter_variant.setValue("5");
			Filter_variant filter_variant1 = new Filter_variant();
			filter_variant1.setId(2);
			filter_variant1.setValue("10");
			Filter_variant filter_variant2 = new Filter_variant();
			filter_variant2.setId(3);
			filter_variant2.setValue("25");
			Filter_variant filter_variant3 = new Filter_variant();
			filter_variant3.setId(4);
			filter_variant3.setValue("50");
			Filter_variant filter_variant4 = new Filter_variant();
			filter_variant4.setId(5);
			filter_variant4.setValue("100");
			Filter_variant filter_variant5 = new Filter_variant();
			filter_variant5.setId(6);
			filter_variant5.setValue("150");
			Filter_variant filter_variant6 = new Filter_variant();
			filter_variant6.setId(7);
			filter_variant6.setValue("500");
			Filter_variant filter_variant7 = new Filter_variant();
			filter_variant7.setId(8);
			filter_variant7.setValue("1000");

			if (!attrs.containsKey(filter)) {
				attrs.put(filter, new ArrayList<>());
			}
			List<Filter_variant> variantList = attrs.get(filter);
			variantList.add(filter_variant);
			variantList.add(filter_variant1);
			variantList.add(filter_variant2);
			variantList.add(filter_variant3);
			variantList.add(filter_variant4);
			variantList.add(filter_variant5);
			variantList.add(filter_variant6);
			variantList.add(filter_variant7);
		}

		return attrs;
	}

}

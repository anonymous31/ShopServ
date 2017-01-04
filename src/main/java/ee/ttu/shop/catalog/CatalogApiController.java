package ee.ttu.shop.catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ee.ttu.shop.filter.AttributeService;
import ee.ttu.shop.filter.Filter;
import ee.ttu.shop.filter.FilterBox;
import ee.ttu.shop.filter.Filter_variant;
import ee.ttu.shop.order.CartRespDto;
import ee.ttu.shop.order.OrderService;
import ee.ttu.shop.product.Product;
import ee.ttu.shop.product.ProductPrepareService;
import ee.ttu.shop.product.ProductService;
import ee.ttu.shop.site.Template;
import ee.ttu.shop.util.PageUtil;

@Controller
public class CatalogApiController {

	private static Logger logger = Logger.getLogger(CatalogController.class);

	@Autowired
	CatalogService catalogService;

	@Autowired
	CatalogPrepareService catalogPrepareService;

	@Autowired
	ProductService productService;

	@Autowired
	AttributeService attributeService;

	@Autowired
	OrderService orderService;

	@Autowired
	ProductPrepareService productPrepareService;

	@RequestMapping(value = "/api/categories/", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getMainCatalogs(HttpServletRequest request) {
		Map<Object, Object> res = new HashMap<>();

		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, "");
		res.put("prepCtlgs", prepCatalogList);

		return res;
	}

	@RequestMapping(value = "/api/cartinfo/", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public CartRespDto getMainCart(HttpServletRequest request) {
		Map<Object, Object> res = new HashMap<>();
		CartRespDto crespDTO = orderService.getCartInfo(request);
		return crespDTO;
	}

	@RequestMapping(value = "/api/category/{selected}/categories/", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getCatalogsChilds(HttpServletRequest request,
			@PathVariable("selected") String selected) {
		Map<Object, Object> res = new HashMap<>();
		List<Catalog> catalogs = catalogService.getCatalogList();
		catalogs = catalogService.getChildsOfSelected(selected, catalogs);
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, selected);
		res.put("childCtlgs", prepCatalogList);
		return res;
	}

	public Map<Object, Object> getProductByCatalogRoot(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, String selected, int page) throws Exception {
		Map<Object, Object> res = new HashMap<>();
		Template tmpl = Template.getTemplate(request);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, selected);
		res.put("prepCtlgs", prepCatalogList);
		CartRespDto crespDTO = orderService.getCartInfo(request);
		res.put("cartInfo", crespDTO);

		int catalogId = catalogService.getIdByName(selected);
		Map<String, String> params = PageUtil.getQueryParameters(request);
		int on_page = attributeService.getOnPage(params, tmpl);
		Integer price_order = attributeService.getPriceOrder(params, tmpl);

		Map<Integer, List<Product>> pMap = null;
		Integer count = null;

		if (params.size() == 0) {
			pMap = productService.getProductsByCatalogForPage(catalogId, page - 1, on_page);
			if (pMap.keySet().iterator().next() == 0) {
				pMap = productService.getProductsByCatalogForPage(catalogId, 0, on_page);
				if (pMap.keySet().iterator().next() == 0) {
					pMap = productService.getProductsFirstRun(catalogId);
					pMap = productService.getProductsByCatalogForPage(catalogId, 0, on_page);
				}
				page = 1;
			}
		} else {
			pMap = productService.getProductsByParamsForPage(catalogId, params, price_order, page - 1, on_page);
			if (pMap.keySet().iterator().next() == 0) {
				pMap = productService.getProductsByParamsForPage(catalogId, params, price_order, 0, on_page);
				page = 1;
			}
		}

		count = pMap.keySet().iterator().next();
		List<Product> productsForPage = pMap.get(count);
		if (productsForPage.size() != 0 || params.size() != 0) {
			Map<Filter, List<Filter_variant>> filters = attributeService.getAttrsByCatalog(catalogId, request);
			res.put("filterBox", new FilterBox(filters, request));
		}

		productsForPage = productService.getProductsInfo(productsForPage, price_order);

		res.put("ppList", productPrepareService.prepareProduct(productsForPage, count, tmpl));

		int pages = ProductPrepareService.getPageCount(count.intValue(), on_page);

		res.put("pages", PageUtil.makePages(count.intValue(), on_page, page, selected, null, request));

		return res;
	}

	@RequestMapping(value = {
			"/api/{selected}/" }, produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getProductByCatalog(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("selected") String selected) throws Exception {
		Map<Object, Object> res = getProductByCatalogRoot(webRequest, request, response, selected, 1);
		return res;
	}

	@RequestMapping(value = {
			"/api/{selected}/items-list-{page}/" }, produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getProductByCatalog(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("selected") String selected, @PathVariable("page") int page)
			throws Exception {
		return getProductByCatalogRoot(webRequest, request, response, selected, page);
	}

	public Map<Object, Object> getProductByCatalogInner(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, String selected, String level1, int page) throws Exception {
		Map<Object, Object> res = new HashMap<>();
		Template tmpl = Template.getTemplate(request);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, selected);
		res.put("prepCatalogList", prepCatalogList);

		int catalogId = catalogService.getIdByName(selected);
		Map<String, String> params = PageUtil.getQueryParameters(request);
		int on_page = attributeService.getOnPage(params, tmpl);
		Integer offset = attributeService.getOffset(params, tmpl);
		Integer price_order = attributeService.getPriceOrder(params, tmpl);

		Map<Integer, List<Product>> pMap = null;
		Integer count = null;
		if (params.size() == 0) {
			pMap = productService.getProductsByCatalogForPage(catalogId, page - 1, on_page);
			if (pMap.keySet().iterator().next() == 0) {
				pMap = productService.getProductsByCatalogForPage(catalogId, 0, on_page);
				if (pMap.keySet().iterator().next() == 0) {
					pMap = productService.getProductsFirstRun(catalogId);
					pMap = productService.getProductsByCatalogForPage(catalogId, 0, on_page);
				}
				page = 1;
			}
		} else {
			pMap = productService.getProductsByParamsForPage(catalogId, params, price_order, page - 1, on_page);
			if (pMap.keySet().iterator().next() == 0) {
				pMap = productService.getProductsByParamsForPage(catalogId, params, price_order, 0, on_page);
				page = 1;
			}
		}

		count = pMap.keySet().iterator().next();
		List<Product> productsForPage = pMap.get(count);
		if (productsForPage.size() != 0 || params.size() != 0) {
			Map<Filter, List<Filter_variant>> filters = attributeService.getAttrsByCatalog(catalogId, request);
			res.put("filterBox", new FilterBox(filters, request));
		}

		productsForPage = productService.getProductsInfo(productsForPage, price_order);

		res.put("cart", orderService.getCartInfo(request));

		res.put("ppList", productPrepareService.prepareProduct(productsForPage, count, tmpl));

		int pages = ProductPrepareService.getPageCount(count.intValue(), on_page);
		if (pages > 1) {
			res.put("pages", PageUtil.makePages(count.intValue(), on_page, page, selected, null, request));
		}

		return res;
	}

	@RequestMapping(value = {
			"/api/{level1}/{selected}/" }, produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getProductByCatalog(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("selected") String selected,
			@PathVariable("level1") String level1, ModelMap model) throws Exception {
		return getProductByCatalogInner(webRequest, request, response, selected, level1, 1);
	}

}

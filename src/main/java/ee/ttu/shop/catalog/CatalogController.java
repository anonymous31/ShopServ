package ee.ttu.shop.catalog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import ee.ttu.shop.filter.AttributeService;
import ee.ttu.shop.filter.Filter;
import ee.ttu.shop.filter.FilterBox;
import ee.ttu.shop.filter.Filter_variant;
import ee.ttu.shop.order.OrderService;
import ee.ttu.shop.peginator.PagesInfo;
import ee.ttu.shop.product.PreparedProductList;
import ee.ttu.shop.product.PreparedProduct;
import ee.ttu.shop.product.Product;
import ee.ttu.shop.product.ProductFilter;
import ee.ttu.shop.product.ProductListDto;
import ee.ttu.shop.product.ProductPrepareService;
import ee.ttu.shop.product.ProductService;
import ee.ttu.shop.site.Template;
import ee.ttu.shop.util.PageUtil;

@Controller
public class CatalogController {

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

	@RequestMapping("/")
	public ModelAndView getShopMain(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, "");
		model.addAttribute("prepCatalogList", prepCatalogList);

		model.addAttribute("cart", orderService.getCartInfo(request));

		return new ModelAndView("shop", model);
	}

	public ModelAndView getProductByCatalogRoot(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, String selected, int page, ModelMap model) throws Exception {

		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();

		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, selected);
		model.addAttribute("prepCatalogList", prepCatalogList);

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

		if (pMap.keySet().iterator().next() != 0 || params.size() != 0) {
			Map<Filter, List<Filter_variant>> filters = attributeService.getAttrsByCatalog(catalogId, request);
			model.addAttribute("filterBox", new FilterBox(filters, request));
		}

		productsForPage = productService.getProductsInfo(productsForPage, price_order);

		model.addAttribute("cart", orderService.getCartInfo(request));

		model.addAttribute("ppList", productPrepareService.prepareProduct(productsForPage, count, tmpl));

		int pages = ProductPrepareService.getPageCount(count.intValue(), on_page);

		if (pages > 1) {
			model.addAttribute("pages", PageUtil.makePages(count.intValue(), on_page, page, selected, null, request));
		}
		return new ModelAndView("shop", model);
	}

	@RequestMapping(value = { "/{selected}/" })
	public ModelAndView getProductByCatalog(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("selected") String selected, ModelMap model) throws Exception {
		return getProductByCatalogRoot(webRequest, request, response, selected, 1, model);
	}

	@RequestMapping(value = { "/{selected}/items-list-{page}/" })
	public ModelAndView getProductByCatalog(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("selected") String selected, @PathVariable("page") int page,
			ModelMap model) throws Exception {
		return getProductByCatalogRoot(webRequest, request, response, selected, page, model);
	}

	public ModelAndView getProductByCatalogInner(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, String selected, String level1, int page, ModelMap model) throws Exception {
		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, selected);
		model.addAttribute("prepCatalogList", prepCatalogList);

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
			model.addAttribute("filterBox", new FilterBox(filters, request));
		}

		productsForPage = productService.getProductsInfo(productsForPage, price_order);

		model.addAttribute("cart", orderService.getCartInfo(request));
		model.addAttribute("ppList", productPrepareService.prepareProduct(productsForPage, count, tmpl));

		int pages = ProductPrepareService.getPageCount(count.intValue(), on_page);
		if (pages > 1) {
			model.addAttribute("pages", PageUtil.makePages(count.intValue(), on_page, page, selected, null, request));
		}

		return new ModelAndView("shop", model);
	}

	@RequestMapping(value = { "/{level1}/{selected}/items-list-{page}/" })
	public ModelAndView getProductByCatalog(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("selected") String selected,
			@PathVariable("level1") String level1, @PathVariable("page") Integer page, ModelMap model)
			throws Exception {
		return getProductByCatalogInner(webRequest, request, response, selected, level1, page, model);
	}

	@RequestMapping(value = { "/{level1}/{selected}/" })
	public ModelAndView getProductByCatalog(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("selected") String selected,
			@PathVariable("level1") String level1, ModelMap model) throws Exception {
		return getProductByCatalogInner(webRequest, request, response, selected, level1, 1, model);
	}

}

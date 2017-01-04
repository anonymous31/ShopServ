package ee.ttu.shop.product;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import ee.ttu.shop.catalog.Catalog;
import ee.ttu.shop.catalog.CatalogPrepareService;
import ee.ttu.shop.catalog.CatalogService;
import ee.ttu.shop.catalog.PreparedCatalogList;
import ee.ttu.shop.order.OrderService;
import ee.ttu.shop.site.Template;

@Controller
public class ProductApiController {
	
	
	@Autowired
	CatalogService catalogService;

	@Autowired
	ProductService productService;

	@Autowired
	CatalogPrepareService catalogPrepareService;

	@Autowired
	ProductPrepareService productPrepareService;
	
	@Autowired
	OrderService orderService;
	
	@RequestMapping(value={"/api/{selected}/{name:.+}-{prodid}"})
	@ResponseBody
	public Map<Object, Object> getProductById(	
			WebRequest webRequest,
			HttpServletRequest request,
			HttpServletResponse response,
		    @PathVariable("selected") String selected,
		    @PathVariable("prodid") int prodid,
		    ModelMap model
			) throws Exception{
		return getProductByIdNew(webRequest,request,response,selected,prodid,"",model);
	}

	@RequestMapping(value={"/api/{level1}/{selected}/{name:.+}-{prodid}"})
	@ResponseBody
	public Map<Object, Object> getProductById(	
			WebRequest webRequest,
			HttpServletRequest request,
			HttpServletResponse response,
		    @PathVariable("selected") String selected,
		    @PathVariable("prodid") int prodid,
		    @PathVariable("level1") String level1,
		    ModelMap model
			) throws Exception{
		return getProductByIdNew(webRequest,request,response,selected,prodid,level1,model);
	}

	public Map<Object, Object> getProductByIdNew(
			WebRequest webRequest,
			HttpServletRequest request,
			HttpServletResponse response,
		    @PathVariable("selected") String selected,
		    @PathVariable("prodid") int prodid,
		    @PathVariable("level1") String level1,
		    ModelMap model) throws Exception{
		Map<Object, Object> res = new HashMap<>();
		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList preparedCatalogList = catalogPrepareService.prepareCatalogList(catalogs, selected);
		res.put("prepCatalogList", preparedCatalogList);
		Product product = productService.getProductById(prodid,null);
		

		res.put("pp", productPrepareService.prepareProduct(product,tmpl));
		res.put("cart", orderService.getCartInfo(request));
		
		return res;
	}

}

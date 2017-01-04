package ee.ttu.shop.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import ee.ttu.shop.catalog.Catalog;
import ee.ttu.shop.catalog.CatalogPrepareService;
import ee.ttu.shop.catalog.CatalogService;
import ee.ttu.shop.catalog.PreparedCatalogList;
import ee.ttu.shop.order.OrderService;
import ee.ttu.shop.site.Template;

@Controller
public class ProductController {

	
	private static Logger logger =  Logger.getLogger(ProductController.class);
	
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
	

	@RequestMapping(value={"/{selected}/{name:.+}-{prodid}"})
	public ModelAndView getProductById(	
			WebRequest webRequest,
			HttpServletRequest request,
			HttpServletResponse response,
		    @PathVariable("selected") String selected,
		    @PathVariable("prodid") int prodid,
		    ModelMap model
			) throws Exception{
		return getProductByIdNew(webRequest,request,response,selected,prodid,"",model);
	}

	@RequestMapping(value={"/{level1}/{selected}/{name:.+}-{prodid}"})
	public ModelAndView getProductById(	
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

	public ModelAndView getProductByIdNew(
			WebRequest webRequest,
			HttpServletRequest request,
			HttpServletResponse response,
		    @PathVariable("selected") String selected,
		    @PathVariable("prodid") int prodid,
		    @PathVariable("level1") String level1,
		    ModelMap model) throws Exception{
		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList preparedCatalogList = catalogPrepareService.prepareCatalogList(catalogs, selected);
		model.addAttribute("prepCatalogList", preparedCatalogList);
		Product product = productService.getProductById(prodid,null);
	
		model.addAttribute("pp", productPrepareService.prepareProduct(product,tmpl));
		model.addAttribute("cart", orderService.getCartInfo(request));
		
		return new ModelAndView("view-product", model);
	}
	
}

package ee.ttu.shop.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;

import ee.ttu.shop.catalog.Catalog;
import ee.ttu.shop.catalog.CatalogController;
import ee.ttu.shop.catalog.CatalogPrepareService;
import ee.ttu.shop.catalog.CatalogService;
import ee.ttu.shop.catalog.PreparedCatalogList;
import ee.ttu.shop.order.Order;
import ee.ttu.shop.order.OrderDto;
import ee.ttu.shop.order.OrderQueueDTO;
import ee.ttu.shop.order.OrderService;
import ee.ttu.shop.site.Template;

@Controller
public class UserController {

	private static Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	CatalogService catalogService;

	@Autowired
	CatalogPrepareService catalogPrepareService;

	@Autowired
	OrderService orderService;

	@Autowired
	RegisterValidator registerValidator;

	@Autowired
	RegisterService registerService;

	@RequestMapping("/register")
	public ModelAndView getShopMain(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, "");
		model.addAttribute("prepCatalogList", prepCatalogList);

		model.addAttribute("registerDTO", new RegisterDTO());
		model.addAttribute("cart", orderService.getCartInfo(request));
		return new ModelAndView("register", model);
	}

	@RequestMapping(value = { "/register" }, method = RequestMethod.POST)
	public ModelAndView doOrder(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("registerDTO") RegisterDTO registerDTO, BindingResult errors, ModelMap model)
			throws Exception {

		Template tmpl = Template.getTemplate(request);
		model.addAttribute("template", tmpl);
		List<Catalog> catalogs = catalogService.getCatalogList();
		PreparedCatalogList prepCatalogList = catalogPrepareService.prepareCatalogList(catalogs, "");
		model.addAttribute("prepCatalogList", prepCatalogList);

		model.addAttribute("cart", orderService.getCartInfo(request));

		registerValidator.validateRegForm(registerDTO, errors);
		if (errors.hasErrors()) {
			model.addAttribute("errors", errors);
			model.addAttribute("registerDTO", registerDTO);
			return new ModelAndView("register", "model", model);
		}
		int userid = registerService.createUser(request, tmpl, registerDTO.getUser());

		return new ModelAndView(new RedirectView(request.getContextPath()));
	}

	@RequestMapping(value = { "/api/register" }, method = RequestMethod.POST)
	@ResponseBody
	public RegDTO doOrderAPI(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,

			@RequestBody RegDTO regDTO

	) throws Exception {
		Template tmpl = Template.getTemplate(request);

		if (registerValidator.validateRegDTO(regDTO)) {

			return regDTO;
		}
		int userid = registerService.createUser(request, tmpl, regDTO.getUser());

		return regDTO;
	}

}

package ee.ttu.shop.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import ee.ttu.shop.peginator.PagesInfo;
import ee.ttu.shop.product.ProductPrepareService;

public class PageUtil {

	public static Map<String, String> getQueryParameters(HttpServletRequest request)
			throws UnsupportedEncodingException {
		Map<String, String> queryParameters = new HashMap<>();
		String queryString = request.getQueryString();

		if (StringUtils.isEmpty(queryString)) {
			return queryParameters;
		}

		queryString = URLDecoder.decode(request.getQueryString(), "UTF-8");

		String[] parameters = queryString.split("&");

		for (String parameter : parameters) {
			String[] keyValuePair = parameter.split("=");
			String value = queryParameters.get(keyValuePair[0]);
			value = keyValuePair.length == 1 ? "" : keyValuePair[1];
			queryParameters.put(keyValuePair[0], value);
		}
		return queryParameters;
	}

	public static PagesInfo makePages(int prodAmount, int productsPerPage, int currentPage, String pname, String cname,
			HttpServletRequest request) throws UnsupportedEncodingException {
		List<String> out = new ArrayList<>();
		int pagesCount = ProductPrepareService.getPageCount(prodAmount, productsPerPage);
		return new PagesInfo(out, currentPage, pagesCount, pname, cname, request);
	}

}

package ee.ttu.shop.catalog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class CatalogPrepareService {

	public static String getCatalogLinkPage(String pname, String cname, int page, HttpServletRequest request)
			throws UnsupportedEncodingException {
		String queryString = request.getQueryString();
		if (!StringUtils.isEmpty(queryString)) {

			queryString = "?" + URLDecoder.decode(request.getQueryString(), "UTF-8");
		} else
			queryString = "";

		if (cname == null)
			return pname + "/items-list-" + page + "/" + queryString;
		else
			return pname + "/" + cname + "/items-list-" + page + "/" + queryString;
	}

	public static String getCatalogLink(String pname, String cname, String queryString) {
		if (cname == null)
			return pname + "/" + queryString;
		else
			return pname + "/" + cname + "/" + queryString;
	}

	public PreparedCatalogList prepareCatalogList(List<Catalog> catalogs, String selected) {
		PreparedCatalogList preparedCatalogList = new PreparedCatalogList();
		preparedCatalogList.setCatalogs(catalogs);
		preparedCatalogList.setSelected(selected);
		return preparedCatalogList;
	}

}

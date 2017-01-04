package ee.ttu.shop.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;
import ee.ttu.shop.site.Template;

public class SecurityFilter extends GenericFilterBean implements InitializingBean{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		HttpServletRequest req = (HttpServletRequest) request;
		request.setAttribute("template", new Template(ctx));
		chain.doFilter(req, response);
		
	}

}

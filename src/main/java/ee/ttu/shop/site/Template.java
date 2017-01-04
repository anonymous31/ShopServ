package ee.ttu.shop.site;

import javax.servlet.ServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ee.ttu.shop.site.Theme;
import ee.ttu.shop.auth.AuthUtil;
import ee.ttu.shop.user.User;

public class Template {

	private final DefaultProfile defaultProfile;
	
	private final Theme theme;
	
	public Template(WebApplicationContext ctx){
		defaultProfile = AuthUtil.getDefaultProfile();
		 theme=DefaultProfile.getDefaultTheme();
	}
	public Template(ServletRequest request){
		this(WebApplicationContextUtils.getWebApplicationContext(request.getServletContext()));
	}

	public static Template getTemplate(ServletRequest request) {
		return new Template(request);
	}
	
	public DefaultProfile getDefaultProfile() {
		return defaultProfile;
	}
	public Theme getTheme() {
		return theme;
	}

	

	public boolean isSessionAuthorized() {
		return AuthUtil.isSessionAuthorized();
	}
	
	public boolean isEmp() {
		return AuthUtil.isEmp();
	}

	
	public String getNick() {
		User currentUser = getCurrentUser();
		
		if (currentUser==null) {
		  return null;
		} else {
		  return currentUser.getNick();
		}
	}

	public User getCurrentUser()  {
		return AuthUtil.getCurrentUser();
	}
}

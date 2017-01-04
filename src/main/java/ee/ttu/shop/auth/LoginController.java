package ee.ttu.shop.auth;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ee.ttu.shop.user.UserDao;
import ee.ttu.shop.user.UserErrorException;

@Controller
public class LoginController {

	
	private static Logger logger =  Logger.getLogger(LoginController.class);
	
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDao userDao;
	
	
	@RequestMapping(value = "/api/login_process", method = RequestMethod.POST)
	@ResponseBody
	public String loginProcessAPI(
			@RequestParam("nick") final String username,
			@RequestParam("passwd") final String password,
			HttpServletRequest request){
		CustomAuthToken token = new CustomAuthToken(username, password);
		try {
			Authentication auth = authenticationManager.authenticate(token);

			UserDetailsImpl userDetails = (UserDetailsImpl)auth.getPrincipal();
			if(userDetails.getUser().getUser_status().getId()==2){
				throw new UserErrorException("User is banned");
			}
			SecurityContextHolder.getContext().setAuthentication(auth);
			AuthUtil.updateLastLogin(auth, userDao);
		}catch(Exception e){
			return "fail";
		}
		return "ok";
	}

	
	@RequestMapping(value = "/login_process", method = RequestMethod.POST)
	public ModelAndView loginProcess(@RequestParam("nick") final String username,
			  @RequestParam("passwd") final String password,HttpServletRequest request){
		CustomAuthToken token = new CustomAuthToken(username, password);
		try {
			Authentication auth = authenticationManager.authenticate(token);
			UserDetailsImpl userDetails = (UserDetailsImpl)auth.getPrincipal();
			if(userDetails.getUser().getUser_status().getId()==2){
				throw new UserErrorException("User is deactivated");
			}
			SecurityContextHolder.getContext().setAuthentication(auth);
			AuthUtil.updateLastLogin(auth, userDao);
		}catch(Exception e){
			return new ModelAndView(new RedirectView(request.getContextPath()+"/login.jsp?error=true"));
		}
		return new ModelAndView(new RedirectView(request.getContextPath()));
	}
	
	
	@RequestMapping(value = "/login.jsp", method = RequestMethod.GET)
	public ModelAndView loginForm() {
		return new ModelAndView("login-form");
	}
	
}

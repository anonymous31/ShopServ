package ee.ttu.shop.auth;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import ee.ttu.shop.auth.CustomAuthToken;
import ee.ttu.shop.user.User;
import ee.ttu.shop.user.UserDao;

@Component
public class CustomAuthManager implements AuthenticationProvider {

	
	Md5PasswordEncoder pwdEncoder = new Md5PasswordEncoder();
	private static final Logger logger = Logger.getLogger(CustomAuthManager.class);
	@Autowired
	private UserDao userDao;
	
	private static List<GrantedAuthority> retrieveUserAuthorities(User user) {
		logger.debug("retrive auth for:" + user.getNick()) ;
		List<GrantedAuthority> results = new ArrayList<>();
		if(user.getUser_status().getId()!=2) {
		  results.add(new SimpleGrantedAuthority("ROLE_USER"));
		  logger.info("added role user ") ;
		  if(user.isIs_employee()){
			  results.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
		  }
		  
		}
		return results;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		CustomAuthToken token = (CustomAuthToken)authentication;
		String nick = token.getName();
		String rawPwd = token.getCredentials().toString();
		User user = null;
	
		user = userDao.getUserCached(nick);
		String hash = user.getPasswd();
		if(hash.equals(pwdEncoder.encodePassword(rawPwd+user.getSalt(), ""))){
			List<GrantedAuthority> result = retrieveUserAuthorities(user);
			UserDetailsImpl principal = new UserDetailsImpl(user, result);
			Authentication auth = new CustomAuthToken(principal, null, result);
			
			return auth;
		}else{
			throw new AuthenticationException("ppc"){
				
			};
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(CustomAuthToken.class);
	}

}

package ee.ttu.shop.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import ee.ttu.shop.site.DefaultProfile;
import ee.ttu.shop.user.User;
import ee.ttu.shop.user.UserDao;

public class AuthUtil {
	
	public static boolean isSessionAuthorized() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && (authentication.isAuthenticated() && hasAuthority("ROLE_USER"));
	}
	
	public static boolean isEmp() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && (authentication.isAuthenticated() && hasAuthority("ROLE_EMPLOYEE"));
	}

	public static DefaultProfile getDefaultProfile() {
		return DefaultProfile.getDefaultProfile();
	}

	public static void updateLastLogin(Authentication authentication, UserDao userDao) {
		if (authentication != null && (authentication.isAuthenticated())) {
		  Object principal = authentication.getPrincipal();
		  if (principal instanceof UserDetailsImpl) {
			UserDetailsImpl userDetails = (UserDetailsImpl) principal;
			User user = userDetails.getUser();
			userDao.updateLastlogin(user, true);
		  }
		}
	}

	private static boolean hasAuthority(String authName) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
		  return false;
		}

		for (GrantedAuthority auth : authentication.getAuthorities()) {
		  if (auth.getAuthority().equals(authName)) {
			return true;
		  }
		}
		return false;
	}


	public static User getCurrentUser() {
		if (!isSessionAuthorized()) {
		  return null;
		}

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsImpl) {
		  return ((UserDetailsImpl) principal).getUser();
		} else {
		  return null;
		}
	}

}

package ee.ttu.shop.auth;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthToken extends UsernamePasswordAuthenticationToken {

	public CustomAuthToken(String principal, String credentials){
		super(principal, credentials);
	}
	
	public CustomAuthToken(
			Object principal,
			Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

}

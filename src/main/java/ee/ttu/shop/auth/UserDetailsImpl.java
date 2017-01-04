package ee.ttu.shop.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ee.ttu.shop.user.User;

public class UserDetailsImpl implements UserDetails {
	
	private User user;	
	List<GrantedAuthority> auth;

	public UserDetailsImpl(User user,List<GrantedAuthority> auth) {
		this.user=user;
		this.auth=auth;
		
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return auth;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPasswd();
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getNick();
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<GrantedAuthority> getAuth() {
		return auth;
	}
	public void setAuth(List<GrantedAuthority> auth) {
		this.auth = auth;
	}

}

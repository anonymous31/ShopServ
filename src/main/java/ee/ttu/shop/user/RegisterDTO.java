package ee.ttu.shop.user;

public class RegisterDTO {
	
	
	private String email;
	private String nick;

	private String password;
	private String password2;
	
	public User getUser(){
		User u = new User();
		u.setNick(nick);
		u.setEmail(email);
		u.setPasswd(password);
		return u;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}

}

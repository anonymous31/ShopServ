package ee.ttu.shop.user;

public class RegDTO {
	
	private String emailError;
	private String email;
	private String nickError;
	private String nick;

	private String pwdError;
	private String password;
	private String pwd2Error;
	private String password2;
	
	public User getUser(){
		User u = new User();
		u.setNick(nick);
		u.setEmail(email);
		u.setPasswd(password);
		return u;
	}
	
	
	public String getEmailError() {
		return emailError;
	}
	public void setEmailError(String emailError) {
		this.emailError = emailError;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickError() {
		return nickError;
	}
	public void setNickError(String nickError) {
		this.nickError = nickError;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPwdError() {
		return pwdError;
	}
	public void setPwdError(String pwdError) {
		this.pwdError = pwdError;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPwd2Error() {
		return pwd2Error;
	}
	public void setPwd2Error(String pwd2Error) {
		this.pwd2Error = pwd2Error;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	
	

}

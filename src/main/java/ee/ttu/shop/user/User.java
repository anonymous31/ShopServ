package ee.ttu.shop.user;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {

	
	private static final long serialVersionUID = 4380765758085304320L;
	private int id;
	private User_status user_status;
	private String nick;
	private String passwd;
	private String salt;
	private String email;
	private Timestamp reg_date;
	private Timestamp last_login;
	private String style;
	private boolean is_client=true;
	private boolean is_employee=false;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User_status getUser_status() {
		return user_status;
	}
	public void setUser_status(User_status user_status) {
		this.user_status = user_status;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Timestamp getReg_date() {
		return reg_date;
	}
	public void setReg_date(Timestamp reg_date) {
		this.reg_date = reg_date;
	}
	public Timestamp getLast_login() {
		return last_login;
	}
	public void setLast_login(Timestamp last_login) {
		this.last_login = last_login;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public boolean isIs_client() {
		return is_client;
	}
	public void setIs_client(boolean is_client) {
		this.is_client = is_client;
	}
	public boolean isIs_employee() {
		return is_employee;
	}
	public void setIs_employee(boolean is_employee) {
		this.is_employee = is_employee;
	}

}

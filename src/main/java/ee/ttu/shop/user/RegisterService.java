package ee.ttu.shop.user;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import ee.ttu.shop.order.Order;
import ee.ttu.shop.site.Template;

@Service
public class RegisterService {

	@Autowired
	UserDao userDao;

	Md5PasswordEncoder pwdEncoder = new Md5PasswordEncoder();
	static final String charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static Random rnd = new Random();

	String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(charset.charAt(rnd.nextInt(charset.length())));
		return sb.toString();
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int createUser(HttpServletRequest request, Template template, User user) {
		String salt = randomString(6);
		String hash = pwdEncoder.encodePassword(user.getPasswd() + salt, "");
		user.setSalt(salt);
		user.setPasswd(hash);

		return userDao.createUser(user);
	}

}

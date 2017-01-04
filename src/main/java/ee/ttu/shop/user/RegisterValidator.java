package ee.ttu.shop.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class RegisterValidator {


	@Autowired
	private UserDao userDao;

	protected static final int MIN_PASSWORD_LEN = 4;

	Set<String> bad_domains = new HashSet<String>(Arrays.asList("asdasd.ru", "nepwk.com", "klzlk.com", "nwldx.com",
			"mailinator.com", "mytrashmail.com", "temporaryinbox.com", "10minutemail.com"));

	public void validateRegForm(RegisterDTO form, BindingResult errors) {
		if (form.getEmail() == null || form.getEmail().length() == 0
				|| bad_domains.contains(form.getEmail().replaceFirst("^[^@]+@", "").toLowerCase())) {
			errors.pushNestedPath("");
			errors.rejectValue("email", "err_qna_not_blank", "bad email");
			errors.popNestedPath();
		}

		String password = form.getPassword();
		String password2 = form.getPassword2();

		if (password == null || password.length() == 0) {
			errors.pushNestedPath("");
			errors.rejectValue("password", "err_qna_not_blank", "Password is empty");
			errors.popNestedPath();
		}
		if (password2 == null || password2.length() == 0) {
			errors.pushNestedPath("");
			errors.rejectValue("password2", "err_qna_not_blank", "Password2 is empty");
			errors.popNestedPath();
		}

		if (form.getPassword2() != null && form.getPassword() != null
				&& !form.getPassword().equals(form.getPassword2())) {
			errors.pushNestedPath("");
			errors.rejectValue("password", "err_qna_not_blank", "Password do not match");
			errors.popNestedPath();
		}

		if (password != null && form.getPassword().length() < MIN_PASSWORD_LEN) {
			errors.pushNestedPath("");
			errors.rejectValue("password", "err_qna_not_blank", "too short, need at least " + MIN_PASSWORD_LEN);
			errors.popNestedPath();
		}

		try {
			userDao.getUserCached(form.getNick());
			errors.pushNestedPath("");
			errors.rejectValue("nick", "err_qna_not_blank", "user with nick " + form.getNick() + " already exists");
			errors.popNestedPath();

		} catch (Exception e) {
		}
	}

	public boolean validateRegDTO(RegDTO regDTO) {
		boolean erros = false;

		if (regDTO.getEmail() == null || regDTO.getEmail().length() == 0
				|| bad_domains.contains(regDTO.getEmail().replaceFirst("^[^@]+@", "").toLowerCase())) {
			regDTO.setEmailError("bad email");
			erros=true;
		}

		String password = regDTO.getPassword();
		String password2 = regDTO.getPassword2();

		if (password == null || password.length() == 0) {
			regDTO.setPwdError("Password is empty");
			erros=true;
			
		}
		if (password2 == null || password2.length() == 0) {
			regDTO.setPwd2Error("Password2 is empty");
			erros=true;
		}

		if (regDTO.getPassword2() != null && regDTO.getPassword() != null
				&& !regDTO.getPassword().equals(regDTO.getPassword2())) {
			regDTO.setPwdError("Passwords do not match");
			erros=true;			
		}

		if (password != null && regDTO.getPassword().length() < MIN_PASSWORD_LEN) {
			regDTO.setPwdError("too short, need at least " + MIN_PASSWORD_LEN);
			erros=true;	
		}

		try {
			userDao.getUserCached(regDTO.getNick());
			regDTO.setNickError("user with nick " + regDTO.getNick() + " already exists");
			erros=true;	

		} catch (Exception e) {
		}
		return erros;
	}
}

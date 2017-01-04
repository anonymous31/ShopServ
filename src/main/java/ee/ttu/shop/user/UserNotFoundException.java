package ee.ttu.shop.user;

public class UserNotFoundException extends RuntimeException {
	
	public UserNotFoundException(int id){
		super("User with id" + id + " not found");
	}

}

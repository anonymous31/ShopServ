package ee.ttu.shop.user;

public class UserErrorException extends Exception {
	
	public UserErrorException()
	{
		super("error");
	}

	public UserErrorException(String info)
	{
		super(info);
	}
	

}

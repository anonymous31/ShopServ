package ee.ttu.shop.product;

public class ProductNotFoundException extends RuntimeException {
	public ProductNotFoundException(int id) {
	    super("Продукт #" + id + " не существуе");
	}
}

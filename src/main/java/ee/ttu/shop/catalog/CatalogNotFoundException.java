package ee.ttu.shop.catalog;

public class CatalogNotFoundException extends RuntimeException {

	public CatalogNotFoundException(int id) {
		super("Неправильно задан номер catalog: "+id);
	}

	public CatalogNotFoundException() {
		super("catalog not found");
	}
}

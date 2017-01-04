package ee.ttu.shop.product;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ProductFilter {

	private List<Product> products;

	private static List<Product> getProducts(List<Product> products, boolean reverse, int offset, int limit) {
		List<Product> out = new ArrayList<>();
		if (products != null)
			for (ListIterator<Product> i = products.listIterator(reverse ? products.size() : 0); reverse
					? i.hasPrevious() : i.hasNext();) {

				int index = reverse ? (products.size() - i.previousIndex()) : i.nextIndex();
				Product product = reverse ? i.previous() : i.next();
				if (index < offset || (limit != 0 && index >= offset + limit)) {
					boolean i1 = index < offset;
					boolean i2 = (limit != 0 && index >= offset + limit);
					continue;
				}
				out.add(product);
			}
		return out;
	}

	public List<Product> getProductsForPage(boolean reverse, int page, int productsPerPage) {
		int offset = 0;
		int limit = 0;

		if (page != -1) {
			limit = productsPerPage;
			offset = productsPerPage * page;
		}

		return getProducts(products, reverse, offset, limit);
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}

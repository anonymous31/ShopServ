package ee.ttu.shop.product;

import java.util.List;
import java.util.Map;

import ee.ttu.shop.filter.Detail;
import ee.ttu.shop.filter.Filter;
import ee.ttu.shop.filter.Filter_variant;
import ee.ttu.shop.order.Order;
import ee.ttu.shop.order.OrderItem;
import ee.ttu.shop.stock.Stock;
import ee.ttu.shop.user.User;

public interface IProductDao {
	public Map<Integer, List<Product>> getProductsByDto(ProductListDto productListDto);

	public Map<Filter, List<Filter_variant>> getAttrsByCatalog(int catalog);

	public List<Product> getProductInfo(List<Product> prods, Integer priceOrder);

	public Map<Integer, List<Product>> getProductsByParams(ProductListDto productListDto, Map<String, String> params,
			Integer priceOrder);

	public void addProductsByCatalog(List<Product> products);

	public void addProducts(List<Product> products);

	void addDetails(List<Detail> details);

	void addProdAttrs(List<ProdAttrTable> prodAttrs);

	void addStocks(List<Stock> stocks);

	void addUsers(List<User> users);

	void addOrders(List<Order> orders, List<OrderItem> orderItems);

	void callSort();

}

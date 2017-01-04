package ee.ttu.shop.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

import ee.ttu.shop.catalog.Catalog;
import ee.ttu.shop.filter.Detail;
import ee.ttu.shop.filter.Filter;
import ee.ttu.shop.filter.Filter_variant;
import ee.ttu.shop.order.Order;
import ee.ttu.shop.order.OrderItem;
import ee.ttu.shop.stock.Shipment_type;
import ee.ttu.shop.stock.Shop;
import ee.ttu.shop.stock.Stock;
import ee.ttu.shop.user.User;

@Service
public class ProductService {

	private static final Logger logger = Logger.getLogger(ProductService.class);

	@Autowired
	@Qualifier("productDao")
	ProductDao productDao;



	@Autowired
	@Qualifier("productDao")
	IProductDao iProductDao;

	public Product getProductById(int id, Integer priceOrder) {
		Product prod = new Product();
		prod.setId(id);
		List<Product> rgProds = new ArrayList<>();
		rgProds.add(prod);
		List<Product> prods = iProductDao.getProductInfo(rgProds, priceOrder);
		if (prods.size() == 0) {

		}
		return prods.get(0);
	}

	public static int genRand(int min, int max) {

		Random rand = new Random();

		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public void genTestData() {

		// Integer id = iProductDao.getNextValueOfProduct();
		//TODO change to 50
		int jk = 1;
		for (int ii = 0; ii < jk; ++ii) {
			logger.info("ii is " + ii);
			List<User> users = new ArrayList<>();
			List<Product> products = new ArrayList<>();
			List<Order> orders = new ArrayList<>();
			List<OrderItem> orderItems = new ArrayList<>();
			List<Detail> details = new ArrayList<>();
			List<Stock> stocks = new ArrayList<>();
			List<ProdAttrTable> prodAttrTables = new ArrayList<>();
			for (int i = ((ii * 20000) + 1); i < ((ii * 20000) + (1 + 20000)); i++) {
				User user = new User();
				user.setId(i);
				user.setNick("test" + i);
				user.setPasswd("579d9ec9d0c3d687aaa91289ac2854e4");
				user.setSalt("123");
				user.setEmail("softy2005z@yahoo.com");
				users.add(user);

				int prize = genRand(100, 1000000);

				int av1_rnd = genRand(1, 15);
				String av1_name = getAVNameById(av1_rnd);

				int av2_rnd = genRand(25, 28);
				String av2_name = getAVNameById(av2_rnd);

				int av3_rnd = genRand(29, 34);
				String av3_name = getAVNameById(av3_rnd);

				int av4_rnd = genRand(35, 38);
				String av4_name = getAVNameById(av4_rnd);

				Product product = new Product();

				product.setId(i);
				product.setName("TESTNAME" + i);
				BigDecimal price = new BigDecimal(prize);
				product.setDescription("TESTDESC" + i);
				product.setImage("img/1.png");
				Catalog catalog = new Catalog();
				catalog.setId(9);
				product.setCatalog(catalog);
				products.add(product);
				product.setPrice(price);
				{
					Filter_variant filter = new Filter_variant();
					filter.setId(av1_rnd);
					product.setBrand_fk(filter);
				}
				product.setBrand(av1_name);
				product.getAttrs().put("brand", av1_name);

				{
					Filter_variant filter = new Filter_variant();
					filter.setId(av2_rnd);
					product.setResolution_fk(filter);
				}
				product.setResolution(av2_name);
				product.getAttrs().put("resolution", av2_name);

				{
					Filter_variant filter = new Filter_variant();
					filter.setId(av3_rnd);
					product.setScreen_fk(filter);
				}
				product.setScreen(av3_name);
				product.getAttrs().put("screen", av3_name);

				{
					Filter_variant filter = new Filter_variant();
					filter.setId(av4_rnd);
					product.setResp_time_fk(filter);
				}
				product.setResp_time(av4_name);
				product.getAttrs().put("Resp time", av4_name);
				product.setType("monitors");

				Order order = new Order();
				order.setId(i);
				order.setFirst_name("fname" + i);
				order.setLast_name("lname" + i);
				order.setPhone("123456789");
				order.setEmail("test2@test.ru");
				order.setAddress("Egypt sphinx district");

				order.setUser(user);

				OrderItem orderItem = new OrderItem();
				orderItem.setPrice(new BigDecimal(prize));
				orderItem.setQty(2);
				orderItem.setProduct(product);
				orderItem.setOrder(order);

				orderItems.add(orderItem);

				orders.add(order);

				Detail detail = new Detail();
				detail.setName("DTLNAME1");
				detail.setValue("DTLVAL1");
				detail.setProdId(i);
				details.add(detail);
				Detail detail2 = new Detail();
				detail2.setName("DTLNAME2");
				detail2.setValue("DTLVAL2");
				detail2.setProdId(i);
				details.add(detail2);

				int stck_num = genRand(1, 3);
				int st_min = 0;
				int st_max = 0;
				for (int jj = 1; jj < stck_num; jj++) {
					int rnd_shop_id = genRand(1, 4);
					if (rnd_shop_id == 4) {
						st_min = 2;
						st_max = 3;
					} else {
						st_min = 1;
						st_max = 1;
					}

					Stock stock = new Stock();
					stock.setQuantity(genRand(1, 30));
					Shop shop = new Shop();
					shop.setId(rnd_shop_id);
					stock.setShop(shop);
					stock.setProdId(i);
					Shipment_type shipment_type = new Shipment_type();
					shipment_type.setId(genRand(st_min, st_max));
					stock.setShipment_type(shipment_type);
					stocks.add(stock);
				}
			}
			logger.debug("done gen ");

			logger.info("startput ");

			productDao.addUsers(users);
			logger.info("startAddProducts ");
			productDao.addProducts(products);
			logger.info("endAddProducts ");
			productDao.addOrders(orders, orderItems);
			logger.info("endAddOrders ");
			productDao.addStocks(stocks);
			productDao.addDetails(details);
			logger.info("endput ");
		}

		logger.info("start phones  ");
		int offset = (((jk - 1) * 20000) + (1 + 20000));
		for (int ii = 0; ii < jk; ++ii) {
			logger.info("ii is " + ii);
			List<User> users = new ArrayList<>();
			List<Product> products = new ArrayList<>();
			List<Order> orders = new ArrayList<>();
			List<OrderItem> orderItems = new ArrayList<>();
			List<Detail> details = new ArrayList<>();
			List<Stock> stocks = new ArrayList<>();
			List<ProdAttrTable> prodAttrTables = new ArrayList<>();
			for (int i = ((ii * 20000) + offset); i < ((ii * 20000) + (offset + 20000)); i++) {
				User user = new User();
				user.setId(i);
				user.setNick("test" + i);
				user.setPasswd("579d9ec9d0c3d687aaa91289ac2854e4");
				user.setSalt("123");
				user.setEmail("softy2005z@yahoo.com");
				users.add(user);

				int prize = genRand(100, 1000000);

				int av1_rnd = genRand(11, 23);
				String av1_name = getAVNameById(av1_rnd);

				int av2_rnd = genRand(24, 27);
				String av2_name = getAVNameById(av2_rnd);

				int av3_rnd = genRand(39, 43);
				String av3_name = getAVNameById(av3_rnd);

				int av4_rnd = genRand(44, 46);
				String av4_name = getAVNameById(av4_rnd);

				Product product = new Product();

				product.setId(i);
				product.setName("TESTNAME" + i);
				BigDecimal price = new BigDecimal(prize);
				product.setDescription("TESTDESC" + i);
				product.setImage("img/3.png");
				Catalog catalog = new Catalog();
				catalog.setId(3);
				product.setCatalog(catalog);
				products.add(product);
				product.setPrice(price);
				{
					Filter_variant filter = new Filter_variant();
					filter.setId(av1_rnd);
					product.setBrand_fk(filter);
				}
				product.setBrand(av1_name);
				product.getAttrs().put("brand", av1_name);

				{
					Filter_variant filter = new Filter_variant();
					filter.setId(av2_rnd);
					product.setResolution_fk(filter);
				}
				product.setResolution(av2_name);

				product.getAttrs().put("resolution", av2_name);

				{
					Filter_variant filter = new Filter_variant();
					filter.setId(av3_rnd);
					product.setColor_fk(filter);
				}
				product.setColor(av3_name);

				product.getAttrs().put("color", av3_name);

				{
					Filter_variant filter = new Filter_variant();
					filter.setId(av4_rnd);
					product.setBattery_life_fk(filter);
				}
				product.setBattery_life(av4_name);

				product.getAttrs().put("Battery Life", av4_name);
				product.setType("smartphones");

				Order order = new Order();
				order.setId(i);
				order.setFirst_name("fname" + i);
				order.setLast_name("lname" + i);
				order.setPhone("123456789");
				order.setEmail("test2@test.ru");
				order.setAddress("Egypt sphinx district");

				order.setUser(user);

				OrderItem orderItem = new OrderItem();
				orderItem.setPrice(new BigDecimal(prize));
				orderItem.setQty(2);
				orderItem.setProduct(product);
				orderItem.setOrder(order);

				orderItems.add(orderItem);

				orders.add(order);

				Detail detail = new Detail();
				detail.setName("DTLNAME1");
				detail.setValue("DTLVAL1");
				detail.setProdId(i);
				details.add(detail);
				Detail detail2 = new Detail();
				detail2.setName("DTLNAME2");
				detail2.setValue("DTLVAL2");
				detail2.setProdId(i);
				details.add(detail2);

				int stck_num = genRand(1, 3);
				int st_min = 0;
				int st_max = 0;
				for (int jj = 1; jj < stck_num; jj++) {
					int rnd_shop_id = genRand(1, 4);
					if (rnd_shop_id == 4) {
						st_min = 2;
						st_max = 3;
					} else {
						st_min = 1;
						st_max = 1;
					}

					Stock stock = new Stock();
					stock.setQuantity(genRand(1, 30));
					Shop shop = new Shop();
					shop.setId(rnd_shop_id);
					stock.setShop(shop);
					stock.setProdId(i);
					Shipment_type shipment_type = new Shipment_type();
					shipment_type.setId(genRand(st_min, st_max));
					stock.setShipment_type(shipment_type);
					stocks.add(stock);
				}
			}

			productDao.addUsers(users);
			logger.info("startAddProducts ");
			productDao.addProducts(products);
			logger.info("endAddProducts ");
			productDao.addOrders(orders, orderItems);
			productDao.addStocks(stocks);
			productDao.addDetails(details);
			logger.info("endput ");
		}
		productDao.callSort();
	}

	public Map<Integer, List<Product>> getProductsFirstRun(int catalogId) {
		ProductListDto productListDto = new ProductListDto();
		productListDto.setCatalogId(catalogId);
		productListDto.setOffset(0);

		Map<Integer, List<Product>> pMap = null;
		pMap = iProductDao.getProductsByDto(productListDto);

		if (pMap.keySet().iterator().next() == 0 && catalogId == 9) {
			genTestData();
		}
		pMap = productDao.getProducts(1, productListDto);
		return pMap;
	}

	private String getAVNameById(int id) {
		String res = "";
		switch (id) {
		case 1:
			res = "Apple";
			break;
		case 2:
			res = "Asus";
			break;
		case 3:
			res = "Datagate";
			break;
		case 4:
			res = "Dell";
			break;
		case 5:
			res = "Asrock";
			break;
		case 6:
			res = "Creative";
			break;
		case 7:
			res = "Gigabyte";
			break;
		case 8:
			res = "AOC";
			break;
		case 9:
			res = "NEC";
			break;
		case 10:
			res = "Acer";
			break;
		case 11:
			res = "Samsung";
			break;
		case 12:
			res = "BenQ";
			break;
		case 13:
			res = "LG";
			break;
		case 14:
			res = "PHILIPS";
			break;
		case 15:
			res = "AsusTest";
			break;
		case 16:
			res = "Google";
			break;
		case 17:
			res = "Garmin";
			break;
		case 18:
			res = "Microsoft";
			break;
		case 19:
			res = "Nokia";
			break;
		case 20:
			res = "SONY ERICSSON";
			break;
		case 21:
			res = "T-Mobile";
			break;
		case 22:
			res = "Motorola";
			break;
		case 23:
			res = "HP";
			break;
		case 24:
			res = "400×800";
			break;
		case 25:
			res = "1024x768";
			break;
		case 26:
			res = "1280x720";
			break;
		case 27:
			res = "1280x800";
			break;
		case 28:
			res = "1920x1080";
			break;
		case 29:
			res = "10''";
			break;

		case 30:
			res = "15''";
			break;
		case 31:
			res = "17''";
			break;
		case 32:
			res = "19''";
			break;
		case 33:
			res = "22''";
			break;
		case 34:
			res = "27''";
			break;
		case 35:
			res = "5 ms";
			break;
		case 36:
			res = "6 ms";
			break;
		case 37:
			res = "7 ms";
			break;
		case 38:
			res = "8 ms";
			break;
		case 39:
			res = "black";
			break;
		case 40:
			res = "white";
			break;
		case 41:
			res = "red";
			break;
		case 42:
			res = "green";
			break;
		case 43:
			res = "blue";
			break;
		case 44:
			res = "1 hour";
			break;
		case 45:
			res = "2 hour";
			break;
		case 46:
			res = "4 hour";
			break;
		case 47:
			res = "666 hour";
			break;
		}

		return res;
	}

	public List<Product> getProductsInfo(List<Product> products, Integer priceOrder) {
		List<Product> prods = iProductDao.getProductInfo(products, priceOrder);
		if (prods.size() < products.size()) {
			if (1 == 1)
				return null;

			int i = 1;
			int max_pull_size = 20000;

			while (true) {
				if (products.size() > i * max_pull_size) {
					int idx = (i - 1) * max_pull_size;
					List<Product> chopped = products.subList(idx > 0 ? idx - 1 : idx, i * max_pull_size);
					logger.info("chopped size " + chopped.size());
					List<Product> prods1 = productDao.getProductsWInfos(chopped);
					iProductDao.addProducts(prods1);
				} else {
					List<Product> chopped = products.subList((i - 1) * max_pull_size, products.size());
					logger.info("chopped last " + chopped.size());
					List<Product> prods1 = productDao.getProductsWInfos(chopped);
					iProductDao.addProducts(prods1);
					return prods1;
				}
				i++;
			}

		}
		return prods;
	}

	public Map<Integer, List<Product>> getProductsByCatalogForPage(int catalogid, int page, int productsPerPage) {
		Map<Integer, List<Product>> pMap = null;
		if (catalogid != 0) {
			ProductListDto productListDto = new ProductListDto();
			productListDto.setLmt(productsPerPage);

			productListDto.setOffset(page * productsPerPage);
			productListDto.setCatalogId(catalogid);

			pMap = iProductDao.getProductsByDto(productListDto);
		}
		return pMap;
	}

	public Map<Integer, List<Product>> getProductsByParamsForPage(int catalogid, Map<String, String> params,
			Integer price_order, int page, int productsPerPage) {
		Map<Integer, List<Product>> pMap = null;

		if (catalogid != 0) {
			ProductListDto pldto = new ProductListDto();
			pldto.setCatalogId(catalogid);
			pldto.setLmt(productsPerPage);

			pldto.setOffset(page * productsPerPage);

			Map<String, String> cleanParams = new HashMap<>();
			for (String key : params.keySet()) {
				if (!key.equals("price")) {
					cleanParams.put(key, params.get(key));
				}
			}
			if (true) {
				pMap = iProductDao.getProductsByParams(pldto, cleanParams, price_order);

			}
		}

		return pMap;
	}

}

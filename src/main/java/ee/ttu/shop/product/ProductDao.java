package ee.ttu.shop.product;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Repository
public class ProductDao implements IProductDao {

	private static final Logger logger = Logger.getLogger(ProductDao.class);

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setDateSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	private List<String> getCataAttrs(int catalogid) {
		Map<String, Object> qParams = new HashMap<>();
		qParams.put("cataid", catalogid);
		String q1 = "SELECT DISTINCT a.name fname,a.id " + " FROM catalog c"
				+ " INNER JOIN catalog_attribute ca ON c.id=ca.catalog_fk "
				+ " INNER JOIN attribute a ON ca.attribute_fk=a.id " + " WHERE c.id=:cataid " + " ORDER BY a.id";
		return namedParameterJdbcTemplate.query(q1, qParams, new ResultSetExtractor<List<String>>() {

			@Override
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<String> attrs = new ArrayList<>();

				while (rs.next()) {
					attrs.add(rs.getString("fname"));

				}

				return attrs;
			}
		});
	}

	public Map<Integer, List<Product>> getProductsByParams(ProductListDto productListDto, Map<String, String> params,
			Integer priceOrder) {

		int count = 0;

		int catalogid = productListDto.getCatalogId();
		List<String> ordered = getCataAttrs(catalogid);

		Integer limit = productListDto.getLmt();
		int offset = productListDto.getOffset();

		String finKey = "";
		if (params.size() > 0) {
			for (String FilterName : ordered) {
				String inputParam = params.get(FilterName);

				if (inputParam != null && inputParam.length() != 0) {
					finKey += ":" + FilterName + ":" + inputParam;
				}
			}

			Map<String, Object> qParams = new HashMap<>();
			if (priceOrder != null) {
				if (priceOrder == 1) {
					finKey = "catalog:" + catalogid + ":price:desc" + finKey;
				} else {
					finKey = "catalog:" + catalogid + ":price:asc" + finKey;
				}
			} else {
				finKey = "catalog:" + catalogid + finKey;
			}
			qParams.put("key", finKey);

			String q1 = " SELECT total_amount, unnest(subarray(value," + (offset + 1) + "," + limit
					+ ")) prodids FROM tbtest2 WHERE key=:key ";

			return namedParameterJdbcTemplate.query(q1, qParams, new ResultSetExtractor<Map<Integer, List<Product>>>() {

				@Override
				public Map<Integer, List<Product>> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Product> products = new ArrayList<>();
					Map<Integer, Product> productsById = new LinkedHashMap<>();
					Integer total = 0;
					while (rs.next()) {
						Integer id = rs.getInt("prodids");
						Product product = productsById.get(id);
						if (product == null) {
							total = rs.getInt("total_amount");
							product = new Product();
							product.setId(id);
							productsById.put(id, product);
							products.add(product);
						}
					}
					Map<Integer, List<Product>> pMap = new LinkedHashMap<>();
					pMap.put(total, products);
					return pMap;
				}
			});

		} else

		{

			String q1 = " SELECT total_amount, unnest(subarray(value," + (offset + 1) + "," + limit
					+ ")) prodids FROM tbtest2 WHERE key=:key ";

			Map<String, Object> qParams = new HashMap<>();
			if (priceOrder != null) {
				if (priceOrder == 1) {
					finKey = "catalog:" + catalogid + ":price:desc" + finKey;
				} else {
					finKey = "catalog:" + catalogid + ":price:asc" + finKey;
				}
			} else {
				finKey = "catalog:" + catalogid + finKey;
			}
			qParams.put("key", finKey);

			return namedParameterJdbcTemplate.query(q1, qParams, new ResultSetExtractor<Map<Integer, List<Product>>>() {

				@Override
				public Map<Integer, List<Product>> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Product> products = new ArrayList<>();
					Map<Integer, Product> productsById = new LinkedHashMap<>();
					Integer total = 0;
					while (rs.next()) {
						Integer id = rs.getInt("prodids");
						Product product = productsById.get(id);
						if (product == null) {
							total = rs.getInt("total_amount");
							product = new Product();
							product.setId(id);
							productsById.put(id, product);
							products.add(product);
						}
					}
					Map<Integer, List<Product>> pMap = new LinkedHashMap<>();
					pMap.put(total, products);
					return pMap;
				}

			});

		}

	}

	public Map<Filter, List<Filter_variant>> getAttrsByCatalog(int catalog) {
		Map<Filter, List<Filter_variant>> attrs = new LinkedHashMap<>();
		Object[] args = { catalog };

		String q = "SELECT DISTINCT a.id,a.name aname, av.attributeValue avalue,av.orderby" + " FROM catalog c"
				+ " INNER JOIN catalog_attribute ca ON c.id=ca.catalog_fk"
				+ " INNER JOIN attribute a ON ca.attribute_fk=a.id"
				+ " INNER JOIN attribute_value av ON ca.attribute_value_fk=av.id" + " WHERE c.id=? "
				+ " ORDER BY a.id,av.orderby asc";

		jdbcTemplate.query(q, args, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Filter filter = new Filter();
				filter.setName(rs.getString("aname"));

				Filter_variant filter_variant = new Filter_variant();
				filter_variant.setValue(rs.getString("avalue"));

				if (filter.getName() != null && !attrs.containsKey(filter)) {
					attrs.put(filter, new ArrayList<>());

					List<Filter_variant> variantList = attrs.get(filter);
					Filter_variant filter_variant1 = new Filter_variant();
					filter_variant1.setValue("all");
					variantList.add(filter_variant1);
				}
				if (filter.getName() != null) {
					List<Filter_variant> variantList = attrs.get(filter);
					variantList.add(filter_variant);
				}

			}
		});
		return attrs;
	}

	public Map<Integer, List<Product>> getProductsByDto(ProductListDto productListDto) {

		Map<String, Object> qParams = new HashMap<>();

		StringBuilder query = new StringBuilder();
		query.append("SELECT c.prod_amount total_count,p.id pid "
				+ "FROM product p INNER JOIN catalog c ON c.id=p.catalog_fk " + " " + "WHERE 1=1 ")
				.append(makeContitions(productListDto, qParams)).append("ORDER BY pid ASC ")
				.append(makeLimitAndOffset(productListDto));

		return namedParameterJdbcTemplate.query(query.toString(), qParams,
				new ResultSetExtractor<Map<Integer, List<Product>>>() {

					@Override
					public Map<Integer, List<Product>> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						List<Product> products = new ArrayList<>();
						Map<Integer, Product> productsById = new LinkedHashMap<>();
						Integer total = 0;
						while (rs.next()) {
							Integer id = rs.getInt("pid");
							Product product = productsById.get(id);
							if (product == null) {
								total = rs.getInt("total_count");
								product = new Product();
								product.setId(id);
								productsById.put(id, product);
								products.add(product);
							}
						}
						Map<Integer, List<Product>> pMap = new LinkedHashMap<>();
						pMap.put(total, products);

						return pMap;
					}

				});

	}

	public List<Product> getProductInfo(List<Product> prods, Integer priceOrder) {

		List<Integer> ids = new ArrayList<>();
		if (prods.size() == 0)
			return new ArrayList<>();
		for (Product prod : prods) {
			ids.add(prod.getId());
		}
		Map<String, Object> qParams = new HashMap<>();
		qParams.put("pids", ids);
		String q = "SELECT p.id pid, p.name, price, description, p.image,brand,resolution,screen,\"Resp time\", color, \"Battery Life\", type,"

				+ "cc.id cid, cc.title ct,cc.image ci, cc.urlname cu,"
				+ "cp.id cpid, cp.title pt, cp.image pi, cp.urlname pu,"
				+ "s.id stockid, quantity stockqty , st.id stid, sp.id spid, st.ship_date, sp.address spaddress, sp.name spname,"
				+ "d.id did, d.name dname, d.detail_value dval " + "FROM product p "
				+ "INNER JOIN catalog cc ON cc.id=p.catalog_fk " + "LEFT JOIN catalog cp ON cc.upper_catalog_fk=cp.id "
				+ "LEFT JOIN stock s ON s.product_fk=p.id " + "LEFT JOIN shipment_type st ON s.shipment_type_fk=st.id "
				+ "LEFT JOIN shop sp ON s.shop_fk=sp.id " + "LEFT JOIN detail d ON d.product_fk=p.id " + "WHERE 1=1 "
				+ "AND p.id IN (:pids) ";
		String order = "";
		StringBuilder stringBuilder = new StringBuilder();
		if (priceOrder != null) {
			stringBuilder.append(" ORDER BY p.price ");
			if (priceOrder == 0) {
				stringBuilder.append(" ASC ");
			} else
				stringBuilder.append(" DESC ");
		} else {
			stringBuilder.append(" ORDER BY pid ASC ");
		}
		order = stringBuilder.toString();
		order = order.isEmpty() ? "" : order + "";
		q += order;

		return namedParameterJdbcTemplate.query(q, qParams, new ResultSetExtractor<List<Product>>() {

			@Override
			public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<Integer, Product> productsById = new LinkedHashMap<>();
				while (rs.next()) {
					int pid = rs.getInt("pid");
					Product product = productsById.get(pid);
					if (product == null) {
						product = new Product();
						product.setId(pid);
						product.setName(rs.getString("name"));
						product.setPrice(rs.getBigDecimal("price"));
						product.setDescription(rs.getString("description"));
						product.setImage(rs.getString("image"));
						product.setBrand(rs.getString("brand"));
						product.setResolution(rs.getString("resolution"));
						product.setScreen(rs.getString("screen"));
						product.setResp_time(rs.getString("Resp time"));
						product.setColor(rs.getString("color"));
						product.setBattery_life(rs.getString("Battery Life"));

						product.getAttrs().put("brand", rs.getString("brand"));
						product.getAttrs().put("resolution", rs.getString("resolution"));
						product.getAttrs().put("screen", rs.getString("screen"));
						product.getAttrs().put("Resp time", rs.getString("Resp time"));
						product.getAttrs().put("color", rs.getString("color"));
						product.getAttrs().put("Battery Life", rs.getString("Battery Life"));

						product.setType(rs.getString("type"));
						if (rs.getString("pu") != null) {
							Catalog child = new Catalog();
							child.setId(rs.getInt("cid"));
							child.setUrlname(rs.getString("cu"));
							product.setCatalog(child);
							Catalog parent = new Catalog();
							parent.setId(rs.getInt("cpid"));
							parent.setUrlname(rs.getString("pu"));
							child.setParent(parent);

						} else {
							Catalog child = new Catalog();
							child.setId(rs.getInt("cid"));
							child.setUrlname(rs.getString("cu"));
							product.setCatalog(child);
						}
						productsById.put(pid, product);
					}

					if (rs.getInt("stockid") != 0) {
						Stock stock = new Stock();
						stock.setId(rs.getInt("stockid"));
						stock.setQuantity(rs.getInt("stockqty"));
						Shipment_type shipment_type = new Shipment_type();
						shipment_type.setId(rs.getInt("stid"));
						shipment_type.setShip_date(rs.getString("ship_date"));
						stock.setShipment_type(shipment_type);
						Shop shop = new Shop();
						shop.setId(rs.getInt("spid"));
						shop.setShop_address(rs.getString("spaddress"));
						shop.setShop_name(rs.getString("spname"));
						stock.setShop(shop);
						product.getStocks().add(stock);
					}

					if (rs.getString("name") != null) {
						Detail detail = new Detail();
						detail.setId(rs.getInt("did"));
						detail.setName(rs.getString("dname"));
						detail.setValue(rs.getString("dval"));
						product.getDetails().add(detail);
					}

				}
				List<Product> al = new ArrayList<Product>(productsById.values());
				return al;
			}
		});
	}

	public List<Product> getProductsWInfos(List<Product> prods) {
		List<Integer> ids = new ArrayList<>();
		for (Product prod : prods) {
			ids.add(prod.getId());
		}
		return namedParameterJdbcTemplate.query(
				"SELECT p.id pid, p.name, price, description, p.image,brand,resolution,screen,\"Resp time\", type,"

						+ "cc.id cid, cc.title ct,cc.image ci, cc.urlname cu,"
						+ "cp.id cpid, cp.title pt, cp.image pi, cp.urlname pu,"
						+ "s.id stockid, quantity stockqty , st.id stid, sp.id spid, st.ship_date, sp.address spaddress, sp.name spname,"
						+ "d.id did, d.name dname, d.detail_value dval " + "FROM product p "

						+ "INNER JOIN catalog cc ON cc.id=p.catalog_fk "
						+ "LEFT JOIN catalog cp ON cc.upper_catalog_fk=cp.id "
						+ "LEFT JOIN stock s ON s.product_fk=p.id "
						+ "LEFT JOIN shipment_type st ON s.shipment_type_fk=st.id "
						+ "LEFT JOIN shop sp ON s.shop_fk=sp.id " + "LEFT JOIN detail d ON d.product_fk=p.id "
						+ "WHERE ((cp.catalog_level=1 AND cc.title IS NULL) OR (cc.title IS NOT NULL )) "
						+ "AND p.id IN (:pids) " + "",
				ImmutableMap.of("pids", ids), new ResultSetExtractor<List<Product>>() {

					@Override
					public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<Integer, Product> productsById = new LinkedHashMap<>();

						while (rs.next()) {
							int pid = rs.getInt("pid");
							Product product = productsById.get(pid);
							if (product == null) {
								product = new Product();
								product.setId(pid);
								product.setName(rs.getString("name"));
								product.setPrice(rs.getBigDecimal("price"));
								product.setDescription(rs.getString("description"));
								product.setImage(rs.getString("image"));
								product.setBrand(rs.getString("brand"));
								product.setResolution(rs.getString("resolution"));
								product.setScreen(rs.getString("screen"));
								product.setResp_time(rs.getString("Resp time"));

								product.getAttrs().put("brand", rs.getString("brand"));
								product.getAttrs().put("resolution", rs.getString("resolution"));
								product.getAttrs().put("screen", rs.getString("screen"));
								product.getAttrs().put("Resp time", rs.getString("Resp time"));
								product.getAttrs().put("color", rs.getString("color"));
								product.getAttrs().put("Battery Life", rs.getString("Battery Life"));

								product.setType(rs.getString("type"));
								if (rs.getString("pu") != null) {
									Catalog child = new Catalog();
									child.setId(rs.getInt("cid"));
									child.setUrlname(rs.getString("cu"));
									product.setCatalog(child);
									Catalog parent = new Catalog();
									parent.setId(rs.getInt("cpid"));
									parent.setUrlname(rs.getString("pu"));
									child.setParent(parent);

								} else {
									Catalog child = new Catalog();
									child.setId(rs.getInt("cid"));
									child.setUrlname(rs.getString("cu"));
									product.setCatalog(child);
								}

								productsById.put(pid, product);
							}

							if (rs.getInt("stockid") != 0) {
								Stock stock = new Stock();
								stock.setId(rs.getInt("stockid"));
								stock.setQuantity(rs.getInt("stockqty"));
								Shipment_type shipment_type = new Shipment_type();
								shipment_type.setId(rs.getInt("stid"));
								shipment_type.setShip_date(rs.getString("ship_date"));
								stock.setShipment_type(shipment_type);
								Shop shop = new Shop();
								shop.setId(rs.getInt("spid"));
								shop.setShop_address(rs.getString("spaddress"));
								shop.setShop_name(rs.getString("spname"));
								stock.setShop(shop);
								product.getStocks().add(stock);
							}

							if (rs.getString("name") != null) {
								Detail detail = new Detail();
								detail.setId(rs.getInt("did"));
								detail.setName(rs.getString("dname"));
								detail.setValue(rs.getString("dval"));
								product.getDetails().add(detail);
							}

						}
						List<Product> al = new ArrayList<Product>(productsById.values());
						return al;
					}
				});
	}

	public Product getProduct(int id) throws ProductNotFoundException {
		Product product = null;
		product = jdbcTemplate.query("SELECT p.id id, s.id sid, st.id stid, sp.id spid " + "FROM product p "
				+ "LEFT JOIN stock s ON p.id=s.product_fk " + "LEFT JOIN shipment_type st ON s.shipment_type_fk=st.id "
				+ "LEFT JOIN shop sp ON s.shop_fk=sp.id " + "WHERE " + "p.id=?", new ResultSetExtractor<Product>() {

					@Override
					public Product extractData(ResultSet rs) throws SQLException, DataAccessException {

						Product product = new Product();
						product.setId(id);

						Map<Integer, Stock> stocksById = new LinkedHashMap<>();
						boolean found = false;
						while (rs.next()) {
							found = true;

							Stock stock = stocksById.get(rs.getInt("sid"));
							if (stock == null) {
								stock = new Stock();
								stock.setId(rs.getInt("sid"));
							}
							Shipment_type shipment_type = new Shipment_type();
							shipment_type.setId(rs.getInt("stid"));
							stock.setShipment_type(shipment_type);
							Shop shop = new Shop();
							shop.setId(rs.getInt("spid"));
							stock.setShop(shop);
							product.getStocks().add(stock);
						}
						if (!found)
							throw new ProductNotFoundException(id);
						return product;
					}

				}, id);
		return product;
	}

	private CharSequence makeContitions(ProductListDto productListDto, Map<String, Object> params) {
		StringBuilder where = new StringBuilder();

		Integer catalogId = productListDto.getCatalogId();
		if (catalogId != null) {
			where.append(" AND p.catalog_fk = :catalogid ");
			params.put("catalogid", catalogId);
		}
		return where;
	}

	private static String makeLimitAndOffset(ProductListDto productListDto) {
		String limitStr = "";
		if (productListDto.getLmt() != null) {
			limitStr += " LIMIT " + productListDto.getLmt().toString();
		}
		if (productListDto.getOffset() != 0) {
			limitStr += " OFFSET " + productListDto.getOffset();
		}

		return limitStr;
	}

	public Map<Integer, List<Product>> getProducts(int id, ProductListDto productListDto) {
		Object[] req = {};
		Map<String, Object> params = new HashMap<>();
		StringBuilder query = new StringBuilder();
		query.append("SELECT count(*) OVER() AS total_count,").append("p.id id, price, cc.id cid ") // ,cc.title

				.append("FROM product p ").append("INNER JOIN catalog cc ON cc.id=p.catalog_fk ")
				.append("LEFT JOIN catalog cp ON cc.upper_catalog_fk=cp.id ")
				.append("WHERE ((cp.catalog_level=1 AND cc.title IS NULL) OR (cc.title IS NOT NULL )) ").append("")
				.append(makeContitions(productListDto, params)).append("ORDER BY id DESC ")
				.append(makeLimitAndOffset(productListDto));

		return namedParameterJdbcTemplate.query(query.toString(), params,
				new ResultSetExtractor<Map<Integer, List<Product>>>() {

					@Override
					public Map<Integer, List<Product>> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						List<Product> products = new ArrayList<>();
						Map<Integer, Product> productsById = new LinkedHashMap<>();
						Integer total = 0;
						while (rs.next()) {
							Integer id = rs.getInt("id");
							Product product = productsById.get(id);
							if (product == null) {
								total = rs.getInt("total_count");
								product = new Product();
								product.setId(id);
								product.setPrice(rs.getBigDecimal("price"));

								Catalog catalog = new Catalog();
								catalog.setId(rs.getInt("cid"));
								product.setCatalog(catalog);

								productsById.put(id, product);
								products.add(product);
							}

						}
						Map<Integer, List<Product>> pMap = new LinkedHashMap<>();
						pMap.put(total, products);
						return pMap;
					}

				});
	}

	private List<String> retoSetOfAttrValues(Product prod) {
		List<String> aVS = new ArrayList<>();
		aVS.add(":price:asc");
		for (String attrKey : prod.getAttrs().keySet()) {
			String attrValue = prod.getAttrs().get(attrKey);
			aVS.add(":" + attrKey + ":" + attrValue);

		}
		return aVS;
	}

	private int[] binary_form(int number) {
		int[] binaryString = new int[32];
		for (int i = 0; i < 32; i++) {
			if (((0x80000000 >>> i) & number) != 0) {
				binaryString[i] = 1;
			} else {
				binaryString[i] = 0;
			}
		}
		return binaryString;
	}

	private int[] cutNulls(int[] binaryString, int nbImportantBits) {
		int[] newBinaryString = new int[nbImportantBits];
		for (int i = 0; i < nbImportantBits; i++) {
			newBinaryString[i] = binaryString[32 - nbImportantBits + i];
		}
		return newBinaryString;
	}

	@Override
	public void addProducts(List<Product> products) {

		jdbcTemplate.batchUpdate("INSERT INTO product (name,description,image,catalog_fk,price,"
				+ "brand,brand_fk,resolution,resolution_fk," + "screen,screen_fk,\"Resp time\",\"Resp time_fk\","
				+ "color,color_fk,\"Battery Life\",\"Battery Life_fk\",type)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);", new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Product prod = products.get(i);
						ps.setString(1, prod.getName());
						ps.setString(2, prod.getDescription());
						ps.setString(3, prod.getImage());
						ps.setInt(4, prod.getCatalog().getId());
						ps.setBigDecimal(5, prod.getPrice());
						ps.setString(6, prod.getBrand());
						ps.setInt(7, prod.getBrand_fk().getId());
						ps.setString(8, prod.getResolution());
						ps.setInt(9, prod.getResolution_fk().getId());
						if (prod.getScreen() != null) {
							ps.setString(10, prod.getScreen());
							ps.setInt(11, prod.getScreen_fk().getId());
						} else {
							ps.setNull(10, java.sql.Types.VARCHAR);
							ps.setNull(11, java.sql.Types.INTEGER);
						}
						if (prod.getResp_time() != null) {
							ps.setString(12, prod.getResp_time());
							ps.setInt(13, prod.getResp_time_fk().getId());
						} else {
							ps.setNull(12, java.sql.Types.VARCHAR);
							ps.setNull(13, java.sql.Types.INTEGER);
						}
						if (prod.getColor() != null) {
							ps.setString(14, prod.getColor());
							ps.setInt(15, prod.getColor_fk().getId());
						} else {
							ps.setNull(14, java.sql.Types.VARCHAR);
							ps.setNull(15, java.sql.Types.INTEGER);
						}
						if (prod.getBattery_life() != null) {
							ps.setString(16, prod.getBattery_life());
							ps.setInt(17, prod.getBattery_life_fk().getId());
						} else {
							ps.setNull(16, java.sql.Types.VARCHAR);
							ps.setNull(17, java.sql.Types.INTEGER);
						}
						ps.setString(18, prod.getType());
					}

					@Override
					public int getBatchSize() {
						return products.size();
					}
				});

		Map<String, List<Integer>> cachedResults = new LinkedHashMap<>();
		for (Product product : products) {
			List<String> aVs = retoSetOfAttrValues(product);

			List<List<String>> subsets = new ArrayList<List<String>>();
			int N = aVs.size();
			int nbOfAllSubsets = (int) Math.pow(2, N);

			//gen all subsets by getting binary representation of numbers between 0 and (2^n)-1
			for (int i = 0; i < nbOfAllSubsets; i++) {
				int[] binaryString = binary_form(i);
				binaryString = cutNulls(binaryString, N);
				List<String> subset = new ArrayList<>();
				int found = 0;
				for (int j = 0; j < binaryString.length; j++) {
					if (binaryString[j] == 1) {
						subset.add(aVs.get(j));
						found = 1;
					}
				}
				if (found == 0)
					subset.add("");
				subsets.add(subset);
			}

			String compositeKey = "";

			for (List<String> subset : subsets) {
				String compositeKeyDescPrice = "";
				//construction of composite key and creation of 3 sorted versions of the key
				compositeKey = "catalog:" + Integer.toString(product.getCatalog().getId());
				for (String element : subset) {
					compositeKey += element;
				}

				if (compositeKey.contains("price"))
					compositeKeyDescPrice = compositeKey.replace("price:asc", "price:desc");

				if (!cachedResults.containsKey(compositeKey)) {
					cachedResults.put(compositeKey, new ArrayList<>());
					if (!compositeKeyDescPrice.isEmpty())
						cachedResults.put(compositeKeyDescPrice, new ArrayList<>());
				}
				//associate product ids with composite key
				List<Integer> prodIds = cachedResults.get(compositeKey);
				prodIds.add(product.getId());
				if (!compositeKeyDescPrice.isEmpty()) {
					prodIds = cachedResults.get(compositeKeyDescPrice);
					prodIds.add(product.getId());
				}
			}
		}

		String SQL = "SELECT insrt_or_append(?,?);";

		//save in DB
		jdbcTemplate.batchUpdate(SQL, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				String key = (String) cachedResults.keySet().toArray()[i];
				Array results = ps.getConnection().createArrayOf("integer", cachedResults.get(key).toArray());
				ps.setArray(1, results);
				ps.setString(2, key);
			}

			@Override
			public int getBatchSize() {
				return cachedResults.size();
			}
		});
	}

	@Override
	public void addDetails(List<Detail> details) {
		jdbcTemplate.batchUpdate(
				"INSERT INTO detail(product_fk,name,detail_value)" + " VALUES(?,'DTLNAME1','DTLVAL1');",
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Detail detail = details.get(i);
						ps.setInt(1, detail.getProdId());

					}

					@Override
					public int getBatchSize() {
						return details.size();
					}
				});

	}

	@Override
	public void addProdAttrs(List<ProdAttrTable> prodAttrs) {

		jdbcTemplate.batchUpdate(
				"INSERT INTO product_attribute(product_fk,attribute_fk,name,attribute_value_fk,attributeValue,orderby)"
						+ " VALUES(?,?,?,?,?,?);",
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ProdAttrTable prodAttrTable = prodAttrs.get(i);
						ps.setInt(1, prodAttrTable.getProd_fk());
						ps.setInt(2, prodAttrTable.getAttribute_fk());
						ps.setString(3, prodAttrTable.getName());
						ps.setInt(4, prodAttrTable.getAttribute_value_fk());
						ps.setString(5, prodAttrTable.getAttributeValue());
						ps.setInt(6, prodAttrTable.getOrderby());

					}

					@Override
					public int getBatchSize() {
						return prodAttrs.size();
					}
				});

	}

	@Override
	public void addStocks(List<Stock> stocks) {
		jdbcTemplate.batchUpdate(
				"INSERT INTO stock(quantity,shop_fk,product_fk,shipment_type_fk)" + " VALUES(?,?,?,?);",
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Stock stock = stocks.get(i);
						ps.setInt(1, stock.getQuantity());
						ps.setInt(2, stock.getShop().getId());
						ps.setInt(3, stock.getProdId());
						ps.setInt(4, stock.getShipment_type().getId());

					}

					@Override
					public int getBatchSize() {
						return stocks.size();
					}
				});
	}

	@Override
	public void addOrders(List<Order> orders, List<OrderItem> orderItems) {

		String SQL = "SELECT insrt_or_append2(?,'unsubmordersdesc');";

		List<Integer> ids = new ArrayList<>();
		for (Order o : orders) {
			ids.add(o.getId());
		}

		jdbcTemplate.batchUpdate(SQL, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub

				Array arr = ps.getConnection().createArrayOf("integer", ids.toArray());
				ps.setArray(1, arr);

			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return 1;
			}
		});

		StringBuilder query = new StringBuilder();
		query.append(
				"INSERT INTO orders(person_fk,order_status_fk,creation_date,address,first_name,last_name,phone,email ");

		query.append("");

		query.append(") " + " VALUES(?,?,CURRENT_TIMESTAMP,?,?,?,?,?);");

		jdbcTemplate.batchUpdate(query.toString(), new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				Order order = orders.get(index);
				if (order.getUser() != null) {
					ps.setInt(1, order.getUser().getId());
				} else {
					ps.setNull(1, java.sql.Types.INTEGER);
				}
				ps.setInt(2, 1);
				ps.setString(3, order.getAddress());
				ps.setString(4, order.getFirst_name());
				ps.setString(5, order.getLast_name());

				ps.setString(6, order.getPhone());
				ps.setString(7, order.getEmail());

				// ps.setBoolean(6, order.isIs_client());
				// ps.setBoolean(7, order.isIs_employee());

				// init class with input order param?
			}

			@Override
			public int getBatchSize() {
				return orders.size();
			}
		});

		StringBuilder query1 = new StringBuilder();
		query1.append("INSERT INTO order_product(product_fk,order_fk,price,quantity) ");
		query1.append(" VALUES(?,?,?,?);");

		jdbcTemplate.batchUpdate(query1.toString(), new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				OrderItem orderItem = orderItems.get(index);
				ps.setInt(1, orderItem.getProduct().getId());
				ps.setInt(2, orderItem.getOrder().getId());
				ps.setBigDecimal(3, orderItem.getPrice());
				ps.setInt(4, orderItem.getQty());
			}

			@Override
			public int getBatchSize() {
				return orderItems.size();
			}
		});
	}

	@Override
	public void addUsers(List<User> users) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO person(person_status_fk,"
				+ "nick,passwd,salt,email,reg_date,style,person_status_name,is_client,is_employee) "
				+ "VALUES(1,?,?,?,?,CURRENT_TIMESTAMP,'white','active',true,?) ");

		jdbcTemplate.batchUpdate(query.toString(), new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				User user = users.get(i);
				ps.setString(1, user.getNick());
				ps.setString(2, user.getPasswd());
				ps.setString(3, user.getSalt());
				ps.setString(4, user.getEmail());
				ps.setBoolean(5, user.isIs_employee());

			}

			@Override
			public int getBatchSize() {
				return users.size();
			}
		});
	}

	@Override
	public void callSort() {
		jdbcTemplate.execute("SELECT call_sort_and_insrt_seller();");
	}

	@Override
	public void addProductsByCatalog(List<Product> products) {
		// TODO Auto-generated method stub

	}

}

package ee.ttu.shop.order;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;

import ee.ttu.shop.catalog.Catalog;
import ee.ttu.shop.catalog.CatalogDao;
import ee.ttu.shop.product.Product;
import ee.ttu.shop.product.ProductListDto;
import ee.ttu.shop.user.User;

@Repository
public class OrderDao {

	private static final Logger logger = Logger.getLogger(CatalogDao.class);

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setDateSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

	}

	public void submitOrderById(int orderId, int param) {
		StringBuilder query1 = new StringBuilder();
		query1.append("UPDATE orders SET order_status_fk=? WHERE id=? ");

		jdbcTemplate.update(query1.toString(), new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param);
				ps.setInt(2, orderId);

			}
		});
	}

	public Map<Integer, List<Order>> getOrderIdsByUser(OrderListDTO orderListDTO) {
		Map<String, Integer> params = new HashMap<>();
		params.put("userid", orderListDTO.getUserid());

		String q = "SELECT count(*) OVER() AS total_count, o.id oid,o.creation_date" + " FROM orders o "
				+ " WHERE o.person_fk=:userid ORDER BY id DESC ";
		q += makeLimitAndOffset(orderListDTO);
		logger.info("getOrderIdsByUser NEW params size>0 " + q.toString());
		return namedParameterJdbcTemplate.query(q,

				params, new ResultSetExtractor<Map<Integer, List<Order>>>() {

					@Override
					public Map<Integer, List<Order>> extractData(ResultSet rs)
							throws SQLException, DataAccessException {

						List<Order> orders = new ArrayList<>();
						Map<Integer, Order> ordersById = new LinkedHashMap<>();
						Integer total = 0;
						while (rs.next()) {
							Integer id = rs.getInt("oid");
							Order order = ordersById.get(id);
							if (order == null) {
								total = rs.getInt("total_count");
								order = new Order();
								order.setId(id);
								ordersById.put(id, order);
								orders.add(order);
							}
						}
						Map<Integer, List<Order>> oMap = new LinkedHashMap<>();
						oMap.put(total, orders);

						return oMap;
					}

				});
	}

	private static String makeLimitAndOffset(OrderListDTO orderListDTO) {
		String limitStr = "";
		if (orderListDTO.getLmt() != null) {
			limitStr += " LIMIT " + orderListDTO.getLmt().toString();
		}
		if (orderListDTO.getOffset() != 0) {
			limitStr += " OFFSET " + orderListDTO.getOffset();
		}

		return limitStr;
	}

	public void remove_OK(int ordId) {
		jdbcTemplate.execute("SELECT remove_order(" + ordId + "); ");
	}

	public void insertOK(int ordId) {

		String SQL = "SELECT insrt_or_append2(?,'unsubmordersdesc');";

		List<Integer> ids = new ArrayList<>();
		ids.add(ordId);

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
		jdbcTemplate.execute("SELECT sort_unsok(); ");
	}

	public Map<Integer, List<Order>> getOrdersByDto(OrderListDTO orderListDTO) {
		Map<String, Object> qParams = new HashMap<>();

		qParams.put("key", "unsubmordersdesc");

		Integer limit = orderListDTO.getLmt();
		int offset = orderListDTO.getOffset();

		String q1 = " SELECT total_amount, unnest(subarray(value," + (offset + 1) + "," + limit
				+ ")) prodids FROM tbtest2 WHERE key=:key ";

		return namedParameterJdbcTemplate.query(q1, qParams, new ResultSetExtractor<Map<Integer, List<Order>>>() {

			@Override
			public Map<Integer, List<Order>> extractData(ResultSet rs) throws SQLException, DataAccessException {

				List<Order> orders = new ArrayList<>();
				Map<Integer, Order> ordersById = new LinkedHashMap<>();
				Integer total = 0;
				while (rs.next()) {
					Integer id = rs.getInt("prodids");
					Order order = ordersById.get(id);
					if (order == null) {
						total = rs.getInt("total_amount");
						order = new Order();
						order.setId(id);
						ordersById.put(id, order);
						orders.add(order);
					}
				}
				Map<Integer, List<Order>> oMap = new LinkedHashMap<>();
				oMap.put(total, orders);

				return oMap;
			}
		});
	}

	public List<Order> getOrdersByIds(List<Integer> orderIds) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderIds", orderIds);

		String q = "SELECT o.id oid,o.creation_date,o.first_name,o.last_name,o.phone,o.email,o.address,person_fk,order_status_fk, "
				+ " os.name order_status_name,os.id order_status_fk,"
				+ " op.quantity opqty, op.price opprice, op.id opid, op.product_fk, "
				+ " p.id pid, p.name, p.price pprice, description, p.image,brand,resolution,screen,\"Resp time\", color, \"Battery Life\", type, "
				+ " cc.id cid, cc.title ct,cc.image ci, cc.urlname cu, cp.id cpid, cp.title pt, cp.image pi, cp.urlname pu "

				+ " FROM orders o " + " INNER JOIN order_product op ON o.id=op.order_fk"
				+ " INNER JOIN product p ON op.product_fk=p.id" + " INNER JOIN catalog cc ON cc.id=p.catalog_fk"
				+ " LEFT JOIN catalog cp ON cc.upper_catalog_fk=cp.id"

				+ " INNER JOIN order_status os ON o.order_status_fk=os.id "
				+ " WHERE o.id IN (:orderIds) ORDER BY o.id DESC;";

		return namedParameterJdbcTemplate.query(q,

				params, new ResultSetExtractor<List<Order>>() {

					@Override
					public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<Integer, Order> ordersById = new LinkedHashMap<Integer, Order>();
						while (rs.next()) {

							int orderId = rs.getInt("oid");
							Order order = ordersById.get(orderId);
							if (order == null) {

								order = new Order();
								order.setId(rs.getInt("oid"));
								order.setCreationDate(rs.getTimestamp("creation_date"));
								order.setFirst_name(rs.getString("first_name"));
								order.setLast_name(rs.getString("last_name"));
								order.setPhone(rs.getString("phone"));
								order.setEmail(rs.getString("email"));
								order.setAddress(rs.getString("address"));

								User user = new User();
								user.setId(rs.getInt("person_fk"));
								order.setUser(user);
								Order_status os = new Order_status();
								os.setId(rs.getInt("order_status_fk"));
								os.setName(rs.getString("order_status_name"));
								order.setOrder_status(os);
								ordersById.put(orderId, order);
							}

							if (rs.getInt("opid") != 0) {
								OrderItem orderItem = new OrderItem();
								orderItem.setPrice(rs.getBigDecimal("opprice"));
								orderItem.setQty(rs.getInt("opqty"));
								Product product = new Product();
								product.setId(rs.getInt("product_fk"));

								product.setName(rs.getString("name"));

								product.setPrice(rs.getBigDecimal("pprice"));
								product.setDescription(rs.getString("description"));
								product.setImage(rs.getString("image"));
								product.setBrand(rs.getString("brand"));
								product.setResolution(rs.getString("resolution"));
								product.setScreen(rs.getString("screen"));
								product.setResp_time(rs.getString("Resp time"));
								product.setColor(rs.getString("color"));
								product.setBattery_life(rs.getString("Battery Life"));
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

								orderItem.setProduct(product);
								List<OrderItem> orderItems = order.getOrderItems();
								orderItems.add(orderItem);

							}

						}
						List<Order> orders = new ArrayList<>(ordersById.values());
						return orders;
					}

				});
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
	public int createOrder(Order order) {
		StringBuilder query = new StringBuilder();
		query.append(
				"INSERT INTO orders(person_fk,order_status_fk,creation_date,address,first_name,last_name,phone,email ");

		query.append("");

		query.append(") " + " VALUES(?,?,CURRENT_TIMESTAMP,?,?,?,?,?);");

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
				if (order.getUser() != null)
					ps.setInt(1, order.getUser().getId());
				else
					ps.setNull(1, java.sql.Types.INTEGER);
				ps.setInt(2, 1);
				ps.setString(3, order.getAddress());
				ps.setString(4, order.getFirst_name());
				ps.setString(5, order.getLast_name());

				ps.setString(6, order.getPhone());
				ps.setString(7, order.getEmail());
				return ps;
			}
		}, keyHolder);

		StringBuilder query1 = new StringBuilder();
		query1.append("INSERT INTO order_product(product_fk,order_fk,price,quantity) ");
		query1.append(" VALUES(?,?,?,?);");

		jdbcTemplate.batchUpdate(query1.toString(), new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				OrderItem orderItem = order.getOrderItems().get(index);
				ps.setInt(1, orderItem.getProduct().getId());
				ps.setInt(2, Integer.parseInt(keyHolder.getKeys().get("id").toString()));
				ps.setBigDecimal(3, orderItem.getPrice());
				ps.setInt(4, orderItem.getQty());
			}

			@Override
			public int getBatchSize() {
				return order.getOrderItems().size();
			}
		});

		return Integer.parseInt(keyHolder.getKeys().get("id").toString());

	}

}

package ee.ttu.shop.user;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ee.ttu.shop.order.Order;
import ee.ttu.shop.order.OrderItem;

@Repository
public class UserDao {

	private static final Logger logger = Logger.getLogger(UserDao.class);

	private JdbcTemplate jdbcTemplate;

	private final String queryUserById = " SELECT id,person_status_fk,nick,passwd,salt,email,reg_date,style,person_status_name,last_login,is_client,is_employee FROM person where id=?";
	private final String queryUserByName = " SELECT id,person_status_fk,nick,passwd,salt,email,reg_date,style,person_status_name,last_login,is_client,is_employee FROM person where nick=?";
	private static final String queryUserInfoClass = "SELECT last_login, reg_date FROM person WHERE id=?";

	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
	public int createUser(User user) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO person(person_status_fk,"
				+ "nick,passwd,salt,email,reg_date,style,person_status_name,is_client,is_employee) "
				+ "VALUES(1,?,?,?,?,CURRENT_TIMESTAMP,'white','active',true,false) ");

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getNick());
				ps.setString(2, user.getPasswd());
				ps.setString(3, user.getSalt());
				ps.setString(4, user.getEmail());

				return ps;
			}
		}, keyHolder);
		return Integer.parseInt(keyHolder.getKeys().get("id").toString());
	}

	public User getUserCached(int id) throws UserNotFoundException {
		return getUserInternal(id);
	}

	public User getUserCached(String nick) throws UserNotFoundException {
		return getUserInternal(nick);
	}

	public User getUser(int id) throws UserNotFoundException {
		return getUserInternal(id);
	}

	public User getUserInfoClass(User user) {
		List<User> userList = jdbcTemplate.query(queryUserInfoClass, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setLast_login(rs.getTimestamp(" last_login"));
				user.setReg_date(rs.getTimestamp("reg_date"));
				return user;
			}

		}, user.getId());
		if (userList.isEmpty()) {
			return null;
		} else {
			return userList.get(0);
		}
	}

	private User getUserInternal(String nick) throws UserNotFoundException {
		User u = jdbcTemplate.queryForObject(queryUserByName, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int i) throws SQLException {
				User user = new User();
				user.setId(rs.getInt("id"));
				User_status us = new User_status();
				us.setId(rs.getInt("person_status_fk"));
				us.setName(rs.getString("person_status_name"));
				user.setUser_status(us);
				user.setNick(rs.getString("nick"));
				user.setPasswd(rs.getString("passwd"));
				user.setSalt(rs.getString("salt"));
				user.setEmail(rs.getString("email"));
				user.setReg_date(rs.getTimestamp("reg_date"));
				user.setLast_login(rs.getTimestamp("last_login"));
				user.setStyle(rs.getString("style"));
				user.setIs_client(rs.getBoolean("is_client"));
				user.setIs_employee(rs.getBoolean("is_employee"));
				return user;
			}
		}, nick);
		if (u == null)
			throw new UsernameNotFoundException("getUserInternal(String nick) not found");
		return u;
	}

	private User getUserInternal(int id) throws UserNotFoundException {
		User u = jdbcTemplate.queryForObject(queryUserById, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int i) throws SQLException {
				User user = new User();
				user.setId(rs.getInt("id"));
				User_status us = new User_status();
				us.setId(rs.getInt("person_status_fk"));
				us.setName(rs.getString("person_status_name"));
				user.setUser_status(us);
				user.setNick(rs.getString("nick"));
				user.setPasswd(rs.getString("passwd"));
				user.setSalt(rs.getString("salt"));
				user.setEmail(rs.getString("email"));
				user.setReg_date(rs.getTimestamp("reg_date"));
				user.setLast_login(rs.getTimestamp("last_login"));
				user.setStyle(rs.getString("style"));
				user.setIs_client(rs.getBoolean("is_client"));
				user.setIs_employee(rs.getBoolean("is_employee"));
				return user;
			}
		}, id);
		if (u == null)
			throw new UsernameNotFoundException("getUserInternal(Int id) not found");
		return u;
	}

	public void updateLastlogin(User user, boolean force) {
		if (force) {
			jdbcTemplate.update("UPDATE person SET last_login=CURRENT_TIMESTAMP WHERE id=?", user.getId());
		} else {
			jdbcTemplate.update(
					"UPDATE person SET last_login=CURRENT_TIMESTAMP WHERE id=? AND CURRENT_TIMESTAMP-last_login > '1 hour'::interval",
					user.getId());
		}
	}

}
